package com.example.findingfalcone.model

sealed class FalconeStatus {
    class Success(val planet: String, val time: Int) : FalconeStatus()
    data object Failure : FalconeStatus()
    data object InvalidToken : FalconeStatus()
}