package com.example.testarch.ui.movie_detail.domain.usercase

import com.example.testarch.ui.movie_detail.domain.entity.Review
import com.example.testarch.ui.movie_detail.domain.repository.ReviewApiRepository
import kotlinx.coroutines.flow.MutableStateFlow

class GetMyReviewFlowUseCase(
    private val reviewApiRepository: ReviewApiRepository
) {
    val source = MutableStateFlow<Review?>(null)

    operator fun invoke() {
        val myReview = reviewApiRepository.getMyReview()
        source.value = myReview
    }
}