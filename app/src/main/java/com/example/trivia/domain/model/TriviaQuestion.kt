package com.example.trivia.domain.model

sealed class TriviaQuestion {
    abstract val id: Int
    abstract val stem: String

    data class MultipleChoice(
        override val id: Int,
        override val stem: String,
        val alternatives: List<String>,
        val correctAnswer: String
    ) : TriviaQuestion()

    data class TrueFalse(
        override val id: Int,
        override val stem: String,
        val correctAnswer: Boolean
    ) : TriviaQuestion()

    data class MultipleAnswer(
        override val id: Int,
        override val stem: String,
        val alternatives: List<String>,
        val correctAnswers: Set<String>
    ) : TriviaQuestion()

    data class OpenEnded(
        override val id: Int,
        override val stem: String,
        val correctAnswer: String, // The expected answer
        val caseSensitive: Boolean = false, // Whether case matters
        val exactMatch: Boolean = false, // Whether an exact match is required or a partial match is sufficient
        val maxLength: Int = 100 // Maximum length for the answer
    ) : TriviaQuestion()

    fun validateAnswer(answer: Any): Boolean {
        return when (this) {
            is MultipleChoice -> {
                if (answer !is String) {
                    return false
                }
                answer == correctAnswer
            }
            is TrueFalse -> {
                if (answer !is Boolean) {
                    return false
                }
                answer == correctAnswer
            }
            is MultipleAnswer -> {
                if (answer !is Set<*>) {
                    return false
                }

                // Type safety check: ensure all elements are strings
                val selectedAnswers = try {
                    @Suppress("UNCHECKED_CAST")
                    answer as Set<String>
                } catch (e: ClassCastException) {
                    return false
                }

                // Verify the selected answers match the correct answers
                selectedAnswers.size == correctAnswers.size &&
                    selectedAnswers.containsAll(correctAnswers)
            }
            is OpenEnded -> {
                if (answer !is String) {
                    return false
                }

                // Check if the answer exceeds the maximum length
                if (answer.length > maxLength) {
                    return false
                }

                // For case-insensitive comparison
                val userAnswer = if (!caseSensitive) answer.lowercase() else answer
                val expectedAnswer = if (!caseSensitive) correctAnswer.lowercase() else correctAnswer

                // Validate based on exact or partial match requirement
                if (exactMatch) {
                    userAnswer == expectedAnswer
                } else {
                    // For partial match, use a simpler approach
                    userAnswer.contains(expectedAnswer)
                }
            }
        }
    }
}
