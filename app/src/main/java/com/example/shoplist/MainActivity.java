package com.example.shoplist;

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

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> listElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        FillListElements();
        //Log.i("DEBUG", "listElements.size(): " + Integer.toString(listElements.size()));
        mAdapter = new ListAdapter(listElements);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                NewItemDialogFragment nidf = new NewItemDialogFragment();
                nidf.show(ft, "new_item_dialog");
            }
        });
    }

    public void FillListElements()
    {
        listElements = new ArrayList<String>();

        for (int i = 0; i < 5; i++)
        {
            listElements.add("Hello World! " + Integer.toString(i));
        }
    }

    public void onDialogPositiveClick(DialogFragment dialog)
    {
        Intent intent = getIntent();
        addNewItem(intent.getStringExtra("input_text"));
    }

    public void addNewItem(String input)
    {
        Log.i("DEBUG", input);
    }
}
