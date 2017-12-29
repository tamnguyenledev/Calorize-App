package com.example.tamnguyen.calorizeapp.Diary;

import android.provider.ContactsContract;

import com.example.tamnguyen.calorizeapp.FoodList.Food;
import com.example.tamnguyen.calorizeapp.FoodList.FoodDatabase;
import com.example.tamnguyen.calorizeapp.FoodList.FoodList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hoangdung on 12/27/17.
 */

@IgnoreExtraProperties
public class Diary {
    @Exclude
    public String ID="";
    public double carbs = 0;
    public double protein = 0;
    public double fat = 0;
    public double calories = 0;
    public double neededCarbs = 0;
    public double neededProtein = 0;
    public double neededFat = 0;
    public double neededCalories = 0;
    public double height = 0;
    public double weight = 0;
    @Exclude
    DiaryFoodList breakfastList = new DiaryFoodList("Breakfast");
    @Exclude
    DiaryFoodList lunchList = new DiaryFoodList("Lunch");
    @Exclude
    DiaryFoodList dinnerList = new DiaryFoodList("Dinner");
    public Map<String, Object> toMap() {
        return new HashMap<>();
    }
    public void fromMap(Map<String,Object> map){
        carbs = Double.parseDouble(map.get("carbs").toString());
        protein = Double.parseDouble(map.get("protein").toString());
        fat = Double.parseDouble(map.get("fat").toString());
        calories = Double.parseDouble(map.get("calories").toString());
        neededCarbs = Double.parseDouble(map.get("neededCarbs").toString());
        neededProtein = Double.parseDouble(map.get("neededProtein").toString());
        neededFat = Double.parseDouble(map.get("neededFat").toString());
        neededCalories = Double.parseDouble(map.get("neededCalories").toString());
        height = map.get("carbs") == null ? 0: Double.parseDouble(map.get("carbs").toString());
        weight = map.get("carbs") == null ? 0: Double.parseDouble(map.get("carbs").toString());
    }
}
