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

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by joslu on 10/31/2017.
 */

//TODO: text looks very light, maybe have a different color for the medicine list
public class MedicineList extends Fragment implements ListView.OnItemClickListener{

    View view;
    private ArrayList<MedicineForm> medicines = new ArrayList<>();
    private ArrayAdapter<MedicineForm> adapter;
    ModifyValueListener mCallback;

    public interface ModifyValueListener {
        void edit(int error, int position);
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
        boolean[] dueW = new boolean[lst.size()];
        int i = 0;
        for (MedicineForm item: lst) {
            nameX.add(item.getName());
            idY.add(item.getId());
            hoursZ.add(item.getHours());
            dueW[i] = item.isDue();
            i++;
        }
        args.putStringArrayList("name", nameX);
        args.putIntegerArrayList("id", idY);
        args.putIntegerArrayList("hours", hoursZ);
        args.putBooleanArray("due", dueW);
        medicineList.setArguments(args);
        return medicineList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_medicine_list, container, false);

        //views that are used inside the fragment
        adapter = new ArrayAdapter<>(this.getActivity(), R.layout.text_view, medicines);
        ListView list = view.findViewById(R.id.meds);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            boolean[] due = getArguments().getBooleanArray("due");
            ArrayList<String> name;
            ArrayList<Integer> id, hours;
            name = getArguments().getStringArrayList("name");
            id = getArguments().getIntegerArrayList("id");
            hours = getArguments().getIntegerArrayList("hours");
            if(accepted(name, hours, id, due)) {
                for (int i = 0; i < name.size(); i++){
                    MedicineForm temp = new MedicineForm();
                    temp.setName(name.get(i));
                    temp.setHours(hours.get(i));
                    temp.setId(id.get(i));
                    temp.setDue(due[i]);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (ModifyValueListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement ModifyValueListener");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MedicineForm item;
        item = adapter.getItem(i);
        if(item != null){
            mCallback.edit(0,i);
        }
        else {
            mCallback.edit(1,i);
        }

    }

    private boolean accepted(ArrayList<String> name, ArrayList<Integer> hours, ArrayList<Integer> id, boolean[] due) {
        return name != null && hours != null && id != null && due != null && name.size() == hours.size() && hours.size() == id.size() && Array.getLength(due) == id.size();
    }

    public ArrayList<MedicineForm> getMedicines() {
        return medicines;
    }

}
