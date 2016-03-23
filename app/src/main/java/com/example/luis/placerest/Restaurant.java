package com.example.luis.placerest;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable{
    private String address;
    private String[][] category_labels;
    private String country;
    private String name;
    private String region;
    private String locality;
    private String tel;
    private String website;
    private int latitude;
    private int longitude;
    private int postCode;

    public Restaurant(){

    }

    public Restaurant(String address, String[][] category_labels, String country, int latitude, int longitude, String name, int postCode, String region,String locality) {
        this.address = address;
        this.category_labels = category_labels;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.locality=locality;
        this.postCode = postCode;
        this.region = region;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCategory_labels(String[][] category_labels) {
        this.category_labels = category_labels;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public int getPostCode() {
        return postCode;
    }

    public String getName() {
        return name;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public String getCountry() {
        return country;
    }

    public String[][] getCategory_labels() {
        return category_labels;
    }

    public String getAddress() {
        return address;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country);
        dest.writeString(name);
        dest.writeString(region);
        dest.writeString(locality);
        dest.writeString(tel);
        dest.writeString( website);
        dest.writeString(address);
        dest.writeInt(latitude);
        dest.writeInt(longitude);
        dest.writeInt(postCode);
    }
}
