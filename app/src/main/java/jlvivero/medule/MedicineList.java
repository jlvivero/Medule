package jlvivero.medule;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by joslu on 10/31/2017.
 */

//TODO: display the listview properly by only showing the name attribute in the objects
public class MedicineList extends Fragment implements ListView.OnItemClickListener{
    View view;
    private ArrayList<MedicineForm> medicines = new ArrayList<>();
    private ArrayAdapter<MedicineForm> adapter;
    ModifyValueListener mCallback;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MedicineForm item;
        item = adapter.getItem(i);
        if(item != null){
            item.setError(0);
            mCallback.edit(item);
        }
    }

    public interface ModifyValueListener {
        void edit(MedicineForm structure);
    }
    public static MedicineList newInstance(ArrayList<MedicineForm> lst) {
        MedicineList medicineList = new MedicineList();
        if(lst == null) {
            return medicineList;
        }
        Bundle args = new Bundle();
        ArrayList<String> nameX = new ArrayList<>();
        ArrayList<Integer> idY = new ArrayList<>();
        ArrayList<Integer> hoursZ = new ArrayList<>();
        for (MedicineForm item: lst) {
            nameX.add(item.getName());
            idY.add(item.getId());
            hoursZ.add(item.getHours());
        }
        args.putStringArrayList("name", nameX);
        args.putIntegerArrayList("id", idY);
        args.putIntegerArrayList("hours", hoursZ);
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
        list.setOnItemClickListener(this);
        return view;
    }

    private boolean accepted(ArrayList<String> name, ArrayList<Integer> hours, ArrayList<Integer> id) {
        return name != null && hours != null && id != null && name.size() == hours.size() && hours.size() == id.size();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            ArrayList<String> name;
            ArrayList<Integer> id;
            ArrayList<Integer> hours;
            name = getArguments().getStringArrayList("name");
            id = getArguments().getIntegerArrayList("id");
            hours = getArguments().getIntegerArrayList("hours");
            if(accepted(name, hours, id)) {
                for (int i = 0; i < name.size(); i++){
                    MedicineForm temp = new MedicineForm();
                    temp.setName(name.get(i));
                    temp.setHours(hours.get(i));
                    temp.setId(id.get(i));
                    medicines.add(temp);
                }
            }
            adapter.notifyDataSetChanged();
            Log.d("fragments", "i added it");
        }
        catch (RuntimeException e) {
            Log.d("fragmentError", "I guess the bundle doesn't exist");
        }
    }


    public ArrayList<MedicineForm> getMedicines() {
        return medicines;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (ModifyValueListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnListIntroducedListener");
        }
    }
}
