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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

import edu.neu.numad21su.attention.quizScreen.Question;
import edu.neu.numad21su.attention.quizScreen.QuizEntry;

public class QuizResults extends AppCompatActivity implements Serializable {

  private static final String TAG = "";
  private FirebaseFirestore primaryDB;
  private FirebaseAuth mAuth;
  FirebaseUser currentUser;
  protected String userFirstName;
  protected String userLastName;
  protected QuizEntry quizEntry;

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

  public void getUserData () {
    String email = mAuth.getCurrentUser().getEmail();
    DocumentReference docRef = primaryDB.collection("userType").document(slugify(email));
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
            userFirstName =  document.get("firstName").toString();
            userLastName =  document.get("lastName").toString();
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
//    DocumentReference docRef = primaryDB.collection("quizEntries").document(quiz.getQuizEntryId());
//    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//      @Override
//      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//        if (task.isSuccessful()) {
//          DocumentSnapshot document = task.getResult();
//          if (document.exists()) {
//            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//            ArrayList<Question> questions = (ArrayList<Question>) document.get("questionEntries");
//            String stringify = questions.toString();
//            int numQuestions = questions.size();
//            System.out.println(numQuestions);
//          } else {
//            Log.d(TAG, "No such document");
//          }
//        } else {
//          Log.d(TAG, "get failed with ", task.getException());
//        }
//      }
//    });
  }
//
//  private void fillQuizInfo(QuizEntry quiz) {
//    String quizID = quiz.getQuizId();
//    primaryDB.child("quizzes").child(quizID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//      @Override
//      public void onComplete(@NonNull Task<DataSnapshot> task) {
//        if (!task.isSuccessful()) {
//          Log.e("firebase", "Error getting data", task.getException());
//        }
//        else {
//          Log.d("firebase", String.valueOf(task.getResult().getValue()));
//        }
//      }
//    });
//
//  }

  /**
   * Input None, Output double (score of the quiz) Takes the quiz object passed from the quizEntry
   * and uses the answers to create a score out of 100. Score is returned as double to be displayed
   * as a percentage
   */
  private double calculateResults(QuizEntry quiz) {
    DocumentReference docRef = primaryDB.collection("quizEntry").document(quiz.getQuizEntryId());
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
            userFirstName =  document.get("firstName").toString();
            userLastName =  document.get("lastName").toString();
            fillUserInfo();
          } else {
            Log.d(TAG, "No such document");
          }
        } else {
          Log.d(TAG, "get failed with ", task.getException());
        }
      }
    });
    return 0.0;
  }
}

