package com.example.trivia.data

import com.example.trivia.domain.model.TriviaQuestion
import kotlinx.coroutines.flow.Flow

interface TriviaRepository {
    suspend fun getQuestions(): Flow<List<TriviaQuestion>>
    suspend fun submitAnswer(questionId: Int, answer: Any): Boolean
}
