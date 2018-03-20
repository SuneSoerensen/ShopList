package com.example.shoplist;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity implements NewItemDialogFragment.NoticeDialogListener, ListAdapter.checkboxInterface {
    public static final int DIALOG_CREATE = 0;
    public static final int DIALOG_EDIT   = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ListItem> listElements;

    private Queue<ListItem> toBeDeleted;
    public ReentrantLock lockTbd;

    private Queue<ListItem> hasBeenChanged;
    public ReentrantLock lockHbc;

    private Queue<ListItem> isNew;
    public ReentrantLock lockIn;

    private boolean updateDb = true;
    private AppDataBase dataBase;

    Thread dbUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listElements = new ArrayList<>();

        //Create queues
        toBeDeleted = new LinkedList<>();
        hasBeenChanged = new LinkedList<>();
        isNew = new LinkedList<>();

        //Create locks
        lockTbd = new ReentrantLock();
        lockHbc = new ReentrantLock();
        lockIn  = new ReentrantLock();

        //Create database:
        dataBase = AppDataBase.getAppDataBase(this);
        Log.i("CustomDebug", "Database built.");

        final List<ListItem> list = dataBase.listItemDao().getAll();
        Log.i("CustomDebug", "Got all elements from database. No of elements: " + Integer.toString(list.size()));

        for(int i = 0; i < list.size(); i++)
        {
            listElements.add(list.get(i));
        }
        Log.i("CustomDebug", "Copied all elements from database.");

        //Create thread to update database:
        dbUpdater = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                updateDatabase();
            }
        });

        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Log.i("CustomDebug", "Hurra.");

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
                b.putInt("id", getMinAvailableId());
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                NewItemDialogFragment nidf = new NewItemDialogFragment();
                nidf.setArguments(b);
                nidf.show(ft, "new_item_dialog");
            }
        });

        dbUpdater.start();
        Log.i("CustomDebug", "Started dbUpdater-thread.");
        Log.i("CustomDebug", "Completed onCreate.");
    }


    public void onDialogPositiveClick(DialogFragment dialog, ListItem li, boolean liIsNew)
    {
        if(liIsNew) //If it's a new element
        {
            listElements.add(li.id,li);
            mAdapter.notifyDataSetChanged();

            lockIn.lock();
            try
            {
                isNew.add(li);
            }
            finally
            {
                lockIn.unlock();
            }
        }
        else //If it's a changed element
        {
            for(int i = 0; i < listElements.size(); i++)
            {
                if(listElements.get(i).id == li.id)
                {
                    listElements.set(i, li);
                    break;
                }
            }


            lockHbc.lock();
            try
            {
                hasBeenChanged.add(li);
            }
            finally
            {
                lockHbc.unlock();
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    public void onDialogNegativeClick(DialogFragment dialog, int anId)
    {
        for(int i = 0; i < listElements.size(); i++)
        {
            if(listElements.get(i).id == anId)
            {
                lockTbd.lock();
                try
                {
                    toBeDeleted.add(listElements.get(i));
                }
                finally
                {
                    lockTbd.unlock();
                }

                listElements.remove(i);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void updateDatabase()
    {
        ListItem temp;

        while(updateDb)
        {
            //Check toBeDeleted-queue
            lockTbd.lock();
            try
            {
                temp = toBeDeleted.poll();
                if(temp != null)
                {
                    dataBase.listItemDao().delete(temp);
                }
            }
            finally
            {
                lockTbd.unlock();
            }

            //Check hasBeenChanged-queue
            lockHbc.lock();
            try
            {
                temp = hasBeenChanged.poll();
                if(temp != null)
                {
                    dataBase.listItemDao().update(temp);
                }
            }
            finally
            {
                lockHbc.unlock();
            }

            lockIn.lock();
            try
            {
                temp = isNew.poll();
                if(temp != null)
                {
                    dataBase.listItemDao().insert(temp);
                }
            }
            finally
            {
                lockIn.unlock();
            }

            //Sleep for a while
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        updateDb = false;

        try
        {
            dbUpdater.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        //Cleanup
        AppDataBase.deleteInstance();
    }

    private int getMinAvailableId()
    {
        for(int i = 0; i < listElements.size(); i++)
        {
            if(listElements.get(i).id > i)
            {
                return i;
            }
        }

        return listElements.size();
    }

    @Override
    public void checkboxHasChanged(int id, boolean val)
    {
        Log.i("CustomDebug", "Got id: " + Integer.toString(id));
        for(int i = 0; i < listElements.size(); i++)
        {
            if(listElements.get(i).id == id)
            {
                lockHbc.lock();
                try
                {
                    hasBeenChanged.add(listElements.get(i));
                }
                finally
                {
                    lockHbc.unlock();
                }
            }
        }
    }
}
