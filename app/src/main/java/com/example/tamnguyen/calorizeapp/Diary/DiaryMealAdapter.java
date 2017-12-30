package com.example.tamnguyen.calorizeapp.Diary;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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


    public DiaryMealAdapter(Context context,DiaryFoodList diaryFoodList,OnItemListener listener){
        this.diaryFoodList = diaryFoodList;
        this.listener = listener;
        this.context = context;
    }
    private DiaryFoodList diaryFoodList;
    private OnItemListener listener;
    private final static int CHILD_FOOD = 1;
    private final static int CHILD_ADD = 2;
    private Context context;
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
        @BindView(R.id.foreground)
        public RelativeLayout foreground;
        @BindView(R.id.background)
        public RelativeLayout background;
        @BindView(R.id.smallFoodImage)
        public ImageView foodIcon;
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
            Glide.with(context).load(diaryFoodList.items.get(position).food.getPhotoUrl()).into(foodIcon);
        }
    }
    class FoodAddViewHolder extends RecyclerView.ViewHolder{

        public FoodAddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public class DiaryMealAdaptertItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        RecyclerView recyclerView;
        public DiaryMealAdaptertItemTouchHelperCallback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            this.recyclerView = recyclerView;
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            listener.onLongClick(DiaryMealAdapter.this.diaryFoodList,viewHolder.getAdapterPosition());
            notifyItemChanged(viewHolder.getAdapterPosition());
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if(viewHolder!=null){
                final View foreground = ((DiaryMealAdapter.FoodViewHolder)viewHolder).foreground;
                getDefaultUIUtil().onSelected(foreground);
            }
        }

        @Override
        public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            final View foregroundView = ((DiaryMealAdapter.FoodViewHolder)viewHolder).foreground;
            if(dX > 0){
                ((FoodViewHolder)viewHolder).background.setVisibility(View.VISIBLE);
            }
            else{
                ((FoodViewHolder)viewHolder).background.setVisibility(View.INVISIBLE);
            }
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            final View foregroundView = ((DiaryMealAdapter.FoodViewHolder)viewHolder).foreground;
            getDefaultUIUtil().clearView(foregroundView);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            final View foregroundView = ((DiaryMealAdapter.FoodViewHolder)viewHolder).foreground;
            getDefaultUIUtil().onDraw(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);

        }
    }
}
