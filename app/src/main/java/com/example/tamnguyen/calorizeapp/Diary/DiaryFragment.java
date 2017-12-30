package com.example.tamnguyen.calorizeapp.Diary;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tamnguyen.calorizeapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.daentech.circulardemo.widgets.ProgressBar;
import uk.co.daentech.circulardemo.widgets.ProgressCircle;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment {


    @BindView(R.id.btnPrevDay)
    public Button prevDayBtn;
    @BindView(R.id.btnNextDay)
    public Button nextDayBtn;
    @BindView(R.id.breakfastRv)
    public RecyclerView breakfastRv;
    @BindView(R.id.lunchRv)
    public RecyclerView lunchRv;
    @BindView(R.id.dinnerRv)
    public RecyclerView dinnerRv;
    @BindView(R.id.progressCarb)
    public ProgressBar progressCarb;
    @BindView(R.id.progressFat)
    public ProgressBar progressFat;
    @BindView(R.id.progressProtein)
    public ProgressBar progressProtein;
    @BindView(R.id.progressCalories)
    public ProgressCircle progressCalories;
    @BindView(R.id.btnToday)
    public Button todayBtn;
    Diary currentDiary;
    private OnItemListener listener;

    public DiaryFragment() {
        // Required empty public constructor
    }

    public static DiaryFragment newInstance(OnItemListener listener) {
        DiaryFragment diaryFragment = new DiaryFragment();
        diaryFragment.listener = listener;
        return diaryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        ButterKnife.bind(this, view);
        breakfastRv.setLayoutManager(new LinearLayoutManager(getContext()));
        lunchRv.setLayoutManager(new LinearLayoutManager(getContext()));
        dinnerRv.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Diary diary = DiaryDatabase.createDiaryFromSnapshot(dataSnapshot);
            currentDiary = diary;
            updateUI(diary);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @SuppressLint("StaticFieldLeak")
    private void initData() {

        //Because DiaryDatabase depends on FoodDatabase
        //Therefore it is neccesary to check FoodDatabase loading is finished
        //We use a background thread for this checking to avoid blocking UI-thread
        //Therefore it is also useful to initialize other resources
        DiaryDatabase.getInstance().getCurrentDiary(new DiaryDatabase.OnCompleteListener() {
            @Override
            public void onSuccess(Diary diary) {
                currentDiary = diary;
                updateUI(diary);
                setupDateManip();
                DiaryDatabase.getInstance().listenToChangesOfCurrentDiary(valueEventListener);
            }

            @Override
            public void onFailure(int code) {

            }
        });
    }

    private void updateUI(Diary diary) {
        setRecyclerViewAndAdapter(diary);
        setupProgressBar(diary);
    }

    private void setRecyclerViewAndAdapter(Diary diary) {

        DiaryMealAdapter bfAdapter = new DiaryMealAdapter(getActivity(),diary.breakfastList, listener);
        breakfastRv.setAdapter(bfAdapter);
        DiaryMealAdapter.DiaryMealAdaptertItemTouchHelperCallback itemTouchHelperCallback = bfAdapter.new DiaryMealAdaptertItemTouchHelperCallback(
                0, ItemTouchHelper.RIGHT
        );
        //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(breakfastRv);

        DiaryMealAdapter lnAdapter = new DiaryMealAdapter(getActivity(),diary.lunchList, listener);
        lunchRv.setAdapter(lnAdapter);
        DiaryMealAdapter.DiaryMealAdaptertItemTouchHelperCallback itemTouchHelperCallback2 = lnAdapter.new DiaryMealAdaptertItemTouchHelperCallback(
                0, ItemTouchHelper.RIGHT
        );
        //new ItemTouchHelper(itemTouchHelperCallback2).attachToRecyclerView(lunchRv);

        DiaryMealAdapter dnAdapter = new DiaryMealAdapter(getActivity(),diary.dinnerList, listener);
        dinnerRv.setAdapter(dnAdapter);
        DiaryMealAdapter.DiaryMealAdaptertItemTouchHelperCallback itemTouchHelperCallback3 = dnAdapter.new DiaryMealAdaptertItemTouchHelperCallback(
                0, ItemTouchHelper.RIGHT
        );
        //new ItemTouchHelper(itemTouchHelperCallback3).attachToRecyclerView(dinnerRv);

    }

    private void setupProgressBar(Diary diary) {
        //Progress Bar for Fat
        if (diary.fat > 0 && diary.neededFat > 0) {
            if (diary.fat >= diary.neededFat)
                progressFat.setProgress(1);
            else
                progressFat.setProgress((float) ((float) diary.fat / diary.neededFat));
            progressFat.startAnimation();
        } else {
            progressFat.setProgress(0);
            progressFat.startAnimation();
        }
        //Progress Bar for Carb
        if (diary.carbs > 0 && diary.neededCarbs > 0) {
            if (diary.carbs >= diary.neededCarbs)
                progressFat.setProgress(1);
            else
                progressCarb.setProgress((float) ((float) diary.carbs / diary.neededCarbs));
            progressCarb.startAnimation();
        } else {
            progressCarb.setProgress(0);
            progressCarb.startAnimation();
        }
        //Progress Bar for Protein
        if (diary.protein > 0 && diary.neededProtein > 0) {
            if (diary.protein >= diary.neededProtein)
                progressFat.setProgress(1);
            else
                progressProtein.setProgress((float) ((float) diary.protein / diary.neededProtein));
            progressProtein.startAnimation();
        } else {
            progressProtein.setProgress(0);
            progressProtein.startAnimation();
        }
        //Progress Bar for Calories
        if (diary.calories > 0 && diary.neededCalories > 0) {
            if (diary.calories >= diary.neededCalories)
                progressFat.setProgress(1);
            else
                progressCalories.setProgress((float) ((float) diary.calories / diary.neededCalories));
            progressCalories.startAnimation();
        } else {
            progressCalories.setProgress(0);
            progressCalories.startAnimation();
        }
    }

    private void setupDateManip() {
        prevDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: check validity for previous date
                DiaryDatabase.getInstance().getPrevDiary(new DiaryDatabase.OnCompleteListener() {
                    @Override
                    public void onSuccess(Diary diary) {
                        Log.d("Dung", "Prev Clicked");
                        currentDiary = diary;
                        String date;
                        if (diary.ID.equals(getCurrentDate()))
                            date = "Today";
                        else
                            date = diary.ID;
                        todayBtn.setText(date);
                        updateUI(diary);
                        DiaryDatabase.getInstance().listenToChangesOfCurrentDiary(valueEventListener);
                    }

                    @Override
                    public void onFailure(int code) {

                    }
                });
            }
        });

        nextDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: check validity for next date
                DiaryDatabase.getInstance().getNextDiary(new DiaryDatabase.OnCompleteListener() {
                    @Override
                    public void onSuccess(Diary diary) {
                        Log.d("Dung", "Next Clicked");
                        currentDiary = diary;
                        String date;
                        if (diary.ID.equals(getCurrentDate()))
                            date = "Today";
                        else
                            date = diary.ID;
                        todayBtn.setText(date);
                        updateUI(diary);
                        DiaryDatabase.getInstance().listenToChangesOfCurrentDiary(valueEventListener);
                    }

                    @Override
                    public void onFailure(int code) {
                    }
                });
            }
        });
    }

    String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(GregorianCalendar.getInstance().getTime());
    }
}
