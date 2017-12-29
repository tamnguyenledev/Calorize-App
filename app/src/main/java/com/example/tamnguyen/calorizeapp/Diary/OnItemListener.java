package com.example.tamnguyen.calorizeapp.Diary;

/**
 * Created by hoangdung on 12/29/17.
 */

public interface OnItemListener {
    void onClick(DiaryFoodList diaryFoodList,int position);
    void onLongClick(DiaryFoodList diaryFoodList,int position);
    void onAddClick(DiaryFoodList diaryFoodList);
}
