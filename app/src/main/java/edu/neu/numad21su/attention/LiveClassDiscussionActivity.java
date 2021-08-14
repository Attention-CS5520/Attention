package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.installations.time.SystemClock;
//import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LiveClassDiscussionActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ArrayList<String> messageList = new ArrayList<>();
    private ArrayList<String> dateList = new ArrayList<>();
    private Discussion myDiscussion = new Discussion("", "", "");
    private RecyclerView recyclerView;
    private MyRviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private ArrayList<MyItemCard> itemList = new ArrayList<>();
    private FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connect with firebase
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_live_class_discussion);
        Query post_history = db.collection("discussion_posts").orderBy("date");
        EventListener<QuerySnapshot> querySnapshotEventListener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d("change", "database changed");
               // getMessageBoard();
            }
        };
        post_history.addSnapshotListener(querySnapshotEventListener);

        // Adding listener to floating button, for user question input
        floatingButton = findViewById(R.id.addButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", "floating action onClick() reached");
                addPost();
            }
        });
        getMessageBoard();
    }

    private void createRecyclerView() {
        rLayoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new MyRviewAdapter(itemList);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    // A method to add a new discussion document to the post collection, gathered from user input
    private void addPost(){

        // Getting the user's discussion post with an AlertDialog
        AlertDialog.Builder questionAlert = new AlertDialog.Builder(this);
        final EditText userQuestion = new EditText(this);
        questionAlert.setTitle("Enter your question:");
        questionAlert.setView(userQuestion);
        LinearLayout alertLayout = new LinearLayout(this);
        alertLayout.setOrientation(LinearLayout.VERTICAL);
        alertLayout.addView(userQuestion);
        questionAlert.setView(alertLayout);
        questionAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Posting the user's comment to the database
                postToDataBase(userQuestion.getText().toString());
                // Refreshing the recycler items
                itemList = new ArrayList<>();
                rviewAdapter.notifyDataSetChanged();
            }
        });
        questionAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel(); // closes dialog
            }
        });
        questionAlert.create();
        questionAlert.show();
    }

    // Getting the current state of the message board
    private void getMessageBoard(){
        // Get all discussion posts
        Query post_history = db.collection("discussion_posts").orderBy("date");
        post_history.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for (int i = 0; i < querySnapshot.size(); i++) {
                        String message = (String) querySnapshot.getDocuments().get(i).get("message");
                        String messageDate = (String) querySnapshot.getDocuments().get(i).get("date");
                        messageList.add(message);
                        dateList.add(messageDate);
                    }
                    for (int k = 0; k < messageList.size(); k++) {
                        MyItemCard itemCard = new MyItemCard(messageList.get(k), dateList.get(k));
                        itemList.add(itemCard);
                    }
                }
                createRecyclerView();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("discussions", "discussion history not found");
            }
        });
    }

    private void postToDataBase(String userPost){
        int postCount = itemList.size() + 1;
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd H:mm aaa");
        Map<String, Object> newPost = new HashMap<>();
        newPost.put("author", "James Harlowe");
        newPost.put("classId", "classId");
        newPost.put("message", userPost);
        newPost.put("subject", "Reply");
        newPost.put("date", ft.format(dNow));
        db.collection("discussion_posts").document("user_post" + postCount)
                .set(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void unused) {
                Toast.makeText(LiveClassDiscussionActivity.this, "Posted!", Toast.LENGTH_SHORT).show();
                // Refresh itemCards
                messageList = new ArrayList<>();
                dateList = new ArrayList<>();
                getMessageBoard();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LiveClassDiscussionActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error", e.toString());
            }
        });
    }
}
