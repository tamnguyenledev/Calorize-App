package com.example.tamnguyen.calorizeapp.FoodList

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.android.parcel.Parcelize

/**
 * Created by hoangdung on 12/17/17.
 */
public class Meal constructor(title: String, items: List<Food>) : ExpandableGroup<Food>(title, items) {
}