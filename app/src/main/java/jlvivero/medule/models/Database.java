package jlvivero.medule.models;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

/**
 * Created by joslu on 11/9/2017.
 */

@android.arch.persistence.room.Database(entities = Medicine.class, version = 1)
public abstract class Database extends RoomDatabase{
    //MedicineDao is a classa nnotated with @dao
    private static Database INSTANCE;
    public abstract  MedicineDao medicineDao();
    public static Database getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "medule").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };
}
