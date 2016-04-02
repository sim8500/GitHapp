package com.dev.sim8500.githapp.models;

import java.util.List;

/**
 * Created by sbernad on 21.03.16.
 */
public class DetailedCommitModel extends CommitModel {

    public class StatsModel {
        public int additions;
        public int deletions;
        public int total;
    }

    public StatsModel stats;
    public List<FileModel> files;

}
