package com.example.testarch.ui.movie_detail.data

import com.example.testarch.ui.movie_detail.domain.entity.Review
import com.example.testarch.ui.movie_detail.domain.repository.ReviewApiRepository
import javax.inject.Inject

class ReviewApiRepositoryImpl @Inject constructor(): ReviewApiRepository {
    override fun getReviewsAll(movieId: Int): List<Review> {
        return getReviews(movieId, 1)
    }

    override fun getReviews(movieId: Int, pageNo: Int): List<Review> {
        val startIndex = (pageNo - 1) * MAX_PAGE_SIZE
        val endIndex = startIndex + MAX_PAGE_SIZE - 1
        val reviews = mutableListOf<Review>()
        for (i in startIndex..endIndex) {
            val review = Review(
                id = movieId*10000+i,
                author = "Author $movieId $i",
                content = "Review content $movieId $i",
                rating = i,
                date = "Date $i",
                title = "Review title $movieId $i",
            )
            reviews.add(review)
        }
        Thread.sleep(100) // Simulate network delay
        return reviews
    }

    override fun getMyReview(): Review? {
        return Review(
            id = Int.MAX_VALUE,
            author = "Author Me",
            content = "Review content Me",
            rating = Int.MIN_VALUE,
            date = "Date Me",
            title = "Review title Me",
        )
    }

    companion object {
        private const val MAX_PAGE_SIZE = 10
    }
}