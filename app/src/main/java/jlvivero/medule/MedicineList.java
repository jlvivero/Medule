package jlvivero.medule;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by joslu on 10/31/2017.
 */
//TODO: add initial values to add to this fragment so that I can update the list
public class MedicineList extends Fragment {
    View view;
    OnListIntroducedListener mCallback;
    private ArrayList<String> medicines = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public interface OnListIntroducedListener {
        public void update();
    }
    //TODO: accept times as part of the bundle as well, specifically for editing
    public static MedicineList newInstance(ArrayList<String> lst) {
        MedicineList medicineList = new MedicineList();
        Bundle args = new Bundle();
        args.putStringArrayList("list", lst);
        medicineList.setArguments(args);
        return medicineList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        Log.d("callback", "who goes first");
        ListView list = view.findViewById(R.id.meds);
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, medicines);
        list.setAdapter(adapter);
        //TODO: add the on long press listener or wtv
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            ArrayList<String> temp = new ArrayList<>();
            temp = getArguments().getStringArrayList("list");
            medicines.addAll(temp);
            adapter.notifyDataSetChanged();
            Log.d("fragments", "i added it");
        }
        catch (RuntimeException e) {
            Log.d("fragmentError", "I guess the bundle doesn't exist");
        }
    }

    public ArrayList<String> getMedicines() {
        return medicines;
    }
/*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnListIntroducedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnListIntroducedListener");
        }
    }
    */
}
