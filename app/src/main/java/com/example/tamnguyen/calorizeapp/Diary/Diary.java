package com.example.tamnguyen.calorizeapp.Diary;

import com.example.tamnguyen.calorizeapp.FoodList.Food;
import com.example.tamnguyen.calorizeapp.FoodList.FoodList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoangdung on 12/27/17.
 */

public class Diary {
    public double carbs = 0;
    public double protein = 0;
    public double fat = 0;
    public double calories = 0;
    FoodList breakfastList = new FoodList(new ArrayList<Food>());
    FoodList lunchList = new FoodList(new ArrayList<Food>());
    FoodList dinnerList = new FoodList(new ArrayList<Food>());

    public void fromMap(Map<String, Object> map) {

    }

    public Map<String, Object> toMap() {
        return new HashMap<>();
    }
}
