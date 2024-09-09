package com.smd.l217704.quizapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeText, descriptionText, questionText, scoreText, resultText, timerText;
    private RadioButton option1, option2, option3, option4;
    private Button startQuizButton, nextButton, prevButton, endQuizButton, showAnswerButton;
    private RadioGroup optionsGroup;

    private String[] questions;
    private String[] options;
    private String[] answers;

    private int currentQuestionIndex = 0;
    private int totalScore = 0;
    private boolean[] answeredQuestions;

    private static final long QUIZ_TIME_MILLIS = 300000; // 5 minutes
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = QUIZ_TIME_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        welcomeText = findViewById(R.id.welcomeText);
        descriptionText = findViewById(R.id.descriptionText);
        questionText = findViewById(R.id.questionText);
        scoreText = findViewById(R.id.scoreText);
        resultText = findViewById(R.id.resultText);
        timerText = findViewById(R.id.timerText);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        startQuizButton = findViewById(R.id.startQuizButton);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        endQuizButton = findViewById(R.id.endQuizButton);
        showAnswerButton = findViewById(R.id.showAnswerButton);
        optionsGroup = findViewById(R.id.optionsGroup);

        // Initially, show only the welcome message and description
        hideQuizElements();

        // Load questions, options, and answers from strings.xml
        questions = getResources().getStringArray(R.array.questions);
        options = getResources().getStringArray(R.array.options);
        answers = getResources().getStringArray(R.array.answers);

        // Initialize answeredQuestions array
        answeredQuestions = new boolean[questions.length];

        // Start Quiz Button Listener
        startQuizButton.setOnClickListener(v -> {
            // Hide welcome and description elements
            welcomeText.setVisibility(View.GONE);
            descriptionText.setVisibility(View.GONE);
            startQuizButton.setVisibility(View.GONE);

            // Show quiz elements
            showQuizElements();

            // Start the countdown timer
            startTimer();

            // Display the first question
            loadQuestion(currentQuestionIndex);
        });

        // Next Button Listener
        nextButton.setOnClickListener(v -> {
            if (currentQuestionIndex < questions.length - 1) {
                if (answeredQuestions[currentQuestionIndex]) {
                    Toast.makeText(MainActivity.this, "You have already answered this question", Toast.LENGTH_SHORT).show();
                } else {
                    checkAnswer();
                }
                currentQuestionIndex++;
                loadQuestion(currentQuestionIndex);
            } else {
                Toast.makeText(MainActivity.this, "This is the last question", Toast.LENGTH_SHORT).show();
            }
        });

        // Previous Button Listener
        prevButton.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                if (answeredQuestions[currentQuestionIndex]) {
                    enableOptions();
                } else {
                    checkAnswer();
                }
                currentQuestionIndex--;
                loadQuestion(currentQuestionIndex);
            } else {
                Toast.makeText(MainActivity.this, "This is the first question", Toast.LENGTH_SHORT).show();
            }
        });

        // End Quiz Button Listener
        endQuizButton.setOnClickListener(v -> {
            checkAnswer();
            updateScore();
            if (areAllQuestionsAnswered()) {
                displayResult();
            } else {
                Toast.makeText(MainActivity.this, "Quiz is not completed. Please answer all questions.", Toast.LENGTH_SHORT).show();
            }
        });

        // Show Answer Button Listener
        showAnswerButton.setOnClickListener(v -> {
            if (!answeredQuestions[currentQuestionIndex]) {
                showAnswer();
            } else {
                Toast.makeText(MainActivity.this, "You have already answered this question", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                displayResult();
            }
        }.start();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerText.setText(timeFormatted);
    }

    private void updateScore() {
        scoreText.setText("Score: " + totalScore);
    }

    private void loadQuestion(int questionIndex) {
        questionText.setText(questions[questionIndex]);
        String[] currentOptions = options[questionIndex].split("\\|");
        option1.setText(currentOptions[0]);
        option2.setText(currentOptions[1]);
        option3.setText(currentOptions[2]);
        option4.setText(currentOptions[3]);
        optionsGroup.clearCheck(); // Clear the selection

        if (answeredQuestions[questionIndex]) {
            disableOptions();
        } else {
            enableOptions();
        }

        updateScore();
        showAnswerButton.setVisibility(View.VISIBLE);

        if (questionIndex == questions.length - 1) {
            nextButton.setVisibility(View.GONE);
            endQuizButton.setVisibility(View.VISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
            endQuizButton.setVisibility(View.GONE);
        }
    }

    private void checkAnswer() {
        int selectedOptionId = optionsGroup.getCheckedRadioButtonId();
        if (selectedOptionId != -1) {
            RadioButton selectedOption = findViewById(selectedOptionId);
            String selectedAnswer = selectedOption.getText().toString();
            String correctAnswer = answers[currentQuestionIndex];

            if (selectedAnswer.equals(correctAnswer)) {
                totalScore += 5;
                Toast.makeText(MainActivity.this, "Correct! +5 points", Toast.LENGTH_SHORT).show();
            } else {
                totalScore -= 1;
                Toast.makeText(MainActivity.this, "Incorrect! -1 point", Toast.LENGTH_SHORT).show();
            }
            answeredQuestions[currentQuestionIndex] = true;
        } else {
            Toast.makeText(MainActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAnswer() {
        String correctAnswer = answers[currentQuestionIndex];
        RadioButton[] options = {option1, option2, option3, option4};
        for (RadioButton option : options) {
            if (option.getText().toString().equals(correctAnswer)) {
                option.setChecked(true);
                break;
            }
        }
        totalScore -= 1;
        updateScore();
        answeredQuestions[currentQuestionIndex] = true;
        disableOptions();
        Toast.makeText(MainActivity.this, "Answer shown and -1 point deducted", Toast.LENGTH_SHORT).show();
    }

    private boolean areAllQuestionsAnswered() {
        for (boolean answered : answeredQuestions) {
            if (!answered) {
                return false;
            }
        }
        return true;
    }

    private void displayResult() {
        double totalMarks = questions.length * 5.0;
        double percentage = (totalScore / totalMarks) * 100;
        resultText.setText(String.format("You Scored: %d/%.0f\nPercentage: %.2f%%", totalScore, totalMarks, percentage));
        resultText.setVisibility(View.VISIBLE);
        questionText.setVisibility(View.GONE);
        optionsGroup.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
        endQuizButton.setVisibility(View.GONE);
        showAnswerButton.setVisibility(View.GONE);
        scoreText.setVisibility(View.GONE);
        timerText.setVisibility(View.GONE);
    }

    private void disableOptions() {
        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);
    }

    private void enableOptions() {
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);
    }

    private void hideQuizElements() {
        questionText.setVisibility(View.GONE);
        optionsGroup.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
        endQuizButton.setVisibility(View.GONE);
        showAnswerButton.setVisibility(View.GONE);
        timerText.setVisibility(View.GONE);
        scoreText.setVisibility(View.GONE);
    }

    private void showQuizElements() {
        questionText.setVisibility(View.VISIBLE);
        optionsGroup.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        prevButton.setVisibility(View.VISIBLE);
        endQuizButton.setVisibility(View.VISIBLE);
        showAnswerButton.setVisibility(View.VISIBLE);
        timerText.setVisibility(View.VISIBLE);
        scoreText.setVisibility(View.VISIBLE);
    }
}
