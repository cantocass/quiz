package com.example.trivia.di

import com.example.trivia.data.TriviaRepository
import com.example.trivia.data.TriviaRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTriviaRepository(
        repository: TriviaRepositoryImpl
    ): TriviaRepository
}
