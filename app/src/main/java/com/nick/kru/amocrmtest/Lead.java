package com.nick.kru.amocrmtest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kru on 04.04.2016.
 */
public class Lead implements Parcelable {
    String name;
    String id;
    String createdUserId;
    double price;
    long lastModified;
    String statusId;

    Lead(String name, String id, String createdUserId, double price, long lastModified, String statusId) {
        this.name = name;
        this.id = id;
        this.createdUserId = createdUserId;
        this.price = price;
        this.lastModified = lastModified;
        this.statusId = statusId;
    }

    public int describeContents() {
        return 0;
    }

    public Lead(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);
        this.id = data[0];
        this.name = data[1];
        this.createdUserId = data[2];
        this.price = Double.valueOf(data[3]);
        this.lastModified = Long.valueOf(data[4]);
        this.statusId = data[5];
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.id,
                this.name,
                this.createdUserId,
                String.valueOf(this.price),
                String.valueOf(this.lastModified),
                this.statusId});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Lead createFromParcel(Parcel in) {
            return new Lead(in);
        }

        public Lead[] newArray(int size) {
            return new Lead[size];
        }
    };

}
