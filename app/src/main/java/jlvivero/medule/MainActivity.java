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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jlvivero.medule.models.Database;
import jlvivero.medule.models.Medicine;
import jlvivero.medule.timers.AlarmReceiver;


//TODO: design decision, maybe add an option to change the size of the font
public class MainActivity extends AppCompatActivity  implements MedicineName.OnFormIntroducedListener, MedicineList.ModifyValueListener, MedicineModify.CallbackValueListener, SortBy.SortByListener, FilterBy.FilterByListener{

    //persistance variables
    private Database db;

    //timer variables
    private PendingIntent alarmIntent;

    //information processing variables
    private int state;
    private int modifyPos;
    private ArrayList<Medicine> list = new ArrayList<>();


    private MedicineList meds;
    private android.support.v4.app.FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mytoolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);



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

    }

    @Override
    public void onResume(){
        super.onResume();
        //test code for database
        db = Database.getDatabase(this);
        List<Medicine> templist = db.medicineDao().getAll();
        list = new ArrayList<>();
        list.addAll(templist);
        meds = MedicineList.newInstance(list);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, meds).commit();
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
                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).isDue()) {
                        alarm(i);
                    }
                }
                return true;
            case R.id.sort_by:
                changeState(3);
                return true;
            case R.id.filter_pending:
                changeState(4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //obtains the list of medicines from the list fragment so that new stuff can be added
    public void passData(ArrayList<Medicine> data) {
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
                Medicine clicked; //medicine that was clicked
                clicked = list.get(modifyPos);
                MedicineModify modify = MedicineModify.newInstance(clicked, modifyPos);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, modify).commit();
                break;
            case 3://sortby fragment
                SortBy sorter = new SortBy();
                sorter.show(getSupportFragmentManager(), "sort");
                break;
            case 4:
                FilterBy filter = new FilterBy();
                filter.show(getSupportFragmentManager(), "filter");
                break;
            default:
                break;
        }

    }

    //callback methods from fragments

    //from medicineName fragment
    public void sent(Medicine callback) {
       list.add(callback);
       db.medicineDao().insertAll(callback);
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
    public void replaceValues(Medicine callback, int position) {
        if(list.get(position).getId() == callback.getId()){
            Medicine med = callback;
            db.medicineDao().updateMed(med);
            list.get(position).setHours(callback.getHours());
            list.get(position).setMedName(callback.getMedName());
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
        //have the method return and alarm Intent and just call alarm.cancel for delete and
        //alarm.set for alarm method
        int code = list.get(position).getId();
        setAlarm(code);
        Medicine med = list.get(position);
        db.medicineDao().delete(med);
        list.remove(position);
        changeState(0);
    }

    public void nothing() {
        changeState(0);
    }

    public void alarm(int pos) {

        int time = list.get(pos).getHours();
        int code = list.get(pos).getId();
        list.get(pos).setDue(false);
        list.get(pos).setDueDate(new Date(SystemClock.elapsedRealtime() + 60 * 1000 * 60 * time));
        Medicine med = list.get(pos);
        db.medicineDao().updateMed(med);

        //code for alarm timers
        AlarmManager alarm = setAlarm(code);
        //uncomment this and comment the other one for testing
        //alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60 * 1000 * time, alarmIntent);
        alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60 * 1000 * 60 * time, alarmIntent);
        changeState(0);
        //TODO: maybe add a message for changestate saying medicine taken was successful
    }

    //sets alarm and deletes any instance of a previous alarm
    //this way it can be used to delete an alarm and can be compounded with alarm.set to start a new alarm (while also deleting any other previous alarm so that they don't overlap)
    private AlarmManager setAlarm(int code) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("id", code);
        AlarmManager alrm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(this, code, intent, 0);
        //cancel any pending alarm from this medicine
        alrm.cancel(alarmIntent);
        return alrm;
    }

    public void sortListener(int i){
        switch (i)
        {
            case 0:
                Medicine.method = 0;
                break;
            case 1:
                Medicine.method = 1;
                break;
            default:
                Medicine.method = 0;
                break;
        }
        //here i actually sort the list duh
        Collections.sort(list, Collections.<Medicine>reverseOrder());
        Log.d("fragments", "SortBy: did it");
        changeState(0);
    }

    //TODO: filter a bug where medicines are still clickable even when filtered
    public void filterListener(int i){
        Log.d("Dialog", "filterListener: I am here for some reason");
        switch (i){
            case 0:
                for(int j = 0; j < list.size(); j++) {
                    list.get(j).visible = true;
                }
                changeState(0);
                break;
            case 1:
                Log.d("Dialog", "filterListener: wtf");
                for(int j = 0; j < list.size(); j++) {
                    if (!list.get(j).isDue()) {
                        list.get(j).visible = false;
                    }
                }
                changeState(0);
                break;
            default:
                break;
        }

    }
    public void Cancel(int i ){
        //I don't think anything should be done if you press cancel tbh
        changeState(0);
        Log.d("fragments", "Cancel: good");
    }



}
