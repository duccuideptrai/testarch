package com.example.testarch.ui.movie_detail.di

import com.example.testarch.ui.movie_detail.data.ReviewApiRepositoryImpl
import com.example.testarch.ui.movie_detail.data.ReviewStorageRepositoryImpl
import com.example.testarch.ui.movie_detail.domain.repository.ReviewApiRepository
import com.example.testarch.ui.movie_detail.domain.repository.ReviewStorageRepository
import com.example.testarch.ui.movie_detail.domain.usercase.GetReviewDataSourceFlowUserCase
import com.example.testarch.ui.movie_detail.domain.usercase.GetReviewsUserCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class ReviewDi {
    @Provides
    @ViewModelScoped
    fun provideReviewStorageRepository(): ReviewStorageRepository {
        return ReviewStorageRepositoryImpl()
    }

    @Provides
    fun provideReviewApiRepository(): ReviewApiRepository {
        return ReviewApiRepositoryImpl()
    }

    @Provides
    fun provideReviewUserCase(
        reviewStorageRepository: ReviewStorageRepository,
        reviewApiRepository: ReviewApiRepository
    ): GetReviewsUserCase {
        return GetReviewsUserCase(
            reviewStorageRepository,
            reviewApiRepository
        )
    }

    @Provides
    fun provideGetReviewDataSourceFlowUseCase(
        reviewStorageRepository: ReviewStorageRepository
    ): GetReviewDataSourceFlowUserCase {
        return GetReviewDataSourceFlowUserCase(reviewStorageRepository)
    }
}