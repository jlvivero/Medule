package jlvivero.medule.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import java.sql.Date;

/**
 * Created by joslu on 11/9/2017.
 */
@Entity
public class Medicine implements Comparable<Medicine>{
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
    public static int method = 0;

    public Medicine(){
        this.medName = "nothing";
        this.hours = 0;
        this.due = true;
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
    }

    public boolean getDue(){
        return this.due;
    }

    @Ignore
    public boolean isDue() {return this.getDue();}

    @Ignore
    private long get_date(){
        return this.dueDate.getTime();
    }

    @Ignore
    private String dueOrNot() {
        StringBuilder builder = new StringBuilder();
        if(this.dueDate.getTime() < SystemClock.elapsedRealtime() || this.isDue()){
            builder.append(" Time between dosages: ");
            builder.append(this.hours);
            if(this.hours == 1) {
                builder.append(" hour");
            }
            else{
                builder.append(" hours");
            }
            builder.append("\n     TAKE NOW");
        }
        else{
            builder.append(" Time before next dose: ");
            builder.append(get_time_string());
        }
        return builder.toString();
    }

    @Ignore
    private String get_time_string(){
        StringBuilder builder = new StringBuilder();
        builder.append("\n   ");
        long hours = this.dueDate.getTime() - SystemClock.elapsedRealtime();
        hours = (hours / 1000) / 60;
        //hours is in minutes right now
        if(hours >= 60) {
            //means that there's at least one hour
            long minutes = hours % 60;
            hours = hours / 60;
            builder.append(hours);
            builder.append(" hours and ");
            builder.append(minutes);
            builder.append(" minutes");
        }
        else {
            builder.append(hours);
            builder.append(" minutes");
        }
        return builder.toString();
    }
    @Override
    public String toString() {
        if(!this.visible) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(this.medName);
        builder.append("\n");
        builder.append(dueOrNot());
        return builder.toString();
    }

    public void setDueDate(Date date) {
        this.dueDate = date;
    }

    public Date getDueDate(){
        return this.dueDate;
    }

    @Ignore
    private int sortById(Medicine medicine){
        if(this.id < medicine.getId()){
            return 1;
        }
        else if(this.id > medicine.getId()){
            return -1;
        }
        else{
            return 0;
        }
    }

    @Ignore
    private int sortbyDue(Medicine medicine) {
        if(this.isDue() && !medicine.isDue()){
            Log.d("sorting", "medicine1:" + this.medName + "medicine2: " + medicine.medName);
            Log.d("sorting", "sortbyDue: i got this one time at least");
            return 1;
        }
        if(!this.isDue() && !medicine.isDue()){
            //return sortbytime
            return sortByTime(medicine);
        }
        if(!this.isDue() && medicine.isDue()){
            return  -1;
        }
        if(this.isDue() && medicine.isDue()){
            //TODO: add a method to sort by the hours value since both are due already (maybe)
            //or jsut sort by id afterwards
            return 0;
        }
        return 0;
    }

    @Ignore
    private int sortByTime(Medicine medicine) {
        long time1 = this.get_date();
        long time2 = medicine.get_date();
        if(time1 > time2){
            return 1;
        }
        else if(time1 < time2){
            return -1;
        }
        else{
            return 0;
        }
    }

    @Override
    public int compareTo(@NonNull Medicine medicine) {
        switch (Medicine.method){
            case 0:
                //sort by id
                return sortById(medicine);
            case 1:
                Log.d("sorting", "I'm sorting this way ");
                return sortbyDue(medicine);
            default:
                return 0;
        }
    }
}
