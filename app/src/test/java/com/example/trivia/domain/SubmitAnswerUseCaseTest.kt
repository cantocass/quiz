package com.example.trivia.domain

import com.example.trivia.data.TriviaRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class SubmitAnswerUseCaseTest {

    private lateinit var repository: TriviaRepository
    private lateinit var submitAnswerUseCase: SubmitAnswerUseCase

    @Before
    fun setup() {
        repository = mock()
        submitAnswerUseCase = SubmitAnswerUseCase(repository)
    }

    @Test
    fun `invoke calls repository submitAnswer with correct parameters`() = runBlocking {
        // Arrange
        val questionId = 1
        val answer = "Paris"
        whenever(repository.submitAnswer(questionId, answer)).thenReturn(true)

        // Act
        val result = submitAnswerUseCase(questionId, answer)

        // Assert
        verify(repository).submitAnswer(questionId, answer)
        assertTrue(result)
    }

    @Test
    fun `invoke returns false when answer is incorrect`() = runBlocking {
        // Arrange
        val questionId = 1
        val answer = "London"
        whenever(repository.submitAnswer(questionId, answer)).thenReturn(false)

        // Act
        val result = submitAnswerUseCase(questionId, answer)

        // Assert
        verify(repository).submitAnswer(questionId, answer)
        assertFalse(result)
    }

    @Test
    fun `invoke works with different answer types`() = runBlocking {
        // Boolean answer
        val booleanAnswer = true
        whenever(repository.submitAnswer(1, booleanAnswer)).thenReturn(true)
        assertTrue(submitAnswerUseCase(1, booleanAnswer))

        // Set answer (multiple selection)
        val setAnswer = setOf("A", "B")
        whenever(repository.submitAnswer(2, setAnswer)).thenReturn(false)
        assertFalse(submitAnswerUseCase(2, setAnswer))
    }
}
