package com.craftrom.kernelmanager.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.craftrom.kernelmanager.R
import com.craftrom.kernelmanager.utils.FileUtils
import com.craftrom.kernelmanager.utils.ProfileUtils
import com.topjohnwu.superuser.internal.UiThreadHandler.handler


class SplashActivity : AppCompatActivity() {
    private val splashTime = 3000L // 3 seconds
    private lateinit var myHandler : Handler
    var sp: SplashActivity? = null

    fun getInstance(): SplashActivity? {
        return sp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        myHandler = Handler()
        myHandler.postDelayed({
            FileUtils.setFilePermissions().submit {
                if (isRootGranted()) {
                    //Apply profile on start
                    ProfileUtils.applyProfile(this)
                    gotoMainActivity()
                }else{
                    gotoNoRootActivity()
                }
            }
        },splashTime)

    }

    fun isRootGranted(): Boolean {
        try {
            Runtime.getRuntime().exec("su")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, R.string.no_root_permission, Toast.LENGTH_LONG)
                .show()
            finish()
            return false
        }
        return true
    }

    fun presentActivity(view: View) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@SplashActivity,
            view,
            "transition"
        )
        val revealX = (view.x + view.width / 2).toInt()
        val revealY = (view.y + view.height / 2).toInt()
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_CIRCULAR_REVEAL_X, revealX)
        intent.putExtra(MainActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY)
        ActivityCompat.startActivity(this@SplashActivity, intent, options.toBundle())
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


}