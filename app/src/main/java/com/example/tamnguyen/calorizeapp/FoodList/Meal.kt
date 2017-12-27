package com.example.tamnguyen.calorizeapp.FoodList

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.android.parcel.Parcelize
import me.xdrop.fuzzywuzzy.FuzzySearch

/**
 * Created by hoangdung on 12/17/17.
 */
public class Meal constructor(title: String,items: List<Food>) : ExpandableGroup<Food>(title, items) {
    var foodNames: ArrayList<String>
    init {
        //Initilize Array of Food Names
        foodNames = ArrayList<String>()
        for(food in items)
        {
            foodNames.add(food.foodName!!)
        }
    }

    /**
     * Utility function used for food searching
     * @param foodName: query string
     * @return Meal: a subset of original Meal based on query string
     */
    fun suggestFood(foodName: String): Meal{
        //Use fuzzy search to extract "text matching" points from Food Names
        val suggests = FuzzySearch.extractAll(foodName,foodNames)
        val foods = ArrayList<Food>()
        //For each suggestion
        for(i in suggests.indices){
            //Score Cutoff Value = 50
            if(suggests[i].score >= 50)
            {
                foods.add(items[i])
            }
        }
        return Meal(title,foods)
    }
}