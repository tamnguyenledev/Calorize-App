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

    public void fromSnapshot(DataSnapshot snapshot) {
        for(DataSnapshot child : snapshot.getChildren()){
            //For each field in Diary Snapshot, determine what field is that and set corresponding value
            switch (child.getKey()){
                case "carbs": carbs = (double) child.getValue(); break;
                case "protein": protein = (double) child.getValue(); break;
                case "fat": protein = (double) child.getValue(); break;
                case "calories": protein = (double) child.getValue(); break;
                case "food_list":{
                    //For each food, put it into corresponding meal
                    for(DataSnapshot foodSnapshot : child.getChildren()){
                        Map<String,Object> data = (Map<String, Object>) foodSnapshot.getValue();
                        Food food = FoodDatabase.Companion.getInstance().getFoodByID((String) data.get("food_id"));
                        if(data.containsKey(Food.Companion.getBREAKFAST()))
                        {
                            breakfastList.getItems().add(food);
                            breakfastVolumeList.add((Integer) data.get("food_num_unit"));
                        }
                        else if(data.containsKey(Food.Companion.getLUNCH())){
                            lunchList.getItems().add(food);
                            lunchVolumeList.add((Integer) data.get("food_num_unit"));
                        }
                        else{
                            dinnerList.getItems().add(food);
                            dinnerVolumeList.add((Integer) data.get("food_num_unit"));
                        }

                    }
                }
            }
        }
    }

    public Map<String, Object> toMap() {
        return new HashMap<>();
    }
}
