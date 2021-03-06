package com.example.shoplist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by sune on 3/21/18.
 */

public class StoreFilterDialogFragment extends DialogFragment
{
    public interface FilterStoreInterface
    {
        void filterStorePositiveBtn(String store);
    }

    FilterStoreInterface filterStoreInterface;

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            filterStoreInterface = (FilterStoreInterface)activity;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + "Must implement FilterStoreInterface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater li = getActivity().getLayoutInflater();
        final View dialogView = li.inflate(R.layout.store_filter_dialog, null);

        builder.setMessage(R.string.filter_store_dialog_text)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        EditText et;
                        et = dialogView.findViewById(R.id.store);
                        String store = et.getText().toString();
                        filterStoreInterface.filterStorePositiveBtn(store);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .setView(dialogView);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
