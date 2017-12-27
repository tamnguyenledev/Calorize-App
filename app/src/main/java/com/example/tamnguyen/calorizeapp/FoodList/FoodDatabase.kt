package com.example.tamnguyen.calorizeapp.FoodList

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by hoangdung on 12/26/17.
 * FoodDatabase is DAO
 * Its responsibility is to let developers works with Food Database
 * It is used to retrieve all food data
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

    /**
     * Used to retrieve Food from its ID
     * This object only valid after getFoodsFromDatabase has successfully retrieved all Food Data
     * Some other operations use this object
     * Therefore, it is necessary for application to load all food data once before users interact with it
     * @see getFoodsFromDatabase
     */
    private lateinit var foodMap: MutableMap<String,Food>

    //Database Reference
    val mDatabase = FirebaseDatabase.getInstance().reference

    /**
     * Get all food in database and put them into FoodList
     * @see FoodList
     */
    fun getFoodsFromDatabase(listener: OnCompleteListener) {
        mDatabase.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(err: DatabaseError) {
                listener.onFailure(err);
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val foodList = ArrayList<Food>()
                foodMap = HashMap<String,Food>()
                for (child in snapshot.children) {
                    val food = child.getValue(Food::class.java)
                    foodList.add(food!!)
                    foodMap[child.key] = food
                }
                listener.onSuccess(FoodList(foodList))
            }
        })
    }

    /**
     * Get Food from its ID method
     */
    fun getFoodByID(id: String): Food{
        return foodMap?.get(id)!!
    }
}