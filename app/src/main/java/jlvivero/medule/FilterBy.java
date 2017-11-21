package jlvivero.medule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by joslu on 11/21/2017.
 */

public class FilterBy extends DialogFragment{
    private int selectedItems;
    FilterByListener callback;

    public interface FilterByListener{
        public void filterListener(int i);
        public void Cancel(int i);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Filter by")
                .setSingleChoiceItems(R.array.filters, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedItems = i;
                    }
                })
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.filterListener(selectedItems);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.Cancel(i);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (FilterByListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FilterByListener");
        }
    }
}
