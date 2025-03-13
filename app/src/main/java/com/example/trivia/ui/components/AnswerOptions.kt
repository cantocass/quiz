package com.example.trivia.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trivia.ui.theme.QuizAppTheme

/**
 * Component for displaying multiple-choice single selection options
 */
@Composable
fun SingleChoiceOptions(
    alternatives: List<String>,
    onSelectAnswer: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        alternatives.forEach { alternative ->
            Button(
                onClick = { onSelectAnswer(alternative) },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            ) {
                Text(text = alternative)
            }
        }
    }
}

/**
 * Component for displaying True/False question options
 */
@Composable
fun TrueFalseOptions(
    onSelectAnswer: (Boolean) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { onSelectAnswer(true) },
            modifier = Modifier.weight(1f),
            enabled = enabled
        ) {
            Text("True")
        }
        Button(
            onClick = { onSelectAnswer(false) },
            modifier = Modifier.weight(1f),
            enabled = enabled
        ) {
            Text("False")
        }
    }
}

/**
 * Component for displaying multiple-choice multiple selection options
 */
@Composable
fun MultipleChoiceOptions(
    alternatives: List<String>,
    selectedAnswers: Set<String>,
    onSelectAnswer: (String, Boolean) -> Unit,
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
                onClick = { onSelectAnswer(alternative, !isSelected) },
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

/**
 * Component for open-ended text input questions
 */
@Composable
fun OpenEndedAnswerInput(
    onSubmitAnswer: (String) -> Unit,
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
            onClick = { onSubmitAnswer(text) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled && text.isNotBlank()
        ) {
            Text("Submit Answer")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleChoiceOptionsPreview() {
    QuizAppTheme {
        SingleChoiceOptions(
            alternatives = listOf("Paris", "London", "Berlin", "Madrid"),
            onSelectAnswer = {},
            enabled = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TrueFalseOptionsPreview() {
    QuizAppTheme {
        TrueFalseOptions(
            onSelectAnswer = {},
            enabled = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MultipleChoiceOptionsPreview() {
    QuizAppTheme {
        MultipleChoiceOptions(
            alternatives = listOf("Activity", "Service", "Fragment", "ViewModel"),
            selectedAnswers = setOf("Activity", "Service"),
            onSelectAnswer = { _, _ -> },
            onSubmit = {},
            enabled = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OpenEndedAnswerInputPreview() {
    QuizAppTheme {
        OpenEndedAnswerInput(
            onSubmitAnswer = {},
            enabled = true,
            maxLength = 100
        )
    }
}
