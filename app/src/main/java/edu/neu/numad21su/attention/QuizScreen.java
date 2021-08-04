package edu.neu.numad21su.attention;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import edu.neu.numad21su.attention.quizScreen.Question;
import edu.neu.numad21su.attention.quizScreen.Quiz;

public class QuizScreen extends AppCompatActivity {

    private static final String QUIZ_SCREEN_TAG = "QUIZ_SCREEN";
    private TextView questionText;
    private Button optionA;
    private Button optionB;
    private Button optionC;
    private Button optionD;
    private Quiz quiz;
    private List<Question> questionList;
    private int curQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);
        questionText = findViewById(R.id.quiz_screen_question_text);
        optionA = findViewById(R.id.quiz_screen_optionA);
        optionB = findViewById(R.id.quiz_screen_optionB);
        optionC = findViewById(R.id.quiz_screen_optionC);
        optionD = findViewById(R.id.quiz_screen_optionD);
        String jsonFileString = Question.getJsonQuiz(getApplicationContext());
        Gson gson = new Gson();
//        Type listUserType = new TypeToken<List<Question>>() { }.getType();
        // get question list from json file
        quiz = gson.fromJson(jsonFileString, Quiz.class);
        questionList = quiz.getQuestions();
        switchQuestion(-1);
    }

    public void onOptionClick(View view) {
        Log.i(QUIZ_SCREEN_TAG, "Question Num: " + curQuestion);
        switch (view.getId()) {
            case R.id.quiz_screen_optionA:
                Log.i(QUIZ_SCREEN_TAG, "Pressed A");
                break;
            case R.id.quiz_screen_optionB:
                Log.i(QUIZ_SCREEN_TAG, "Pressed B");
                break;
            case R.id.quiz_screen_optionC:
                Log.i(QUIZ_SCREEN_TAG, "Pressed C");
                break;
            case R.id.quiz_screen_optionD:
                Log.i(QUIZ_SCREEN_TAG, "Pressed D");
                break;
            default:
        }
        switchQuestion(curQuestion+1);
    }

    private void switchQuestion(int oldQuestion){
        int newQuestion = (oldQuestion+1)%3;
        curQuestion = newQuestion;
        questionText.setText(this.questionList.get(newQuestion).getQuestionText());
        optionA.setText(this.questionList.get(newQuestion).getOptionA());
        optionB.setText(this.questionList.get(newQuestion).getOptionB());
        optionC.setText(this.questionList.get(newQuestion).getOptionC());
        optionD.setText(this.questionList.get(newQuestion).getOptionD());
    }

}