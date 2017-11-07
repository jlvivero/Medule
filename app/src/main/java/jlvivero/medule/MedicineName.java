package jlvivero.medule;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MedicineName extends Fragment implements View.OnClickListener{

    private EditText name;
    private EditText hours;
    View view;
    OnFormIntroducedListener mCallback;

    public interface OnFormIntroducedListener {
        void sent(MedicineForm form);
    }

    public static MedicineName newInstance(int id) {
        MedicineName form = new MedicineName();
        Bundle args = new Bundle();
        args.putInt("id", id);
        form.setArguments(args);
        return form;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_medicine_name, container, false);
        Button buttonAccept = view.findViewById(R.id.add);
        buttonAccept.setOnClickListener(this);
        Button buttonCancel = view.findViewById(R.id.delete);
        buttonCancel.setOnClickListener(this);
        name = view.findViewById(R.id.medName);
        hours = view.findViewById(R.id.Time);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //bundle should have the last id
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnFormIntroducedListener) activity;
        } catch (ClassCastException e ) {
            throw new ClassCastException(activity.toString() + "must implement OnFormIntroducedListneer");
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("fragments", "Did I even get here");
        MedicineForm form;
        switch (v.getId()) {
            case R.id.add:
                Log.d("fragments", "I did select the right button");
                form = new MedicineForm();
                form.setName(name.getText().toString());
                form.setHours(Integer.parseInt(hours.getText().toString()));
                form.setError(0);
                form.setId(getArguments().getInt("id") + 1);
                mCallback.sent(form);
                break;
            case R.id.delete:
                Log.d("fragments", "I selected the wrong button");
                form = new MedicineForm();
                mCallback.sent(form);
                break;
            default:
                Log.d("fragments", "I don't know what button I clicked");
        }
    }

}
