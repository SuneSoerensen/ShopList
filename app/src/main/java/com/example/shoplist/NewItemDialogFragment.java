package com.example.shoplist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

/**
 * Created by frederik on 12-03-2018.
 */

public class NewItemDialogFragment extends DialogFragment {
    public interface NoticeDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
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
        LayoutInflater li = getActivity().getLayoutInflater();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.new_item_dialog_text)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        EditText et;
                        et = (EditText)((AlertDialog)dialog).findViewById(R.id.inputText);
                        String inputText = et.getText().toString();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("input_text", inputText);
                        mListener.onDialogPositiveClick(NewItemDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
        .setView(li.inflate(R.layout.new_item_dialog_text_field, null));
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
