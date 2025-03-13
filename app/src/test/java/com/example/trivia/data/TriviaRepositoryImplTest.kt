package com.example.trivia.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TriviaRepositoryImplTest {

    private lateinit var repository: TriviaRepositoryImpl

    @Before
    fun setup() {
        repository = TriviaRepositoryImpl()
    }

    @Test
    fun `getQuestions returns sample questions`() = runBlocking {
        // Act
        val questions = repository.getQuestions().first()

        // Assert
        assertNotNull(questions)
        assertTrue(questions.isNotEmpty())
        assertEquals(6, questions.size)
    }

    @Test
    fun `submitAnswer validates multiple choice correctly`() = runBlocking {
        // Act & Assert
        assertTrue(repository.submitAnswer(1, "Kotlin"))
        assertFalse(repository.submitAnswer(1, "Swift"))
    }

    @Test
    fun `submitAnswer validates true-false correctly`() = runBlocking {
        // Act & Assert
        assertTrue(repository.submitAnswer(2, true))
        assertFalse(repository.submitAnswer(2, false))
    }

    @Test
    fun `submitAnswer validates multiple answers correctly`() = runBlocking {
        // Create a valid answer set
        val correctAnswers = setOf("Activity", "Service", "Content Provider", "Broadcast Receiver")

        // Act & Assert
        assertTrue(repository.submitAnswer(3, correctAnswers))

        // Test with wrong answers
        val wrongAnswers = setOf("Activity", "Service", "Fragment")
        assertFalse(repository.submitAnswer(3, wrongAnswers))

        // Test with incomplete answers
        val incompleteAnswers = setOf("Activity", "Service")
        assertFalse(repository.submitAnswer(3, incompleteAnswers))
    }

    @Test
    fun `submitAnswer returns false for non-existent question ID`() = runBlocking {
        // Act & Assert
        assertFalse(repository.submitAnswer(999, "Any answer"))
    }

    @Test
    fun `submitAnswer validates open-ended correctly`() = runBlocking {
        // Act & Assert for exact match
        assertTrue(repository.submitAnswer(6, "Gradle"))
        assertFalse(repository.submitAnswer(6, "Maven")) // Wrong answer is rejected
    }
}
