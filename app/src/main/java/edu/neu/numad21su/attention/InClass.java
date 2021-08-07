package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.installations.time.SystemClock;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class InClass extends AppCompatActivity {

    private FirebaseFirestore db;

    private DocumentReference documentReference;

    // Class name
    private TextView className;

    // Class code
    private TextView classCode;

    // Building
    private TextView building;

    // Room number
    private TextView roomNumber;

    // Instructor name
    private TextView instructorName;


    // This text area allows the user to access the full class discussion
    private TextView moreText;

    // The first discussion excerpt
    //private TextView discussion1;

    // The second discussion excerpt
    private TextView discussion2;

    // The third discussion excerpt
    private TextView discussion3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class);



        // Connect with firebase
        db = FirebaseFirestore.getInstance();

        getClassDiscussion();


        // Setting up the More text
        moreText = findViewById(R.id.moreText);

        // When the More text is clicked, see_all_discussion is triggered.
        moreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moreText.setTextColor(Color.BLACK);

                see_all_discussion(v);

            }
        });


    }

    // A method to get the current class info [...]




    // https://dzone.com/articles/cloud-firestore-read-write-update-and-delete

    public void getClassDiscussion(){

        TextView discussion1 = findViewById(R.id.InClassDiscussionText1);

        TextView discussion2 = findViewById(R.id.discussionText2);

        TextView discussion3 = findViewById(R.id.discussionText3);

        DocumentReference post = db.collection("discussion_posts")
                .document("user_post");

        Query post_history = db.collection("discussion_posts").orderBy("date").limit(3);

        post_history.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    QuerySnapshot querySnapshot = task.getResult();

                    StringBuilder post1 = new StringBuilder("");
                    StringBuilder post2 = new StringBuilder("");
                    StringBuilder post3 = new StringBuilder("");

                    post1.append(querySnapshot.getDocuments().get(0).get("message"));
                    post2.append(querySnapshot.getDocuments().get(1).get("message"));
                    post3.append(querySnapshot.getDocuments().get(2).get("message"));

                    discussion1.setText(post1);
                    discussion2.setText(post2);
                    discussion3.setText(post3);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("discussions", "discussion history not found");

            }
        });





//        post.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
//            @Override
//            public void onComplete(@NonNull Task < DocumentSnapshot > task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot doc = task.getResult();
//                    StringBuilder fields = new StringBuilder("");
//                    fields.append(doc.get("message"));
//                    discussion1.setText(fields.toString());
//                }
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Log.d("Failed", "item not read from firebase");
//
//                    }
//                });



    }

    // A method to add a new discussion document to the post collection
    private void addPost(){

        SystemClock clock = SystemClock.getInstance();


        Map<String, Object> newPost = new HashMap<>();
        newPost.put("author", "Robert Lovelace");
        newPost.put("classId", "classId");
        newPost.put("message", "Here is my response");
        newPost.put("subject", "Re: re: re:");
        newPost.put("date", clock.currentTimeMillis());

        db.collection("discussion_posts").document("user_post3")
                .set(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void unused) {
                Toast.makeText(InClass.this, "Posted!", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InClass.this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error", e.toString());

            }
        });




    }



    // This method brings the user to the Class Discussion activity
    public void see_all_discussion(View view) {

        // The user switches to the Class Discussion activity
         Intent switchActivityIntent = new Intent(this, LiveClassDiscussionActivity.class);
         startActivity(switchActivityIntent);


    }



}
