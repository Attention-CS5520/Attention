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
}