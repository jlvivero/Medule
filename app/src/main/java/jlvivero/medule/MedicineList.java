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
import java.sql.Date;
import java.util.ArrayList;

import jlvivero.medule.models.Medicine;

/**
 * Created by joslu on 10/31/2017.
 */

//TODO: text looks very light, maybe have a different color for the medicine list
public class MedicineList extends Fragment implements ListView.OnItemClickListener{

    View view;
    private ArrayList<Medicine> medicines = new ArrayList<>();
    private ArrayAdapter<Medicine> adapter;
    ModifyValueListener mCallback;

    public interface ModifyValueListener {
        void edit(int error, int position);
    }

    public static MedicineList newInstance(ArrayList<Medicine> lst) {
        MedicineList medicineList = new MedicineList();
        if(lst == null) {
            return medicineList;
        }
        Bundle args = new Bundle();
        ArrayList<String> nameX = new ArrayList<>();
        ArrayList<Integer> idY = new ArrayList<>();
        ArrayList<Integer> hoursZ = new ArrayList<>();
        boolean[] dueW = new boolean[lst.size()];
        ArrayList<Integer> visible = new ArrayList<>();
        long[] dueDate = new long[lst.size()];
        int i = 0;
        for (Medicine item: lst) {
            if(item.visible)
                visible.add(1);
            else
                visible.add(0);
            nameX.add(item.getMedName());
            idY.add(item.getId());
            hoursZ.add(item.getHours());
            dueW[i] = item.isDue();
            dueDate[i] = item.getDueDate().getTime();
            i++;
        }
        args.putStringArrayList("name", nameX);
        args.putIntegerArrayList("id", idY);
        args.putIntegerArrayList("hours", hoursZ);
        args.putBooleanArray("due", dueW);
        args.putIntegerArrayList("vis", visible);
        args.putLongArray("dates", dueDate);
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
            long[] dueDate = getArguments().getLongArray("dates");
            ArrayList<String> name;
            ArrayList<Integer> id, hours;
            ArrayList<Integer> visible;
            name = getArguments().getStringArrayList("name");
            id = getArguments().getIntegerArrayList("id");
            hours = getArguments().getIntegerArrayList("hours");
            visible = getArguments().getIntegerArrayList("vis");
            if(accepted(name, hours, id, due)) {
                for (int i = 0; i < name.size(); i++){
                    Medicine temp = new Medicine();
                    temp.setMedName(name.get(i));
                    temp.setHours(hours.get(i));
                    temp.setId(id.get(i));
                    temp.setDue(due[i]);
                    temp.setDueDate(new Date(dueDate[i]));
                    temp.visible = visible.get(i) == 1;
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
        Medicine item;
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

    public ArrayList<Medicine> getMedicines() {
        return medicines;
    }

}
