package com.example.tamnguyen.calorizeapp.Diary;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.util.Log;

import com.example.tamnguyen.calorizeapp.FoodList.Food;
import com.example.tamnguyen.calorizeapp.FoodList.FoodDatabase;
import com.example.tamnguyen.calorizeapp.FoodList.FoodList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.net.SocketImpl;
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
    public interface OnBatchCompleteListener{
        void onSuccess(ArrayList<Diary> result);
        void onFailure(int code);
    }

    public interface OnCompleteListener {
        void onSuccess(Diary diary);
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
        protected Void doInBackground(Void... voids) {
            try {
                FoodDatabase.Companion.getInstance().getFinishTracker().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    private Calendar mCalendar = GregorianCalendar.getInstance();
    private int maxCacheSize = 20;
    private int mCurrentIndex = -1;
    private ArrayList<Diary> cacheDiary = new ArrayList<>();
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


    public void getDiaryByInterval(Calendar calendar1, Calendar calendar2, OnBatchCompleteListener listener){
        String date1 = formatDate(calendar1);
        String date2 = formatDate(calendar2);
        diaryRef.orderByKey().startAt(date1).endAt(date2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Diary> result = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Diary diary = createDiaryFromSnapshot(child);
                    result.add(diary);
                }
                listener.onSuccess(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(0);
            }
        });
    }

    private synchronized void getNextDiaryImp(final OnCompleteListener listener) {
        if (mCurrentIndex  + 1 <= cacheDiary.size() - 1) {
            //If Previous diary already appears in cache
            mCalendar.add(Calendar.DATE, +1);
            ++mCurrentIndex;
            listener.onSuccess(cacheDiary.get(mCurrentIndex));
        } else {
            boolean cacheHaveRooms = false;
            //If Previous diary not exist in cache
            if (cacheDiary.size() < maxCacheSize) {
                //If Cache still have rooms
                cacheHaveRooms = true;
            } else {
                //If Cache is full
                //Remove the first Diary in  cache
                cacheHaveRooms = false;
            }
            //In both cases above, we need to get Diary from Database and put it into cache
            mCalendar.add(Calendar.DATE, +1);
            boolean finalCacheHaveRooms = cacheHaveRooms;
            getDiaryFromDatabase(mCalendar, new OnCompleteListener() {
                @Override
                public void onSuccess(Diary diary) {
                    synchronized (DiaryDatabase.this) {
                        if(finalCacheHaveRooms)
                            ++mCurrentIndex;
                        else
                            cacheDiary.remove(0);
                        cacheDiary.add(diary);
                    }
                    listener.onSuccess(diary);
                }

                @Override
                public void onFailure(int code) {
                    listener.onFailure(code);
                }
            });
        }
    }

    private synchronized void getPrevDiaryImp(final OnCompleteListener listener) {
        if (mCurrentIndex - 1 >= 0) {
            //If Previous diary already appears in cache
            --mCurrentIndex;
            mCalendar.add(Calendar.DATE, -1);
            listener.onSuccess(cacheDiary.get(mCurrentIndex));
        } else {
            boolean cacheHaveRooms = false;
            //If Previous diary not exist in cache
            if (cacheDiary.size() >= maxCacheSize) {
                //If Cache is full
                //Remove the last Diary in cache
                cacheHaveRooms = false;
            }
            else
                cacheHaveRooms = true;
            //In both cases above, we need to get Diary from Database and put it into cache
            mCalendar.add(Calendar.DATE, -1);
            boolean finalCacheHaveRooms = cacheHaveRooms;
            getDiaryFromDatabase(mCalendar, new OnCompleteListener() {
                @Override
                public void onSuccess(Diary diary) {
                    if (!finalCacheHaveRooms)
                        cacheDiary.remove(cacheDiary.size() - 1);
                    mCurrentIndex = 0;
                    cacheDiary.add(0,diary);
                    listener.onSuccess(diary);
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
            getDiaryFromDatabase(calendar, new OnCompleteListener() {
                @Override
                public void onSuccess(Diary diary) {
                    synchronized (DiaryDatabase.this) {
                        mCurrentIndex = 0;
                        cacheDiary.add(diary);
                    }
                    listener.onSuccess(diary);
                }

                @Override
                public void onFailure(int code) {
                    listener.onFailure(code);
                }
            });
        } else {
            listener.onSuccess(cacheDiary.get(mCurrentIndex));
        }

    }

    public void addFoodIntoDiary(String date, DiaryFood diaryFood, OnCompleteListener listener){
        this.diaryRef.child(date).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                //Update calories,carbs,... of Diary
                Diary diary = mutableData.getValue(Diary.class);
                diary.carbs += diaryFood.getFood().getCarb()*diaryFood.num_of_units;
                diary.protein+= diaryFood.getFood().getProtein()*diaryFood.num_of_units;
                diary.fat += diaryFood.getFood().getFat()*diaryFood.num_of_units;
                diary.calories+=diaryFood.getFood().getCalorie()*diaryFood.num_of_units;
                //Add Food to Database
                Map<String,Object> data = diaryFood.toDatabase();
                String key = DiaryDatabase.this.diaryRef.child(date).child(FOOD_LIST).push().getKey();
                DiaryDatabase.this.diaryRef.child(date).child(FOOD_LIST).child(key).setValue(data);
                mutableData.child("carbs").setValue(diary.carbs);
                mutableData.child("fat").setValue(diary.fat);
                mutableData.child("protein").setValue(diary.protein);
                mutableData.child("calories").setValue(diary.calories);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                listener.onSuccess(createDiaryFromSnapshot(dataSnapshot));
            }
        });
    }
    private ValueEventListener valueEventListener;
    public void listenToChangesOfCurrentDiary(ValueEventListener childEventListener){
        //Save new childEventListener
        this.valueEventListener = childEventListener;
        //Remove old childEventListener
        diaryRef.child(formatDate(mCalendar))
                .removeEventListener(this.valueEventListener);
        //Add new listener to dbRef
        diaryRef.child(formatDate(mCalendar))
                .addValueEventListener(this.valueEventListener);
    }
    /**
     * Key to access Corresponding Fields of Diary on database
     */
    final static String CARBS = "carbs";
    final static String FAT = "fat";
    final static String PROTEIN = "protein";
    final static String CALORIES = "calories";
    final static String NEEDED_CARBS = "neededCarbs";
    final static String NEEDED_FAT = "neededFat";
    final static String NEEDED_PROTEIN = "neededProtein";
    final static String NEEDED_CALORIES = "neededCalories";
    final static String FOOD_LIST = "food_list";
    final static String FOOD_ID = "food_id";
    final static String FOOD_NUM_UNIT = "food_num_unit";
    final static String HEIGHT = "height";
    final static String WEIGHT = "weight";

    /**
     * Helper method for geting Diary Database of current user
     *
     * @param calendar  : Specify date to get data
     * @param listener: OnCompleteListener to listen to query operate
     */
    public synchronized void getDiary(Calendar calendar,final  OnCompleteListener listener){
        //Get Diary's key
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String key = sdf.format(calendar.getTime());
        Log.d("Dung",key);
        //Loop through cache to see whether that diary already exists in cache
        for (int i = 0; i < cacheDiary.size(); ++i) {
            if (cacheDiary.get(i).ID.equals(key)) {
                //If that diary exists,return it through callback
                mCurrentIndex = i;
                listener.onSuccess(cacheDiary.get(i));
                return;
            }
        }
        //If Diary not exists in cache
        //Query Diary from Database
        getDiary(calendar, new OnCompleteListener() {
            @Override
            public void onSuccess(Diary diary) {
                synchronized (DiaryDatabase.this){
                    cacheDiary.clear();
                    mCurrentIndex = 0;
                    cacheDiary.add(diary);
                }
                listener.onSuccess(diary);
            }

            @Override
            public void onFailure(int code) {
                listener.onFailure(code);
            }
        });

    }
    public void getDiaryFromDatabase(Calendar calendar, final OnCompleteListener listener) {
        //Get Diary's key
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String key = sdf.format(calendar.getTime());
        Log.d("Dung",key);
        //Query Diary from Database
        diaryRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Diary diary = new Diary();
                diary.ID = dataSnapshot.getKey();
                if (dataSnapshot.exists()) {
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

    public static Diary createDiaryFromSnapshot(DataSnapshot dataSnapshot) {
        Diary diary = new Diary();
        diary.ID = dataSnapshot.getKey();
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
                case NEEDED_CALORIES:
                    diary.neededCalories = Double.parseDouble(child.getValue().toString());
                    break;
                case NEEDED_FAT:
                    diary.neededFat = Double.parseDouble(child.getValue().toString());
                    break;
                case NEEDED_CARBS:
                    diary.neededCarbs = Double.parseDouble(child.getValue().toString());
                    break;
                case NEEDED_PROTEIN:
                    diary.neededProtein = Double.parseDouble(child.getValue().toString());
                    break;
                case HEIGHT:
                    diary.height = Double.parseDouble(child.getValue().toString());
                    break;
                case WEIGHT:
                    diary.weight = Double.parseDouble(child.getValue().toString());
                    break;
                case FOOD_LIST: {
                    //For each food, put it into corresponding meal and save number of units of that food
                    for (DataSnapshot foodSnapshot : child.getChildren()) {
                        DiaryFood diaryFood = createDiaryFoodFromSnapshot(foodSnapshot);
                        Map<String,Object> data = (Map<String, Object>) foodSnapshot.getValue();
                        if (data.containsKey(Food.Companion.getBREAKFAST())) {
                            diary.breakfastList.items.add(diaryFood);

                        } else if (data.containsKey(Food.Companion.getLUNCH())) {
                            diary.lunchList.items.add(diaryFood);

                        } else {
                            diary.dinnerList.items.add(diaryFood);
                        }

                    }
                }
            }
        }
        return diary;
    }
    public static DiaryFood createDiaryFoodFromSnapshot(DataSnapshot dataSnapshot){
        DiaryFood diaryFood = new DiaryFood();
        Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
        Food food = FoodDatabase.Companion.getInstance().getFoodByID((String) data.get(DiaryDatabase.FOOD_ID));
        diaryFood.food= food;
        diaryFood.ID = dataSnapshot.getKey();
        diaryFood.num_of_units = Double.parseDouble(data.get(FOOD_NUM_UNIT).toString());
        if (data.containsKey(Food.Companion.getBREAKFAST())) {
            diaryFood.mealType = "breakfast";

        } else if (data.containsKey(Food.Companion.getLUNCH())) {
            diaryFood.mealType = "lunch";

        } else {
            diaryFood.mealType = "dinner";
        }
        return diaryFood;
    }
    private String formatDate(Calendar calendar){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(calendar.getTime());
    }
}
