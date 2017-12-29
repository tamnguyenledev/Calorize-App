package com.example.tamnguyen.calorizeapp.Diary;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tamnguyen.calorizeapp.FoodList.FoodList;
import com.example.tamnguyen.calorizeapp.R;

import java.util.ArrayList;

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

    /**
     * Wrapper method for loading today's diary
     */
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

        breakfastRv.setAdapter(new DiaryMealAdapter(diary.breakfastList,listener));
        lunchRv.setAdapter(new DiaryMealAdapter(diary.lunchList,listener));
        dinnerRv.setAdapter(new DiaryMealAdapter(diary.dinnerList,listener));
    }

    private void setupProgressBar(Diary diary) {
        //Progress Bar for Fat
        if(diary.neededFat != 0 && diary.neededFat!=0){
            progressFat.setProgress((float) ((float) diary.fat/diary.neededFat));
            progressFat.startAnimation();
        }
       if(diary.neededCarbs != 0&& diary.neededCarbs!=0){
           //Progress Bar for Carb
           progressCarb.setProgress((float) ((float) diary.carbs/diary.neededCarbs));
           progressCarb.startAnimation();
       }
        if(diary.neededProtein!=0&& diary.neededProtein!=0){
            //Progress Bar for Protein
            progressProtein.setProgress((float) ((float) diary.protein/diary.neededProtein));
            progressProtein.startAnimation();
        }
        if(diary.neededCalories != 0&& diary.neededCalories!=0){
            //Progress Bar for Calories
            progressCalories.setProgress((float) ((float) diary.calories/diary.neededCalories));
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
                        Log.d("Dung","Prev Clicked");
                        currentDiary = diary;
                        updateUI(diary);
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
                        Log.d("Dung","Next Clicked");
                        currentDiary = diary;
                        updateUI(diary);
                    }

                    @Override
                    public void onFailure(int code) {
                    }
                });
            }
        });
    }
}
