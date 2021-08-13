package edu.neu.numad21su.attention;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import edu.neu.numad21su.attention.quizmanager.QuizEditor;
import edu.neu.numad21su.attention.quizmanager.QuizManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        Query quizToTake = db.collection("quizToTake");
        quizToTake.addSnapshotListener((value, error) -> Log.d("msg", "data changed"));
    }

    public void openLiveClassDiscussion(View view) {
        Intent activity2Intent = new Intent(getApplicationContext(), LiveClassDiscussionActivity.class);
        startActivity(activity2Intent);
    }

    public void openQuizScreen(View view) {
        Intent activity2Intent = new Intent(getApplicationContext(), QuizScreen.class);
        startActivity(activity2Intent);
    }
    public void goToAttendance(View view) {
      MainActivity.this.startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
    }

    public void goToQuizResults(View view) {
      MainActivity.this.startActivity(new Intent(MainActivity.this, QuizResults.class));
    }

    public void goToWelcomeScreen(View view) {
        MainActivity.this.startActivity(new Intent(MainActivity.this, WelcomeLogin.class));
    }

    public void goToInClassScreen(View view){

        MainActivity.this.startActivity(new Intent(MainActivity.this, InClass.class));

    }

    public void goToQuizManager(View view) {
        MainActivity.this.startActivity(new Intent(MainActivity.this, QuizManager.class));
    }

    public void goToQuizEditor(View view) {
        MainActivity.this.startActivity(new Intent(MainActivity.this, QuizEditor.class));
    }
}
