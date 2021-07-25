package edu.neu.numad21su.attention;

import edu.neu.numad21su.attention.ItemClickListener;

public class MyItemCard implements ItemClickListener {

    //private final String itemName;
    private final String itemDesc;
    private int imageSource;

    // sender name = user name?
    private String senderName;


    //Constructor
    public MyItemCard(String itemDesc) {

        //this.itemName = itemName;
        this.itemDesc = itemDesc;
        //this.imageSource = imageSource;
    }

    //Getters for the imageSource, itemName and itemDesc


    public String getItemDesc() {
        return itemDesc;
    }

    //public String getItemName() {
    //    return itemName;
    //}

    public int getImageSource() {
        return imageSource;
    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onCheckBoxClick(int position) {

    }
}
