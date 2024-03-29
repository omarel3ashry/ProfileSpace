package com.example.profilespace.common

/**
 * Created by Omar Elashry on 8/9/2023.
 */
// A handy resource to handle the status of any operation.
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data, null)
        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(Status.ERROR, data, message)

        fun <T> loading(data: T?): Resource<T> = Resource(Status.LOADING, data, null)
    }
}
