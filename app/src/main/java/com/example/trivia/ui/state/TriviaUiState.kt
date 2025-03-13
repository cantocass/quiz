package com.example.trivia.ui.state

import com.example.trivia.domain.model.TriviaQuestion

data class TriviaUiState(
    val currentQuestion: TriviaQuestion? = null,
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 0,
    val lastAnswerCorrect: Boolean? = null,
    val isComplete: Boolean = false,
    val score: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
