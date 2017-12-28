package com.example.tamnguyen.calorizeapp.FoodList

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.tamnguyen.calorizeapp.R
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder

/**
 * Created by hoangdung on 12/17/17.
 */
public class FoodListAdapter(val mContext: Context, val mFoodList: FoodList) : RecyclerView.Adapter<FoodListAdapter.FoodViewHolder>() {
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        holder.setInfo(mFoodList.items[position])
        holder.foodAdd.setOnClickListener{v:View->
            listener?.onItemClick(mFoodList.items[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.food_item,parent,false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mFoodList.items.size
    }
    var listener: OnItemClickListener? = null


    inner class FoodViewHolder(itemView: View) : ChildViewHolder(itemView) {
        var foodName: TextView
        var foodUnit: TextView
        var foodCalo: TextView
        var foodAdd: ImageView
        init{
            foodName = itemView.findViewById(R.id.tvFoodName)
            foodUnit = itemView.findViewById(R.id.tvMeasurement)
            foodCalo = itemView.findViewById(R.id.tvCaloriesAmount)
            foodAdd = itemView.findViewById(R.id.smallFoodImage)
        }
        fun setInfo(food: Food){
            foodName.text = food.foodName
            foodCalo.text = food.calorie.toString()
            foodUnit.text = Food.formatMeasurement(food,1.toDouble());

        }
    }
}