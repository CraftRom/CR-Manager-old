package com.craftrom.kernelmanager.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.craftrom.kernelmanager.R
import com.craftrom.kernelmanager.activities.Choice
import com.craftrom.kernelmanager.activities.MainActivity
import com.craftrom.kernelmanager.utils.ProfileUtils

class ProfilesFragment : Fragment() {

    private lateinit var profBattery: MaterialCardView
    private lateinit var profChidori: MaterialCardView
    private lateinit var profPerformance: MaterialCardView
    private lateinit var profGame: MaterialCardView
    private lateinit var profCustom: MaterialCardView
    private lateinit var titleBattery: TextView
    private lateinit var titleChidori: TextView
    private lateinit var titlePerformance: TextView
    private lateinit var titleGame: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Choice.choice = 1
        val sharedPreferences = context!!.getSharedPreferences("profile", Context.MODE_PRIVATE)
        val sharedPreferencesBoot = context!!.getSharedPreferences("update", Context.MODE_PRIVATE)
        val view = inflater.inflate(R.layout.fragment_profiles, container, false)

        profBattery = view.findViewById(R.id.profile_battery)
        profChidori = view.findViewById(R.id.profile_chidori)
        profPerformance = view.findViewById(R.id.profile_performance)
        profGame = view.findViewById(R.id.profile_game)
        profCustom = view.findViewById(R.id.profile_custom)
        titleBattery = view.findViewById(R.id.battery_head)
        titleChidori = view.findViewById(R.id.chidori_head)
        titlePerformance = view.findViewById(R.id.performance_head)
        titleGame = view.findViewById(R.id.game_head)
        if (sharedPreferencesBoot?.getBoolean("apply_super_battery", true) != false){
            titleBattery.setText(context!!.getText(R.string.profile_super_battery))
        } else {
            if (sharedPreferencesBoot?.getBoolean("apply_super_battery", false))
            titleBattery.setText(context!!.getText(R.string.profile_battery))
        }
        when(sharedPreferences.getInt("current", 1)) {
            0 -> {
                //Battery
                profBattery.strokeColor = context!!.getColor(R.color.colorBattery)
                titleBattery.setTextColor(context!!.getColor(R.color.colorBattery))
            }
            1 -> {
                //chidori
                profChidori.strokeColor = context!!.getColor(R.color.colorChidori)
                titleChidori.setTextColor(context!!.getColor(R.color.colorChidori))
            }
            2 -> {
                //Performance
                profPerformance.strokeColor = context!!.getColor(R.color.colorPerformance)
                titlePerformance.setTextColor(context!!.getColor(R.color.colorPerformance))
            }
            3 -> {
                //Gaming
                profGame.strokeColor = context!!.getColor(R.color.colorGame)
                titleGame.setTextColor(context!!.getColor(R.color.colorGame))
            }
            else -> {
                //Custom values
                profCustom.visibility = View.VISIBLE
            }
        }

        profBattery.setOnClickListener {
            if(sharedPreferences.edit().putInt("current", 0).commit()) {
                ProfileUtils.applyProfile(activity as MainActivity)
                profBattery.strokeColor = context!!.getColor(R.color.colorBattery)
                titleBattery.setTextColor(context!!.getColor(R.color.colorBattery))

                profChidori.strokeColor = context!!.getColor(R.color.colorStroke)
                profPerformance.strokeColor = context!!.getColor(R.color.colorStroke)
                profGame.strokeColor = context!!.getColor(R.color.colorStroke)
                titleChidori.setTextColor(context!!.getColor(R.color.colorHead))
                titlePerformance.setTextColor(context!!.getColor(R.color.colorHead))
                titleGame.setTextColor(context!!.getColor(R.color.colorHead))

                profCustom.visibility = View.GONE
            }
        }

        profChidori.setOnClickListener {
            if(sharedPreferences.edit().putInt("current", 1).commit()) {
                ProfileUtils.applyProfile(activity as MainActivity)
                profChidori.strokeColor = context!!.getColor(R.color.colorChidori)
                titleChidori.setTextColor(context!!.getColor(R.color.colorChidori))

                profBattery.strokeColor = context!!.getColor(R.color.colorStroke)
                profPerformance.strokeColor = context!!.getColor(R.color.colorStroke)
                profGame.strokeColor = context!!.getColor(R.color.colorStroke)
                titleBattery.setTextColor(context!!.getColor(R.color.colorHead))
                titlePerformance.setTextColor(context!!.getColor(R.color.colorHead))
                titleGame.setTextColor(context!!.getColor(R.color.colorHead))

                profCustom.visibility = View.GONE
            }
        }

        profPerformance.setOnClickListener {
            if(sharedPreferences.edit().putInt("current", 2).commit()) {
                ProfileUtils.applyProfile(activity as MainActivity)
                profPerformance.strokeColor = context!!.getColor(R.color.colorPerformance)
                titlePerformance.setTextColor(context!!.getColor(R.color.colorPerformance))

                profBattery.strokeColor = context!!.getColor(R.color.colorStroke)
                profChidori.strokeColor = context!!.getColor(R.color.colorStroke)
                profGame.strokeColor = context!!.getColor(R.color.colorStroke)
                titleBattery.setTextColor(context!!.getColor(R.color.colorHead))
                titleChidori.setTextColor(context!!.getColor(R.color.colorHead))
                titleGame.setTextColor(context!!.getColor(R.color.colorHead))

                profCustom.visibility = View.GONE
            }
        }

        profGame.setOnClickListener {
            if(sharedPreferences.edit().putInt("current", 3).commit()) {
                ProfileUtils.applyProfile(activity as MainActivity)
                profGame.strokeColor = context!!.getColor(R.color.colorGame)
                titleGame.setTextColor(context!!.getColor(R.color.colorGame))

                profBattery.strokeColor = context!!.getColor(R.color.colorStroke)
                profChidori.strokeColor = context!!.getColor(R.color.colorStroke)
                profPerformance.strokeColor = context!!.getColor(R.color.colorStroke)
                titleBattery.setTextColor(context!!.getColor(R.color.colorHead))
                titleChidori.setTextColor(context!!.getColor(R.color.colorHead))
                titlePerformance.setTextColor(context!!.getColor(R.color.colorHead))

                profCustom.visibility = View.GONE
            }
        }

        (activity as MainActivity).fab.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return view
    }

}
