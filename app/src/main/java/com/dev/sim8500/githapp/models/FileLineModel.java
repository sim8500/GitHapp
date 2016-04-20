package com.dev.sim8500.githapp.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by sbernad on 09.04.16.
 */
public class FileLineModel {

    @IntDef({PATCH_STATUS_NONE, PATCH_STATUS_DELETED, PATCH_STATUS_ADDED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PatchStatus {}

    public static final int PATCH_STATUS_NONE = 0;
    public static final int PATCH_STATUS_DELETED = 1;
    public static final int PATCH_STATUS_ADDED = 2;

    public int lineNumber;
    public String lineContent;
    public @FileLineModel.PatchStatus int lineStatus = PATCH_STATUS_NONE;
}
