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

import java.util.ArrayList;
//if this doesn't work out, remove implements blablabla
public class MainActivity extends AppCompatActivity  implements MedicineName.OnFormIntroducedListener{
    private ArrayList<String> medicines = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    //TODO: change the listview to a fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = findViewById(R.id.listView);
        FloatingActionButton floatButton =  findViewById(R.id.floatingActionButton);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicines);
        list.setAdapter(adapter);

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineName form = new MedicineName();
                form.setArguments(getIntent().getExtras());
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, form).commit();
            }
        });
        //TODO: add an onclick for the listview so I can edit medicine
        //TODO: add the timer functionality and the notification functionality

    }

    public void sent(MedicineForm callback) {
        //TODO: have the listview fragment change after adding this
        if(callback.hasError() == 0) {
            Log.d("fragments", "It has no error");
            //has no error, must add the values to the lists
            medicines.add(callback.getName());
            adapter.notifyDataSetChanged();
        }
        else{
            Log.d("fragments", "it has an error");
            // just return since you cancelled
        }

    }
}
