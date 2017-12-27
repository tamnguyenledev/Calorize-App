package com.example.tamnguyen.calorizeapp.FoodList

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by hoangdung on 12/17/17.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Food(var amount: Long,
           var calorie: Double,
           var carb: Double,
           var fat: Double,
           var protein: Double,
                var massPerUnit: Double,
                var dayType: Map<String, Boolean>,
           var unit: String,
           var foodName: String,
           var type: String,
                var photoUrl: String
            ) : Parcelable {

    constructor(): this(0,0.toDouble(),0.toDouble(),0.toDouble(),0.toDouble(),0.toDouble(),HashMap<String,Boolean>(),"","","","")
    companion object {
        val BREAKFAST = "breakfast"
        val LUNCH = "lunch"
        val DINNER = "dinner"
    }

}