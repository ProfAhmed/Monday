package com.aosama.it.models.responses.boards;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aosama.it.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.linkedin.android.spyglass.mentions.Mentionable;
import com.tylersuehr.chips.Chip;

public class UserBoard extends Chip implements Mentionable {


    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("id")
    @Expose
    private String id2;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("shortName")
    @Expose
    private String shortName;
    @SerializedName("userImage")
    @Expose
    private String userImage;
    @SerializedName("fullName")
    @Expose
    private String fullName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public String getTitle() {
        return fullName;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        return shortName;
    }

    @Nullable
    @Override
    public Uri getAvatarUri() {
        return Uri.parse(userImage);
    }

    @Nullable
    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // --------------------------------------------------
    // Mentionable Implementation
    // --------------------------------------------------

    @NonNull
    @Override
    public String getTextForDisplayMode(MentionDisplayMode mode) {
        switch (mode) {
            case FULL:
                return userName;
            case PARTIAL:
            case NONE:
            default:
                return "";
        }
    }

    @NonNull
    @Override
    public MentionDeleteStyle getDeleteStyle() {
        // Note: Cities do not support partial deletion
        // i.e. "San Francisco" -> DEL -> ""
        return MentionDeleteStyle.PARTIAL_NAME_DELETE;
    }

    @Override
    public int getSuggestibleId() {
        return userName.hashCode();
    }

    @Override
    public String getSuggestiblePrimaryText() {
        return userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
    }

    public UserBoard(Parcel in) {
        userName = in.readString();
    }

    public static final Parcelable.Creator<Assignee> CREATOR
            = new Parcelable.Creator<Assignee>() {
        public Assignee createFromParcel(Parcel in) {
            return new Assignee(in);
        }

        public Assignee[] newArray(int size) {
            return new Assignee[size];
        }
    };
}
