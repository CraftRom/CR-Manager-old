package com.craftrom.kernelmanager.utils

import android.util.Log
import org.json.JSONObject

class JsonUtils {

    companion object {

        @JvmStatic
        fun getJsonObject(json: String): JSONObject? {

            var res: JSONObject? = null
            try {
                res = JSONObject(json)
            } catch (e: Exception) {
                Log.e("JsonUtils", "getJsonObject: Invalid JSON Format \n${json}")
            }
            return res

        }

    }

}