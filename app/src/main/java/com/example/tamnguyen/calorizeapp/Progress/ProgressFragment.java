package com.example.tamnguyen.calorizeapp.Progress;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tamnguyen.calorizeapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.daentech.circulardemo.widgets.ProgressBar;
import uk.co.daentech.circulardemo.widgets.ProgressCircle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment
{
    @BindView(R.id.progress_circle)
    ProgressCircle progressCircle;
    @BindView(R.id.progress_bar_protein)
    ProgressBar progressBarProtein;

    public ProgressFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onStart()
    {
        super.onStart();
        progressCircle.setProgress(0.7f);
        progressCircle.startAnimation();

        progressBarProtein.setProgress(0.4f);
        progressBarProtein.startAnimation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
