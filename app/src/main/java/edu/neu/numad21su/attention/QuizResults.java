package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

import java.util.ArrayList;

import edu.neu.numad21su.attention.quizScreen.Question;
import edu.neu.numad21su.attention.quizScreen.QuizEntry;

public class QuizResults extends AppCompatActivity {

  private static final String TAG = "";
  private FirebaseFirestore primaryDB;
  private FirebaseAuth mAuth;
  FirebaseUser currentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz_results);
    primaryDB = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
    currentUser = mAuth.getCurrentUser();
    getData();
    fillInfo();
  }

  public void fillInfo() {
    TextView login = findViewById(R.id.user_name);
    login.setText(mAuth.getCurrentUser().getEmail());

  }

  public void getData() {
    DocumentReference docRef = primaryDB.collection("quizEntries").document("JSONQuiz-1-shayan-shayan-com");
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
            ArrayList<Question> questions = (ArrayList<Question>) document.get("questionEntries");
            String stringify = questions.toString();
            int numQuestions = questions.size();
            System.out.println(numQuestions);
          } else {
            Log.d(TAG, "No such document");
          }
        } else {
          Log.d(TAG, "get failed with ", task.getException());
        }
      }
    });
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

  private void calculateResults() {

  }
}
