package com.example.tamnguyen.calorizeapp.FoodList

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by hoangdung on 12/26/17.
 */
class FoodDatabase {

    private object Holder {
        val INSTANCE = FoodDatabase()
    }

    companion object {
        val instance: FoodDatabase by lazy { Holder.INSTANCE }
        val path = "foodCourt"
    }

    interface OnCompleteListener {
        fun onSuccess(foodList: FoodList)
        fun onFailure(err: DatabaseError)
    }

    val mDatabase = FirebaseDatabase.getInstance().reference
    fun getFoods(listener: OnCompleteListener) {
        mDatabase.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(err: DatabaseError) {
                listener.onFailure(err);
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                //Get All Foods in Food Database
                //Put each food into corresponding meals
                val foodList = ArrayList<Food>()
                for (child in snapshot.children) {
                    val food = child.getValue(Food::class.java)
                    foodList.add(food!!)
                }
                listener.onSuccess(FoodList(foodList))
            }
        })
    }
}