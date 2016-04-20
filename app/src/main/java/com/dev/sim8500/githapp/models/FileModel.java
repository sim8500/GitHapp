package com.dev.sim8500.githapp.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by sbernad on 01.04.16.
 */
public class FileModel {

    @IntDef({FILE_STATUS_ADDED, FILE_STATUS_MODIFIED, FILE_STATUS_REMOVED, FILE_STATUS_RENAMED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FileStatus {}

    public static final int FILE_STATUS_ADDED = 0;
    public static final int FILE_STATUS_MODIFIED = 1;
    public static final int FILE_STATUS_REMOVED = 2;
    public static final int FILE_STATUS_RENAMED = 3;

    protected static final String STRING_STATUS_ADDED = "added";
    protected static final String STRING_STATUS_MODIFIED = "modified";
    protected static final String STRING_STATUS_REMOVED = "removed";
    protected static final String STRING_STATUS_RENAMED = "renamed";

    public @FileStatus int getStatus() {
        switch(status) {
            case STRING_STATUS_MODIFIED:
                return FILE_STATUS_MODIFIED;

            case STRING_STATUS_REMOVED:
                return FILE_STATUS_REMOVED;

            case STRING_STATUS_RENAMED:
                return FILE_STATUS_RENAMED;
            case STRING_STATUS_ADDED:
            default:
                return FILE_STATUS_ADDED;
        }
    }

    public String filename;
    public int additions;
    public int deletions;
    public int changes;
    public String status;
    public String raw_url;
    public String blob_url;
    public String patch;
}