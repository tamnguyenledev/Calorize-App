package com.example.tamnguyen.calorizeapp.Diary;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tamnguyen.calorizeapp.FoodList.Food;
import com.example.tamnguyen.calorizeapp.FoodList.FoodList;
import com.example.tamnguyen.calorizeapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoangdung on 12/28/17.
 */

public class DiaryMealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    public DiaryMealAdapter(DiaryFoodList diaryFoodList,OnItemListener listener){
        this.diaryFoodList = diaryFoodList;
        this.listener = listener;
    }
    private DiaryFoodList diaryFoodList;
    private OnItemListener listener;
    private final static int CHILD_FOOD = 1;
    private final static int CHILD_ADD = 2;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewType == CHILD_FOOD)
        {
            view = inflater.inflate(R.layout.food_item,parent,false);
            return new FoodViewHolder(view);
        }
        else{
            view = inflater.inflate(R.layout.add_food_item,parent,false);
            return new FoodAddViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        if(viewType == CHILD_FOOD){
            FoodViewHolder viewHolder = (FoodViewHolder) holder;
            viewHolder.setInfo(position);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(diaryFoodList,position);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(diaryFoodList,position);
                    return  true;
                }
            });
        }
        else {
            FoodAddViewHolder viewHolder = (FoodAddViewHolder) holder;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddClick(diaryFoodList);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position != diaryFoodList.items.size())
            return CHILD_FOOD;
        return CHILD_ADD;
    }

    @Override
    public int getItemCount() {
        return diaryFoodList.items.size()  + 1;
    }


    class FoodViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvFoodName)
        public TextView tvFoodName;
        @BindView(R.id.tvMeasurement)
        public TextView tvMeasurement;
        @BindView(R.id.tvCaloriesAmount)
        public TextView tvCalorieAmount;
        public FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void setInfo(int position){
            Food food = diaryFoodList.items.get(position).food;
            Double num_units = diaryFoodList.items.get(position).num_of_units;
            tvFoodName.setText(food.getFoodName());
            tvMeasurement.setText(Food.Companion.formatMeasurement(food,num_units));
            tvCalorieAmount.setText(String.valueOf(food.getCalorie()*num_units));
        }
    }
    class FoodAddViewHolder extends RecyclerView.ViewHolder{

        public FoodAddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
