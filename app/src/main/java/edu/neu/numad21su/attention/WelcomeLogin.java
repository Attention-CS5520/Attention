package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeLogin extends AppCompatActivity {

    private static final String TAG = "";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_login);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    public void createAccount(View view) {
        Intent activity2Intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(activity2Intent);
    }

    public void signInParse(View view){
        EditText rawEmail, rawPassword;
        rawEmail = findViewById(R.id.username);
        rawPassword = findViewById(R.id.password);
        String email = rawEmail.getText().toString();
        String password = rawPassword.getText().toString();
        System.out.println(email + password);
        signIn(email, password);
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Temporary Toast Message - Need to change to In Class Screen once the in Class screen is merged
                        Intent activity2Intent = new Intent(getApplicationContext(), AttendanceActivity.class);
                        startActivity(activity2Intent);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(WelcomeLogin.this, "Authentication failed. Please check your username and password",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void reload() {
        TextView login = findViewById(R.id.loginConfirmation);
        login.setText(mAuth.getCurrentUser().getEmail());
    }
}