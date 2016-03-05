package com.dev.sim8500.githapp.models;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sbernad on 21.02.16.
 */
public class CommitModel {

    public String url;
    public String sha;
    public CommitDetailsModel commit;

    public class CommitDetailsModel {

        public String url;
        public String message;
        public UserDetailsModel author;
        public UserDetailsModel committer;

        public int comment_count;
    }

    public class UserDetailsModel {

        public String name;
        public String email;
        public String date;

        public Date getParsedDate() {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.ENGLISH);
            Date resultDate = null;
            try {
                resultDate = sdf.parse(date);
            }
            catch(ParseException ex) {
                Log.e("CommitModel", "Cannot parse date in format: yyyy-MM-ddEEEEhh:mm:ssZ");
            }
            return resultDate;
        }
    }


}
