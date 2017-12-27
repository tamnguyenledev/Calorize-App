package com.example.tamnguyen.calorizeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.tamnguyen.calorizeapp.FoodList.Food;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.tamnguyen.calorizeapp.FoodList.Food;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);

        currentChosenFood = (Food)getIntent().getParcelableExtra("food");

        tvFoodName.setText(currentChosenFood.getFoodName());
        tvFoodType.setText(currentChosenFood.getType());
        tvCaloriesAmount.setText(String.valueOf(currentChosenFood.getCalorie()));
        tvCarbsAmount.setText(String.valueOf(currentChosenFood.getCarb()));
        tvFatAmount.setText(String.valueOf(currentChosenFood.getFat()));
    }
}
