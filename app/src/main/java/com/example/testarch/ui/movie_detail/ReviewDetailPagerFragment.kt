package com.example.testarch.ui.movie_detail

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testarch.ui.movie_detail.domain.entity.Review
import com.example.testarch.ui.movie_detail.utils.FullViewChildSupportFragment
import dagger.hilt.android.AndroidEntryPoint

object ReviewDetailPagerFragmentArgs {
    const val MOVIE_ID = "movieId"
    const val KEY_PAGE_NO = "pageNo"
}

@AndroidEntryPoint
class ReviewDetailPagerFragment: FullViewChildSupportFragment() {
    companion object {
        fun newInstance(movieId: Int, pageNo: Int = 0) = ReviewDetailPagerFragment().apply {
            arguments = Bundle().apply {
                putInt(ReviewDetailPagerFragmentArgs.KEY_PAGE_NO, pageNo)
                putInt(ReviewDetailPagerFragmentArgs.MOVIE_ID, movieId)
            }
        }
    }

    private val reviewsViewModel: ReviewsViewModel by viewModels(
        ownerProducer = { findSupportParentFragment<ReviewDataHolder>() }
    )

    private val pageNo by lazy { arguments?.getInt(ReviewDetailPagerFragmentArgs.KEY_PAGE_NO)?: 0 }
    private val movieId by lazy { arguments?.getInt(ReviewDetailPagerFragmentArgs.MOVIE_ID)?: 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reviewsViewModel.getReviews(movieId)
    }

    override fun applyMainView(): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    val reviews = reviewsViewModel.reviews.collectAsLazyPagingItems()
                    ReviewDetailPager(
                        initialPage = pageNo,
                        reviews = reviews,
                        onClick = {
                            move2Fragment(MovieDetailFragment.newInstance(3))
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ReviewDetailPager(
    initialPage: Int,
    reviews: LazyPagingItems<Review>,
    onClick: () -> Unit
) {
    var pageNo by remember { mutableIntStateOf(initialPage) }

    Scaffold(
        topBar = {
            ReviewDetailPagerTopBar(
                pageNo = pageNo,
                reviews = reviews,
                onClick = onClick
            )
        },
        content = { padding ->
            ReviewDetailPagerContent(
                modifier = Modifier.padding(padding),
                initialPage = initialPage,
                reviews = reviews,
                onPageChange = { pageNo = it }
            )
        }
    )
}

@Composable
fun ReviewDetailPagerTopBar(pageNo: Int, reviews: LazyPagingItems<Review>, onClick: () -> Unit) {
    Row {
        Text(
            text = reviews[pageNo]!!.title,
            style = MaterialTheme.typography.titleLarge
        )
        Button(onClick = onClick) {
            Text(text = "Click me!!")
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReviewDetailPagerContent(
    modifier: Modifier = Modifier,
    initialPage: Int,
    reviews: LazyPagingItems<Review>,
    onPageChange: (Int) -> Unit) {
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { reviews.itemCount }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            onPageChange(it)
        }
    }

    HorizontalPager(state = pagerState, modifier = modifier) { page ->
        ReviewDetail(reviews[page]!!)  // Replace with your own review detail component here
    }
}


@Composable
fun ReviewDetail(review: Review) {
    Column {
        Text(text = review.title)
        Text(text = review.content)
        Text(text = review.author)
    }
}

