package jlvivero.medule;

import android.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
//if this doesn't work out, remove implements blablabla
public class MainActivity extends AppCompatActivity  implements MedicineName.OnFormIntroducedListener{
    //for now the state will be an int, i can make a class state to work better in the future
    //states:
    //0 = listview
    //1 = add medicine
    //2 = modify existing medicine
    private int state = 0;
    private ArrayList<String> list = new ArrayList<>();
    private MedicineList meds;
    private MedicineName form;
    private android.support.v4.app.FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        meds = MedicineList.newInstance(null);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, meds).commit();
        FloatingActionButton floatButton =  findViewById(R.id.floatingActionButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passData(meds.getMedicines());
                changeState(1);
            }
        });
        //TODO: add the timer functionality and the notification functionality

    }

    public void passData(ArrayList<String> data) {
        list = new ArrayList<>();
        list.addAll(data);
    }

    public void changeState(int i) {
        state = i;
        switch (state) {
            case 0:
                //TODO: add times and ID to the bundle
                meds = MedicineList.newInstance(list);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, meds).commit();
                break;
            case 1:
                form = new MedicineName();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, form).commit();
                break;
            default:
                break;

        }

    }

    //TODO: add an ID to the medicine to prepare for the scheduling
    //TODO: add the datastructure to the scheduling class
    public void sent(MedicineForm callback) {
        if(callback.hasError() == 0) {
            Log.d("fragments", "It has no error");
            list.add(callback.getName());
            changeState(0);
            //has no error, must add the values to the lists
            //medicines.add(callback.getName());
            //adapter.notifyDataSetChanged();
        }
        else{
            Log.d("fragments", "it has an error");
            changeState(0);
            // just return since you cancelled
        }

    }
}
