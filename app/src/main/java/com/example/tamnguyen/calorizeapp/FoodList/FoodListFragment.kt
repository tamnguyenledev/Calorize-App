package com.example.tamnguyen.calorizeapp.FoodList


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.tamnguyen.calorizeapp.R
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.food_list_rv.*
import kotlinx.android.synthetic.main.fragment_food_list.*
import java.util.*
import kotlin.Comparator


/**
 * A simple [Fragment] subclass.
 */
class FoodListFragment : Fragment() {
    companion object {
        fun newInstance(listener: OnItemClickListener) : FoodListFragment{
            val fragment = FoodListFragment()
            fragment.listener = listener
            return fragment
        }
    }
    lateinit var mAdapter: FoodListAdapter
    lateinit var listener: OnItemClickListener
    var mFoodList: FoodList? = null
    /**
     * Used for 1s (1000ms) delay after users finish inputting
     */
    val delay = 1000L
    var runnable: Runnable? = null
    val handler = Handler()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Init RecyclerView
        rvDiaryFoodList.layoutManager = LinearLayoutManager(context!!)
        //Load Foods From Database
        loadFoods()

        searchFoodConfig()
    }

    /**
     * Wrapper method for loading foods from database
     */
    private fun loadFoods(){
        //Load foods into RecyclerView
        FoodDatabase.instance.getFoodList(object: FoodDatabase.OnCompleteListener{
            override fun onSuccess(foodList: FoodList) {
                //Sort food by current time
                mFoodList  = sortFoodByTime(foodList)
                //Set data to adapter
                mAdapter = FoodListAdapter(context!!, foodList)
                mAdapter.listener = listener
                rvDiaryFoodList.adapter = mAdapter
            }
            override fun onFailure(err: DatabaseError) {
                Toast.makeText(context!!,err.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    /**
     * Food Searching Configuration
     */
    private fun searchFoodConfig(){
        etSearchFood.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable) {
                //If Users input nothing, display all data
                if(s.toString().length == 0 && mFoodList != null)
                {
                    mAdapter = FoodListAdapter(context!!, mFoodList!!)
                    rvDiaryFoodList.adapter = mAdapter
                    return
                }
                //After users finish typing, we should call query operation after delay time
                runnable = Runnable {
                    searchFoodImp(s.toString())
                }
                handler.postDelayed(runnable,delay)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //When users keep typing, we should not call query operation
                handler.removeCallbacks(runnable)
            }
        })

    }

    /**
     * Food Searching Implementation
     */
    private fun searchFoodImp(query: String){
        val newFoodList = mFoodList?.suggestFood(query)
        mAdapter = FoodListAdapter(context!!,newFoodList!!)
        rvDiaryFoodList.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }

    /**
     * Food Reordering Based On Current Time
     */
    private fun sortFoodByTime(foodList: FoodList): FoodList{
        val calendar = GregorianCalendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        var meal = ""
        //Determine which meal users should take
        if(hour in 5 until 11)
            meal = "breakfast"
        else if(hour in 11 until 15)
            meal = "lunch"
        else
            meal = "dinner"
        return FoodList(foodList.items.sortedWith(object: Comparator<Food>{
            override fun compare(food1: Food, food2: Food): Int {
                //If Both food belongs to current Meal, order them by alphabet
                if(food1.dayType?.containsKey(meal)!! && food2.dayType?.containsKey(meal)!!)
                {
                    return food1.foodName!!.compareTo(food2.foodName!!)
                }
                else if(food1.dayType?.containsKey(meal)!!)
                {
                    return 1
                }
                else if(food2.dayType?.containsKey(meal)!!)
                {
                    return -1
                }
                else
                    return food1.foodName!!.compareTo(food2.foodName!!)
            }
        })
        )

    }
}// Required empty public constructor
