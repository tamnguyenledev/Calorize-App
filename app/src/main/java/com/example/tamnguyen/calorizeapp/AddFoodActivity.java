package com.example.tamnguyen.calorizeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamnguyen.calorizeapp.FoodList.Food;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFoodActivity extends AppCompatActivity
{
    private Food currentChosenFood;
    @BindView(R.id.tvFoodName)
    TextView tvFoodName;
    @BindView(R.id.tvFoodType)
    TextView tvFoodType;
    @BindView(R.id.tvCaloriesAmount)
    TextView tvCaloriesAmount;
    @BindView(R.id.tvCarbsAmount)
    TextView tvCarbsAmount;
    @BindView(R.id.tvProteinAmount)
    TextView tvProteinAmount;
    @BindView(R.id.tvFatAmount)
    TextView tvFatAmount;
    @BindView(R.id.etQuantity)
    EditText etQuantity;
    @BindView(R.id.measurementTypeSpinner)
    Spinner snMeasurementType;
    @BindView(R.id.spinner)
    Spinner snMealType;

    ArrayAdapter<CharSequence> measurementTypeAdapter;
    ArrayAdapter<CharSequence> mealTypeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);

        currentChosenFood = (Food)getIntent().getParcelableExtra("food");

        initViews();

    }

    private void initViews()
    {
        tvFoodName.setText(currentChosenFood.getFoodName());
        tvFoodType.setText(currentChosenFood.getType());


        // TODO: check quantity == 0 and empty
        etQuantity.setText("1");

        // unit spinner
        measurementTypeAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        measurementTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurementTypeAdapter.add(currentChosenFood.getUnit() + "(" + currentChosenFood.getMassPerUnit() + "g)");
        measurementTypeAdapter.add("gram");
        snMeasurementType.setAdapter(measurementTypeAdapter);

        snMeasurementType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0) // custom food unit
                    setFoodMetric(1);
                else
                    setFoodMetric(currentChosenFood.getMassPerUnit());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });
        snMeasurementType.setSelection(0);

        // TODO: set the correct data
        // meal type spinner
        mealTypeAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeAdapter.add("Breakfast");
        mealTypeAdapter.add("Lunch");
        mealTypeAdapter.add("Dinner");
        snMealType.setAdapter(mealTypeAdapter);

    }

    private void setFoodMetric(double scaleDownRatio)
    {
        // TODO: add units
        tvCaloriesAmount.setText(String.valueOf(Math.round(currentChosenFood.getCalorie()/scaleDownRatio*1000.0)/1000.0));
        tvCarbsAmount.setText(String.valueOf(Math.round(currentChosenFood.getCarb()/scaleDownRatio*1000.0)/1000.0));
        tvProteinAmount.setText(String.valueOf(Math.round(currentChosenFood.getProtein()/scaleDownRatio*1000.0)/1000.0));
        tvFatAmount.setText(String.valueOf(Math.round(currentChosenFood.getFat()/scaleDownRatio*1000.0)/1000.0));
    }
}
