package com.example.tamnguyen.calorizeapp.Diary;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tamnguyen.calorizeapp.FoodList.Food;
import com.example.tamnguyen.calorizeapp.FoodList.FoodList;
import com.example.tamnguyen.calorizeapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by hoangdung on 12/28/17.
 */

public class DiaryMealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public DiaryMealAdapter(FoodList foodList, ArrayList<Integer> volume, OnItemListener listener){
        mFoodList = foodList;
        mVolumes = volume;
        this.listener = listener;
    }
    public interface OnItemListener{
        void onClick(FoodList foodList,ArrayList<Integer> volumes,int position);
        void onLongClick(FoodList foodList,ArrayList<Integer> volumes,int position);
        void onAddClick(FoodList foodList,ArrayList<Integer> volumes);
    }
    private FoodList mFoodList;
    private ArrayList<Integer> mVolumes;
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
                    listener.onClick(mFoodList,mVolumes,position);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(mFoodList,mVolumes,position);
                    return  true;
                }
            });
        }
        else {
            FoodAddViewHolder viewHolder = (FoodAddViewHolder) holder;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddClick(mFoodList,mVolumes);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position != mFoodList.getItems().size())
            return CHILD_FOOD;
        return CHILD_ADD;
    }

    @Override
    public int getItemCount() {
        return mFoodList.getItems().size()  + 1;
    }

    public FoodList getmFoodList() {
        return mFoodList;
    }

    public void setmFoodList(FoodList mFoodList) {
        this.mFoodList = mFoodList;
    }

    public ArrayList<Integer> getmVolumes() {
        return mVolumes;
    }

    public void setmVolumes(ArrayList<Integer> mVolumes) {
        this.mVolumes = mVolumes;
    }

    class FoodViewHolder extends RecyclerView.ViewHolder{

        public FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void setInfo(int position){

        }
    }
    class FoodAddViewHolder extends RecyclerView.ViewHolder{

        public FoodAddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}