package com.example.tamnguyen.calorizeapp.Profile;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by HUYNHXUANKHANH on 12/25/2017.
 */

public class Profile implements Parcelable {
    private String fullName,gender,dateOfBirth,urlAvatar;
    private int iAge;
    private float iWeight,iHeight;
    private boolean bWeightType,bHeightType;
    /*
    * bWeightType ... false: kg | true: pound
    * bHeightType ... false: cm | true: foot
    * */

    public Profile(String fullName, String gender, String dateOfBirth, String urlAvatar, int iAge, float iWeight, float iHeight, boolean bWeightType, boolean bHeightType) {
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.urlAvatar = urlAvatar;
        this.iAge = iAge;
        this.iWeight = iWeight;
        this.iHeight = iHeight;
        this.bWeightType = bWeightType;
        this.bHeightType = bHeightType;
    }

    protected Profile(Parcel in) {
        fullName = in.readString();
        gender = in.readString();
        dateOfBirth = in.readString();
        urlAvatar = in.readString();
        iAge = in.readInt();
        iWeight = in.readFloat();
        iHeight = in.readFloat();
        bWeightType = in.readByte() != 0;
        bHeightType = in.readByte() != 0;
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public int generateAge(){
        if(dateOfBirth!=null &&!dateOfBirth.isEmpty()){
            String year = dateOfBirth.substring(dateOfBirth.lastIndexOf("/") + 1);
            iAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(year);
            return iAge;
        }
        return 0;
    }
    public float convertCm2Feet(float cm){
        return (float) ((float)Math.round(cm * 0.03*100.0)/100.0);
    }
    public float convertFeet2Cm(float feet){
        return (float) ((float)Math.round(feet / 0.03 *100.0)/100.0);
    }
    public float convertKilogram2Pound(float kilo){
        return (float) ((float)Math.round(kilo * 2.2 * 100.0)/100.0);
    }
    public float convertPound2Kilogram(float pound){
        return (float) ((float)Math.round(pound / 2.20 * 100.0)/100.0);
    }
    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public int getiAge() {
        return iAge;
    }

    public float getiWeight() {
        return iWeight;
    }

    public float getiHeight() {
        return iHeight;
    }

    public boolean isbWeightType() {
        return bWeightType;
    }

    public boolean isbHeightType() {
        return bHeightType;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public void setiAge(int iAge) {
        this.iAge = iAge;
    }

    public void setiWeight(float iWeight) {
        this.iWeight = iWeight;
    }

    public void setiHeight(float iHeight) {
        this.iHeight = iHeight;
    }

    public void setbWeightType(boolean bWeightType) {
        this.bWeightType = bWeightType;
    }

    public void setbHeightType(boolean bHeightType) {
        this.bHeightType = bHeightType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fullName);
        parcel.writeString(gender);
        parcel.writeString(dateOfBirth);
        parcel.writeString(urlAvatar);
        parcel.writeInt(iAge);
        parcel.writeFloat(iWeight);
        parcel.writeFloat(iHeight);
        parcel.writeByte((byte) (bWeightType ? 1 : 0));
        parcel.writeByte((byte) (bHeightType ? 1 : 0));
    }
}
