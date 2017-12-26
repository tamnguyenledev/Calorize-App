package com.example.tamnguyen.calorizeapp.FoodList

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by hoangdung on 12/17/17.
 */
@SuppressLint("ParcelCreator")
@Parcelize
public class Food : Parcelable{
    public var amount: Long? = null;
    public var calorie: Double? = null
    public var carb: Double? = null
    public var fat: Double? = null
    public var protein: Double? = null
    public var dayType: Map<String,Boolean>? = null
    public var unit: String? = null
    public var foodName: String? = null
    public var type: String? = null

    companion object {
        val BREAKFAST = "breakfast"
        val LUNCH = "lunch"
        val DINNER= "dinner"
    }
}