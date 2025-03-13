package com.example.trivia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trivia.domain.model.TriviaQuestion
import com.example.trivia.ui.components.AnswerFeedback
import com.example.trivia.ui.components.QuestionContent
import com.example.trivia.ui.components.QuizComplete
import com.example.trivia.ui.components.QuizProgress
import com.example.trivia.ui.state.TriviaUiState
import com.example.trivia.ui.theme.QuizAppTheme

/**
 * @param uiState The state of the trivia screen
 * @param onAnswerSubmit A callback to submit an answer
 * @param onNextQuestion A callback to move to the next question
 * @param modifier A modifier for the composable
 */
@Composable
fun TriviaScreen(
    uiState: TriviaUiState,
    onAnswerSubmit: (Int, Any) -> Unit,
    onNextQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    onSubmitAnswer = { answer ->
                        onAnswerSubmit(question.id, answer)
                    },
                    enabled = uiState.lastAnswerCorrect == null,
                    modifier = Modifier.weight(1f)
                )

                uiState.lastAnswerCorrect?.let { isCorrect ->
                    AnswerFeedback(
                        isCorrect = isCorrect,
                        onNextQuestion = onNextQuestion
                    )
                }
            }
        }
    }
}

/**
 * Wrapper composable that connects the TriviaScreen with a ViewModel
 * @param viewModel The view model
 * @param modifier A modifier for the composable
 */
@Composable
fun TriviaScreenWithViewModel(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    TriviaScreen(
        uiState = uiState,
        onAnswerSubmit = { questionId, answer ->
            viewModel.submitAnswer(questionId, answer)
        },
        onNextQuestion = {
            viewModel.moveToNextQuestion()
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun TriviaScreenPreview() {
    QuizAppTheme {
        TriviaScreen(
            uiState = TriviaUiState(
                currentQuestionIndex = 0,
                totalQuestions = 10,
                score = 5,
                isComplete = false,
                currentQuestion = TriviaQuestion.MultipleChoice(
                    id = 1,
                    stem = "What is the capital of France?",
                    alternatives = listOf("Paris", "London", "Berlin", "Madrid"),
                    correctAnswer = "Paris"
                ),
                lastAnswerCorrect = null
            ),
            onAnswerSubmit = { _, _ -> },
            onNextQuestion = { },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TriviaScreenWithFeedbackPreview() {
    QuizAppTheme {
        TriviaScreen(
            uiState = TriviaUiState(
                currentQuestionIndex = 2,
                totalQuestions = 10,
                score = 2,
                isComplete = false,
                currentQuestion = TriviaQuestion.TrueFalse(
                    id = 3,
                    stem = "Kotlin is the official language for Android development.",
                    correctAnswer = true
                ),
                lastAnswerCorrect = true
            ),
            onAnswerSubmit = { _, _ -> },
            onNextQuestion = { },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TriviaScreenCompletePreview() {
    QuizAppTheme {
        TriviaScreen(
            uiState = TriviaUiState(
                currentQuestionIndex = 10,
                totalQuestions = 10,
                score = 8,
                isComplete = true,
                currentQuestion = null,
                lastAnswerCorrect = null
            ),
            onAnswerSubmit = { _, _ -> },
            onNextQuestion = { },
            modifier = Modifier.padding(16.dp)
        )
    }
}
