package edu.neu.numad21su.attention;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LiveClassDiscussionActivity extends AppCompatActivity {

    private Discussion myDiscussion = new Discussion("", "", "");
    private RecyclerView recyclerView;
    private MyRviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private ArrayList<MyItemCard> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_class_discussion);
        // Just adding a couple of items for now, will come from database in future.
        MyItemCard itemCard = new MyItemCard("1.Welcome to the class!");
        itemList.add(itemCard);

        itemCard = new MyItemCard("2.Hope You are enjoying the class! This is a great class " +
                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
        itemList.add(itemCard);

        itemCard = new MyItemCard("3.Hope You are enjoying the class! This is a great class " +
                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
        itemList.add(itemCard);

        itemCard = new MyItemCard("4.Hope You are enjoying the class! This is a great class " +
                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
        itemList.add(itemCard);

        itemCard = new MyItemCard("5.Hope You are enjoying the class! This is a great class " +
                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
        itemList.add(itemCard);

        itemCard = new MyItemCard("6.Hope You are enjoying the class! This is a great class " +
                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
        itemList.add(itemCard);

        // ItemCard with sample JSON object
        try {
            itemCard = createItemCard(makeJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Adding the sample item to the list
        itemList.add(itemCard);


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
    private MyItemCard createItemCard(JSONObject jsonObject){

        String itemDesc = "desc";


        MyItemCard newItemCard = new MyItemCard(itemDesc);

        return newItemCard;

    }

    JSONObject makeJSON() throws JSONException {

        // creating JSONObject
        JSONObject jo = new JSONObject();

        // putting data to JSONObject
        jo.put("ItemDesc", "desc 1");



        return jo;


    }

}