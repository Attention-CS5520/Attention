package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.installations.time.SystemClock;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LiveClassDiscussionActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ArrayList<String> messageList = new ArrayList<>();

    private Discussion myDiscussion = new Discussion("", "", "");
    private RecyclerView recyclerView;
    private MyRviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private ArrayList<MyItemCard> itemList = new ArrayList<>();
    private FloatingActionButton floatingButton;

    // Gathering a new discussion post from the user
    ArrayList<CharSequence> arrayListCollection = new ArrayList<>();
   // ArrayAdapter<CharSequence> adapter;
    EditText txt; // user input bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Connect with firebase
        db = FirebaseFirestore.getInstance();


        setContentView(R.layout.activity_live_class_discussion);

        // Adding listener to floating button, for user question input
        floatingButton = findViewById(R.id.addButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("onClick", "floating action onClick() reached");

                addPost();
            }
        });

//        ArrayList<String> messageList = getMessageBoard();
//
//
//        for (int i = 0; i < messageList.size(); i++){
//
//            MyItemCard itemCard = new MyItemCard(messageList.get(i));
//
//            itemList.add(itemCard);
//
//        }
//
//
        getMessageBoard();
//
//        createRecyclerView();



        // Just adding a couple of items for now, will come from database in future.
//        MyItemCard itemCard = new MyItemCard("1.Welcome to the class!");
//        itemList.add(itemCard);
//
//        itemCard = new MyItemCard("2.Hope You are enjoying the class! This is a great class " +
//                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
//        itemList.add(itemCard);
//
//        itemCard = new MyItemCard("3.Hope You are enjoying the class! This is a great class " +
//                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
//        itemList.add(itemCard);
//
//        itemCard = new MyItemCard("4.Hope You are enjoying the class! This is a great class " +
//                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
//        itemList.add(itemCard);
//
//        itemCard = new MyItemCard("5.Hope You are enjoying the class! This is a great class " +
//                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
//        itemList.add(itemCard);
//
//        itemCard = new MyItemCard("6.Hope You are enjoying the class! This is a great class " +
//                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
//        itemList.add(itemCard);





        // ItemCard with sample JSON object- should come from database?
//        try {
//            MyItemCard itemCard = createItemCard(makeJSON());
//
//            itemList.add(itemCard);
//
//            // Put String data in intent, start activity:
//            Intent i = new Intent(LiveClassDiscussionActivity.this, InClass.class);
//            i.putExtra("DISCUSSION_TEXT", itemCard.getItemDesc());
//            //startActivity(i);
//
//
//
//
//        } catch (JSONException e) {
//            //e.printStackTrace();
//            Log.d("JSON", "JSON exception found");
//        }





    }

    private void createRecyclerView() {

        rLayoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new MyRviewAdapter(itemList);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }



//    // Collects the user's discussion post. (Where does it go next?)
//    public void collectInput(){
//        // convert edit text to string
//        String getInput = txt.getText().toString();
//
//        // ensure that user input bar is not empty
//        if (getInput ==null || getInput.trim().equals("")){
//            Toast.makeText(getBaseContext(), "Please add a question", Toast.LENGTH_LONG).show();
//        }
//        // add input into an data collection arraylist
//        else {
//            arrayListCollection.add(getInput);
//           // adapter.notifyDataSetChanged();
//        }
//    }


    // When the floating button is clicked, the user's question is added and gathered via collectInput()
//    public void addQuestion(View view) {
//
//        Log.d("add question()", "addQuestion() reached");
//
//        AlertDialog.Builder questionAlert = new AlertDialog.Builder(this);
//        final EditText userQuestion = new EditText(this);
//
//        questionAlert.setTitle("Enter your question:");
//
//        questionAlert.setView(userQuestion);
//        LinearLayout alertLayout = new LinearLayout(this);
//        alertLayout.setOrientation(LinearLayout.VERTICAL);
//        alertLayout.addView(userQuestion);
//        questionAlert.setView(alertLayout);
//
//        questionAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                txt = userQuestion; // storing the user input
//                //collectInput();
//
//
//            }
//        });
//
//        questionAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                dialog.cancel(); // closes dialog
//
//
//            }
//        });
//
//        questionAlert.create();
//        questionAlert.show();
//
//
//
//
//    }

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
                txt = userQuestion; // storing the user input
                //collectInput();

                // Posting the user's comment to the database
                postToDataBase(userQuestion.getText().toString());

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

                if (task.isSuccessful()){

                    QuerySnapshot querySnapshot = task.getResult();

                    for (int i = 0; i < querySnapshot.size(); i++){

                        String message = (String) querySnapshot.getDocuments().get(i).get("message");

                        messageList.add(message);


                    }

                    for (int k = 0; k < messageList.size(); k++){

                        MyItemCard itemCard = new MyItemCard(messageList.get(k));

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


        SystemClock clock = SystemClock.getInstance();


        Map<String, Object> newPost = new HashMap<>();
        newPost.put("author", "James Harlowe");
        newPost.put("classId", "classId");
        newPost.put("message", userPost);
        newPost.put("subject", "Reply");
        newPost.put("date", clock.currentTimeMillis());

        db.collection("discussion_posts").document("user_post4")
                .set(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void unused) {
                Toast.makeText(LiveClassDiscussionActivity.this, "Posted!", Toast.LENGTH_SHORT).show();

                // Refresh itemCards
                messageList = new ArrayList<>();
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
