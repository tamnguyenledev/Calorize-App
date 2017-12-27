package com.example.tamnguyen.calorizeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
        // TODO: add units
        tvCaloriesAmount.setText(String.valueOf(currentChosenFood.getCalorie()));
        tvCarbsAmount.setText(String.valueOf(currentChosenFood.getCarb()));
        tvProteinAmount.setText(String.valueOf(currentChosenFood.getProtein()));
        tvFatAmount.setText(String.valueOf(currentChosenFood.getFat()));

        // TODO: check quantity == 0
        etQuantity.setText("1");

        // unit spinner
        measurementTypeAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        measurementTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurementTypeAdapter.add(currentChosenFood.getUnit() + "(" + currentChosenFood.getMassPerUnit() + "g)");
        measurementTypeAdapter.add("gram");
        snMeasurementType.setAdapter(measurementTypeAdapter);

        // meal type spinner
        mealTypeAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeAdapter.add("Breakfast");
        mealTypeAdapter.add("Lunch");
        mealTypeAdapter.add("Dinner");
        snMealType.setAdapter(mealTypeAdapter);

    }

}
