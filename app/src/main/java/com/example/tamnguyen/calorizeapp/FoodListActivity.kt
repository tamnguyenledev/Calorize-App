package com.example.tamnguyen.calorizeapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.tamnguyen.calorizeapp.FoodList.Food
import com.example.tamnguyen.calorizeapp.FoodList.FoodListFragment
import com.example.tamnguyen.calorizeapp.FoodList.OnItemClickListener
import kotlinx.android.synthetic.main.activity_food_list.*

class FoodListActivity : AppCompatActivity() {

    lateinit var fragment: FoodListFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)
        //Set up Food List Fragment Item Click Listener
        fragment = supportFragmentManager.findFragmentById(R.id.list_fragment) as FoodListFragment
        fragment.setItemClickListener(object: OnItemClickListener{
            //When a food item is selected, start add food activity
            override fun onItemClick(food: Food) {
                val intent = Intent(this@FoodListActivity,AddFoodActivity::class.java)
                intent.putExtra("food",food)
                intent.putExtra("choice",spinner.selectedItem.toString())
                startActivity(intent)
            }

        })
        //Set up Spinner
        val adapter = ArrayAdapter.createFromResource(this,R.array.meals_array,android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
