package edu.neu.numad21su.attention;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
        MyItemCard itemCard = new MyItemCard("Welcome to the class!");
        itemList.add(itemCard);

        itemCard = new MyItemCard("Hope You are enjoying the class! This is a great class " +
                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
        itemList.add(itemCard);

        itemCard = new MyItemCard("Hope You are enjoying the class! This is a great class " +
                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
        itemList.add(itemCard);

        itemCard = new MyItemCard("Hope You are enjoying the class! This is a great class " +
                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
        itemList.add(itemCard);

        itemCard = new MyItemCard("Hope You are enjoying the class! This is a great class " +
                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
        itemList.add(itemCard);

        itemCard = new MyItemCard("Hope You are enjoying the class! This is a great class " +
                "students are helpful!!!!!!!!!!!!!!!!!!!!!!!!");
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
}