# Quiz App

A simple Quiz App developed for Android using Java and XML. This app provides a fun and interactive way to take quizzes, score answers, and view results.

## Features

- **Question Corpus**: A set of 20-30 multiple-choice questions, each with four options.
- **Navigation**: Users can navigate through questions using "Next" and "Previous" buttons.
- **Scoring System**:
  - **Correct Answer**: +5 points
  - **Incorrect Answer**: -1 point
  - **Show Answer**: -1 point (if the user opts to view the correct answer)
- **Timer**: A countdown timer for the entire quiz duration (default: 5 minutes).
- **Results**: Displays total marks and percentage after quiz completion or timer expiration.

## Installation

1. **Open the Project**:
   - Open Android Studio.
   - Click on `Open` and select the cloned `quiz-app` directory.

2. **Build and Run**:
   - Click `Run` or press `Shift + F10` to build and run the app on an Android emulator or physical device.

## Code Overview

- **`MainActivity.java`**: Contains the logic for quiz functionality, including navigation, scoring, and timer.
- **`activity_main.xml`**: Defines the layout for the main quiz activity.

## Configuration

- The quiz time is set to 5 minutes by default. Modify the `QUIZ_TIME_MILLIS` constant in `MainActivity.java` to adjust the duration.
- Update `strings.xml` with your quiz questions, options, and answers.
