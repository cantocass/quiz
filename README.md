# QuizApp - Android Trivia Application

A modern Android quiz application built with Kotlin, Jetpack Compose, and following clean architecture principles.

## Overview

QuizApp is a comprehensive trivia application that supports multiple question types, including multiple-choice, true/false, multiple answer selection, and open-ended text responses. The app showcases modern Android development practices like:

- **Jetpack Compose UI**: Declarative UI toolkit for native Android development
- **Clean Architecture**: Separation of concerns with domain, data, and UI layers
- **Dependency Injection**: Using Hilt for managing app dependencies
- **MVVM Pattern**: ViewModel-based UI state management
- **Kotlin Coroutines & Flow**: For asynchronous operations and reactive data streams
- **Unit Testing**: Comprehensive test coverage for domain logic and UI components
- **Detekt Linting**: Integrates detekt and ktlint for ensuring code style and formatting, with a baseline file acknowledging issues needing to be addressed

## App Structure

The application follows a clean architecture approach with the following main packages:

```
com.example.trivia/
├── data/            # Data layer - repositories and data sources
├── di/              # Dependency injection modules
├── domain/          # Domain layer - business logic and models
│   └── model/       # Core domain entities
├── ui/              # UI layer - screens, components and viewmodels
│   ├── components/  # Reusable UI components
│   ├── state/       # UI state definitions
│   └── theme/       # App theming and styling
```

## Improvements to Make
1. **Theming** The UX/UI of the app is bland, would greatly benefit from a design overhaul
2. **Validation** For open ended questions, the existing validation is hard to understand and will not handle many edge cases.

## Key Components

### Domain Models

The app is built around the `TriviaQuestion` sealed class that supports four different question types:

1. **MultipleChoice**: Single selection from a list of options
2. **TrueFalse**: Simple true/false questions
3. **MultipleAnswer**: Multiple selection from a list of options
4. **OpenEnded**: Free text response with validation options

Each question type has its own validation logic implemented in the `validateAnswer()` method.

### Data Layer

The data is currently served from an in-memory repository (`TriviaRepositoryImpl`) that provides sample questions. This could be extended to fetch data from a network API or local database.

Key interfaces:
- `TriviaRepository`: Interface defining the data operations
- `TriviaRepositoryImpl`: Implementation providing sample quiz questions

### Domain Layer

The domain layer contains the core business logic encapsulated in use cases:

- `GetTriviaQuestionsUseCase`: Retrieves the list of quiz questions
- `SubmitAnswerUseCase`: Handles answer validation

### UI Layer

The UI is built with Jetpack Compose and follows the MVVM pattern:

- **ViewModel**: `TriviaViewModel` manages the quiz state and handles user interactions
- **UI State**: `TriviaUiState` represents the current state of the quiz UI
- **Screen**: `TriviaScreen` is the main screen composable function
- **Components**: Reusable UI components in `QuizComponents.kt` handle different question types

### Key UI Components

1. **QuizProgressIndicator**: Shows progress through the quiz with an animated bar
2. **QuestionContent**: Displays the current question based on its type
3. **Answer Input Components**:
   - `SingleChoiceOptions`: For multiple-choice questions
   - `TrueFalseOptions`: For true/false questions
   - `MultipleChoiceOptions`: For multiple-answer questions
   - `OpenEndedAnswerInput`: For free-text response questions
4. **AnswerFeedback**: Shows feedback after answering a question
5. **QuizComplete**: End-of-quiz summary screen

## Quiz Flow

1. The app loads a set of questions from the repository
2. The user is presented with one question at a time
3. After submitting an answer, the user receives feedback (correct/incorrect)
4. The user advances to the next question
5. Once all questions are answered, a summary screen shows the final score

## Testing

The app has comprehensive test coverage:

- **Unit Tests**: Test domain logic, models, repositories, and ViewModels
- **UI Tests**: Test UI components in isolation using Compose testing APIs

## Architecture

The app follows clean architecture principles:

1. **UI Layer (Presentation)**:
   - Compose UI components
   - ViewModels
   - UI State handling

2. **Domain Layer**:
   - Use Cases that encapsulate business logic
   - Domain Models representing core entities
   - Repository interfaces defining the needed data operations

3. **Data Layer**:
   - Repository implementations
   - (Potential extensions: Remote data sources, local database, etc.)

## Dependency Injection

Dependency injection is handled with Hilt:

- `RepositoryModule`: Provides repository implementations
- `@HiltViewModel`: Used to inject dependencies into ViewModels

## Getting Started

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or newer
- Kotlin 1.9.22 or newer
- Android SDK with minimum API level 28 (Android 9.0 Pie)

### Running the App
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

## Extending the App

### Adding More Questions
To add more questions, modify the `sampleQuestions` list in `TriviaRepositoryImpl.kt`. You can add any of the supported question types.

### Implementing a Remote Data Source
Replace the in-memory repository with a network-backed implementation to fetch questions from an API.

### Persisting Quiz Results
Add a local database (Room) to store quiz history and user scores.

## Architectural Decisions

### Compose for UI
Jetpack Compose was chosen for its modern declarative approach to UI development, which simplifies UI implementation and testing.

### MVVM with Clean Architecture
This architecture provides a clear separation of concerns, making the code more maintainable, testable, and flexible to change.

### Kotlin Coroutines & Flow
Used for asynchronous operations and reactive data streams, providing a more concise and less error-prone approach compared to callbacks or RxJava.

### In-memory Repository
Currently using an in-memory repository for simplicity, but the architecture allows for easy replacement with real data sources.
