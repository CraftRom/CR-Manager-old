package com.craftrom.kernelmanager.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.craftrom.kernelmanager.R
import com.craftrom.kernelmanager.utils.FileUtils
import com.craftrom.kernelmanager.utils.ProfileUtils
import com.topjohnwu.superuser.Shell

class SplashActivity : AppCompatActivity() {
    var bar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        FileUtils.setFilePermissions().submit {
            //Apply profile on start
            ProfileUtils.applyProfile(this);
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        instance = this@SplashActivity
        bar = findViewById(R.id.progressBar) as ProgressBar?
        val handler = Handler()
        handler.postDelayed({ presentActivity(bar) }, 700)
    }

    fun presentActivity(view: View?) {
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@SplashActivity,
            view!!,
            "transition"
        )
        val revealX = (view!!.x + view.width / 2).toInt()
        val revealY = (view.y + view.height / 2).toInt()
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        ActivityCompat.startActivity(this@SplashActivity, intent, options.toBundle())
    }

    companion object {
        var instance: SplashActivity? = null
    }
}