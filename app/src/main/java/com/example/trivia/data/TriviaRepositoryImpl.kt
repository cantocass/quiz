package com.example.trivia.data

import com.example.trivia.domain.model.TriviaQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TriviaRepositoryImpl @Inject constructor() : TriviaRepository {
    override suspend fun getQuestions(): Flow<List<TriviaQuestion>> = flow {
        emit(sampleQuestions)
    }

    override suspend fun submitAnswer(questionId: Int, answer: Any): Boolean {
        return sampleQuestions.find { it.id == questionId }?.validateAnswer(answer) ?: false
    }

    private val sampleQuestions = listOf(
        TriviaQuestion.MultipleChoice(
            id = 1,
            stem = "Which of the following is the preferred programming language for Android app development?",
            alternatives = listOf("Swift", "Kotlin", "Objective-C", "Java"),
            correctAnswer = "Kotlin"
        ),
        TriviaQuestion.TrueFalse(
            id = 2,
            stem = "Android Studio is the official IDE for Android development.",
            correctAnswer = true
        ),
        TriviaQuestion.MultipleAnswer(
            id = 3,
            stem = "Which of the following are core Android components? (Select all that apply)",
            alternatives = listOf(
                "Activity",
                "Service",
                "Content Provider",
                "Broadcast Receiver",
                "Fragment",
                "ViewModel"
            ),
            correctAnswers = setOf("Activity", "Service", "Content Provider", "Broadcast Receiver")
        ),
        TriviaQuestion.MultipleChoice(
            id = 4,
            stem = "What is the purpose of the 'AndroidManifest.xml' file in an Android project?",
            alternatives = listOf(
                "To define the layout of the user interface",
                "To declare the app's components and permissions",
                "To store the app's data",
                "To manage the app's network connections"
            ),
            correctAnswer = "To declare the app's components and permissions"
        ),
        TriviaQuestion.TrueFalse(
            id = 5,
            stem = "Jetpack Compose is a modern toolkit for building native Android UI.",
            correctAnswer = true
        ),
        TriviaQuestion.OpenEnded(
            id = 6,
            stem = "What is the name of the Android build system tool that replaced Ant?",
            correctAnswer = "Gradle",
            exactMatch = true
        )
    )
}
