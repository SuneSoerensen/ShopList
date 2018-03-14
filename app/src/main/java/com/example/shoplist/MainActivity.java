package com.example.shoplist;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NewItemDialogFragment.NoticeDialogListener {
    public static final int DIALOG_CREATE = 0;
    public static final int DIALOG_EDIT   = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ListItem> listElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        listElements = new ArrayList<>();

            //To test if empty listElements results in error:
        ListItem li = new ListItem(0, "TestItem", 42, true);

        //listElements.add(li);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ListAdapter(listElements,this);
        mRecyclerView.setAdapter(mAdapter);



        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                int state = DIALOG_CREATE;
                b.putInt("DialogState", state);
                b.putInt("id", listElements.size());
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                NewItemDialogFragment nidf = new NewItemDialogFragment();
                nidf.setArguments(b);
                nidf.show(ft, "new_item_dialog");
            }
        });
    }


    public void onDialogPositiveClick(DialogFragment dialog, ListItem li)
    {
        if(li.id == listElements.size())
        {
            listElements.add(li);
        }
        else
        {
            listElements.set(li.id, li);
            mAdapter.notifyDataSetChanged();
        }

    }

}
