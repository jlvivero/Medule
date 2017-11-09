package jlvivero.medule.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by joslu on 11/9/2017.
 */

@Dao
public interface MedicineDao {
    @Query("SELECT * FROM medicine")
    List<Medicine> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Medicine... medicines);

    @Update
    public void updateMed(Medicine... medicines);

    @Delete
    void delete(Medicine medicine);

}
