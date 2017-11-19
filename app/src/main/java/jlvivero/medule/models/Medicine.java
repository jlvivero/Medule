package jlvivero.medule.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by joslu on 11/9/2017.
 */
//TODO: design decision, make a wrapper for database managment so that you can transform from medicineform to medicine or remove medicine form
//TODO: add take_time and due_date values so that we can dinamically calculate how much time is left (take time might not be necessary)
@Entity
public class Medicine {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "medicine_name")
    private String medName;

    @ColumnInfo(name = "dosage_time")
    private int hours;

    @ColumnInfo(name = "due")
    private boolean due;

    @Ignore
    public boolean visible = true;

    @Ignore
    private String dueName = "Take now";

    public Medicine(){
        this.medName = "nothing";
        this.hours = 0;
        this.due = true;
        this.dueName = "Take now";
        this.visible = true;
    }

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

    public void setDue(boolean due) {
        this.due = due;
        if(this.due) {
            this.dueName = "\n Take now";
        }
        else {
            this.dueName = " ";
        }
    }

    public boolean getDue(){
        return this.due;
    }

    @Ignore
    public boolean isDue() {return this.getDue();}

    @Override
    public String toString() {
        if(!this.visible) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(this.medName);
        builder.append("\n");
        builder.append(" Time between dosages: ");
        builder.append(this.hours);
        builder.append(this.dueName);
        return builder.toString();
    }
}
