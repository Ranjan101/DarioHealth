package com.dario.health.repository

sealed class Response<T>(
    val data: T? = null,
    val onlySuccessMsg: String? = null,
    val errorMsg: String? = null,
    val isloading: Boolean? = null
) {
    class Loading<T>(isloading: Boolean) : Response<T>(isloading = isloading)
    class Success<T>(data: T? = null) : Response<T>(data = data)
    class OnlySuccessMessage<T>(onlySuccessMsg: String) :
        Response<T>(onlySuccessMsg = onlySuccessMsg)

    class Error<T>(errorMsg: String) : Response<T>(errorMsg = errorMsg)
}

