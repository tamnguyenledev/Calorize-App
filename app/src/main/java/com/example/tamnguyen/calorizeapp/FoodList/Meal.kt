package com.example.tamnguyen.calorizeapp.FoodList

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

/**
 * Created by hoangdung on 12/17/17.
 */
public class Meal constructor(title: String?, items: MutableList<Food>?) : ExpandableGroup<Food>(title, items) {
}