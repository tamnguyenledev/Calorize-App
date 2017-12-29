package com.example.tamnguyen.calorizeapp.Diary;

import android.provider.ContactsContract;

import com.example.tamnguyen.calorizeapp.FoodList.Food;
import com.example.tamnguyen.calorizeapp.FoodList.FoodDatabase;
import com.example.tamnguyen.calorizeapp.FoodList.FoodList;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hoangdung on 12/27/17.
 */

public class Diary {
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
    DiaryFoodList breakfastList = new DiaryFoodList("Breakfast");
    DiaryFoodList lunchList = new DiaryFoodList("Lunch");
    DiaryFoodList dinnerList = new DiaryFoodList("Dinner");
    public Map<String, Object> toMap() {
        return new HashMap<>();
    }
}
