package jlvivero.medule;

/**
 * Created by joslu on 10/31/2017.
 */

public class MedicineForm {
    private int error;
    private String name;
    private int hours;
    public MedicineForm() {
        this.error = 1;
        this.name = "nothing";
        this.hours = 0;
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
}
