package com.example.shoplist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import com.example.shoplist.MainActivity;

import static com.example.shoplist.MainActivity.DIALOG_EDIT;

/**
 * Created by frederik on 12-03-2018.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    public interface checkboxInterface
    {
        void checkboxHasChanged(int id, boolean val);
    }

    checkboxInterface cListener;

    private ArrayList<ListItem> mDataset;
    private Context cont;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(ArrayList<ListItem> myDataset, Context context) {

        Log.i("CustomDebug", "Created ListAdapter-instance.");
        mDataset = myDataset;

        Log.i("CustomDebug", "ListAdapter got data successfully.");

        cont = context;

        cListener = (checkboxInterface)context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_element, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mView.setText(mDataset[position]);
        TextView tw = holder.mView.findViewById(R.id.text_field);

        tw.setText(mDataset.get(position).title);

        final int newPos = position;

        //TODO: change to better solution when proficient...
        tw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                editListItem(mDataset.get(newPos));
            }
        });

        CheckBox cb = holder.mView.findViewById(R.id.checkbox);
        cb.setChecked(mDataset.get(position).checkBox);

        cb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i("CustomDebug", "Clicked on Checkbox at pos: " + Integer.toString(newPos));

                mDataset.get(newPos).checkBox = !mDataset.get(newPos).checkBox;
                cListener.checkboxHasChanged( mDataset.get(newPos).id,  mDataset.get(newPos).checkBox);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    public void editListItem(ListItem li)
    {
        Bundle b = new Bundle();
        b.putInt("DialogState", DIALOG_EDIT);
        b.putInt("id", li.id);
        b.putString("title", li.title);
        b.putInt("price", li.price);
        b.putBoolean("checkbox", li.checkBox);

        FragmentManager fm = ((Activity)cont).getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        NewItemDialogFragment nidf = new NewItemDialogFragment();
        nidf.setArguments(b);
        nidf.show(ft, "new_item_dialog");
    }
}

