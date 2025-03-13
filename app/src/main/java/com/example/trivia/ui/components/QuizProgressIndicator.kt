package com.example.trivia.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trivia.ui.theme.QuizAppTheme
import kotlin.math.min

/**
 * Displays the quiz progress as an animated bar with a text indicator
 */
@Composable
fun QuizProgressIndicator(
    currentQuestion: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier,
    isComplete: Boolean = false,
) {
    val displayQuestion = min(currentQuestion + 1, totalQuestions)
    val progress by animateFloatAsState(
        targetValue = if (totalQuestions > 0) {
            if (isComplete) {
                1f // Show full progress when quiz is complete
            } else {
                min(currentQuestion.toFloat() / totalQuestions.toFloat(), 1f)
            }
        } else {
            0f
        },
        animationSpec = tween(durationMillis = 300),
        label = "Progress"
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        ) {
            // Track
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            ) {}

            // Progress
            Surface(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight(),
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            ) {}
        }

        // Question counter (only shown if quiz is not complete)
        if (!isComplete) {
            Text(
                text = "Question $displayQuestion of $totalQuestions",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            // Show completion text when quiz is complete
            Text(
                text = "Quiz complete",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun QuizProgress(
    currentIndex: Int,
    totalQuestions: Int,
    score: Int,
    modifier: Modifier = Modifier,
    isComplete: Boolean = false,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuizProgressIndicator(
            currentQuestion = currentIndex,
            totalQuestions = totalQuestions,
            isComplete = isComplete,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Score: $score",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizProgressIndicatorPreview() {
    QuizAppTheme {
        QuizProgressIndicator(
            currentQuestion = 2,
            totalQuestions = 6,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizProgressIndicatorCompletePreview() {
    QuizAppTheme {
        QuizProgressIndicator(
            currentQuestion = 6,
            totalQuestions = 6,
            isComplete = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizProgressPreview() {
    QuizAppTheme {
        QuizProgress(
            currentIndex = 3,
            totalQuestions = 6,
            score = 2,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizProgressCompletePreview() {
    QuizAppTheme {
        QuizProgress(
            currentIndex = 6,
            totalQuestions = 6,
            score = 5,
            isComplete = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
