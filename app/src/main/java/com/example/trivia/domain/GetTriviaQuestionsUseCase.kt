package com.example.trivia.domain

import com.example.trivia.data.TriviaRepository
import com.example.trivia.domain.model.TriviaQuestion
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTriviaQuestionsUseCase @Inject constructor(
    private val repository: TriviaRepository
) {
    suspend operator fun invoke(): Flow<List<TriviaQuestion>> {
        return repository.getQuestions()
    }
}
