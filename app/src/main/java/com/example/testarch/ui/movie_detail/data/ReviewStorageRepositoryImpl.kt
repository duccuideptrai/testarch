package com.example.testarch.ui.movie_detail.data

import com.example.testarch.ui.movie_detail.domain.entity.Review
import com.example.testarch.ui.movie_detail.domain.repository.ReviewStorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReviewStorageRepositoryImpl: ReviewStorageRepository {
    private val _reviews = MutableStateFlow(listOf<Review>())

    override fun getReviewsStateFlow() = _reviews.asStateFlow()

    override fun upsetReview(reviews: List<Review>) {
        _reviews.value = reviews
    }

    override fun deleteReview(reviewId: Int) {
        _reviews.value = _reviews.value.filter { it.id!=reviewId }
    }

    override fun deleteAllReviews() {
        _reviews.value = emptyList()
    }
}