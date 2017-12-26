package com.example.tamnguyen.calorizeapp.FoodList


import android.os.Bundle
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


    }
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
    private fun searchFoodConfig(){
        etSearchFood.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }
}// Required empty public constructor
