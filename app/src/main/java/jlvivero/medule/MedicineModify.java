package jlvivero.medule;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import jlvivero.medule.timers.AlarmReceiver;

/**
 * Created by joslu on 11/6/2017.
 */
public class MedicineModify extends Fragment implements View.OnClickListener{
    View view;
    private MedicineForm modify;
    private Button confirm;
    private int lastPress;
    CallbackValueListener mCallback;

    public interface CallbackValueListener {
        void replaceValues(MedicineForm values, int position);
        void deleteValue(int position);
        void nothing();
        void alarm(int pos);
    }

    public static MedicineModify newInstance(MedicineForm pack, int position) {
        MedicineModify instance = new MedicineModify();
        if(pack == null) {
            return instance;
        }
        Bundle args = new Bundle();

        String name;
        int id, hours;
        id = pack.getId();
        hours = pack.getHours();
        name = pack.getName();

        args.putInt("id", id);
        args.putString("name", name);
        args.putInt("hours", hours);
        args.putInt("pos", position);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_medicine_modify, container, false);

        //views that are used inside the fragment
        confirm = view.findViewById(R.id.confirm);
        Button takeMeds = view.findViewById(R.id.take);
        Button changeTime = view.findViewById(R.id.time);
        Button changeName = view.findViewById(R.id.rename);
        Button delete = view.findViewById(R.id.delete);
        takeMeds.setOnClickListener(this);
        changeTime.setOnClickListener(this);
        changeName.setOnClickListener(this);
        delete.setOnClickListener(this);
        confirm.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            modify = new MedicineForm();
            modify.setName(getArguments().getString("name", "generic"));
            modify.setId(getArguments().getInt("id", -1));
            modify.setHours(getArguments().getInt("hours", 8));
            modify.setError(0);
        }
        catch (RuntimeException e) {
            Log.d("fragmentError", "Bundle Error");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (CallbackValueListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement CallbackValueListener");
        }
    }

    @Override
    public void onClick(View v) {
        TextView nameLabel = view.findViewById(R.id.name_change);
        TextView hoursLabel = view.findViewById(R.id.time_change);
        EditText editHours = view.findViewById(R.id.textHours);
        EditText editName = view.findViewById(R.id.textName);
        TextView deleteLabel = view.findViewById(R.id.delete_label);
        editHours.setHint(String.valueOf(modify.getHours()));
        editName.setHint(modify.getName());

        switch (v.getId()) {
            case R.id.take: //medicine taken
                mCallback.alarm(getArguments().getInt("pos"));
                break;

            case R.id.time: //changes the hours between dosage value
                deleteLabel.setVisibility(View.GONE);
                nameLabel.setVisibility(View.GONE);
                editName.setVisibility(View.GONE);
                hoursLabel.setVisibility(View.VISIBLE);
                editHours.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);
                lastPress = R.id.time;
                break;

            case R.id.rename: //changes the naem of the medicine
                deleteLabel.setVisibility(View.GONE);
                hoursLabel.setVisibility(View.GONE);
                editHours.setVisibility(View.GONE);
                nameLabel.setVisibility(View.VISIBLE);
                editName.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);
                lastPress = R.id.rename;
                break;

            case R.id.delete: //deletes the medicine
                hoursLabel.setVisibility(View.GONE);
                editHours.setVisibility(View.GONE);
                nameLabel.setVisibility(View.GONE);
                editName.setVisibility(View.GONE);
                deleteLabel.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);
                lastPress = R.id.delete;
                break;
            case R.id.confirm: //confirm button for the previous options
                switch (lastPress) {
                    case R.id.time:
                        Log.d("callbacks", "I got this far");
                        if(editHours.getText().toString().equals("")) {
                            mCallback.replaceValues(modify, getArguments().getInt("pos"));
                        }
                        else {
                            modify.setHours(Integer.parseInt(editHours.getText().toString()));
                            mCallback.replaceValues(modify, getArguments().getInt("pos"));
                        }
                        break;
                    case R.id.rename:
                        if(editName.getText().toString().equals("")){
                            mCallback.replaceValues(modify, getArguments().getInt("pos"));
                        }
                        else {
                            modify.setName(editName.getText().toString());
                            mCallback.replaceValues(modify, getArguments().getInt("pos"));
                        }
                        break;
                    case R.id.delete:
                        mCallback.deleteValue(getArguments().getInt("pos"));
                        break;
                }
                break;
            default:
                Log.d("fragments", "I don't know what button I clicked");

        }
    }
}
