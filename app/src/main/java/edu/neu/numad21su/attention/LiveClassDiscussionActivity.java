package edu.neu.numad21su.attention;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

public class LiveClassDiscussionActivity extends AppCompatActivity {

    private Discussion myDiscussion = new Discussion("", "", "");
    private RecyclerView recyclerView;
    private MyRviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private ArrayList<MyItemCard> itemList = new ArrayList<>();
    private FloatingActionButton floatingButton;

    ArrayList<CharSequence> arrayListCollection = new ArrayList<>();
    ArrayAdapter<CharSequence> adapter;
    EditText txt; // user input bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_live_class_discussion);

        // Adding listener to floating button, for user question input
        floatingButton = findViewById(R.id.addButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("onClick", "floating action onClick() reached");

                addQuestion(v);
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
            adapter.notifyDataSetChanged();
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
}
