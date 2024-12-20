package com.kls.fetchtakehometest.data

data class Data(
    val id: Int,
    val listId: Int,
    val name: String? = null
)

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}