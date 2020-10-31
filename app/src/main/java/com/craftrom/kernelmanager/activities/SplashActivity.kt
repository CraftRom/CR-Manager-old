package com.craftrom.kernelmanager.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.craftrom.kernelmanager.R
import com.craftrom.kernelmanager.utils.Device
import com.craftrom.kernelmanager.utils.FileUtils
import com.craftrom.kernelmanager.utils.ProfileUtils
import com.craftrom.kernelmanager.utils.root.CheckRoot
import com.topjohnwu.superuser.internal.UiThreadHandler.handler


class SplashActivity : AppCompatActivity() {
    private val splashTime = 3000L // 3 seconds
    private lateinit var myHandler : Handler
    private val dName = Device.deviceName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        myHandler = Handler()
        myHandler.postDelayed({

            FileUtils.setFilePermissions().submit {
                if (CheckRoot.isDeviceRooted && FileUtils.existFile("/proc/chidori_kernel",true) && dName == "onclite" || dName == "onc") {
                    //Apply profile on start
                    ProfileUtils.applyProfile(this)
                    gotoMainActivity()
                } else if (CheckRoot.isDeviceRooted && FileUtils.existFile("/proc/chidori_kernel",true)) {
                    gotoNoDeviceActivity()
                } else if (dName == "onclite" || dName == "onc" && FileUtils.existFile("/proc/chidori_kernel",true)){
                    gotoNoRootActivity()
                } else {
                    gotoNoKernelActivity()
                }

            }
        },splashTime)

    }

    private fun gotoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun gotoNoRootActivity() {
        val intent = Intent(this, NoRootActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun gotoNoDeviceActivity() {
        val intent = Intent(this, NoDeviceActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun gotoNoKernelActivity() {
        val intent = Intent(this, NoKernelActivity::class.java)
        startActivity(intent)
        finish()

   for (int i=0; i < 2; i++){
   Toast.makeText(this, "Welcome to craft rom", Toast.LENGTH_SHORT).show();
			}
}

** Called when the new exit button is clicked. */
   public void nav_exit(View view)
		{
			
        finish();
	}
