package com.dev.sim8500.githapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.inject.Named;

/**
 * Created by sbernad on 19.12.15.
 */
public class UserModel implements Parcelable
{
    public String login;
    public String url;
    public String name;
    @SerializedName("avatar_url") public String avatarUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(login);
        dest.writeString(url);
        dest.writeString(name);
        dest.writeString(avatarUrl);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>()
    {

        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private UserModel(Parcel in)
    {
        login = in.readString();
        url = in.readString();
        name = in.readString();
        avatarUrl = in.readString();
    }

}
