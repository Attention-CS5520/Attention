package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.time.SystemClock;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LiveClassDiscussionActivity extends AppCompatActivity {

    private FirebaseFirestore db;

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
        try {
            MyItemCard itemCard = createItemCard(makeJSON());

            itemList.add(itemCard);

            // Put String data in intent, start activity:
            Intent i = new Intent(LiveClassDiscussionActivity.this, InClass.class);
            i.putExtra("DISCUSSION_TEXT", itemCard.getItemDesc());
            //startActivity(i);




        } catch (JSONException e) {
            //e.printStackTrace();
            Log.d("JSON", "JSON exception found");
        }




        createRecyclerView();
    }

    private void createRecyclerView() {

        rLayoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new MyRviewAdapter(itemList);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    // A method to take in a JSON and produce a MyItemCard
    private MyItemCard createItemCard(JSONObject jsonObject) throws JSONException {

        String itemDesc = jsonObject.getString("ItemDesc");


        MyItemCard newItemCard = new MyItemCard(itemDesc);

        return newItemCard;

    }

    // Making a sample JSON object
    JSONObject makeJSON() throws JSONException {

        // creating JSONObject
        JSONObject jo = new JSONObject();

        // putting data to JSONObject
        jo.put("ItemDesc", "text from JSON object");

        return jo;

    }


    // Collects the user's discussion post. (Where does it go next?)
    public void collectInput(){
        // convert edit text to string
        String getInput = txt.getText().toString();

        // ensure that user input bar is not empty
        if (getInput ==null || getInput.trim().equals("")){
            Toast.makeText(getBaseContext(), "Please add a question", Toast.LENGTH_LONG).show();
        }
        // add input into an data collection arraylist
        else {
            arrayListCollection.add(getInput);
           // adapter.notifyDataSetChanged();
        }
    }


    // When the floating button is clicked, the user's question is added and gathered via collectInput()
    public void addQuestion(View view) {

        Log.d("add question()", "addQuestion() reached");

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
                collectInput();


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

    // A method to add a new discussion document to the post collection
    private void addPost(){

        SystemClock clock = SystemClock.getInstance();


        Map<String, Object> newPost = new HashMap<>();
        newPost.put("author", "James Harlowe");
        newPost.put("classId", "classId");
        newPost.put("message", "Here is my message");
        newPost.put("subject", "Reply");
        newPost.put("date", clock.currentTimeMillis());

        db.collection("discussion_posts").document("user_post4")
                .set(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void unused) {
                Toast.makeText(LiveClassDiscussionActivity.this, "Posted!", Toast.LENGTH_SHORT).show();


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
