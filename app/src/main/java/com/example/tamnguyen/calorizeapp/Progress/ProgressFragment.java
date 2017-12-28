package com.example.tamnguyen.calorizeapp.Progress;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.tamnguyen.calorizeapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment implements View.OnClickListener {
    ImageButton fromDatePicker;
    ImageButton toDatePicker;

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
        fromDatePicker.setOnClickListener(this);
        toDatePicker.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fromDatePicker:
            case R.id.toDatePicker:
                showDatePickerDialog(v);
        }
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
}
