package edu.neu.numad21su.attention;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void goToAttendance(View view) {
    MainActivity.this.startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
  }

  public void goToQuizResults(View view) {
    MainActivity.this.startActivity(new Intent(MainActivity.this, QuizResults.class));
  }
}
