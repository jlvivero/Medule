package jlvivero.medule.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.SystemClock;

import java.sql.Date;

/**
 * Created by joslu on 11/9/2017.
 */
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

    @ColumnInfo(name = "due_date")
    private Date dueDate;

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
        this.dueDate = new Date(SystemClock.elapsedRealtime());
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
        //TODO: implement method that changes the toString display dynamically in a modular way
        if(this.dueDate.getTime() < SystemClock.elapsedRealtime()){
            //this means that I have the take now thing
            builder.append(" Time between dosages: ");
            builder.append(this.hours);
            builder.append(" hours");
        }
        else{
            builder.append(" Time before next dosage: ");
            long hours = this.dueDate.getTime() - SystemClock.elapsedRealtime();
            builder.append("\n    ");
            hours = (hours / 1000) / 60;
            //hours is so far minutes
            if(hours >= 60){
                long minutes = hours % 60;
                hours = hours / 60;
                builder.append(hours);
                builder.append(" hours");
                builder.append(" and ");
                builder.append(minutes);
                builder.append(" minutes");
            }
            else{
                builder.append(hours);
                builder.append(" minutes");
            }
        }
        builder.append(this.dueName);
        return builder.toString();
    }

    public void setDueDate(Date date) {
        this.dueDate = date;
    }

    public Date getDueDate(){
        return this.dueDate;
    }
}
