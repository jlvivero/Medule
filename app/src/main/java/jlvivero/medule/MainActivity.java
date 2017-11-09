package jlvivero.medule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jlvivero.medule.models.Database;
import jlvivero.medule.models.Medicine;
import jlvivero.medule.timers.AlarmReceiver;


//TODO: make text bigger overall
//TODO: design decision, maybe add an option to change the size of the font
//TODO: filter by pending
//TODO: sort by next dose
public class MainActivity extends AppCompatActivity  implements MedicineName.OnFormIntroducedListener, MedicineList.ModifyValueListener, MedicineModify.CallbackValueListener{

    //persistance variables
    private Database db;

    //timer variables
    private AlarmManager alarm;
    private PendingIntent alarmIntent;
    private ArrayList<PendingIntent> intentArray;

    //information processing variables
    private int state;
    private int modifyPos;
    private ArrayList<MedicineForm> list = new ArrayList<>();
    private MedicineList meds;
    private android.support.v4.app.FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mytoolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);

        //TODO: consider making a handler class for db
        //test code for database
        db = Database.getDatabase(this);
        List<Medicine> templist = db.medicineDao().getAll();
        list = new ArrayList<>();
        for (Medicine lst: templist) {
            lst.converting();
            list.add(lst.getForm());
        }

        //list of medicines fragment
        meds = MedicineList.newInstance(list);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_all:
                //TODO: implement a take all method once timers are made
                return true;
            case R.id.sort_by:
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        {
            Medicine med = new Medicine();
            med.setForm(callback);
            list.add(callback);
            db.medicineDao().insertAll(med);
        }
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
            Medicine med = new Medicine();
            med.setForm(callback);
            db.medicineDao().updateMed(med);
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
        Medicine med = new Medicine();
        med.setForm(list.get(position));
        db.medicineDao().delete(med);
        list.remove(position);
        changeState(0);
    }

    public void nothing() {
        changeState(0);
    }
    public void alarm(int pos) {
        //TODO: design decision, probably need to add intent id to database or a data structure that related med id with intent
        //TODO: check how you can delete alarms after closing the app im thinking recreating the alarmintent with the code
        int time = list.get(pos).getHours();
        int code = list.get(pos).getId();
        //dummy code for alarm timers
        Log.d("alarm", "just activated the alarm");
        Intent intent = new Intent(this, AlarmReceiver.class);
        alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(this, code, intent, 0);
        //this is set to minutes instead of hours for testing purposes
        //TODO: change time for time * 60 for it to be hours
        alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60 * 1000 * time, alarmIntent);
        intentArray.add(alarmIntent);
        changeState(0);
        //TODO: maybe add a message for changestate saying medicine taken was successful
    }
}
