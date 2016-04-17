package com.dev.sim8500.githapp.models;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by sbernad on 17.04.16.
 */
public class ModelDataUtils {

    public static Date getParsedDate(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date resultDate = null;
        try {
            resultDate = sdf.parse(dateString);
        }
        catch(ParseException ex) {
            Log.e("CommitModel", "Cannot parse date in given format.");
        }
        return resultDate;
    }
}
