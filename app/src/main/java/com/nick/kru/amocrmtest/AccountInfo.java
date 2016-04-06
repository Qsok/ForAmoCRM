package com.nick.kru.amocrmtest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kru on 05.04.2016.
 */
public class AccountInfo implements Parcelable {
    String name;
    String id;
    String color;
    String editable;

    AccountInfo(String name, String id, String color, String editable) {
        this.name = name;
        this.id = id;
        this.color = color;
        this.editable = editable;

    }

    public int describeContents() {
        return 0;
    }

    public AccountInfo(Parcel in) {
        String[] data = new String[4];

        in.readStringArray(data);
        this.name = data[0];
        this.id = data[1];
        this.color = data[2];
        this.editable = data[3];
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.name,
                this.id,
                this.color,
                this.editable});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AccountInfo createFromParcel(Parcel in) {
            return new AccountInfo(in);
        }

        public AccountInfo[] newArray(int size) {
            return new AccountInfo[size];
        }
    };

}


