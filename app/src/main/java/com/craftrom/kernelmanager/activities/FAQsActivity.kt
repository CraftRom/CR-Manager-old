package com.craftrom.kernelmanager.activities

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.craftrom.kernelmanager.R


class FAQsActivity : AppCompatActivity() {
     lateinit var versionApp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faqs)
        versionApp = this.findViewById(R.id.version)

        val manager = this.packageManager
        try {
            val info = manager.getPackageInfo(this.packageName, 0)
            val packageName = info.packageName
            val versionName: String  = info.versionName
            // display the collected information in text view
            versionApp.setText("v$versionName")

        } catch (e: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
        }
    }
}