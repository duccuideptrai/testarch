package com.example.testarch.ui.movie_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testarch.ui.movie_detail.domain.entity.Review
import com.example.testarch.ui.movie_detail.utils.FullViewChildSupportFragment
import com.example.testarch.ui.movie_detail.utils.move2Fragment
import com.example.testarch.ui.movie_detail.utils.move2FragmentByDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

object MovieDetailFragmentArgs {
    const val KEY_MOVIE_ID = "movieId"
}

@AndroidEntryPoint
class MovieDetailFragment: FullViewChildSupportFragment() {
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
                    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
                    MovieDetail(
                        movieId = movieId,
                        reviews = reviews,
                        onClickReview = { index, _ ->
                            move2Fragment(ReviewDetailPagerFragment.newInstance(pageNo = index))
                        },
                        onClickTop = {
                            move2Fragment(MovieDetailFragment.newInstance(movieId = 5), addToBackStack = false)
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

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return ComposeView(requireContext()).apply {
//            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
//            setContent {
//                MaterialTheme {
//                    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
//                    MovieDetail(
//                        movieId = movieId,
//                        reviews = reviews,
//                        onClick = { index, _ ->
//                            move2FragmentByDialog(ReviewDetailPagerFragment.newInstance(pageNo = index))
//                        }
//                    )
//                }
//            }
//        }
//    }
}

@Composable
fun MovieDetail(
    movieId: Int,
    reviews: List<Review>,
    onClickReview: (Int, Review) -> Unit,
    onClickTop: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            MovieDetailTopBar(movieId = movieId, onClick = onClickTop)
        },
        content = { padding ->
            MovieDetailContent(
                modifier = Modifier.padding(padding),
                reviews = reviews,
                onClick = onClickReview
            )
        }
    )
}

@Composable
fun MovieDetailContent(
    modifier: Modifier = Modifier,
    reviews: List<Review>,
    onClick: (Int, Review) -> Unit
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(
            items = reviews,
            key = { _, review -> review.title }) { index, review ->
            ClickableText(
                text = AnnotatedString(review.title),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                onClick = { onClick(index, review) }
            )
        }
    }
}

@Composable
fun MovieDetailTopBar(movieId: Int, onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Movie Detail $movieId",
            style = MaterialTheme.typography.titleLarge
        )
        Button(onClick = onClick) {
            Text(text = "Click me!!!")
        }
    }
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