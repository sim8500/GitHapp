package com.dev.sim8500.githapp.models;

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
    }


}
