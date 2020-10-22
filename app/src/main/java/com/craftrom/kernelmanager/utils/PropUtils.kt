package com.craftrom.kernelmanager.utils

import android.annotation.SuppressLint
import android.system.Os
import android.system.StructUtsname
import android.util.Log

class PropUtils {

    companion object {

        @SuppressLint("PrivateApi")
        @JvmStatic
        fun readProp(prop: String): String {

            var res = "Unknown"
            try {
                res = Class.forName("android.os.SystemProperties").getMethod("get", String::class.java).invoke(null, prop) as String
            } catch (e: Exception) {
                Log.e("PropUtils", "readProp: Couldn't read the prop ${prop}")
            }
            return res

        }

        @JvmStatic
        fun kernelVersion(): String {

            val uname: StructUtsname = Os.uname()
            return uname.release ?: "Unknown"

        }

    }

}