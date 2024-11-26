package com.example.testarch.ui.movie_detail

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testarch.ui.movie_detail.domain.entity.Review
import com.example.testarch.ui.movie_detail.utils.FullViewChildSupportFragment

object ReviewListFragmentArgs {
    const val MOVIE_ID = "movieId"
}

class ReviewListFragment: FullViewChildSupportFragment() {
    companion object {
        fun newInstance(movieId: Int) = ReviewListFragment().apply {
            arguments = Bundle().apply {
                putInt(ReviewListFragmentArgs.MOVIE_ID, movieId)
            }
        }
    }

    private val reviewsViewModel: ReviewsViewModel by viewModels(
        ownerProducer = { findSupportParentFragment<ReviewDataHolder>() }
    )

    private val movieId by lazy { arguments?.getInt(ReviewListFragmentArgs.MOVIE_ID)?: 0 }

    override fun applyMainView(): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    val reviews = reviewsViewModel.reviews.collectAsLazyPagingItems()
                    ReviewList(
                        movieId = movieId,
                        reviews = reviews,
                        onClickAReview = { index, _ ->
                            move2ChildFragment(
                                ReviewDetailPagerFragment.newInstance(
                                    pageNo = index,
                                    movieId = movieId
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewList(
    movieId: Int,
    reviews: LazyPagingItems<Review>,
    onClickAReview: (Int, Review) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ReviewListTopBar(
                scrollBehavior = scrollBehavior,
                movieId = movieId
            )
        },
        content = { padding ->
            ReviewListContent(
                modifier = Modifier.padding(padding),
                reviews = reviews,
                onClickAReview = onClickAReview
            )
        }
    )
}

@Composable
fun ReviewListContent(
    modifier: Modifier,
    reviews: LazyPagingItems<Review>,
    onClickAReview: (Int, Review) -> Unit
) {
    val state = rememberLazyListState()
    LazyColumn(state = state, modifier = modifier) {
        items(count = reviews.itemCount) { index ->
            val review = reviews[index]!!
            ClickableText(
                text = AnnotatedString(review.title),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                onClick = { onClickAReview(index, review) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewListTopBar(movieId: Int, scrollBehavior: TopAppBarScrollBehavior) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary),
        title = {
            Text(text = "Review List for Movie ID: $movieId")
        },
        scrollBehavior = scrollBehavior
    )
}
