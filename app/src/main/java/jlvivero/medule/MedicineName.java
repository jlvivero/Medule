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

    View view;
    private EditText name;
    private EditText hours;
    OnFormIntroducedListener mCallback;

    public interface OnFormIntroducedListener {
        void sent(MedicineForm form);
        void cancel();
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

        //views that are used inside the fragment
        Button buttonAccept = view.findViewById(R.id.add);
        Button buttonCancel = view.findViewById(R.id.delete);
        name = view.findViewById(R.id.medName);
        hours = view.findViewById(R.id.Time);
        buttonAccept.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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
        MedicineForm form;
        switch (v.getId()) {
            //adds medicine
            case R.id.add:
                form = new MedicineForm();
                form.setName(name.getText().toString());
                form.setHours(Integer.parseInt(hours.getText().toString()));
                //TODO: remove setError value from MedicineForm and anything related
                form.setError(0);
                form.setId(getArguments().getInt("id") + 1);
                mCallback.sent(form);
                break;
            //cancels and returns to normal
            case R.id.delete:
                mCallback.cancel();
                break;
            default:
                Log.d("fragments", "I don't know what button I clicked");
        }
    }

}
