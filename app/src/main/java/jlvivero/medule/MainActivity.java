package jlvivero.medule;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> medicines = new ArrayList<>();
    private ArrayAdapter<String> adapter;

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
                //TODO: instead of adding a random string, make a prompt where you write the following:
                //name of the medicine
                //time between doses
                Intent intent = new Intent (MainActivity.this, Medicine.class);
                medicines.add("it worked");
                adapter.notifyDataSetChanged();
            }
        });
        //TODO: add an onclick for the listview so I can edit medicine
        //TODO: add the timer functionality and the notification functionality

    }
}
