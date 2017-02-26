package com.dev.sim8500.githapp.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sbernad on 19.02.2017.
 */

@Table(name = "RepoVisits")
public class RepoVisits extends Model {

    @Column(name = "Name")
    public String name;

    @Column(name = "RepoUrl")
    public String repoUrl;

    @Column(name = "VisitedAt")
    public long visitedAt;

    @Column(name = "Owner")
    public String owner;

    public RepoVisits() {
        super();
    }
}
