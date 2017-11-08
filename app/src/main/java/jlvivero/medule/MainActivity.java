package jlvivero.medule;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
//TODO: create a top menu
//TODO: filter by pending
//TODO: sort by next dose
//TODO: take all button for all pending medicines
//TODO: add permanence to the medicines
public class MainActivity extends AppCompatActivity  implements MedicineName.OnFormIntroducedListener, MedicineList.ModifyValueListener, MedicineModify.CallbackValueListener{

    private int state;
    private int modifyPos;
    private ArrayList<MedicineForm> list = new ArrayList<>();
    private MedicineList meds;
    private android.support.v4.app.FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: once a databse is set pull the values before making the new instance
        //list of medicines fragment
        meds = MedicineList.newInstance(null);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, meds).commit();

        FloatingActionButton floatButton =  findViewById(R.id.floatingActionButton);
        //floating button should always add new medicine
        //TODO: design decision, should the floating button change action depending on context?
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passData(meds.getMedicines());
                changeState(1);
            }
        });
        //TODO: add the timer functionality and the notification functionality

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(Integer.parseInt(Build.VERSION.SDK) > 5 && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDownCalled");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressedCalled");
        //if state is already 0, should exit the app
        if(state != 0){
            changeState(0);
        }
        else{
            super.onBackPressed();
        }
    }


    //obtains the list of medicines from the list fragment so that new stuff can be added
    public void passData(ArrayList<MedicineForm> data) {
        list = new ArrayList<>();
        list.addAll(data);
    }

    public void changeState(int i) {
        state = i;
        switch (i) {
            case 0://show medicine list fragment
                meds = MedicineList.newInstance(list);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, meds).commit();
                break;
            case 1://show fragment to add new medicine
                MedicineName form;
                if(list == null || list.isEmpty()){
                    //list is empty so we send -1 so we can start with id 0
                    form = MedicineName.newInstance(-1);
                }
                else {// list is not empty we send the last medicine id
                    form = MedicineName.newInstance(list.get(list.size() - 1).getId());
                }
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, form).commit();
                break;
            case 2://show fragment to modify a medicine
                MedicineForm clicked; //medicine that was clicked
                clicked = list.get(modifyPos);
                MedicineModify modify = MedicineModify.newInstance(clicked, modifyPos);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, modify).commit();
            default:
                break;
        }

    }

    //callback methods from fragments

    //from medicineName fragment
    public void sent(MedicineForm callback) {
        if(callback.hasError() == 0)
            list.add(callback);
        changeState(0);
    }

    public void cancel(){
        changeState(0);
    }

    //from medicineList fragment
    public void edit(int error, int position) {
        //callback is the medicine form to edit in list at position "position"
        if(error == 0) {
            //works fine call the editing thing
            modifyPos = position;
            changeState(2);
        }
    }

    //from medicineModify fragment
    public void replaceValues(MedicineForm callback, int position) {
        if(list.get(position).getId() == callback.getId()){
            list.get(position).setHours(callback.getHours());
            list.get(position).setName(callback.getName());
            changeState(0);
        }
        else {
            //means the values do not match which means there's a problem
            Log.d("transfer-error", "the medicine does not match the original");
            //TODO: add an error logging for when state 0 is called
            changeState(0);
        }
    }

    public void deleteValue(int position) {
        //TODO: once timers are implemented call for the deletion of the id before deleting from list
        list.remove(position);
        changeState(0);
    }

    public void nothing() {
        changeState(0);
    }
}
