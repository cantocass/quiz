package com.example.trivia.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.trivia.domain.model.TriviaQuestion
import com.example.trivia.ui.components.QuestionContent
import org.junit.Rule
import org.junit.Test

class QuestionContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun multipleChoiceQuestionDisplaysAllOptions() {
        // Arrange
        val question = TriviaQuestion.MultipleChoice(
            id = 1,
            stem = "Which is the capital of France?",
            alternatives = listOf("London", "Paris", "Berlin", "Madrid"),
            correctAnswer = "Paris"
        )
        
        // Act
        composeTestRule.setContent {
            QuestionContent(
                question = question,
                onAnswerSubmitted = {},
                enabled = true
            )
        }
        
        // Assert
        composeTestRule.onNodeWithText("Which is the capital of France?").assertIsDisplayed()
        composeTestRule.onNodeWithText("London").assertIsDisplayed()
        composeTestRule.onNodeWithText("Paris").assertIsDisplayed()
        composeTestRule.onNodeWithText("Berlin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Madrid").assertIsDisplayed()
    }
    
    @Test
    fun trueFalseQuestionDisplaysBothOptions() {
        // Arrange
        val question = TriviaQuestion.TrueFalse(
            id = 2,
            stem = "Paris is the capital of France",
            correctAnswer = true
        )
        
        // Act
        composeTestRule.setContent {
            QuestionContent(
                question = question,
                onAnswerSubmitted = {},
                enabled = true
            )
        }
        
        // Assert
        composeTestRule.onNodeWithText("Paris is the capital of France").assertIsDisplayed()
        composeTestRule.onNodeWithText("True").assertIsDisplayed()
        composeTestRule.onNodeWithText("False").assertIsDisplayed()
    }
    
    @Test
    fun disabledQuestionHasDisabledOptions() {
        // Arrange
        val question = TriviaQuestion.MultipleChoice(
            id = 1,
            stem = "Which is the capital of France?",
            alternatives = listOf("London", "Paris", "Berlin", "Madrid"),
            correctAnswer = "Paris"
        )
        
        // Act
        composeTestRule.setContent {
            QuestionContent(
                question = question,
                onAnswerSubmitted = {},
                enabled = false
            )
        }
        
        // Assert
        composeTestRule.onNodeWithText("London").assertIsNotEnabled()
        composeTestRule.onNodeWithText("Paris").assertIsNotEnabled()
    }
    
    @Test
    fun clickingOptionCallsOnAnswerSubmitted() {
        // Arrange
        val question = TriviaQuestion.MultipleChoice(
            id = 1,
            stem = "Which is the capital of France?",
            alternatives = listOf("London", "Paris", "Berlin", "Madrid"),
            correctAnswer = "Paris"
        )
        var answerSubmitted = false
        
        // Act
        composeTestRule.setContent {
            QuestionContent(
                question = question,
                onAnswerSubmitted = { answerSubmitted = true },
                enabled = true
            )
        }
        
        composeTestRule.onNodeWithText("Paris").performClick()
        
        // Assert
        assert(answerSubmitted)
    }

    @Test
    fun openEndedQuestionDisplaysInputField() {
        // Arrange
        val question = TriviaQuestion.OpenEnded(
            id = 6,
            stem = "What is the name of the Android build system tool that replaced Ant?",
            correctAnswer = "Gradle",
            exactMatch = true
        )
        
        // Act
        composeTestRule.setContent {
            QuestionContent(
                question = question,
                onAnswerSubmitted = {},
                enabled = true
            )
        }
        
        // Assert
        composeTestRule.onNodeWithText("What is the name of the Android build system tool that replaced Ant?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Your answer").assertIsDisplayed()
        composeTestRule.onNodeWithText("100 characters remaining").assertIsDisplayed()
    }
}