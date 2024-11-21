package com.example.testarch.ui.movie_detail.domain.usercase

import com.example.testarch.ui.movie_detail.domain.UserCaseResult
import com.example.testarch.ui.movie_detail.domain.entity.Review
import com.example.testarch.ui.movie_detail.domain.repository.ReviewApiRepository
import com.example.testarch.ui.movie_detail.domain.repository.ReviewStorageRepository
import javax.inject.Inject

class GetReviewsUserCase @Inject constructor(
    private val reviewStorageRepository: ReviewStorageRepository,
    private val reviewApiRepository: ReviewApiRepository
) {
    operator fun invoke(movieId: Int): UserCaseResult.Success<List<Review>> {
        val reviews = reviewApiRepository.getReviewsAll(movieId)
        reviewStorageRepository.upsetReview(reviews)
        return UserCaseResult.Success(reviews)
    }
}

