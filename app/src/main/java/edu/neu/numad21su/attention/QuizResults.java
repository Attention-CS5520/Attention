package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.numad21su.attention.R;
import edu.neu.numad21su.attention.quizScreen.QuizEntry;

public class QuizResults extends AppCompatActivity {

  private DatabaseReference primaryDB;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz_results);
    primaryDB = FirebaseDatabase.getInstance("https://attentiondatabaseprimary-default-rtdb.firebaseio.com/").getReference();
    // Test Quiz Entry for DB Performance Check
    QuizEntry newEntry = new QuizEntry("quiz2");
    fillQuizInfo(newEntry);
  }

  private void fillQuizInfo(QuizEntry quiz) {
    String quizID = quiz.getQuizId();
    primaryDB.child("quizzes").child(quizID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DataSnapshot> task) {
        if (!task.isSuccessful()) {
          Log.e("firebase", "Error getting data", task.getException());
        }
        else {
          Log.d("firebase", String.valueOf(task.getResult().getValue()));
        }
      }
    });

  }

  private void calculateResults() {

  }
}
