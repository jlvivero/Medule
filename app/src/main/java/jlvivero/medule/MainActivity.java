package jlvivero.medule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
//TODO: refactor code once all the CRUD aspects of the app are finished
//TODO: filter by pending
//TODO: sort by next dose
//TODO: take all button for all pending medicines
//TODO: change the star button for a + button and make sure the size is correct
public class MainActivity extends AppCompatActivity  implements MedicineName.OnFormIntroducedListener, MedicineList.ModifyValueListener, MedicineModify.CallbackValueListener{

    private int modifyPos;
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
                MedicineName form;
                if(list == null || list.isEmpty()){
                    //list is empty
                    form = MedicineName.newInstance(-1);
                }
                else {
                    form = MedicineName.newInstance(list.get(list.size() - 1).getId());
                }
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, form).commit();
                break;
            case 2:
                MedicineForm clicked;
                clicked = list.get(modifyPos);
                MedicineModify modify = MedicineModify.newInstance(clicked, modifyPos);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, modify).commit();
            default:
                break;

        }

    }

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

    public void edit(int error, int position) {
        //callback is the medicine form to edit in list at position "position"
        if(error == 0) {
            //works fine call the editing thing
            modifyPos = position;
            changeState(2);
        }
    }

    public void replaceValues(MedicineForm callback, int position, boolean delete) {
        Log.d("callbacks", "did i even got this far");
        if(delete) {
            //TODO: once timers are implemented call for the deletion of the id before deleting from list
            list.remove(position);
            changeState(0);
            return;
        }
        if(list.get(position).getId() == callback.getId()){
            list.get(position).setHours(callback.getHours());
            list.get(position).setName(callback.getName());
            changeState(0);
        }
        else {
            //means the values do not match which means there's a problem
            Log.d("transfer-error", "the medicine does not match the original");
        }
    }
}
