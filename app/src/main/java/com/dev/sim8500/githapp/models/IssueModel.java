package com.dev.sim8500.githapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sbernad on 19.12.15.
 */
public class IssueModel implements Parcelable
{
    public String title;
    public String body;
    public int id;
    public String state;
    public UserModel user;
    public UserModel assignee;

    @SerializedName("comments")
    public int commentsCount;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(body);
        dest.writeInt(id);
        dest.writeString(state);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(assignee, flags);
        dest.writeInt(commentsCount);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<IssueModel> CREATOR = new Parcelable.Creator<IssueModel>() {

        public IssueModel createFromParcel(Parcel in) {
            return new IssueModel(in);
        }

        public IssueModel[] newArray(int size) {
            return new IssueModel[size];
        }
    };

    private IssueModel(Parcel in) {
        title = in.readString();
        body = in.readString();
        id = in.readInt();
        state = in.readString();
        user = in.readParcelable(UserModel.class.getClassLoader());
        assignee = in.readParcelable(UserModel.class.getClassLoader());
        commentsCount = in.readInt();
    }

}
