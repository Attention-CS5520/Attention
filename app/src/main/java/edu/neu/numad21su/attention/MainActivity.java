package edu.neu.numad21su.attention;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
