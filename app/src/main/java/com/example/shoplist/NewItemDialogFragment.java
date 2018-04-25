package com.example.shoplist;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import static com.example.shoplist.MainActivity.DIALOG_EDIT;

/**
 * Created by frederik on 12-03-2018.
 */

public class NewItemDialogFragment extends DialogFragment {
    public interface NoticeDialogListener{
        void onDialogPositiveClick(DialogFragment dialog, ListItem li, boolean liIsNew);
        void onDialogNegativeClick(DialogFragment dialog, int anId);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) //To display keyboard when dialog is created.
    {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Log.i("CustomDebug", "Keyboard up");
    }

    /*@Override
    public void onDestroyView()
    {
        super.onDestroyView();
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Log.i("CustomDebug", "Keyboard up");
    }*/

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Bundle bundle = getArguments();
        final int state = bundle.getInt("DialogState");
        final int ID = bundle.getInt("id");
        Log.i("CustomDebug", "id = "+Integer.toString(ID));
        Log.i("CustomDebug", "state = " + state);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater li = getActivity().getLayoutInflater();
        View dialogView = li.inflate(R.layout.item_dialog, null);
        /*EditText firstFocus;
        firstFocus = dialogView.findViewById(R.id.title);
        firstFocus.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);*/



        boolean isNew = true;

        if(state == DIALOG_EDIT)
        {
            isNew = false;
            EditText etTitleE;
            etTitleE = dialogView.findViewById(R.id.title);
            etTitleE.setText(bundle.getString("title"));
            Log.i("CustomDebug", "title edited");

            EditText etPriceE;
            etPriceE = dialogView.findViewById(R.id.price);
            etPriceE.setText(Double.toString(bundle.getDouble( "price")));
            Log.i("CustomDebug", "price edited to " + etPriceE.getText().toString());

            /*CheckBox cbCheckBoxE;
            cbCheckBoxE = dialogView.findViewById(R.id.checkbox_dialog);
            cbCheckBoxE.setChecked(bundle.getBoolean("checkbox"));*/

            EditText etStore;
            etStore = dialogView.findViewById(R.id.store);
            etStore.setText(bundle.getString("store"));
        }
        final boolean isNewCopy = isNew;
        // Use the Builder class for convenient dialog construction

        builder.setMessage(R.string.new_item_dialog_text)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            EditText etTitle;
                            etTitle = ((AlertDialog)dialog).findViewById(R.id.title);
                            //etTitle.requestFocus();
                            //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                            String title = etTitle.getText().toString();

                            EditText etPrice;
                            etPrice = ((AlertDialog)dialog).findViewById(R.id.price);

                            double price;
                            if(etPrice.getText().toString().length() == 0)
                            {
                                price = 0;
                            }
                            else
                            {
                                price = Double.parseDouble(etPrice.getText().toString());
                            }

                            /*CheckBox cbCheckBox;
                            cbCheckBox = b;*/
                            boolean cb = bundle.getBoolean("checkbox");

                            EditText etStore;
                            etStore = ((AlertDialog)dialog).findViewById(R.id.store);
                            String store = etStore.getText().toString();

                            ListItem li = new ListItem(ID, title, price,cb, store);

                            mListener.onDialogPositiveClick(NewItemDialogFragment.this, li, isNewCopy);
                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        if(state == DIALOG_EDIT)
                            mListener.onDialogNegativeClick(NewItemDialogFragment.this, ID);
                    }
                })
                .setView(dialogView);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
