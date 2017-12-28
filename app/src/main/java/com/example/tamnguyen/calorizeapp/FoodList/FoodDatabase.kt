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
        val DATA_LOADING_NOT_FINISHED = 10
    }

    interface OnCompleteListener {
        fun onSuccess(foodList: FoodList)
        fun onFailure(code: Int)
    }
    interface OnInitSuccessListener{
        fun onSuccess()
    }
    /**
     * Used to retrieve Food from its ID
     * This object only valid after init has successfully retrieved all Food Data
     * Some other operations use this object
     * Therefore, it is necessary for application to load all food data once before users interact with it
     * @see getFoodList
     */
    private lateinit var foodMap: MutableMap<String,Food>
    var isLoadFinished = false
    var isLoading = false
    //Database Reference
    val mDatabase = FirebaseDatabase.getInstance().reference
    var foodList: FoodList = FoodList(ArrayList<Food>())
    /**
     * Get all food in database and put them into FoodList
     * @see FoodList
     */
    @Synchronized
    fun getFoodList(listener: OnCompleteListener) {
        if(!isLoadFinished && !isLoading){
            //If Food List is not already loaded, and nothing is loading
            //Load the list from database
            isLoading = true
            mDatabase.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(err: DatabaseError) {
                    listener.onFailure(err.code)
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    val foodList = ArrayList<Food>()
                    foodMap = HashMap<String,Food>()
                    for (child in snapshot.children) {
                        val food = child.getValue(Food::class.java)
                        foodList.add(food!!)
                        foodMap[child.key] = food
                    }
                    this@FoodDatabase.foodList = FoodList(foodList)
                    synchronized(this@FoodDatabase){
                        this@FoodDatabase.isLoadFinished = true
                        this@FoodDatabase.isLoading = false
                    }
                    listener.onSuccess(foodList = this@FoodDatabase.foodList)
                }
            })
        }
        else if(!isLoadFinished && isLoading)
        {
            listener.onFailure(DATA_LOADING_NOT_FINISHED)
        }
        else if(isLoadFinished){
            listener.onSuccess(foodList)
        }

    }

    /**
     * Get Food from its ID method
     */
    fun getFoodByID(id: String): Food{
        return foodMap?.get(id)!!
    }
}