package edu.neu.numad21su.attention;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.neu.numad21su.attention.ItemClickListener;

public class MyItemCard implements ItemClickListener {

    private final String itemTime;
    private final String itemDesc;
    private int imageSource;

    // sender name = user name?
    private String senderName;


    //Constructor
    public MyItemCard(String itemDesc) {

        this.itemTime = date();
        this.itemDesc = itemDesc;
        //this.imageSource = imageSource;
    }

    //Getters for the imageSource, itemName and itemDesc


    public String getItemDesc() {
        return itemDesc;
    }

    public String getItemTime() {
        return itemTime;
    }

    //public String getItemName() {
    //    return itemName;
    //}

    public int getImageSource() {
        return imageSource;
    }

    public static String date() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd H:mm aaa");
        return ft.format(dNow);
    }


    @Override
    public void onItemClick(int position) {

    }
}
