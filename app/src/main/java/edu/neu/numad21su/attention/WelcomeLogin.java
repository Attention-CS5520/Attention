package edu.neu.numad21su.attention;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_login);
    }

    public void createAccount(View view) {
        Intent activity2Intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(activity2Intent);
    }

    public void signIn(View view) {
    }
}