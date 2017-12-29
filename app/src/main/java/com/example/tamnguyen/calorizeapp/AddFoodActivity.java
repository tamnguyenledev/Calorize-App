package com.example.tamnguyen.calorizeapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamnguyen.calorizeapp.Diary.Diary;
import com.example.tamnguyen.calorizeapp.Diary.DiaryDatabase;
import com.example.tamnguyen.calorizeapp.Diary.DiaryFood;
import com.example.tamnguyen.calorizeapp.FoodList.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFoodActivity extends AppCompatActivity
{
    private Food currentChosenFood;
    private int mealChoice;
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
    @BindView(R.id.btnAddFood)
    Button btnAddFood;
    ArrayAdapter<CharSequence> measurementTypeAdapter;
    ArrayAdapter<CharSequence> mealTypeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);

        currentChosenFood = getIntent().getParcelableExtra("food");
        mealChoice = getIntent().getIntExtra("choice",0);
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
        snMealType.setSelection(mealChoice);

        // add food button
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaryFood diaryFood = new DiaryFood();
                diaryFood.setFood(currentChosenFood);
                diaryFood.setMealType(snMealType.getSelectedItem().toString().toLowerCase());
                diaryFood.setNum_of_units(Double.parseDouble(etQuantity.getText().toString()));
                DiaryDatabase.getInstance().addFoodIntoDiary(formatDate(DiaryDatabase.getInstance().getCalendar()),
                        diaryFood,
                        new DiaryDatabase.OnCompleteListener() {
                            @Override
                            public void onSuccess(Diary diary) {
                                Toast.makeText(AddFoodActivity.this,"Your food is added",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int code) {
                                Toast.makeText(AddFoodActivity.this,"Something wrong!Try again",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void setFoodMetric(double scaleDownRatio)
    {
        // TODO: add units
        tvCaloriesAmount.setText(String.valueOf(Math.round(currentChosenFood.getCalorie()/scaleDownRatio*1000.0)/1000.0));
        tvCarbsAmount.setText(String.valueOf(Math.round(currentChosenFood.getCarb()/scaleDownRatio*1000.0)/1000.0));
        tvProteinAmount.setText(String.valueOf(Math.round(currentChosenFood.getProtein()/scaleDownRatio*1000.0)/1000.0));
        tvFatAmount.setText(String.valueOf(Math.round(currentChosenFood.getFat()/scaleDownRatio*1000.0)/1000.0));
    }
    private String formatDate(Calendar calendar){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(calendar.getTime());
    }
}
