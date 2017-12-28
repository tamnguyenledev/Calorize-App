package com.example.tamnguyen.calorizeapp.Diary;

import com.example.tamnguyen.calorizeapp.FoodList.Food;
import com.example.tamnguyen.calorizeapp.FoodList.FoodDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by hoangdung on 12/27/17.
 */

public class DiaryDatabase {
    private static final DiaryDatabase ourInstance = new DiaryDatabase();

    public static DiaryDatabase getInstance() {
        return ourInstance;
    }

    private static String DiaryPath = "diary";
    private static String DiaryMetaPath = "diary_meta";
    private static String FoodSubPath = "food";
    private DatabaseReference diaryRef = FirebaseDatabase.getInstance().getReference().child(DiaryPath)
            .child(FirebaseAuth.getInstance().getUid());
    private DatabaseReference diaryMetaRef = FirebaseDatabase.getInstance().getReference().child(DiaryMetaPath)
            .child(FirebaseAuth.getInstance().getUid());
    /**
     * Callback Interface for Query Completion
     */
    public interface OnCompleteListener{
        void onSuccess(Diary diary);
        void onFailure(int code);
    }
    private DiaryDatabase() {
    }

    /**
     * Key to access Corresponding Fields of Diary on database
     */
    private final static String CARBS = "carbs";
    private final static String FAT = "fat";
    private final static String PROTEIN = "protein";
    private final static String CALORIES = "calories";
    private final static String FOOD_LIST = "food_list";
    private final static String FOOD_ID = "food_id";
    private final static String FOOD_NUM_UNIT = "food_num_unit";
    /**
     * Helper method for geting Diary Database of current user
     * @param calendar : Specify date to get data
     * @param listener: OnCompleteListener to listen to query operation
     */
    public void getDiary(Calendar calendar, final OnCompleteListener listener){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String key = sdf.format(calendar.getTime());
        diaryRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Diary diary = new Diary();
                if(dataSnapshot.exists())
               {
                   diary = createDiaryFromSnapshot(dataSnapshot);
               }
               listener.onSuccess(diary);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getCode());
            }
        });
    }
    public static Diary createDiaryFromSnapshot(DataSnapshot dataSnapshot){
        Diary diary = new Diary();
        for(DataSnapshot child : dataSnapshot.getChildren()){
            //For each field in Diary Snapshot, determine what field is that and set corresponding value
            switch (child.getKey()){
                case CARBS: diary.carbs = (double) child.getValue(); break;
                case PROTEIN: diary.protein = (double) child.getValue(); break;
                case FAT: diary.fat = (double) child.getValue(); break;
                case CALORIES: diary.calories = (double) child.getValue(); break;
                case FOOD_LIST:{
                    //For each food, put it into corresponding meal and save number of units of that food
                    for(DataSnapshot foodSnapshot : child.getChildren()){
                        Map<String,Object> data = (Map<String, Object>) foodSnapshot.getValue();
                        Food food = FoodDatabase.Companion.getInstance().getFoodByID((String) data.get(FOOD_ID));
                        if(data.containsKey(Food.Companion.getBREAKFAST()))
                        {
                            diary.breakfastList.getItems().add(food);
                            diary.breakfastVolumeList.add((Integer) data.get(FOOD_NUM_UNIT));
                        }
                        else if(data.containsKey(Food.Companion.getLUNCH())){
                            diary.lunchList.getItems().add(food);
                            diary.lunchVolumeList.add((Integer) data.get(FOOD_NUM_UNIT));
                        }
                        else{
                            diary.dinnerList.getItems().add(food);
                            diary.dinnerVolumeList.add((Integer) data.get(FOOD_NUM_UNIT));
                        }

                    }
                }
            }
        }
        return diary;
    }
}
