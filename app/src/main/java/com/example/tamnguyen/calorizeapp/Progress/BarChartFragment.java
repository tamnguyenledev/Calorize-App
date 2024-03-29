package com.example.tamnguyen.calorizeapp.Progress;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tamnguyen.calorizeapp.ChartHandle.DayAxisValueFormatter;
import com.example.tamnguyen.calorizeapp.ChartHandle.MyAxisValueFormatter;
import com.example.tamnguyen.calorizeapp.ChartHandle.XYMarkerView;
import com.example.tamnguyen.calorizeapp.Diary.Diary;
import com.example.tamnguyen.calorizeapp.Diary.DiaryDatabase;
import com.example.tamnguyen.calorizeapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by nguyenbao on 12/29/17.
 */

public class BarChartFragment extends Fragment {
    //private OnFragmentInteractionListener mListener;
    private ArrayList<Double> caloResult;
    private Calendar childFromDate, childToDate;
    protected BarChart mChart;
    private int difDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barchart, container, false);
    }

    public void setChildDate(Calendar c1, Calendar c2) {
        childFromDate = (Calendar) c1.clone();
        childToDate = (Calendar) c2.clone();
        difDate = (int) ((childToDate.getTimeInMillis()-childFromDate.getTimeInMillis())/(1000*60*60*24));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DiaryDatabase.getInstance().getDiaryByInterval(childFromDate, childToDate, new DiaryDatabase.OnBatchCompleteListener() {
            @Override
            public void onSuccess(ArrayList<Diary> result) {
                caloResult = (ArrayList<Double>)result.clone();

            }

            @Override
            public void onFailure(int code) {
                Log.e("Error", "Could not find Database, error code: " + code);
            }
        });

        mChart = view.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;

                RectF bounds = mOnValueSelectedRectF;
                mChart.getBarBounds((BarEntry) e, bounds);
                MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

                Log.i("bounds", bounds.toString());
                Log.i("position", position.toString());

                Log.i("x-index",
                        "low: " + mChart.getLowestVisibleX() + ", high: "
                                + mChart.getHighestVisibleX());

                MPPointF.recycleInstance(position);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart, childFromDate.get(Calendar.DAY_OF_YEAR));
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart
        setData();

    }
    private void setData() {

        float start = 1f;
        double range;
        if (caloResult != null)
            range = Collections.max(caloResult);
        else {
            caloResult = new ArrayList<>();
            for (int i = 0; i < difDate+1; ++i) {
                caloResult.add((double) 0);
            }
            range = 50;
        }
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = (int) start; i < start + difDate + 1; i++) {
            float mult = ((float)range + 1);
            double val = (float) (Math.random() * mult);
            //double val =  caloResult.get(i-(int)start);
            if (Math.random() * 100 < 25) {
                yVals1.add(new BarEntry(i, (float) val, getResources().getDrawable(R.drawable.star)));
            } else {
                yVals1.add(new BarEntry(i, (float) val));
            }
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Progress in 2017");

            set1.setDrawIcons(false);

            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }

    protected RectF mOnValueSelectedRectF = new RectF();

}
