package com.example.testarch.ui.movie_detail

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testarch.ui.movie_detail.domain.entity.Review
import com.example.testarch.ui.movie_detail.utils.FullViewChildSupportFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

object MovieDetailFragmentArgs {
    const val KEY_MOVIE_ID = "movieId"
}

interface ReviewDataHolder

@AndroidEntryPoint
class MovieDetailFragment: FullViewChildSupportFragment(), ReviewDataHolder {
    companion object {
        fun newInstance(movieId: Int = 1) = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(MovieDetailFragmentArgs.KEY_MOVIE_ID, movieId)
            }
        }
    }
    private val viewModel by viewModels<ReviewsViewModel>()
    private var movieId by Delegates.notNull<Int>()

    override fun applyMainView(): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    val reviews = viewModel.reviews.collectAsLazyPagingItems()
                    MovieDetail(
                        movieId = movieId,
                        reviews = reviews,
                        onClickReview = { index, _ ->
                            move2ChildFragment(ReviewDetailPagerFragment.newInstance(pageNo = index, movieId = movieId))
                        },
                        onClickTop = {
                            move2Fragment(newInstance(movieId = 5))
                        },
                        onClickShowListReviews = {
                            move2ChildFragment(ReviewListFragment.newInstance(movieId = movieId))
                        }
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = arguments?.getInt(MovieDetailFragmentArgs.KEY_MOVIE_ID)?: 1
        viewModel.getReviews(movieId)
    }
}

@Composable
fun MovieDetail(
    movieId: Int,
    reviews: LazyPagingItems<Review>,
    onClickReview: (Int, Review) -> Unit,
    onClickTop: () -> Unit = {},
    onClickShowListReviews: () -> Unit
) {
    Scaffold(
        topBar = {
            MovieDetailTopBar(movieId = movieId, onClick = onClickTop)
        },
        content = { padding ->
            MovieDetailContent(
                modifier = Modifier.padding(padding),
                movieId = movieId,
                reviews = reviews,
                onClickAReview = onClickReview,
                onClickShowListReviews = onClickShowListReviews
            )
        }
    )
}

@Composable
fun MovieDetailContent(
    modifier: Modifier = Modifier,
    movieId: Int,
    reviews: LazyPagingItems<Review>,
    onClickAReview: (Int, Review) -> Unit,
    onClickShowListReviews: () -> Unit
) {
    val state = rememberLazyListState()
    LazyColumn(state = state, modifier = modifier) {
        item(key = "movieId") {
            Text(text = "MovieID: $movieId", style = MaterialTheme.typography.labelLarge)
            HorizontalDivider()
        }

        for (index in 0..< reviews.itemCount.coerceAtMost(3)) {
            item(key = "Review_$index") {
                val review = reviews[index]!!
                TextButton(onClick = { onClickAReview(index, review) }) {
                    Text(text = review.title)
                }
            }
        }

        if (reviews.itemCount > 3) {
            item(key = "show_reviews_list") {
                Button(onClick = onClickShowListReviews) {
                    Text(text = "View List Reviews")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailTopBar(movieId: Int, onClick: () -> Unit = {}) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary),
        title = {
            Text(
                text = "Movie Detail $movieId",
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(
                onClick = onClick,
                enabled = false,
                colors = IconButtonDefaults.iconButtonColors().copy(
                    contentColor = Color.Red,
                    disabledContentColor = Color.Red.copy(alpha = 0.5f)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    tint = LocalContentColor.current,
                    contentDescription = ""
                )
            }
        }
    )
}

@Composable
fun MovieDetailText(text: String) {
    Text(
        text = text,
        color = Color.Black,
    )
}

@Preview
@Composable
private fun MovieDetailTextPreview() {
    MovieDetailText(text = "Hello")
}