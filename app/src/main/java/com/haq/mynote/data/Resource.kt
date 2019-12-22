package com.haq.mynote.data

class Resource<out T>(
    val status: Status,
    val error: Exception? = null,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T) = Resource(Status.Success, data = data)
        fun <T> error(error: Exception? = null, data: T?) = Resource(Status.Error, error = error, data = data)
        fun <T> loading(data: T? = null) = Resource(Status.Loading, data = data)
        fun <T> copy(status: Status, error: Exception?, data: T?) = Resource(status, error, data)
    }

    sealed class Status {
        object Success : Status()
        object Error : Status()
        object Loading : Status()
    }
}