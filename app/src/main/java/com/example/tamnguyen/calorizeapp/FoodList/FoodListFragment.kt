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
import kotlinx.android.synthetic.main.food_list.*
import kotlinx.android.synthetic.main.food_list_rv.*
import kotlinx.android.synthetic.main.fragment_food_list.*
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.util.*
import kotlin.collections.ArrayList


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
        FoodDatabase.instance.getFoods(object: FoodDatabase.OnCompleteListener{
            override fun onSuccess(meals: List<Meal>) {
                mAdapter = FoodListAdapter(context!!,meals)
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
        val newMeals = ArrayList<Meal>()
        for(meal in mAdapter.mGroups){
            newMeals.add((meal as Meal).suggestFood(query))
        }
        mAdapter = FoodListAdapter(context!!,newMeals)
        mAdapter.notifyDataSetChanged()
    }
}// Required empty public constructor
