package com.i2ia.grocer.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Store implements Parcelable{
	private String name;
	private String iconName;
	
	
	public Store(String i_name, String i_iconName){
		this.setName(i_name);
		this.setIconName(i_iconName);
	}
	
	public Store(Cursor c){
		this.setName(c.getString(0));
		this.setIconName(c.getString(5));
	}
	
	public Store(Parcel in) {
	     name = in.readString();
	     iconName = in.readString();
	    }


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getIconName() {
		return iconName;
	}


	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(name);
		parcel.writeString(iconName);
	}
	
	public static final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>() {
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        public Store[] newArray(int size) {
            return new Store[size];
        }
    };
	
}



