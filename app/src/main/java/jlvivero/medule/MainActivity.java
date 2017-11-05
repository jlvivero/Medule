package jlvivero.medule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
//TODO: refactor code once all the CRUD aspects of the app are finished
public class MainActivity extends AppCompatActivity  implements MedicineName.OnFormIntroducedListener, MedicineList.ModifyValueListener{
    private ArrayList<MedicineForm> list = new ArrayList<>();
    private MedicineList meds;
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

    public void passData(ArrayList<MedicineForm> data) {
        list = new ArrayList<>();
        list.addAll(data);
    }

    public void changeState(int i) {
        switch (i) {
            case 0:
                meds = MedicineList.newInstance(list);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, meds).commit();
                break;
            case 1:
                MedicineName form = new MedicineName();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, form).commit();
                break;
            default:
                break;

        }

    }

    //TODO: add the datastructure to the scheduling class
    public void sent(MedicineForm callback) {
        if(callback.hasError() == 0) {
            list.add(callback);
            changeState(0);
        }
        else{
            // just change the state without modifying the list
            changeState(0);
        }

    }

    public void edit(MedicineForm callback) {
        //TODO: implement the handling of the edit method
        Log.d("pending", "not implemented");
    }
}
