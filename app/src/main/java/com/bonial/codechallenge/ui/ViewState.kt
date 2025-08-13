package com.bonial.codechallenge.ui

sealed class ViewState<out T> where T : Any? {

    object Loading : ViewState<Nothing>()

    object Failure : ViewState<Nothing>()

    data class Success<T>(val data: T) : ViewState<T>()
}
