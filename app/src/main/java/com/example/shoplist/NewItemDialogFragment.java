package com.example.shoplist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import static com.example.shoplist.MainActivity.DIALOG_EDIT;

/**
 * Created by frederik on 12-03-2018.
 */

public class NewItemDialogFragment extends DialogFragment {
    public interface NoticeDialogListener{
        void onDialogPositiveClick(DialogFragment dialog, ListItem li);
    }
    NoticeDialogListener mListener;
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (NoticeDialogListener)activity;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + "Must implement NoticeDialogListener");
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();
        final int state = bundle.getInt("DialogState");
        final int ID = bundle.getInt("id");
        Log.i("CustomDebug", "id = "+Integer.toString(ID));
        Log.i("CustomDebug", "state = " + state);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater li = getActivity().getLayoutInflater();
        View dialogView = li.inflate(R.layout.item_dialog, null);

        if(state == DIALOG_EDIT)
        {
            EditText etTitleE;
            etTitleE = dialogView.findViewById(R.id.title);
            etTitleE.setText(bundle.getString("title"));
            Log.i("CustomDebug", "title edited");

            EditText etPriceE;
            etPriceE = dialogView.findViewById(R.id.price);
            etPriceE.setText(Integer.toString(bundle.getInt("price")));
            Log.i("CustomDebug", "price edited");

            CheckBox cbCheckBoxE;
            cbCheckBoxE = dialogView.findViewById(R.id.checkbox_dialog);
            cbCheckBoxE.setChecked(bundle.getBoolean("checkbox"));
        }
        // Use the Builder class for convenient dialog construction

        builder.setMessage(R.string.new_item_dialog_text)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            EditText etTitle;
                            etTitle = ((AlertDialog)dialog).findViewById(R.id.title);
                            String title = etTitle.getText().toString();

                            EditText etPrice;
                            etPrice = ((AlertDialog)dialog).findViewById(R.id.price);

                            int price;
                            if(etPrice.getText().toString().length() == 0)
                            {
                                price = 0;
                            }
                            else
                            {
                                price = Integer.parseInt(etPrice.getText().toString());
                            }

                            CheckBox cbCheckBox;
                            cbCheckBox = ((AlertDialog)dialog).findViewById(R.id.checkbox_dialog);
                            boolean cb = cbCheckBox.isChecked();

                            ListItem li = new ListItem(ID, title, price, cb);
                            mListener.onDialogPositiveClick(NewItemDialogFragment.this, li);
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
