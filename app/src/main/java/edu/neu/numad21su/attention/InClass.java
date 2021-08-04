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

        addPost();




        // or get the first three discussion items as JSONs from FB, not the LiveClassDiscussion activity?

//        try {
//            JSONObject jsonObject = makeJSON();
//
//            String itemDesc = jsonObject.getString("ItemDesc");
//
//
//
//            discussion1.setText(itemDesc);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }












        // https://stackoverflow.com/questions/3510649/how-to-pass-a-value-from-one-activity-to-another-in-android
        // Create intent in ClassDiscussion? Put String data in intent, start activity:
        // Intent i = new Intent(ClassDiscussion.this, InClass.class);
        // i.putExtra("MY_kEY",String X);


        // Getting the discussions from LiveClassDiscussion to display here
       // Intent intent = getIntent();
       // String liveDiscussion = intent.getStringExtra("DISCUSSION_TEXT");

        //discussion1 = findViewById(R.id.discussionText1);
        //discussion1.setText(liveDiscussion);


        // Also have to get the header info [...]. Where does the header info come from? what format?



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

    // https://dzone.com/articles/cloud-firestore-read-write-update-and-delete

    public void getClassDiscussion(){

        TextView discussion1 = findViewById(R.id.InClassDiscussionText1);

        DocumentReference post = db.collection("posts")
                .document("L6yXHPcK6cg7v7mTG0Oc");

        post.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
            @Override
            public void onComplete(@NonNull Task < DocumentSnapshot > task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    StringBuilder fields = new StringBuilder("");
                    fields.append("author: ").append(doc.get("author"));
                    discussion1.setText(fields.toString());
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("Failed", "item not read from firebase");

                    }
                });



    }

    // A method to add a new discussion document to the post collection
    private void addPost(){

        Map<String, Object> newPost = new HashMap<>();
        newPost.put("author", "Bilbo Baggins");
        newPost.put("classId", "classId");
        newPost.put("message", "Time for tea");
        newPost.put("subject", "Comments");

        db.collection("discussion_posts").document("user_post")
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

    // Making a sample JSON object
    JSONObject makeJSON() throws JSONException {

        // creating JSONObject
        JSONObject jo = new JSONObject();

        // putting data to JSONObject
        jo.put("ItemDesc", "text from JSON object");

        return jo;

    }

}
