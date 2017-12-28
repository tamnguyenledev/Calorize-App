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
    public double carbs = 0;
    public double protein = 0;
    public double fat = 0;
    public double calories = 0;
    FoodList breakfastList = new FoodList(new ArrayList<Food>());
    List<Integer> breakfastVolumeList = new ArrayList<Integer>();
    FoodList lunchList = new FoodList(new ArrayList<Food>());
    List<Integer> lunchVolumeList = new ArrayList<Integer>();
    FoodList dinnerList = new FoodList(new ArrayList<Food>());
    List<Integer> dinnerVolumeList = new ArrayList<Integer>();

    /**
     * Utility Method from Constructing Diary Object from DataSnapshot which is retrieved from database
     * @param snapshot
     */
    public void fromSnapshot(DataSnapshot snapshot) {

    }

    public Map<String, Object> toMap() {
        return new HashMap<>();
    }
}
