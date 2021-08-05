package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.numad21su.attention.accountCreation.userAccount;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    // Create a Firebase Authentication Variable
    private FirebaseAuth mAuth;
    // Create a reference to Firebase DB
    private DatabaseReference primaryDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.fragment_register);
        primaryDB = FirebaseDatabase.getInstance("https://attentiondatabaseprimary-default-rtdb.firebaseio.com/").getReference();

    }

    @Override
    // Check to see if the user is already logged in on device
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    // Take the E-Mail and Password input and save it to a String to create the account
    public void registerAccount(View view){
        EditText rawEmail, rawPassword;
        rawEmail = findViewById(R.id.editEmailAddress);
        rawPassword = findViewById(R.id.editPassword);
        String email = rawEmail.getText().toString();
        String password = rawPassword.getText().toString();
        createAccount(email, password);
    }

    // Logic that communicated with Firebase and actually creates the account on DB
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        createNewUser(email);
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
    }

    private void createNewUser(String email) {
        EditText rawFirstName, rawLastName;
        rawFirstName = findViewById(R.id.editFirstName);
        rawLastName = findViewById(R.id.editLastName);
        String firstName = rawFirstName.getText().toString();
        String lastName = rawLastName.getText().toString();
        userAccount newUser = new userAccount(email, firstName, lastName);
        primaryDB.child("users").setValue(email);
        primaryDB.child("users").child(email).setValue(newUser);
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // Email sent
                }
            });
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
}