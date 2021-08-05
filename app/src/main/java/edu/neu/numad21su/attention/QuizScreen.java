package edu.neu.numad21su.attention;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.neu.numad21su.attention.quizScreen.Question;
import edu.neu.numad21su.attention.quizScreen.QuestionEntry;
import edu.neu.numad21su.attention.quizScreen.Quiz;
import edu.neu.numad21su.attention.quizScreen.QuizEntry;

public class QuizScreen extends AppCompatActivity {

    private static final String QUIZ_SCREEN_TAG = "QUIZ_SCREEN";
    private TextView questionText;
    private Button optionA;
    private Button optionB;
    private Button optionC;
    private Button optionD;
    private Quiz quiz;
    private List<Question> questionList;
    private List<QuestionEntry> answeredQuestions;
    private int curQuestion = 0;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
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
        answeredQuestions = new ArrayList<>();
        switchQuestion(-1);
    }

    public void onOptionClick(View view) {
        Log.i(QUIZ_SCREEN_TAG, "Question Num: " + curQuestion);
        switch (view.getId()) {
            case R.id.quiz_screen_optionA:
                Log.i(QUIZ_SCREEN_TAG, "Pressed A");
                saveQuestion("A");
                break;
            case R.id.quiz_screen_optionB:
                Log.i(QUIZ_SCREEN_TAG, "Pressed B");
                saveQuestion("B");
                break;
            case R.id.quiz_screen_optionC:
                Log.i(QUIZ_SCREEN_TAG, "Pressed C");
                saveQuestion("C");
                break;
            case R.id.quiz_screen_optionD:
                Log.i(QUIZ_SCREEN_TAG, "Pressed D");
                saveQuestion("D");
                break;
            default:
        }
        switchQuestion(curQuestion+1);
    }

    private void saveQuestion(String userAnswered){
        QuestionEntry questionEntry = new QuestionEntry(quiz.getQuizId(), userAnswered);
        answeredQuestions.add(questionEntry);
    }

    private void switchQuestion(int oldQuestion){
        int newQuestion = oldQuestion+1;
        if(oldQuestion+1 >= questionList.size()){
            //save to db
            String userEmail = mAuth.getCurrentUser().getEmail();
            QuizEntry quizEntry = new QuizEntry(answeredQuestions, userEmail);
//            db.save(quizEntry)
            return;
        }
        curQuestion = newQuestion;
        questionText.setText(this.questionList.get(newQuestion).getQuestionText());
        optionA.setText(this.questionList.get(newQuestion).getOptionA());
        optionB.setText(this.questionList.get(newQuestion).getOptionB());
        optionC.setText(this.questionList.get(newQuestion).getOptionC());
        optionD.setText(this.questionList.get(newQuestion).getOptionD());
    }

}