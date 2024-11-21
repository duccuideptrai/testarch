package com.example.testarch.ui.movie_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.testarch.ui.movie_detail.domain.usercase.GetReviewDataSourceFlowUserCase
import com.example.testarch.ui.movie_detail.domain.usercase.GetReviewsUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val getReviewDataSourceFlowUserCase: GetReviewDataSourceFlowUserCase,
    private val getReviewsUserCase: GetReviewsUserCase
): ViewModel() {
    val reviews = getReviewDataSourceFlowUserCase.source

    fun getReviews(movieId: Int) {
        getReviewsUserCase(movieId)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MyFragment", "ViewModel onCleared")
    }
}