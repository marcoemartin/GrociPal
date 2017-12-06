package com.i2ia.grocer.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
/***
 * Object class for Products
 * @author marcom
 *
 */

public class Product implements Parcelable {
	String name;
	int productnum;
	String category;
	String imageName;
	int amount;
	String units;
	String brand;
	String organic;
	
	public Product(String name, int productnum, String category, String imageName, 
					int amount, String units, String brand, String organic){
		this.name = name;
		this.productnum = productnum;
		this.category = category;
		this.imageName = imageName;
		this.amount = amount;
		this.units = units;
		this.brand = brand;
		this.organic = organic;
	}
	@Override
	public String toString(){
		return name + ", " + category;
		
	}
	
	public Product(Cursor c){
		this.setName(c.getString(0));
		this.setProductnum(Integer.parseInt(c.getString(1)));
		this.setCategory(c.getString(2));
		this.setImageName(c.getString(3));
		this.setAmount(c.getInt(4));
		this.setUnits(c.getString(5));
		this.setBrand(c.getString(6));
		this.setOrganic(c.getString(7));
	}
	
	public Product(Parcel in) {
	     name = in.readString();
	     productnum = in.readInt();
	     category = in.readString();
	     imageName = in.readString();
	     amount = in.readInt();
	     units = in.readString();
	     brand = in.readString();
	     organic = in.readString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProductnum() {
		return productnum;
	}

	public void setProductnum(int productnum) {
		this.productnum = productnum;
	}
	public String getCategory() {
		return category;
	}

	public int getAmount() {
		return amount;
	}
	
	public String getUnits() {
		return units;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public String getOrganic() {
		return organic;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setOrganic(String organic) {
		this.organic = organic;
		
	}
	
	public String getImageName() {
		return imageName;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setUnits(String unit) {
		this.units = unit;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(name);
		parcel.writeInt(productnum);
		parcel.writeString(category);
		parcel.writeString(imageName);
		parcel.writeInt(amount);
		parcel.writeString(units);
		parcel.writeString(brand);
		parcel.writeString(organic);
	}
	
	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

}
