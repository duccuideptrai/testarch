package com.example.testarch.ui.movie_detail.domain.repository

import com.example.testarch.ui.movie_detail.domain.entity.Review
import kotlinx.coroutines.flow.StateFlow

interface ReviewStorageRepository {
    fun getReviewsStateFlow(): StateFlow<List<Review>>
    fun upsetReview(reviews: List<Review>)
    fun deleteReview(reviewId: Int)
    fun deleteAllReviews()
}