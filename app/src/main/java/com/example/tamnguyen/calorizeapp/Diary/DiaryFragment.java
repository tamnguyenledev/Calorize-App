package com.example.tamnguyen.calorizeapp.Diary;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tamnguyen.calorizeapp.FoodList.FoodDatabase;
import com.example.tamnguyen.calorizeapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment {


    @BindView(R.id.btnPrevDay)
    public Button prevDayBtn;
    @BindView(R.id.btnNextDay)
    public Button nextDayBtn;

    public DiaryFragment() {
        // Required empty public constructor
    }
    Diary mCurrentDiary;
    Calendar mCalendar;

    public static DiaryFragment newInstance(){
        DiaryFragment diaryFragment = new DiaryFragment();
        return diaryFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCalendar = GregorianCalendar.getInstance();
        //Load Diary First
        loadTodayDiary();

    }

    /**
     * Wrapper method for loading today's diary
     */
    private void loadTodayDiary(){

    }
    private void setupDateManip(){
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
