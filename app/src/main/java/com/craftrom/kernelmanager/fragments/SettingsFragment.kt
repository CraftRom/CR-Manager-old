package com.craftrom.kernelmanager.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial
import com.craftrom.kernelmanager.R
import com.craftrom.kernelmanager.activities.Choice
import com.craftrom.kernelmanager.activities.MainActivity

class SettingsFragment : Fragment() {

    private lateinit var updStartup: SwitchMaterial
    private lateinit var updChannel: SwitchMaterial
    private lateinit var startupProfiles: SwitchMaterial

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Choice.choice = 2
        val sharedPreferences = context?.getSharedPreferences("update", Context.MODE_PRIVATE)
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        updStartup = view.findViewById(R.id.upd_start)
        updChannel = view.findViewById(R.id.beta_sign)
        startupProfiles = view.findViewById(R.id.profile_start)

        if(sharedPreferences != null) {

            updStartup.isChecked = sharedPreferences.getBoolean("startup", true)
            updChannel.isChecked = sharedPreferences.getString("channel", "Stable").equals("Beta")
            startupProfiles.isChecked = sharedPreferences.getBoolean("apply_profile_on_boot", true)

            updStartup.setOnClickListener {
                if (!(sharedPreferences.edit().putBoolean("startup", updStartup.isChecked).commit()))
                    updStartup.toggle()
            }

            updChannel.setOnClickListener {
                Choice.isMainRedrawn = 1
                if(!(sharedPreferences.edit().putString("channel", if (sharedPreferences.getString("channel", "Stable").equals("Beta")) "Stable" else "Beta").commit()))
                    updChannel.toggle()
            }

            startupProfiles.setOnClickListener {
                if(!(sharedPreferences.edit().putBoolean("apply_profile_on_boot", startupProfiles.isChecked).commit()))
                    startupProfiles.toggle()
            }

            (activity as MainActivity).fab.setOnClickListener {
                (activity as MainActivity).onBackPressed()
            }

        } else {
            Toast.makeText(context, "Failed to access Application's Data, Try reinstalling!", Toast.LENGTH_LONG).show()
            (activity as MainActivity).onBackPressed()
        }

        return view
    }

}
