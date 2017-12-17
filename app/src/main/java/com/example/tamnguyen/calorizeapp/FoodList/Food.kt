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
    public var carb: Float? = null
    public var fiber: Float? = null
    public var protein: Float? = null
    public var fat: Float? = null
    public var calo: Float? = null
    public var unit: String? = null
    public var photoUrl: String? = null
    public var foodName: String? = null

}