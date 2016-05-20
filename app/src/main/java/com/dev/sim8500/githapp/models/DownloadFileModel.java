package com.dev.sim8500.githapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sbernad on 20.05.16.
 */
public class DownloadFileModel implements Parcelable {

    public String filename;
    public String raw_url;
    public @FileModel.FileStatus int status = FileModel.FILE_STATUS_NONE;
    public String sha;
    public String patch;

    public DownloadFileModel(String filename, String url) {
        this.filename = filename;
        this.raw_url = url;
    }

    protected DownloadFileModel(Parcel in) {
        filename = in.readString();
        //noinspection WrongConstant
        status = in.readInt();
        raw_url = in.readString();
        sha = in.readString();
        patch = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filename);
        dest.writeInt(status);
        dest.writeString(raw_url);
        dest.writeString(sha);
        dest.writeString(patch);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DownloadFileModel> CREATOR = new Creator<DownloadFileModel>() {
        @Override
        public DownloadFileModel createFromParcel(Parcel in) {
            return new DownloadFileModel(in);
        }

        @Override
        public DownloadFileModel[] newArray(int size) {
            return new DownloadFileModel[size];
        }
    };
}
