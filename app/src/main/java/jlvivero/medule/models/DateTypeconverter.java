package jlvivero.medule.models;

import android.arch.persistence.room.TypeConverter;

import java.sql.Date;

/**
 * Created by joslu on 11/19/2017.
 */

public class DateTypeconverter {
    @TypeConverter
    public static Date toDate(Long dueDate) {
        return dueDate == null ? null : new Date(dueDate);
    }

    @TypeConverter
    public static Long toLong(Date dueDate) {
        return dueDate == null ? null : dueDate.getTime();
    }
}
