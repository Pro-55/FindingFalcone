package com.example.findingfalcone.ui.selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findingfalcone.data.use_case.FindFalconeUseCase
import com.example.findingfalcone.data.use_case.GetAvailableVehiclesUseCase
import com.example.findingfalcone.data.use_case.GetPlanetsAndVehiclesUseCase
import com.example.findingfalcone.data.use_case.GetTokenUseCase
import com.example.findingfalcone.model.FalconeStatus
import com.example.findingfalcone.model.Planet
import com.example.findingfalcone.model.Resource
import com.example.findingfalcone.model.SelectionItem
import com.example.findingfalcone.model.SelectionScreenState
import com.example.findingfalcone.model.Vehicle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SelectionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTokenUseCase: GetTokenUseCase,
    private val getPlanetsAndVehiclesUseCase: GetPlanetsAndVehiclesUseCase,
    private val getAvailableVehiclesUseCase: GetAvailableVehiclesUseCase,
    private val findFalconeUseCase: FindFalconeUseCase
) : ViewModel() {

    // Global
    private val TAG = SelectionViewModel::class.java.simpleName
    private val masterPlanets = mutableListOf<Planet>()
    private val masterVehicles = mutableListOf<Vehicle>()
    private val selectionMap = mutableMapOf<Planet, Vehicle>()
    private val _state = MutableLiveData<Resource<SelectionScreenState>>()
    val state: LiveData<Resource<SelectionScreenState>> = _state
    private val _availableVehicles = MutableLiveData<Resource<Pair<Planet, List<Vehicle>>>?>()
    val availableVehicles: LiveData<Resource<Pair<Planet, List<Vehicle>>>?> = _availableVehicles
    private val _falconeStatus = MutableLiveData<Resource<FalconeStatus>?>()
    val falconeStatus: MutableLiveData<Resource<FalconeStatus>?> = _falconeStatus

    init {
        getToken()
    }

    fun getToken() {
        getTokenUseCase()
            .onEach {
                when (it) {
                    is Resource.Loading -> _state.postValue(Resource.Loading())
                    is Resource.Success -> getPlanetsAndVehicles()
                    is Resource.Error -> _state.postValue(Resource.Error(msg = it.msg))
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getPlanetsAndVehicles() {
        getPlanetsAndVehiclesUseCase()
            .onEach {
                when (it) {
                    is Resource.Loading -> _state.postValue(Resource.Loading())
                    is Resource.Success -> {
                        val data = it.data!!
                        masterPlanets.clear()
                        masterPlanets.addAll(data.planets)

                        masterVehicles.clear()
                        masterVehicles.addAll(data.vehicles)

                        selectionMap.clear()

                        _state.postValue(
                            Resource.Success(
                                data = SelectionScreenState(
                                    selectedCount = selectionMap.size,
                                    estimatedTime = getEstimatedTime(),
                                    planets = masterPlanets.map { planet ->
                                        SelectionItem(planet = planet)
                                    }
                                )
                            )
                        )
                    }
                    is Resource.Error -> _state.postValue(Resource.Error(msg = it.msg))
                }
            }
            .launchIn(viewModelScope)
    }

    fun getSelectionSize(): Int = selectionMap.size

    fun getAvailableVehicles(planet: Planet) {
        getAvailableVehiclesUseCase(
            planet = planet,
            vehicles = masterVehicles,
            selectionMap = selectionMap
        )
            .onEach { _availableVehicles.postValue(it) }
            .launchIn(viewModelScope)
    }

    fun resetAvailableVehicles() {
        _availableVehicles.postValue(null)
    }

    fun selectPlanetAndVehicle(
        planet: Planet,
        vehicle: Vehicle
    ) {
        selectionMap[planet] = vehicle
        _state.postValue(
            Resource.Success(
                data = SelectionScreenState(
                    selectedCount = selectionMap.size,
                    estimatedTime = getEstimatedTime(),
                    planets = masterPlanets.map {
                        SelectionItem(
                            planet = it,
                            vehicle = selectionMap[it]
                        )
                    }
                )
            )
        )
    }

    fun unselectPlanetAndVehicle(planet: Planet) {
        selectionMap.remove(planet)
        _state.postValue(
            Resource.Success(
                data = SelectionScreenState(
                    selectedCount = selectionMap.size,
                    estimatedTime = getEstimatedTime(),
                    planets = masterPlanets.map {
                        SelectionItem(
                            planet = it,
                            vehicle = selectionMap[it]
                        )
                    }
                )
            )
        )
    }

    fun findFalcone() {
        findFalconeUseCase(
            selectionMap = selectionMap
        )
            .onEach { _falconeStatus.postValue(it) }
            .launchIn(viewModelScope)
    }

    fun resetFalconeStatus() {
        _falconeStatus.postValue(null)
    }

    fun resetData() {
        selectionMap.clear()
        _state.postValue(
            Resource.Success(
                data = SelectionScreenState(
                    selectedCount = selectionMap.size,
                    estimatedTime = getEstimatedTime(),
                    planets = masterPlanets.map { planet ->
                        SelectionItem(planet = planet)
                    }
                )
            )
        )
    }

    private fun getEstimatedTime(): Int {
        var estimatedTime = 0
        for ((planet, vehicle) in selectionMap) {
            estimatedTime += planet.distance / vehicle.speed
        }
        return estimatedTime
    }
}