package com.kosmas.tecprin.utils

sealed class ExceptionUtils : Exception() {

    class GenericErrorException(override val message: String? = null) : ExceptionUtils()
    class NetworkErrorException : ExceptionUtils()
}
