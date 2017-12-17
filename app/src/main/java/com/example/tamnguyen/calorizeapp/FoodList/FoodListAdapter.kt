package com.example.tamnguyen.calorizeapp.FoodList

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tamnguyen.calorizeapp.R
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.food_item.view.*

/**
 * Created by hoangdung on 12/17/17.
 */
public class FoodListAdapter(val mContext: Context, val mGroups: List<ExpandableGroup<Food>>) :
        ExpandableRecyclerViewAdapter<FoodListAdapter.MealViewHolder,FoodListAdapter.FoodViewHolder>(mGroups) {


    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): MealViewHolder {
        val inflater = LayoutInflater.from(mContext)
        return MealViewHolder(inflater.inflate(R.layout.meal_item,parent,false))
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(mContext)
        return FoodViewHolder(inflater.inflate(R.layout.food_item,parent,false))
    }

    override fun onBindGroupViewHolder(holder: MealViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        holder.mealName.text = group.title
    }

    override fun onBindChildViewHolder(holder: FoodViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        holder.setInfo(group.items.get(childIndex) as Food)
    }


    class FoodViewHolder(itemView: View) : ChildViewHolder(itemView) {
        var foodName: TextView
        var foodUnit: TextView
        var foodCalo: TextView
        init{
            foodName = itemView.findViewById(R.id.tvFoodName)
            foodUnit = itemView.findViewById(R.id.tvMeasurement)
            foodCalo = itemView.findViewById(R.id.tvCaloriesAmount)
        }
        public fun setInfo(food: Food){
            foodName.text = food.foodName
            foodUnit.text = food.calo.toString()
            foodUnit.text = food.unit
        }
    }
    class MealViewHolder(itemView: View) : GroupViewHolder(itemView){
        lateinit var mealName: TextView
        init {
            mealName = itemView.findViewById(R.id.tvMealName)
        }

    }
}