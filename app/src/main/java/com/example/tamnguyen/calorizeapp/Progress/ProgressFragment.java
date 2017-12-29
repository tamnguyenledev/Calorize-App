package com.example.tamnguyen.calorizeapp.Progress;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tamnguyen.calorizeapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */

/**
 * IN THIS MAIN FRAGMENT, THERE WILL BE ANOTHER 2 CHILD FRAGMENTS
 * NAMED COMPARE FRAGMENT AND BARCHART FRAGMENT
 */
public class ProgressFragment extends Fragment implements View.OnClickListener {
    ImageButton fromDatePicker;
    ImageButton toDatePicker;
    TextView tvToDate, tvFromDate;
    Calendar myCalendar = Calendar.getInstance();
    Calendar fromDate = null, toDate = null;
    int curButton;
    boolean flagSetFrom =false, flagSetTo = false;
    private final int COMPARE_FRAG = 1;
    private final int BAR_FRAG = 2;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fromDatePicker = view.findViewById(R.id.fromDatePicker);
        toDatePicker = view.findViewById(R.id.toDatePicker);
        tvFromDate = view.findViewById(R.id.tvFromDate);
        tvToDate = view.findViewById(R.id.tvToDate);

        fromDatePicker.setOnClickListener(this);
        toDatePicker.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fromDatePicker:
            case R.id.tvFromDate:
                flagSetFrom = true;
                curButton = 1;
                setDateTimeField();
                break;
            case R.id.toDatePicker:
            case R.id.tvToDate:
                flagSetTo = true;
                curButton = 2;
                setDateTimeField();
                break;
        }
    }

    ////////////////////////////////////
    //// Handle for datepicker dialog
    private void updateLabel() {
        String myFormat = "dd MMM, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (curButton == 1) {
            fromDate = (Calendar)myCalendar.clone();
            validDate();
            tvFromDate.setText(sdf.format(fromDate.getTime()));
        } else {
            toDate = (Calendar)myCalendar.clone();
            validDate();
            tvToDate.setText(sdf.format(toDate.getTime()));
        }
        checkState(BAR_FRAG);
    }
    private void setDateTimeField() {
        new DatePickerDialog(getContext(), date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    /////////////////////////////////////////
    private void checkState(int idChildFrag) {
        if (flagSetFrom && flagSetTo) {

            Fragment childFragment;
            if (idChildFrag == BAR_FRAG) {
                BarChartFragment childChart = new BarChartFragment();
                childFragment = childChart;
                childChart.setChildDate(fromDate, toDate);

            }
            else
                childFragment = new CompareFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.child_Fragment, childFragment).commit();
        }
    }
    private void validDate() {
        if (fromDate != null && toDate != null) {
            if (fromDate.compareTo(toDate) > 0) {
                Calendar tmp = (Calendar)fromDate.clone();
                tmp.add(Calendar.DATE, 1);
                toDate = (Calendar)tmp.clone();
            }
        }
    }
    /**
     * Handle for 2 child fragments
     */

}

