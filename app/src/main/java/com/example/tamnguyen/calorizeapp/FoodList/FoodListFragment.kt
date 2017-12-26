package com.example.tamnguyen.calorizeapp.FoodList


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.tamnguyen.calorizeapp.R
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class FoodListFragment : Fragment() {

    lateinit var mAdapter: FoodListAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Init RecyclerView and Adapter

        mAdapter = FoodListAdapter(context!!,getMockData())
        //rvDiaryFoodList.layoutManager = LinearLayoutManager(context!!)
        //rvDiaryFoodList.adapter = mAdapter

    }

    private fun getMockData(): List<Meal>{
        val food_1 = Food()
        food_1.foodName = "Banana"
        food_1.calorie = 200.toFloat()
        food_1.unit = "1 trai"


        val breakfast = Meal("Breakfast",Arrays.asList(food_1))
        val lunch = Meal("Lunch",Arrays.asList(food_1))
        val dinner = Meal("Dinner",Arrays.asList(food_1))

        val meals = Arrays.asList(
                breakfast,
                lunch,
                dinner
        )
        return meals
    }
    public fun setItemClickListener(listener: OnItemClickListener){
        mAdapter?.listener = listener
    }
}// Required empty public constructor
