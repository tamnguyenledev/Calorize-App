package com.example.tamnguyen.calorizeapp.Diary;

import com.example.tamnguyen.calorizeapp.FoodList.Food;

import java.util.ArrayList;

/**
 * Created by hoangdung on 12/29/17.
 */

public class DiaryFoodList {
    public DiaryFoodList(String title){
        this.title = title;
    }
    ArrayList<DiaryFood> items = new ArrayList<>();
    String title = "";

    public ArrayList<DiaryFood> getItems() {
        return items;
    }

    public void setItems(ArrayList<DiaryFood> items) {
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
