package com.example.trivia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trivia.ui.components.AnswerFeedback
import com.example.trivia.ui.components.QuestionContent
import com.example.trivia.ui.components.QuizComplete
import com.example.trivia.ui.components.QuizProgress

@Composable
fun TriviaScreen(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuizProgress(
            currentIndex = uiState.currentQuestionIndex,
            totalQuestions = uiState.totalQuestions,
            score = uiState.score,
            isComplete = uiState.isComplete,
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState.isComplete) {
            QuizComplete(
                score = uiState.score,
                totalQuestions = uiState.totalQuestions,
                modifier = Modifier.weight(1f)
            )
        } else {
            uiState.currentQuestion?.let { question ->
                QuestionContent(
                    question = question,
                    onAnswerSubmitted = { answer ->
                        viewModel.submitAnswer(question.id, answer)
                    },
                    enabled = uiState.lastAnswerCorrect == null,
                    modifier = Modifier.weight(1f)
                )

                uiState.lastAnswerCorrect?.let { isCorrect ->
                    AnswerFeedback(
                        isCorrect = isCorrect,
                        onNextQuestion = { viewModel.moveToNextQuestion() }
                    )
                }
            }
        }
    }
}
