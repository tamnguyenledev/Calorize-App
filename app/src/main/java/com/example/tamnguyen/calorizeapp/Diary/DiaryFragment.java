package com.example.tamnguyen.calorizeapp.Diary;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    public DiaryFragment() {
        // Required empty public constructor
    }

    public static DiaryFragment newInstance() {
        DiaryFragment diaryFragment = new DiaryFragment();
        return diaryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        ButterKnife.bind(this, view);
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
            public void onSuccess(String key, Diary diary) {
                currentDiary = diary;
                updateUI(diary);
            }

            @Override
            public void onFailure(int code) {

            }
        });
    }

    private void updateUI(Diary diary) {
        setRecyclerViewAndAdapter(diary);
    }

    private void setRecyclerViewAndAdapter(Diary diary) {
        final DiaryMealAdapter.OnItemListener itemListener = new DiaryMealAdapter.OnItemListener() {
            @Override
            public void onClick(FoodList foodList, ArrayList<Double> volumes, int position) {

            }

            @Override
            public void onLongClick(FoodList foodList, ArrayList<Double> volumes, int position) {

            }

            @Override
            public void onAddClick(FoodList foodList, ArrayList<Double> volumes) {

            }
        };
        breakfastRv.setLayoutManager(new LinearLayoutManager(getContext()));
        lunchRv.setLayoutManager(new LinearLayoutManager(getContext()));
        dinnerRv.setLayoutManager(new LinearLayoutManager(getContext()));

        breakfastRv.setAdapter(new DiaryMealAdapter(diary.breakfastList, diary.breakfastVolumeList, itemListener));
        lunchRv.setAdapter(new DiaryMealAdapter(diary.lunchList, diary.lunchVolumeList, itemListener));
        dinnerRv.setAdapter(new DiaryMealAdapter(diary.dinnerList, diary.dinnerVolumeList, itemListener));
    }

    private void setupProgressBar(Diary diary) {
        progressFat.setProgress((float) diary.neededFat);
        progressCarb.setProgress((float) diary.neededCarbs);
        progressProtein.setProgress((float) diary.neededProtein);
        progressCalories.setProgress((float) diary.neededCalories);
    }

    private void setupDateManip() {
        prevDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: check validity for previous date

            }
        });

        nextDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: check validity for next date
            }
        });
    }
}
