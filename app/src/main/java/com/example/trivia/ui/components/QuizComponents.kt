package com.example.trivia.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trivia.domain.model.TriviaQuestion
import kotlin.math.min

@Composable
fun QuizProgressIndicator(
    currentQuestion: Int,
    totalQuestions: Int,
    isComplete: Boolean = false,
    modifier: Modifier = Modifier
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
    isComplete: Boolean = false,
    modifier: Modifier = Modifier
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

@Composable
fun QuestionContent(
    question: TriviaQuestion,
    onAnswerSubmitted: (Any) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = question.stem,
            style = MaterialTheme.typography.headlineSmall
        )

        when (question) {
            is TriviaQuestion.MultipleChoice -> {
                SingleChoiceOptions(
                    alternatives = question.alternatives,
                    onAnswerSelected = onAnswerSubmitted,
                    enabled = enabled
                )
            }

            is TriviaQuestion.TrueFalse -> {
                TrueFalseOptions(
                    onAnswerSelected = onAnswerSubmitted,
                    enabled = enabled
                )
            }

            is TriviaQuestion.MultipleAnswer -> {
                var selectedAnswers by remember { mutableStateOf(setOf<String>()) }

                MultipleChoiceOptions(
                    alternatives = question.alternatives,
                    selectedAnswers = selectedAnswers,
                    onAnswerSelected = { answer, isSelected ->
                        selectedAnswers = if (isSelected) {
                            selectedAnswers + answer
                        } else {
                            selectedAnswers - answer
                        }
                    },
                    onSubmit = { onAnswerSubmitted(selectedAnswers) },
                    enabled = enabled
                )
            }

            is TriviaQuestion.OpenEnded -> {
                OpenEndedAnswerInput(
                    onAnswerSubmitted = { onAnswerSubmitted(it) },
                    enabled = enabled,
                    maxLength = question.maxLength
                )
            }
        }
    }
}

@Composable
private fun SingleChoiceOptions(
    alternatives: List<String>,
    onAnswerSelected: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        alternatives.forEach { alternative ->
            Button(
                onClick = { onAnswerSelected(alternative) },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            ) {
                Text(text = alternative)
            }
        }
    }
}

@Composable
private fun TrueFalseOptions(
    onAnswerSelected: (Boolean) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { onAnswerSelected(true) },
            modifier = Modifier.weight(1f),
            enabled = enabled
        ) {
            Text("True")
        }
        Button(
            onClick = { onAnswerSelected(false) },
            modifier = Modifier.weight(1f),
            enabled = enabled
        ) {
            Text("False")
        }
    }
}

@Composable
private fun MultipleChoiceOptions(
    alternatives: List<String>,
    selectedAnswers: Set<String>,
    onAnswerSelected: (String, Boolean) -> Unit,
    onSubmit: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        alternatives.forEach { alternative ->
            val isSelected = selectedAnswers.contains(alternative)
            OutlinedButton(
                onClick = { onAnswerSelected(alternative, !isSelected) },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Text(text = alternative)
            }
        }

        if (selectedAnswers.isNotEmpty()) {
            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            ) {
                Text("Submit Answers")
            }
        }
    }
}

@Composable
private fun OpenEndedAnswerInput(
    onAnswerSubmitted: (String) -> Unit,
    enabled: Boolean,
    maxLength: Int,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val remainingChars = maxLength - text.length

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                if (it.length <= maxLength) {
                    text = it
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Your answer") },
            supportingText = { Text("$remainingChars characters remaining") },
            enabled = enabled,
            singleLine = false,
            maxLines = 3
        )

        Button(
            onClick = { onAnswerSubmitted(text) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled && text.isNotBlank()
        ) {
            Text("Submit Answer")
        }
    }
}

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
