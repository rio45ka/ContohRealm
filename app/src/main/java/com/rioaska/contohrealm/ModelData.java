package com.rioaska.contohrealm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rio on 12/02/16.
 */
public class ModelData implements Parcelable {

    public String id;
    public String nama;
    public int no_hp;

    public ModelData() {}

    public ModelData(Parcel in) {
        id = in.readString();
        nama = in.readString();
        no_hp = in.readInt();
    }

    public static final Creator<ModelData> CREATOR = new Creator<ModelData>() {
        @Override
        public ModelData createFromParcel(Parcel in) {
            return new ModelData(in);
        }

        @Override
        public ModelData[] newArray(int size) {
            return new ModelData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama);
        dest.writeInt(no_hp);
    }
}
