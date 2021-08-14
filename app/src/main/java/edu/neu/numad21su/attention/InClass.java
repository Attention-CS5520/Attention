package edu.neu.numad21su.attention;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.slugify.Slugify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.neu.numad21su.attention.quizScreen.Quiz;

public class InClass extends AppCompatActivity {

  private FirebaseFirestore db;

  private DocumentReference documentReference;

  // Class name
  private TextView className;

  // Class code
  private TextView classCodeText;

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
  private FirebaseAuth mAuth;
  private FirebaseUser user;
  private long classJoinTime = System.currentTimeMillis();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_in_class);
    mAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();
    Query quizToTake = db.collection("quizToTake");
    // Adding quizToTake listener for students only
    user = mAuth.getCurrentUser();
    if (user != null) {
      String emailSlug = new Slugify().slugify(user.getEmail());
      db.collection("userType").document(emailSlug).get().addOnSuccessListener(dr -> {
        String type = (String) dr.get("type");
        if (type != null && type.equals("student")) {
          quizToTake.addSnapshotListener((value, error) -> {
            if (value != null) {
              value.getDocumentChanges().forEach(quizDoc -> {
                Quiz quiz = quizDoc.getDocument().toObject(Quiz.class);
                if (quiz.startedAtMillis > this.classJoinTime) {
                  Intent quizIntent = new Intent(getApplicationContext(), QuizScreen.class);
                  quizIntent.putExtra("Quiz", quiz);
                  InClass.this.startActivity(quizIntent);
                }
              });
            }
          });
        }
      });
    }
    getClassDiscussion();
    getHeaderInfo();
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


  public void getClassDiscussion() {

    TextView discussion1 = findViewById(R.id.InClassDiscussionText1);

    TextView discussion2 = findViewById(R.id.discussionText2);

    TextView discussion3 = findViewById(R.id.discussionText3);

    Query post_history = db.collection("discussion_posts");

    post_history.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {

        if (task.isSuccessful()) {


          QuerySnapshot querySnapshot = task.getResult();

          StringBuilder post1 = new StringBuilder("");
          StringBuilder post2 = new StringBuilder("");
          StringBuilder post3 = new StringBuilder("");

          // If there are no items, no discussion appears

          // If there is one item, one discussion message appears
          if (querySnapshot.size() == 1) {

            post1.append(querySnapshot.getDocuments().get(0).get("message"));
            discussion1.setText(post1);


          }

          // If there are two items, two discussion messages appear
          if (querySnapshot.size() == 2) {

            post1.append(querySnapshot.getDocuments().get(1).get("message"));
            discussion1.setText(post1);

            post2.append(querySnapshot.getDocuments().get(0).get("message"));
            discussion2.setText(post2);


          }

          // If there are three items, three discussion messages appear
          if (querySnapshot.size() == 3) {

            post1.append(querySnapshot.getDocuments().get(2).get("message"));
            discussion1.setText(post1);

            post2.append(querySnapshot.getDocuments().get(1).get("message"));
            discussion2.setText(post2);

            post3.append(querySnapshot.getDocuments().get(0).get("message"));
            discussion3.setText(post3);


          }

          // If there are more than three items, show the most recent three
          if (querySnapshot.size() > 3) {

            Log.d("length of querysnapshot", String.valueOf(querySnapshot.size()));

            post1.append(querySnapshot.getDocuments().get(querySnapshot.size() - 1).get("message"));
            discussion1.setText(post1);

            post2.append(querySnapshot.getDocuments().get(querySnapshot.size() - 2).get("message"));
            discussion2.setText(post2);

            post3.append(querySnapshot.getDocuments().get(querySnapshot.size() - 3).get("message"));
            discussion3.setText(post3);


          }


        }

      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {

        Log.d("discussions", "discussion history not found");

      }
    });

  }


  // This method brings the user to the Class Discussion activity
  public void see_all_discussion(View view) {

    // The user switches to the Class Discussion activity
    Intent switchActivityIntent = new Intent(this, LiveClassDiscussionActivity.class);
    startActivity(switchActivityIntent);


  }

  // Getting the header info from the database
  public void getHeaderInfo() {

    TextView classCodeTextView = findViewById(R.id.classCode);
    TextView classNameTextView = findViewById(R.id.className);
    TextView instructorNameTextView = findViewById(R.id.instructorName);
    TextView buildingTextView = findViewById(R.id.building);
    TextView roomNumberTextView = findViewById(R.id.roomNumber);


    Query header_info = db.collection("classes");

    header_info.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {

        if (task.isSuccessful()) {

          QuerySnapshot querySnapshot = task.getResult();

          StringBuilder classCode = new StringBuilder("");
          StringBuilder className = new StringBuilder("");
          StringBuilder instructorName = new StringBuilder("");
          StringBuilder building = new StringBuilder("");
          StringBuilder room = new StringBuilder("");

          classCode.append(querySnapshot.getDocuments().get(0).get("classCode"));
          classCodeTextView.setText(classCode);

          className.append(querySnapshot.getDocuments().get(0).get("className"));
          classNameTextView.setText(className);

          instructorName.append(querySnapshot.getDocuments().get(0).get("professor"));
          instructorNameTextView.setText(instructorName);

          building.append(querySnapshot.getDocuments().get(0).get("building"));
          buildingTextView.setText(building);

          room.append(querySnapshot.getDocuments().get(0).get("roomNumber"));
          roomNumberTextView.setText(room);


        }


      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {

      }
    });


  }


}
