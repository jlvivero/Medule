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


public class MedicineName extends Fragment implements View.OnClickListener{
    View view;
    OnFormIntroducedListener mCallback;
    public interface OnFormIntroducedListener {
        public void sent(MedicineForm form);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_medicine_name, container, false);
        Button buttonAccept = view.findViewById(R.id.add);
        buttonAccept.setOnClickListener(this);
        Button buttonCancel = view.findViewById(R.id.delete);
        buttonCancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //do nothing cause nothing needs to be done
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
                //TODO: add real values to the form
                form = new MedicineForm();
                form.setHours(8);
                form.setName("modafinil");
                form.setError(0);
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
