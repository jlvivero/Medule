package jlvivero.medule;

import android.util.Log;

import static java.lang.String.*;

/**
 * Created by joslu on 10/31/2017.
 */

public class MedicineForm {
    private int error;
    private String name;
    private int hours;
    private int id;
    private boolean due;
    private String dueName;
    public MedicineForm() {
        this.error = 1;
        this.name = "nothing";
        this.hours = 0;
        this.due = true;
        this.dueName = "xx";
    }

    public int hasError() {
        return this.error;
    }

    public String getName() {
        return this.name;
    }

    public int getHours() {
        return this.hours;
    }

    public void setError(int n) {
        this.error = n;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setHours(int n) {
        this.hours = n;
    }

    public void setId(int n) {this.id = n;}

    public int getId(){ return this.id;}

    public void setDue(boolean d) {
        this.due = d;
        if(this.due){
            this.dueName = "\n Take now";
        }
        else {
            this.dueName = " ";
        }
    }
    public boolean isDue() {
        if(this.due) {
            this.dueName = "\n Take now";
        }
        else {
            this.dueName = " ";
        }
        return this.due;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name);
        builder.append("\n");
        builder.append(" Time between dosages: ");
        builder.append(this.hours);
        builder.append(this.dueName);
        return builder.toString();
    }
}
