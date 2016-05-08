package com.dev.sim8500.githapp.models;

import android.graphics.AvoidXfermode;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by sbernad on 18.12.15.
 */
public class RepoModel implements Parcelable {

    public String name;
    public String description;
    public String url;
    public UserModel owner;
    public String created_at;
    public String updated_at;
    @SerializedName("default_branch")
    public String defaultBranch;

    public Date getCreatedAtDate() {
        return ModelDataUtils.getParsedDate(created_at);
    }

    public Date getUpdatedAtDate() {
        return ModelDataUtils.getParsedDate(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.url);
        dest.writeParcelable(this.owner, 0);
    }

    protected RepoModel(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.owner = in.readParcelable(UserModel.class.getClassLoader());
    }

    public static final Creator<RepoModel> CREATOR = new Creator<RepoModel>() {
        public RepoModel createFromParcel(Parcel source) {
            return new RepoModel(source);
        }

        public RepoModel[] newArray(int size) {
            return new RepoModel[size];
        }
    };
}
