package com.example.trivia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trivia.ui.TriviaScreen
import com.example.trivia.ui.TriviaViewModel
import com.example.trivia.ui.theme.QuizAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: TriviaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TriviaScreen(
                        uiState = viewModel.uiState.collectAsState().value,
                        onAnswerSubmit = { questionId, answer ->
                            viewModel.submitAnswer(questionId, answer)
                        },
                        onNextQuestion = {
                            viewModel.moveToNextQuestion()
                        },
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
