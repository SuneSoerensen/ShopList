package com.example.shoplist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by sune on 3/14/18.
 */

@Entity
public class ListItem
{
    //Attributes:
    @PrimaryKey
    public int id;

    //@ColumnInfo(name = "aTitle")
    public String title;

    //@ColumnInfo(name = "aPrice")
    public int price;

   //@ColumnInfo(name = "aCheckBox")
    public boolean checkBox;

    public ListItem()
    {
    }

    public ListItem(int anId, String aTitle, int aPrice, boolean aCheckBox)
    {
        id = anId;
        title = aTitle;
        price = aPrice;
        checkBox = aCheckBox;
    }
}
