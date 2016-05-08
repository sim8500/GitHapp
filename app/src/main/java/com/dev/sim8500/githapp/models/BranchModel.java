package com.dev.sim8500.githapp.models;

/**
 * Created by sbernad on 30.04.16.
 */
public class BranchModel {

    public class CommitModel {
        public String sha;
        public String url;
    }

    public String name;
    public CommitModel commit;
}
