package com.example.trivia.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trivia.ui.theme.QuizAppTheme

/**
 * Displays feedback after answering a question
 */
@Composable
fun AnswerFeedback(
    isCorrect: Boolean,
    onNextQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isCorrect) "Correct!" else "Incorrect",
            style = MaterialTheme.typography.titleLarge,
            color = if (isCorrect) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNextQuestion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next Question")
        }
    }
}

/**
 * Displays quiz completion screen with final score
 */
@Composable
fun QuizComplete(
    score: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Complete!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Final Score: $score out of $totalQuestions",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CorrectAnswerFeedbackPreview() {
    QuizAppTheme {
        AnswerFeedback(
            isCorrect = true,
            onNextQuestion = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IncorrectAnswerFeedbackPreview() {
    QuizAppTheme {
        AnswerFeedback(
            isCorrect = false,
            onNextQuestion = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizCompletePreview() {
    QuizAppTheme {
        QuizComplete(
            score = 4,
            totalQuestions = 6
        )
    }
}
