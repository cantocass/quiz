package com.example.trivia.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TriviaQuestionTest {

    @Test
    fun openEndedBasicTest() {
        // Simple exact match test - case sensitive by default
        val exactMatch = TriviaQuestion.OpenEnded(
            id = 1,
            stem = "Test question",
            correctAnswer = "Gradle",
            exactMatch = true,
            caseSensitive = true
        )

        // Basic assertions
        assertTrue(exactMatch.validateAnswer("Gradle")) // Exact match works
        assertFalse(exactMatch.validateAnswer("Maven")) // Wrong answer fails
        assertFalse(exactMatch.validateAnswer("gradle")) // Case sensitivity is enforced

        // Contains test - answer must contain the correct string
        val containsMatch = TriviaQuestion.OpenEnded(
            id = 2,
            stem = "Test question",
            correctAnswer = "Gradle",
            exactMatch = false
        )

        assertTrue(containsMatch.validateAnswer("I use Gradle")) // Contains correct answer
        assertFalse(containsMatch.validateAnswer("I use Maven")) // Doesn't contain correct answer
    }

    @Test
    fun `validateAnswer correctly validates MultipleChoice answers`() {
        val question = TriviaQuestion.MultipleChoice(
            id = 1,
            stem = "Which is the capital of France?",
            alternatives = listOf("London", "Paris", "Berlin", "Madrid"),
            correctAnswer = "Paris"
        )

        // Test correct answer
        assertTrue(question.validateAnswer("Paris"))

        // Test incorrect answer
        assertFalse(question.validateAnswer("London"))

        // Test invalid type
        assertFalse(question.validateAnswer(true))
        assertFalse(question.validateAnswer(setOf("Paris")))
    }

    @Test
    fun `validateAnswer correctly validates TrueFalse answers`() {
        val question = TriviaQuestion.TrueFalse(
            id = 1,
            stem = "Paris is the capital of France",
            correctAnswer = true
        )

        // Test correct answer
        assertTrue(question.validateAnswer(true))

        // Test incorrect answer
        assertFalse(question.validateAnswer(false))

        // Test invalid type
        assertFalse(question.validateAnswer("true"))
        assertFalse(question.validateAnswer(setOf(true)))
    }

    @Test
    fun `validateAnswer correctly validates MultipleAnswer answers`() {
        val question = TriviaQuestion.MultipleAnswer(
            id = 1,
            stem = "Which are European capitals?",
            alternatives = listOf("London", "Paris", "New York", "Tokyo", "Berlin"),
            correctAnswers = setOf("London", "Paris", "Berlin")
        )

        // Test correct answer (order shouldn't matter)
        assertTrue(question.validateAnswer(setOf("Paris", "London", "Berlin")))

        // Test incorrect answers - missing one
        assertFalse(question.validateAnswer(setOf("London", "Paris")))

        // Test incorrect answers - extra one
        assertFalse(question.validateAnswer(setOf("London", "Paris", "Berlin", "Tokyo")))

        // Test incorrect answers - wrong selection
        assertFalse(question.validateAnswer(setOf("London", "Paris", "Tokyo")))

        // Test invalid type
        assertFalse(question.validateAnswer("London"))
        assertFalse(question.validateAnswer(true))
    }
}
