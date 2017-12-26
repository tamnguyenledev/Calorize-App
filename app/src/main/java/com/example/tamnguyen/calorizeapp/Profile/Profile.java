package com.example.tamnguyen.calorizeapp.Profile;

/**
 * Created by HUYNHXUANKHANH on 12/25/2017.
 */

public class Profile {
    private String fullName,gender,dateOfBirth;
    private int iAge,iWeight,iHeight;
    private boolean bWeightType,bHeightType;

    public Profile(String fullName, String gender, String dateOfBirth, int iAge, int iWeight, int iHeight, boolean bWeightType, boolean bHeightType) {

        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.iAge = iAge;
        this.iWeight = iWeight;
        this.iHeight = iHeight;
        this.bWeightType = bWeightType;
        this.bHeightType = bHeightType;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getiAge() {
        return iAge;
    }

    public void setiAge(int iAge) {
        this.iAge = iAge;
    }

    public int getiWeight() {
        return iWeight;
    }

    public void setiWeight(int iWeight) {
        this.iWeight = iWeight;
    }

    public int getiHeight() {
        return iHeight;
    }

    public void setiHeight(int iHeight) {
        this.iHeight = iHeight;
    }

    public boolean isbWeightType() {
        return bWeightType;
    }

    public void setbWeightType(boolean bWeightType) {
        this.bWeightType = bWeightType;
    }

    public boolean isbHeightType() {
        return bHeightType;
    }

    public void setbHeightType(boolean bHeightType) {
        this.bHeightType = bHeightType;
    }

}
