package com.example.trivia.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trivia.domain.model.TriviaQuestion
import com.example.trivia.ui.theme.QuizAppTheme

/**
 * Displays the current question and appropriate input controls based on question type
 */
@Composable
fun QuestionContent(
    question: TriviaQuestion,
    onSubmitAnswer: (Any) -> Unit,
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
                    onSelectAnswer = onSubmitAnswer,
                    enabled = enabled
                )
            }

            is TriviaQuestion.TrueFalse -> {
                TrueFalseOptions(
                    onSelectAnswer = onSubmitAnswer,
                    enabled = enabled
                )
            }

            is TriviaQuestion.MultipleAnswer -> {
                var selectedAnswers by remember { mutableStateOf(setOf<String>()) }

                MultipleChoiceOptions(
                    alternatives = question.alternatives,
                    selectedAnswers = selectedAnswers,
                    onSelectAnswer = { answer, isSelected ->
                        selectedAnswers = if (isSelected) {
                            selectedAnswers + answer
                        } else {
                            selectedAnswers - answer
                        }
                    },
                    onSubmit = { onSubmitAnswer(selectedAnswers) },
                    enabled = enabled
                )
            }

            is TriviaQuestion.OpenEnded -> {
                OpenEndedAnswerInput(
                    onSubmitAnswer = { onSubmitAnswer(it) },
                    enabled = enabled,
                    maxLength = question.maxLength
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MultipleChoiceQuestionPreview() {
    QuizAppTheme {
        QuestionContent(
            question = TriviaQuestion.MultipleChoice(
                id = 1,
                stem = "Which of the following is the preferred programming language for Android app development?",
                alternatives = listOf("Swift", "Kotlin", "Objective-C", "Java"),
                correctAnswer = "Kotlin"
            ),
            onSubmitAnswer = {},
            enabled = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TrueFalseQuestionPreview() {
    QuizAppTheme {
        QuestionContent(
            question = TriviaQuestion.TrueFalse(
                id = 2,
                stem = "Android Studio is the official IDE for Android development.",
                correctAnswer = true
            ),
            onSubmitAnswer = {},
            enabled = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OpenEndedQuestionPreview() {
    QuizAppTheme {
        QuestionContent(
            question = TriviaQuestion.OpenEnded(
                id = 6,
                stem = "What is the name of the Android build system tool that replaced Ant?",
                correctAnswer = "Gradle",
                exactMatch = true
            ),
            onSubmitAnswer = {},
            enabled = true
        )
    }
}
