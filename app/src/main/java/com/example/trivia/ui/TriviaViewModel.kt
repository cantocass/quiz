package com.example.trivia.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trivia.domain.GetTriviaQuestionsUseCase
import com.example.trivia.domain.SubmitAnswerUseCase
import com.example.trivia.domain.model.TriviaQuestion
import com.example.trivia.ui.state.TriviaUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI State
data class TriviaUiState(
    val currentQuestion: TriviaQuestion? = null,
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 0,
    val lastAnswerCorrect: Boolean? = null,
    val isComplete: Boolean = false,
    val score: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

// ViewModel
@HiltViewModel
class TriviaViewModel @Inject constructor(
    private val getTriviaQuestionsUseCase: GetTriviaQuestionsUseCase,
    private val submitAnswerUseCase: SubmitAnswerUseCase
) : ViewModel() {

    private val _questions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
    private val _uiState = MutableStateFlow(TriviaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initializeQuiz()
    }

    private fun initializeQuiz() {
        viewModelScope.launch {
            getTriviaQuestionsUseCase()
                .onEach { questions -> _questions.value = questions }
                .collect { questions ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentQuestion = questions.getOrNull(currentState.currentQuestionIndex),
                            totalQuestions = questions.size
                        )
                    }
                }
        }
    }

    fun submitAnswer(questionId: Int, answer: Any) {
        viewModelScope.launch {
            val isCorrect = submitAnswerUseCase(questionId, answer)
            _uiState.update { currentState ->
                currentState.copy(
                    lastAnswerCorrect = isCorrect,
                    score = if (isCorrect) currentState.score + 1 else currentState.score
                )
            }
        }
    }

    fun moveToNextQuestion() {
        val currentQuestions = _questions.value
        _uiState.update { currentState ->
            val nextIndex = currentState.currentQuestionIndex + 1
            val isComplete = nextIndex >= currentQuestions.size
            if (isComplete) {
                currentState.copy(
                    isComplete = true,
                    lastAnswerCorrect = null
                )
            } else {
                currentState.copy(
                    currentQuestionIndex = nextIndex,
                    currentQuestion = currentQuestions.getOrNull(nextIndex),
                    lastAnswerCorrect = null
                )
            }
        }
    }
}
