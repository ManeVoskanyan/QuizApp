package  com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView totalQuestionsTextview;
    TextView questionTextView;
    Button ansA, ansB, ansC, ansD;

    Button submitBtn;
    Button lastSelectedButton; // переменная для хранения ссылки на последнюю выбранную кнопку

    int score = 0;
    int totalQuestion = QuestionAnswer.questions.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ansA = findViewById(R.id.ansA);
        ansB = findViewById(R.id.ansB);
        ansC = findViewById(R.id.ansC);
        ansD = findViewById(R.id.ansD);
        submitBtn = findViewById(R.id.submit_btn);
        totalQuestionsTextview = findViewById(R.id.total_questions);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        totalQuestionsTextview.setText("Total questions : " + totalQuestion);
        loadNewQuestion();
    }


    @Override
    public void onClick(View view) {
        int blue_light = ContextCompat.getColor(this, R.color.blue_light);
        Button clickedButton = (Button) view;
        if (clickedButton.getId() == R.id.submit_btn) {
            checkAnswer();
            loadNewQuestionWithDelay();
        } else {
            selectedAnswer = clickedButton.getText().toString();
            if (clickedButton != submitBtn) {
                changeButtonColor(clickedButton, blue_light);
                if (lastSelectedButton != null && lastSelectedButton != clickedButton) {
                    resetButtonColor(lastSelectedButton);
                }
                lastSelectedButton = clickedButton;
            }
        }
    }
    void checkAnswer() {
        int green = ContextCompat.getColor(this, R.color.green);
        int red = ContextCompat.getColor(this, R.color.red);

        if (selectedAnswer.equals(QuestionAnswer.correctAnswers[currentQuestionIndex])) {
            score++;
            changeButtonColor(getSelectedAnswerButton(), green);
        } else {
            changeButtonColor(getSelectedAnswerButton(), red);
            changeButtonColor(getCorrectAnswerButton(), green);
        }
    }

    void loadNewQuestionWithDelay() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentQuestionIndex++;
                if (currentQuestionIndex < totalQuestion) {
                    loadNewQuestion();
                } else {
                    finishQuiz();
                }
            }
        }, 2000); // 2 seconds delay
    }

    void loadNewQuestion() {
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ansA);
        ansB = findViewById(R.id.ansB);
        ansC = findViewById(R.id.ansC);
        ansD = findViewById(R.id.ansD);
        questionTextView.setText((QuestionAnswer.questions[currentQuestionIndex]));
        ansA.setText(QuestionAnswer.choices[currentQuestionIndex][0]);
        ansB.setText(QuestionAnswer.choices[currentQuestionIndex][1]);
        ansC.setText(QuestionAnswer.choices[currentQuestionIndex][2]);
        ansD.setText(QuestionAnswer.choices[currentQuestionIndex][3]);

        resetButtonColors();
    }

    void finishQuiz() {
        String passStatus = (score > totalQuestion * 0.60) ? "Great!" : "Try once more";
        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Score is " + score + " out of " + totalQuestion)
                .setPositiveButton("Restart", (dialogInterface, i) -> restartQuiz())
                .show();
    }

    void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        loadNewQuestion();
    }

    void changeButtonColor(Button button, int color) {
        button.setBackgroundColor(color);
    }

    void resetButtonColor(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
    }

    Button getSelectedAnswerButton() {
        switch (selectedAnswer) {
            case "ansA":
                return ansA;
            case "ansB":
                return ansB;
            case "ansC":
                return ansC;
            case "ansD":
                return ansD;
            default:
                return null;
        }
    }

    Button getCorrectAnswerButton() {
        switch (QuestionAnswer.correctAnswers[currentQuestionIndex]) {
            case "ansA":
                return ansA;
            case "ansB":
                return ansB;

            case "ansC":
                return ansC;
            case "ansD":
                return ansD;
            default:
                return null;
        }
    }
    void resetButtonColors() {
        int blue = ContextCompat.getColor(this, R.color.blue);
        ansA.setBackgroundColor(blue);
        ansB.setBackgroundColor(blue);
        ansC.setBackgroundColor(blue);
        ansD.setBackgroundColor(blue);
    }
}
