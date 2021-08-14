package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.github.slugify.Slugify;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import edu.neu.numad21su.attention.quizScreen.Question;
import edu.neu.numad21su.attention.quizScreen.QuestionEntry;
import edu.neu.numad21su.attention.quizScreen.Quiz;
import edu.neu.numad21su.attention.quizScreen.QuizEntry;


public class QuizScreen extends AppCompatActivity implements Serializable {

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
//    private DatabaseReference mDatabase;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase DB
//        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Connect with firebase
        db = FirebaseFirestore.getInstance();
        questionText = findViewById(R.id.quiz_screen_question_text);
        optionA = findViewById(R.id.quiz_screen_optionA);
        optionB = findViewById(R.id.quiz_screen_optionB);
        optionC = findViewById(R.id.quiz_screen_optionC);
        optionD = findViewById(R.id.quiz_screen_optionD);
        String jsonFileString = Question.getJsonQuiz(getApplicationContext());
        Gson gson = new Gson();
//        Type listUserType = new TypeToken<List<Question>>() { }.getType();
        // get question list from json file
//        quiz = gson.fromJson(jsonFileString, Quiz.class);
        if(this.getIntent().hasExtra("Quiz")){
            this.quiz = (Quiz) this.getIntent().getSerializableExtra("Quiz");
        }
        else {
            quiz = gson.fromJson(jsonFileString, Quiz.class);
        }
        questionList = quiz.getQuestions();
        answeredQuestions = new ArrayList<>();
        switchQuestion(-1);
    }

    public void onOptionClick(View view) {
        if(curQuestion >= questionList.size()) return;
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
        switchQuestion(curQuestion);
    }

    private void saveQuestion(String userAnswered){
        String userEmail = mAuth.getCurrentUser().getEmail();
        userEmail = userEmail == null? "" : userEmail;
        Slugify slg = new Slugify();
        String emailSlug = slg.slugify(userEmail);
        QuestionEntry questionEntry = new QuestionEntry(quiz.getQuizTitle() + "-"
                + quiz.getQuizId() + "-" + emailSlug + "-" + (curQuestion+1),
                questionList.get(curQuestion), userAnswered);
        answeredQuestions.add(questionEntry);
    }

    private void switchQuestion(int oldQuestion){
        int newQuestion = oldQuestion+1;
        if(newQuestion > questionList.size()){
            return;
        }
        if(oldQuestion+1 == questionList.size()){
            //save to db
            String userEmail = mAuth.getCurrentUser().getEmail();
            if(userEmail == null) {
                Toast.makeText(getApplicationContext(),"Please login",Toast.LENGTH_SHORT).show();
                return;
            }
            Slugify slg = new Slugify();
            String emailSlug = slg.slugify(userEmail);
            QuizEntry quizEntry = new QuizEntry(quiz.quizId, quiz.quizTitle,
                    quiz.getQuizTitle()+"-"+quiz.getQuizId()+"-"+emailSlug, answeredQuestions, userEmail);
//            mDatabase.child("quizzes").child(quiz.getQuizTitle()+"-"+quiz.getQuizId()+"-"+emailSlug).setValue(quizEntry);

            db.collection("quizEntries").document(quiz.getQuizTitle()+"-"+quiz.getQuizId()+"-"+emailSlug)
                    .set(quizEntry).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(@NonNull Void unused) {
                    Toast.makeText(getApplicationContext(),
                            "Submission Successful!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), QuizResults.class);
                    intent.putExtra("quizEntry", (Serializable) quizEntry);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Submission Failed! You can exit the quiz" +
                                    " by pressing the back button and try again.",Toast.LENGTH_LONG).show();
                }
            });
            //            db.save(quizEntry)
            curQuestion = newQuestion;
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
