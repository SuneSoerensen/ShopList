package com.example.shoplist;

/**
 * Created by sune on 3/14/18.
 */

public class ListItem
{
    //Attributes:
    public int id;
    public String title;
    public int price;
    public boolean checkBox;

    public ListItem(int anId, String aTitle, int aPrice, boolean aCheckBox)
    {
        id = anId;
        title = aTitle;
        price = aPrice;
        checkBox = aCheckBox;
    }
}
