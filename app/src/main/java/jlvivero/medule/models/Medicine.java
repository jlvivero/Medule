package jlvivero.medule.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import jlvivero.medule.MedicineForm;

/**
 * Created by joslu on 11/9/2017.
 */
//TODO: consider removing all the instances of MedicineForm and replacing them with medicine class
@Entity
public class Medicine {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "medicine_name")
    private String medName;

    @ColumnInfo(name = "dosage_time")
    private int hours;

    @Ignore
    MedicineForm convert;

    public String getMedName() {
        return this.medName;
    }

    public Integer getId() {
        return this.id;
    }

    public int getHours(){return this.hours;}

    public void setHours(int n){this.hours = n;}

    public void setMedName(String name) {
        this.medName = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void converting(){
        this.convert = new MedicineForm();
        this.convert.setId(this.id);
        this.convert.setHours(this.hours);
        this.convert.setName(this.medName);
    }

    public MedicineForm getForm() {
        return this.convert;
    }

    public void setForm(MedicineForm form) {
        this.convert = form;
        this.medName = this.convert.getName();
        this.id = this.convert.getId();
        this.hours = this.convert.getHours();
    }
}
