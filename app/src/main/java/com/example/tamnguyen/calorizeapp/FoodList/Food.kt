package com.example.tamnguyen.calorizeapp.FoodList

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by hoangdung on 12/17/17.
 */
@SuppressLint("ParcelCreator")
@Parcelize
public class Food : Parcelable{
    public var amount: Int? = null;
    public var calorie: Float? = null
    public var carb: Float? = null
    public var dayType: String? = null
    public var protein: Float? = null
    public var fat: Float? = null
    public var unit: String? = null
    public var foodName: String? = null
    public var type: String? = null
}