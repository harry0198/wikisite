package com.harrydrummond.wikisite.util;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateUtil {

    public static String getDateCreatedFormatted(Date dateCreated) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(dateCreated);
    }
}