package com.example.findingfalcone.model

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    class Success<T>(val data: T?) : Resource<T>()
    class Error<T>(val msg: UIMessage) : Resource<T>()
}