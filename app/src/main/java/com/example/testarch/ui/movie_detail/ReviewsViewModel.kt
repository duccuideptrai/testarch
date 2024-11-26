package com.example.testarch.ui.movie_detail

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.example.testarch.ui.movie_detail.domain.usercase.GetMyReviewFlowUseCase
import com.example.testarch.ui.movie_detail.domain.usercase.GetReviewPageFlowUserCase
import com.example.testarch.ui.movie_detail.utils.TraceableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    getReviewPageFlowUserCase: GetReviewPageFlowUserCase,
    private val getMyReviewFlowUseCase: GetMyReviewFlowUseCase
): TraceableViewModel() {
    private val _movieIDState = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val reviews = _movieIDState.flatMapLatest { movieId ->
        movieId?.let {
            getReviewPageFlowUserCase
                .getSource(it)
                .combine(getMyReviewFlowUseCase.source) { reviewsPage, myReview ->
                    reviewsPage.insertSeparators { before, _ ->
                        if (before == null) {
                            myReview
                        } else null
                    }
                }
        }?: MutableStateFlow(PagingData.empty())
    }.cachedIn(viewModelScope)

    fun getReviews(movieId: Int) {
        _movieIDState.value = movieId
        getMyReviewFlowUseCase()
    }
}