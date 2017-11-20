package jlvivero.medule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by joslu on 11/20/2017.
 */

public class SortBy extends DialogFragment {
    private int selectedItem;
    SortByListener callback;
    public interface  SortByListener {
        public void sortListener(int i);
        public void Cancel(int i);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<String> test = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort by")
                .setSingleChoiceItems(R.array.sortable, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedItem = i;
                    }
                })
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("dialog", "onClick: " + String.valueOf(selectedItem));
                        callback.sortListener(selectedItem);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("dialog", "onClick: cancel");
                        callback.Cancel(selectedItem);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            callback = (SortByListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement sortbyListener");
        }
    }
}
