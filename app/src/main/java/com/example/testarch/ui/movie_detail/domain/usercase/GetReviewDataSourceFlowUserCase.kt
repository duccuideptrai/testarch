package com.example.testarch.ui.movie_detail.domain.usercase

import com.example.testarch.ui.movie_detail.domain.repository.ReviewStorageRepository

class GetReviewDataSourceFlowUserCase(reviewStorageRepository: ReviewStorageRepository) {
    val source = reviewStorageRepository.getReviewsStateFlow()
}