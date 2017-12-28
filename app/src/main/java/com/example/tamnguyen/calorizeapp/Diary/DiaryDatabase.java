package com.example.tamnguyen.calorizeapp.Diary;

import android.os.AsyncTask;

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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoangdung on 12/27/17.
 */

public class DiaryDatabase {
    private static final DiaryDatabase ourInstance = new DiaryDatabase();

    public static DiaryDatabase getInstance() {
        return ourInstance;
    }

    private static final String DiaryPath = "diary";
    private static final String DiaryMetaPath = "diary_meta";
    private static final String FoodSubPath = "food";
    private DatabaseReference diaryRef = FirebaseDatabase.getInstance().getReference().child(DiaryPath)
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private DatabaseReference diaryMetaRef = FirebaseDatabase.getInstance().getReference().child(DiaryMetaPath)
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    /**
     * Callback Interface for Query Completion
     */
    public interface OnCompleteListener {
        void onSuccess(String key, Diary diary);
        void onFailure(int code);
    }

    /**
     * Checking Task is used to check whether FoodDatabase has successfully load all data
     * Because DiaryDatabase depends on FoodDatabase service and uses its data
     * Therefore it is neccesary to check Loading Status of FoodDatabase for correctness
     * To avoid block UI thread, AsynTask is a great option
     */
    private static class CheckingTask extends AsyncTask<Void, Void, Void> {
        DatabaseOperation databaseOperation;
        OnCompleteListener listener;

        public CheckingTask(DatabaseOperation operation, OnCompleteListener listener) {
            databaseOperation = operation;
            this.listener = listener;
        }

        @Override
        protected synchronized Void doInBackground(Void... voids) {
            while (!FoodDatabase.Companion.getInstance().isLoadFinished()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            databaseOperation.operate(listener);
        }
    }

    private DiaryDatabase() {

    }

    /**
     * Diary Cache Implement
     */
    class Pair<L, R> {
        L first;
        R second;

        Pair() {

        }

        Pair(L l, R r) {
            first = l;
            second = r;
        }
    }

    private Calendar mCalendar = GregorianCalendar.getInstance();
    private int maxCacheSize = 20;
    private int mCurrentIndex = -1;
    private ArrayList<Pair<String, Diary>> cacheDiary = new ArrayList<>();
    private Map<String, Diary> cacheLookup = new HashMap<>();
    private boolean isFirstTimeLoading = true;

    public Calendar getCalendar() {
        return mCalendar;
    }

    public static final int NO_DATA = 0;

    @FunctionalInterface
    private interface DatabaseOperation {
        void operate(OnCompleteListener listener);
    }

    /**
     * Main API of this class
     * Support getting Previous Diary, Next Diary, Current Diary
     * Integrate CheckingTask
     *
     * @param listener
     */
    public void getPrevDiary(OnCompleteListener listener) {
        new CheckingTask(this::getPrevDiaryImp, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getNextDiary(OnCompleteListener listener) {
        new CheckingTask(this::getNextDiaryImp, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getCurrentDiary(OnCompleteListener listener) {
        new CheckingTask(this::getCurrentDiaryImp, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private synchronized void getPrevDiaryImp(final OnCompleteListener listener) {
        if (mCurrentIndex + 1 <= cacheDiary.size() - 1) {
            //If Previous diary already appears in cache
            mCalendar.add(Calendar.DATE, -1);
            ++mCurrentIndex;
            listener.onSuccess(cacheDiary.get(mCurrentIndex).first, cacheDiary.get(mCurrentIndex).second);
        } else {
            //If Previous diary not exist in cache
            if (cacheDiary.size() < maxCacheSize) {
                //If Cache still have rooms
                ++mCurrentIndex;
            } else {
                //If Cache is full
                //Remove the first Diary in cache
                cacheLookup.remove(cacheDiary.get(0).first);
                cacheDiary.remove(0);
            }
            //In both cases above, we need to get Diary from Database and put it into cache
            mCalendar.add(Calendar.DATE, -1);
            getDiary(mCalendar, new OnCompleteListener() {
                @Override
                public void onSuccess(String key, Diary diary) {
                    synchronized (DiaryDatabase.this) {
                        cacheDiary.add(new Pair<>(key, diary));
                        cacheLookup.put(key, diary);
                    }
                    listener.onSuccess(key, diary);
                }

                @Override
                public void onFailure(int code) {
                    listener.onFailure(code);
                }
            });
        }
    }

    private synchronized void getNextDiaryImp(final OnCompleteListener listener) {
        if (mCurrentIndex - 1 >= 0) {
            //If Previous diary already appears in cache
            --mCurrentIndex;
            mCalendar.add(Calendar.DATE, 1);
            listener.onSuccess(cacheDiary.get(mCurrentIndex).first, cacheDiary.get(mCurrentIndex).second);
        } else {
            //If Previous diary not exist in cache
            if (cacheDiary.size() >= maxCacheSize) {
                //If Cache is full
                //Remove the last Diary in cache
                cacheLookup.remove(cacheDiary.get(cacheDiary.size() - 1));
                cacheDiary.remove(cacheDiary.size() - 1);
            }
            //In both cases above, we need to get Diary from Database and put it into cache
            mCalendar.add(Calendar.DATE, +1);
            getDiary(mCalendar, new OnCompleteListener() {
                @Override
                public void onSuccess(String key, Diary diary) {
                    cacheDiary.add(0, new Pair<>(key, diary));
                    cacheLookup.put(key, diary);
                    listener.onSuccess(key, diary);
                }

                @Override
                public void onFailure(int code) {
                    listener.onFailure(code);
                }
            });
        }
    }

    private synchronized void getCurrentDiaryImp(final OnCompleteListener listener) {
        if (isFirstTimeLoading) {
            //If diary database hasn't been loaded before
            //Load Today Diary and put it into cache
            isFirstTimeLoading = false;
            Calendar calendar = GregorianCalendar.getInstance();
            getDiary(calendar, new OnCompleteListener() {
                @Override
                public void onSuccess(String key, Diary diary) {
                    synchronized (DiaryDatabase.this) {
                        mCurrentIndex = 0;
                        cacheDiary.add(new Pair<String, Diary>(key, diary));
                        cacheLookup.put(key, diary);
                    }
                    listener.onSuccess(key, diary);
                }

                @Override
                public void onFailure(int code) {
                    listener.onFailure(code);
                }
            });
        } else {
            listener.onSuccess(cacheDiary.get(mCurrentIndex).first, cacheDiary.get(mCurrentIndex).second);
        }

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
     *
     * @param calendar  : Specify date to get data
     * @param listener: OnCompleteListener to listen to query operate
     */
    public void getDiary(Calendar calendar, final OnCompleteListener listener) {
        //Get Diary's key
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String key = sdf.format(calendar.getTime());
        if (cacheLookup.containsKey(key)) {
            //Check if Today Diary is already in cache
            listener.onSuccess(key, cacheLookup.get(key));
        } else {
            //Load Today Diary from database
            //Because of getPrev,getNext,getCurrent operations, today diary is not put into cache for synchronization
            diaryRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Diary diary = new Diary();
                    if (dataSnapshot.exists()) {
                        diary = createDiaryFromSnapshot(dataSnapshot);
                    }
                    listener.onSuccess(dataSnapshot.getKey(), diary);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onFailure(databaseError.getCode());
                }
            });
        }

    }

    public static Diary createDiaryFromSnapshot(DataSnapshot dataSnapshot) {
        Diary diary = new Diary();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            //For each field in Diary Snapshot, determine what field is that and set corresponding value
            switch (child.getKey()) {
                case CARBS:
                    diary.carbs = Double.parseDouble(child.getValue().toString());
                    break;
                case PROTEIN:
                    diary.protein = Double.parseDouble(child.getValue().toString());
                    break;
                case FAT:
                    diary.fat = Double.parseDouble(child.getValue().toString());
                    break;
                case CALORIES:
                    diary.calories = Double.parseDouble(child.getValue().toString());
                    break;
                case FOOD_LIST: {
                    //For each food, put it into corresponding meal and save number of units of that food
                    for (DataSnapshot foodSnapshot : child.getChildren()) {
                        Map<String, Object> data = (Map<String, Object>) foodSnapshot.getValue();
                        Food food = FoodDatabase.Companion.getInstance().getFoodByID((String) data.get(FOOD_ID));
                        if (data.containsKey(Food.Companion.getBREAKFAST())) {
                            diary.breakfastList.getItems().add(food);
                            diary.breakfastVolumeList.add(Double.parseDouble(data.get(FOOD_NUM_UNIT).toString()));
                        } else if (data.containsKey(Food.Companion.getLUNCH())) {
                            diary.lunchList.getItems().add(food);
                            diary.lunchVolumeList.add(Double.parseDouble(data.get(FOOD_NUM_UNIT).toString()));
                        } else {
                            diary.dinnerList.getItems().add(food);
                            diary.dinnerVolumeList.add(Double.parseDouble(data.get(FOOD_NUM_UNIT).toString()));
                        }

                    }
                }
            }
        }
        return diary;
    }
}
