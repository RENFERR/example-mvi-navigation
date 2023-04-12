package com.example.mvi.core

sealed class Response<out T> {
    data class Success<T>(val data: T?) : Response<T>()
    data class Failure(val exception: Exception) : Response<Nothing>()

    companion object {
        suspend fun <T> safeCall(suspendCall: suspend () -> T): Response<T> {
            return try {
                Success(data = suspendCall())
            } catch (e: Exception) {
                Failure(exception = e)
            }
        }

        fun <T> safeCall(call: () -> T): Response<T> {
            return try {
                Success(data = call())
            } catch (e: Exception) {
                Failure(exception = e)
            }
        }
    }
}