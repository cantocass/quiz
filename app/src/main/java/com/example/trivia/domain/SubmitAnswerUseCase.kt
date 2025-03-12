package com.example.trivia.domain

import com.example.trivia.data.TriviaRepository
import javax.inject.Inject

class SubmitAnswerUseCase @Inject constructor(
    private val repository: TriviaRepository
) {
    suspend operator fun invoke(questionId: Int, answer: Any): Boolean {
        return repository.submitAnswer(questionId, answer)
    }
}
