package com.example.pictureme.data

sealed class Response<out T> {
    data class Success<out T>(val result: T): Response<T>()
    data class Failure(val e: Exception): Response<Nothing>()
    object Loading: Response<Nothing>()


}
