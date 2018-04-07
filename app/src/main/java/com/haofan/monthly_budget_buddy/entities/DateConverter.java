package com.haofan.monthly_budget_buddy.entities;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * https://developer.android.com/training/data-storage/room/referencing-data.html
 */
public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp != null ? new Date(timestamp):null;
    }
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date != null ? date.getTime():null;
    }

}
