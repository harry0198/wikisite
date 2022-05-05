package com.harrydrummond.projecthjd.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

    public static String getDateCreatedFormatted(Date dateCreated) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(dateCreated);
    }

    public static java.util.Date addDayToJavaUtilDate(java.util.Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK, days);
        return calendar.getTime();
    }
}