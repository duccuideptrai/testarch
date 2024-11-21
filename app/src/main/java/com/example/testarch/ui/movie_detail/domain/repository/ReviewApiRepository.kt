package com.example.testarch.ui.movie_detail.domain.repository

import com.example.testarch.ui.movie_detail.domain.entity.Review

interface ReviewApiRepository {
    fun getReviewsAll(movieId: Int): List<Review>
    fun getReviews(movieId: Int, pageNo: Int): List<Review>
    fun getMyReview(): Review?
}