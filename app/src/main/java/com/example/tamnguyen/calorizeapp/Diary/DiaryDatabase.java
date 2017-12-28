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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
        void onSuccess( String key,Diary diary);
        void onFailure(int code);
    }
    private  DiaryDatabase() {
        synchronized (DiaryDatabase.this) {
            getDiary(mCalendar, new OnCompleteListener() {
                @Override
                public void onSuccess(String key, Diary diary) {

                    mCurrentIndex = 0;
                    cacheDiary.clear();
                    cacheDiary.add(new Pair<>(key, diary));
                }

                @Override
                public void onFailure(int code) {

                }
            });
        }
    }

    /**
     * Diary Cache Implement
     */
    class Pair<L,R>{
        L first;
        R second;
        Pair(){

        }
        Pair(L l, R r){
            first = l;
            second = r;
        }
    }
    private Calendar mCalendar = GregorianCalendar.getInstance();
    private int maxCacheSize = 20;
    private int mCurrentIndex = -1;
    private ArrayList<Pair<String,Diary>> cacheDiary = new ArrayList<>();

    public Calendar getCalendar() {
        return mCalendar;
    }
    /**
     * Main API of this class
     * Used to retrieve diary based on internal calendar
     * @param listener
     */
    public synchronized void getPrevDiary(final OnCompleteListener listener){
        if(mCurrentIndex + 1 <= cacheDiary.size() - 1){
            //If Previous diary already appears in cache
            mCalendar.add(Calendar.DATE,-1);
            ++mCurrentIndex;
            listener.onSuccess(cacheDiary.get(mCurrentIndex).first,cacheDiary.get(mCurrentIndex).second);
        }
        else
        {
            //If Previous diary not exist in cache
            if(cacheDiary.size() < maxCacheSize){
                //If Cache still have rooms
                ++mCurrentIndex;
            }
            else{
                //If Cache is full
                //Remove the first Diary in cache
                cacheDiary.remove(0);
            }
            //In both cases above, we need to get Diary from Database and put it into cache
            mCalendar.add(Calendar.DATE,-1);
            getDiary(mCalendar, new OnCompleteListener() {
                @Override
                public void onSuccess(String key, Diary diary) {
                    cacheDiary.add(new Pair<>(key,diary));
                    listener.onSuccess(key,diary);
                }

                @Override
                public void onFailure(int code) {
                    listener.onFailure(code);
                }
            });
        }
    }
    public synchronized void getNextDiary(final OnCompleteListener listener){
        if(mCurrentIndex - 1 >= 0){
            //If Previous diary already appears in cache
            --mCurrentIndex;
            mCalendar.add(Calendar.DATE,1);
            listener.onSuccess(cacheDiary.get(mCurrentIndex).first,cacheDiary.get(mCurrentIndex).second);
        }
        else
        {
            //If Previous diary not exist in cache
            if(cacheDiary.size() >= maxCacheSize){
                //If Cache is full
                //Remove the last Diary in cache
                cacheDiary.remove(cacheDiary.size()-1);
            }
            //In both cases above, we need to get Diary from Database and put it into cache
            mCalendar.add(Calendar.DATE,+1);
            getDiary(mCalendar, new OnCompleteListener() {
                @Override
                public void onSuccess(String key, Diary diary) {
                    cacheDiary.add(0,new Pair<>(key,diary));
                    listener.onSuccess(key,diary);
                }

                @Override
                public void onFailure(int code) {
                    listener.onFailure(code);
                }
            });
        }
    }
    public synchronized void getTodayDiary(final OnCompleteListener listener){
        listener.onSuccess(cacheDiary.get(mCurrentIndex).first,cacheDiary.get(mCurrentIndex).second);
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
               listener.onSuccess(dataSnapshot.getKey(),diary);
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
