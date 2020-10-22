package com.craftrom.kernelmanager.utils

import android.content.Context
import android.util.Log
import android.widget.TextView
import org.json.JSONException
import com.craftrom.kernelmanager.fragments.IntroFragment

import org.json.JSONObject

class UpdateUtils {

    companion object {
        @JvmStatic
        fun checkUpd(context: Context) {
            val obj = JsonUtils.getJsonObject(FileUtils.readFile("/proc/chidori_kernel") ?: "{}")
            val kernel = obj?.getString("kernel-name") ?: "Generic"
            val channel = context.getSharedPreferences("update", Context.MODE_PRIVATE).getString(
               "channel",
               "stable"
           ) ?: "Generic"
            val type = obj?.getString("type") ?: "Generic"
            val version = obj?.getString("version") ?: "Generic"
            if (!(kernel.equals("Generic", true) || channel.equals("Generic", true) || type.equals(
                    "Generic",
                    true
                ) || version.equals("Generic", true)))
                getUpdate(context, kernel, channel, type, version)
        }

        @JvmStatic
        fun getUpdate(
            context: Context,
            device: String,
            channel: String,
            type: String,
            version: String
        ) {
            val device = PropUtils.readProp("ro.product.device")
            val updateURL = "https://raw.githubusercontent.com/CraftRom/KernelUpdates/android-10/$device/$channel/update.json"
            val updateObject = JsonUtils.getJsonObject(NetworkUtils.fetchJSON(updateURL))
            if(updateObject?.getString("version")?.toFloat() ?: -1f > version.toFloat()) {
                if (!(context.getSharedPreferences("update", Context.MODE_PRIVATE).edit().putString(
                        "updateStatus",
                        "Update Available"
                    ).commit()))
                    Log.e("TAG", "getUpdate: Failed to write to Shared Preferences")
            } else {
                if (!(context.getSharedPreferences("update", Context.MODE_PRIVATE).edit().putString(
                        "updateStatus",
                        "Updated"
                    ).commit()))
                    Log.e("TAG", "getUpdate: Failed to write to Shared Preferences")
            }
            val saveLatest = context.getSharedPreferences("latest", Context.MODE_PRIVATE).edit()
            saveLatest.putFloat("version", updateObject?.getString("version")?.toFloat() ?: -1f)
            saveLatest.putString("url", updateObject?.getString("url"))
            saveLatest.putString("size", updateObject?.getString("size"))
            saveLatest.putString("md5", updateObject?.getString("md5"))
            saveLatest.putString("sha256", updateObject?.getString("sha256"))
            saveLatest.putString("changelog", updateObject?.getString("changelog"))
            saveLatest.apply()
        }

    }

}