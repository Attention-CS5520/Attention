package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.slugify.Slugify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.numad21su.attention.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.neu.numad21su.attention.quizScreen.Question;
import edu.neu.numad21su.attention.quizScreen.QuestionEntry;
import edu.neu.numad21su.attention.quizScreen.QuizEntry;

public class QuizResults extends AppCompatActivity implements Serializable {

  private static final String TAG = "";
  private FirebaseFirestore primaryDB;
  private FirebaseAuth mAuth;
  protected String userFirstName;
  protected String userLastName;
  protected QuizEntry quizEntry;
  FirebaseUser currentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz_results);
    primaryDB = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
    currentUser = mAuth.getCurrentUser();
    quizEntry = (QuizEntry) getIntent().getSerializableExtra("quizEntry");
    System.out.println(quizEntry);
    getUserData();
    getQuizData(quizEntry);
    updateDisplay(calculateResults(quizEntry));
  }

  public void updateDisplay(double score) {
    TextView percent = findViewById(R.id.percent);
    String percentageScore = Double.toString(score);
    percent.setText(percentageScore);
  }

  public void fillUserInfo() {
    TextView login = findViewById(R.id.user_name);
    String usersName = (userFirstName + " " + userLastName);
    login.setText(usersName);
  }

  private String slugify(String text) {
    Slugify slg = new Slugify();
    String slug = slg.slugify(text);
    return slug;
  }

  public void getUserData() {
    String email = mAuth.getCurrentUser().getEmail();
    DocumentReference docRef = primaryDB.collection("userType").document(slugify(email));
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            userFirstName = document.get("firstName").toString();
            userLastName = document.get("lastName").toString();
            fillUserInfo();
          } else {
            Log.d(TAG, "No such document");
          }
        } else {
          Log.d(TAG, "get failed with ", task.getException());
        }
      }
    });
  }

  public void getQuizData(QuizEntry quiz) {
    TextView quizName = findViewById(R.id.quiz_name);
    quizName.setText(quizEntry.getQuizName());
  }

  /**
   * Input None, Output double (score of the quiz) Takes the quiz object passed from the quizEntry
   * and uses the answers to create a score out of 100. Score is returned as double to be displayed
   * as a percentage
   */
  private double calculateResults(QuizEntry quiz) {
    double totalPoints = 0.0;
    double pointsPerQuestion = (100 / quiz.getQuestionEntries().size()) + 100%quiz.getQuestionEntries().size();
    List<QuestionEntry> questionEntries = quiz.getQuestionEntries();
    for (int i = 0; i < questionEntries.size(); i++) {
      QuestionEntry currentEntry = questionEntries.get(i);
      System.out.println(currentEntry.getSelectedOption());
      System.out.println(currentEntry.getQuestionId().correctAnswer);
      if (currentEntry.getSelectedOption().equals(currentEntry.getQuestionId().correctAnswer)) {
        totalPoints = (totalPoints + pointsPerQuestion);
      }
    }
    System.out.println(totalPoints);
    return totalPoints;
  }
}
