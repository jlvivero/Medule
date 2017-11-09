package jlvivero.medule;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import jlvivero.medule.models.Database;
import jlvivero.medule.models.Medicine;

/**
 * Created by joslu on 11/9/2017.
 */
//TODO: design decision, should this even exist?
public class DatabaseInitializer {
    public static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final Database db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final Database db) {
        populateWithTestData(db);
    }

    private static Medicine addMed(final Database db, Medicine medicine) {
        db.medicineDao().insertAll(medicine);
        return medicine;
    }


    private static void populateWithTestData(Database db) {
        Medicine medicine = new Medicine();
        medicine.setMedName("Modafinil");
        medicine.setHours(8);
        medicine.setId(1);
        addMed(db, medicine);
        List<Medicine> medlist = db.medicineDao().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + medlist.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final Database mdb;

        PopulateDbAsync(Database db) {
            mdb = db;
        }

        //probably change populatewithtestdata so that you can add the values yourself
        //TODO: change populatewithtestData so that any value can be added
        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mdb);
            return null;
        }

    }
}
