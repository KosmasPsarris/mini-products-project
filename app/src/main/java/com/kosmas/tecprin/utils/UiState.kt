package com.kosmas.tecprin.utils

sealed class UIState<out T> {

    /**
     * Successful state containing some data.
     */
    data class Result<out T>(val data: T): UIState<T>()

    /**
     * Failed state, containing the error as well.
     */
    data class Error(val error: Throwable): UIState<Nothing>()

    /**
     * State about to change.
     */
    object InProgress: UIState<Nothing>()

    /**
     * Undefined/default state.
     */
    object IDLE: UIState<Nothing>()

    /**
     * State where api call job is cancelled.
     */
    object CANCELED: UIState<Nothing>()

    /**
     * State where no more data is available.
     */
    object COMPLETE: UIState<Nothing>()

}
