package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.github.slugify.Slugify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    // Create a Firebase Authentication Variable
    private FirebaseAuth mAuth;
    // Create a reference to Firebase DB
    private DatabaseReference primaryDB;
    private FirebaseFirestore db;
    private boolean isStudent = false;
    private String registrantFirstName;
    private String registrantLastName;
    private String registrantEmail;
    private String registrantPassword;


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

    public void parseValues() {
        EditText rawEmail, rawPassword, rawFirstName, rawLastName;
        rawEmail = findViewById(R.id.editEmailAddress);
        rawPassword = findViewById(R.id.editPassword);
        rawFirstName = findViewById(R.id.editFirstName);
        rawLastName = findViewById(R.id.editLastName);
        registrantEmail = rawEmail.getText().toString();
        registrantFirstName = rawFirstName.getText().toString();
        registrantLastName = rawLastName.getText().toString();
        registrantPassword = rawPassword.getText().toString();

    }

    // Take the E-Mail and Password input and save it to a String to create the account
    public void onClick(View view){
        parseValues();
        createAccount();
       createAccount();
    }

    // Logic that communicated with Firebase and actually creates the account on DB
    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(registrantEmail, registrantPassword)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        db = FirebaseFirestore.getInstance();
                        Map<String, Object> newPost = new HashMap<>();
                        newPost.put("type", isStudent? "student" : "instructor");
                        newPost.put("firstName", registrantFirstName);
                        newPost.put("lastName", registrantLastName);
                        Slugify slg = new Slugify();
                        String emailSlug = slg.slugify(registrantEmail);
                        db.collection("userType").document(emailSlug)
                                .set(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Log.d("UserType", "success");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("UserType", "success");}
                        });
                        Intent activity2Intent = new Intent(getApplicationContext(), InClass.class);
                        startActivity(activity2Intent);
                        Toast.makeText(RegisterActivity.this, "Account Created Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_instructor:
                if (checked)
                    // Pirates are the best
                    isStudent = false;
                    break;
            case R.id.radio_student:
                if (checked)
                    isStudent = true;
                    break;
        }
    }
    private void reload() { }
}