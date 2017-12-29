package com.example.tamnguyen.calorizeapp.Diary;

import com.example.tamnguyen.calorizeapp.FoodList.Food;
import com.example.tamnguyen.calorizeapp.FoodList.FoodDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoangdung on 12/29/17.
 */

public class DiaryFood {
    Food food;
    String ID;
    double num_of_units;
    String mealType;

    Map<String,Object> toDatabase(){
        Map<String,Object> map = new HashMap<>();
        map.put(mealType,true);
        map.put("food_id",food.getFoodID());
        map.put("food_num_unit",num_of_units);
        return map;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getNum_of_units() {
        return num_of_units;
    }

    public void setNum_of_units(double num_of_units) {
        this.num_of_units = num_of_units;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
}
