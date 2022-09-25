package com.hgabriel.gamemetascore.data

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status { SUCCESS, ERROR }

    companion object {
        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data, null)
        fun <T> error(msg: String): Resource<T> = Resource(Status.ERROR, null, msg)
    }
}
