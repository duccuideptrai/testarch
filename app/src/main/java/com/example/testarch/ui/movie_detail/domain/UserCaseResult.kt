package com.example.testarch.ui.movie_detail.domain

sealed interface UserCaseResult<out T> {
    data class Success<T>(val data: T) : UserCaseResult<T>
    data class Error(val exception: Throwable) : UserCaseResult<Nothing>
    data object Loading : UserCaseResult<Nothing>
}