package com.craftrom.kernelmanager.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class OnBoot: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val sharedPreferences = context?.getSharedPreferences("update", Context.MODE_PRIVATE)
        if (sharedPreferences?.getBoolean("apply_profile_on_boot", true) != false)
            context?.startForegroundService(Intent(context, BootService::class.java))
    }

}