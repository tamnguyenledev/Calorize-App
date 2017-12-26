package com.example.tamnguyen.calorizeapp.Profile

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tamnguyen.calorizeapp.FoodList.FoodListAdapter
import com.example.tamnguyen.calorizeapp.R

/**
 * Created by HUYNHXUANKHANH on 12/25/2017.
 */
class ProfileFragment : Fragment() {
    lateinit var buttonEdit : FloatingActionButton
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonEdit = view.findViewById<FloatingActionButton>(R.id.profile_edit) as FloatingActionButton
        buttonEdit.setOnClickListener(){
            // open activity edit here

        }
    }

}