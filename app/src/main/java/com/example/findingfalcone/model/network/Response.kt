package com.example.findingfalcone.model.network

import com.example.findingfalcone.model.UIMessage

sealed class Response<T> {
    class Success<T>(val data: T) : Response<T>()
    class UnknownHostException<T>(val msg: UIMessage) : Response<T>()
    class InvalidPathException<T>(val msg: UIMessage) : Response<T>()
    class InvalidRequestException<T>(val msg: UIMessage) : Response<T>()
    class ServerException<T>(val msg: UIMessage) : Response<T>()
    class UnknownException<T>(val msg: UIMessage) : Response<T>()
}