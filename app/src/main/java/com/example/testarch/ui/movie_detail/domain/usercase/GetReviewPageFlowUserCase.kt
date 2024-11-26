package com.example.testarch.ui.movie_detail.domain.usercase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.testarch.ui.movie_detail.domain.entity.Review
import com.example.testarch.ui.movie_detail.domain.repository.ReviewApiRepository
import kotlinx.coroutines.flow.Flow

class GetReviewPageFlowUserCase(
    private val reviewApiRepository: ReviewApiRepository
) {
    fun getSource(movieId: Int): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                ReviewPagingSource(reviewApiRepository, movieId)
            }
        ).flow
    }
}

private class ReviewPagingSource(
    private val reviewApiRepository: ReviewApiRepository,
    private val movieId: Int
): PagingSource<Int, Review>() {
    override val jumpingSupported = true

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        val pageNo = params.key?: 1
        return try {
            val reviews = reviewApiRepository.getReviews(movieId, pageNo)
            LoadResult.Page(
                data = reviews,
                prevKey = if (pageNo > 1) pageNo - 1 else null,
                nextKey = if (reviews.isEmpty()) null else pageNo + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}