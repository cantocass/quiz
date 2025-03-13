package com.example.trivia.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.trivia.domain.GetTriviaQuestionsUseCase
import com.example.trivia.domain.SubmitAnswerUseCase
import com.example.trivia.domain.model.TriviaQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TriviaViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Use a test scope with the dispatcher to properly control coroutine execution
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getTriviaQuestionsUseCase: GetTriviaQuestionsUseCase

    @Mock
    private lateinit var submitAnswerUseCase: SubmitAnswerUseCase

    private lateinit var viewModel: TriviaViewModel
    private val sampleQuestions = listOf(
        TriviaQuestion.MultipleChoice(
            id = 1,
            stem = "Which is the capital of France?",
            alternatives = listOf("London", "Paris", "Berlin", "Madrid"),
            correctAnswer = "Paris"
        ),
        TriviaQuestion.TrueFalse(
            id = 2,
            stem = "Android is an operating system",
            correctAnswer = true
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty`() = runTest(testDispatcher) {
        // Arrange
        whenever(getTriviaQuestionsUseCase()).thenReturn(flowOf(emptyList()))

        // Act
        viewModel = TriviaViewModel(getTriviaQuestionsUseCase, submitAnswerUseCase)
        // Advance the dispatcher to allow coroutines to run
        testDispatcher.scheduler.advanceUntilIdle()
        val initialState = viewModel.uiState.value

        // Assert
        assertEquals(0, initialState.currentQuestionIndex)
        assertEquals(0, initialState.totalQuestions)
        assertNull(initialState.currentQuestion)
        assertNull(initialState.lastAnswerCorrect)
        assertFalse(initialState.isComplete)
        assertEquals(0, initialState.score)
    }

    @Test
    fun `loads questions on initialization`() = runTest(testDispatcher) {
        // Arrange
        whenever(getTriviaQuestionsUseCase()).thenReturn(flowOf(sampleQuestions))

        // Act
        viewModel = TriviaViewModel(getTriviaQuestionsUseCase, submitAnswerUseCase)
        // Advance the dispatcher to allow coroutines to run
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(2, viewModel.uiState.value.totalQuestions)
        assertEquals(sampleQuestions[0], viewModel.uiState.value.currentQuestion)
    }

    @Test
    fun `submit correct answer updates state`() = runTest(testDispatcher) {
        // Arrange
        whenever(getTriviaQuestionsUseCase()).thenReturn(flowOf(sampleQuestions))
        whenever(submitAnswerUseCase(1, "Paris")).thenReturn(true)
        viewModel = TriviaViewModel(getTriviaQuestionsUseCase, submitAnswerUseCase)
        // Advance the dispatcher to allow initialization to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.submitAnswer(1, "Paris")
        // Advance the dispatcher again to allow the submitAnswer coroutine to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.lastAnswerCorrect!!)
        assertEquals(1, viewModel.uiState.value.score)
    }

    @Test
    fun `submit incorrect answer updates state`() = runTest(testDispatcher) {
        // Arrange
        whenever(getTriviaQuestionsUseCase()).thenReturn(flowOf(sampleQuestions))
        whenever(submitAnswerUseCase(1, "London")).thenReturn(false)
        viewModel = TriviaViewModel(getTriviaQuestionsUseCase, submitAnswerUseCase)
        // Advance the dispatcher to allow initialization to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.submitAnswer(1, "London")
        // Advance the dispatcher again to allow the submitAnswer coroutine to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertFalse(viewModel.uiState.value.lastAnswerCorrect!!)
        assertEquals(0, viewModel.uiState.value.score)
    }

    @Test
    fun `move to next question advances to next question`() = runTest(testDispatcher) {
        // Arrange
        whenever(getTriviaQuestionsUseCase()).thenReturn(flowOf(sampleQuestions))
        viewModel = TriviaViewModel(getTriviaQuestionsUseCase, submitAnswerUseCase)
        // Advance the dispatcher to allow initialization to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.moveToNextQuestion()
        // Advance the dispatcher again after the action
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(1, viewModel.uiState.value.currentQuestionIndex)
        assertEquals(sampleQuestions[1], viewModel.uiState.value.currentQuestion)
        assertNull(viewModel.uiState.value.lastAnswerCorrect)
    }

    @Test
    fun `move past last question marks quiz as complete`() = runTest(testDispatcher) {
        // Arrange
        whenever(getTriviaQuestionsUseCase()).thenReturn(flowOf(sampleQuestions))
        viewModel = TriviaViewModel(getTriviaQuestionsUseCase, submitAnswerUseCase)
        // Advance the dispatcher to allow initialization to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.moveToNextQuestion() // Move to question 2
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.moveToNextQuestion() // Move past last question
        testDispatcher.scheduler.advanceUntilIdle()

        // Let's check the state values to debug the issue
        println("Current index: ${viewModel.uiState.value.currentQuestionIndex}")
        println("Total questions: ${viewModel.uiState.value.totalQuestions}")
        println("Is complete: ${viewModel.uiState.value.isComplete}")
        assertTrue(viewModel.uiState.value.isComplete)
        // The ViewModel sets the quiz to complete when the index is at the last question
        assertEquals(1, viewModel.uiState.value.currentQuestionIndex)
    }
}
