package com.craftrom.kernelmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.craftrom.kernelmanager.R
import com.craftrom.kernelmanager.utils.FileUtils
import com.craftrom.kernelmanager.utils.ProfileUtils
import com.topjohnwu.superuser.Shell

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        FileUtils.setFilePermissions().submit {
            //Apply profile on start
            ProfileUtils.applyProfile(this);
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

}