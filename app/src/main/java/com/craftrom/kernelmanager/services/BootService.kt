package com.craftrom.kernelmanager.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import com.craftrom.kernelmanager.R
import com.craftrom.kernelmanager.activities.SplashActivity
import com.craftrom.kernelmanager.utils.FileUtils
import com.craftrom.kernelmanager.utils.ProfileUtils

class BootService: Service() {

    private val NOTIFICATION_CHANNEL_BOOT = "notification_channel_boot"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_BOOT, "Applying Kernel Profile", NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.setSound(null, null)
        notificationManager.createNotificationChannel(notificationChannel)

        val builder = Notification.Builder(this, NOTIFICATION_CHANNEL_BOOT)
        builder.setContentTitle("Applying Kernel Profile")
                .setSmallIcon(R.drawable.ic_tile_default)
                .setColor(this.getColor(R.color.colorAccent))

        startForeground(1, builder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bool = applyOnBoot(this);
        if (!bool)
            stopSelf()

        return START_NOT_STICKY
    }

    private fun applyOnBoot(service: BootService): Boolean {
        val delay = 5
        val launchIntent = Intent(service, SplashActivity::class.java)
        val contentIntent = PendingIntent.getActivity(service,0, launchIntent, 0)

        val builder = Notification.Builder(service, NOTIFICATION_CHANNEL_BOOT)
        builder.setContentTitle(service.getString(R.string.app_name))
                .setContentText(service.getString(R.string.applying_values_on_boot, delay))
                .setSmallIcon(R.drawable.ic_tile_default)
                .setColor(service.getColor(R.color.colorAccent))
                .setOngoing(true)
                .setWhen(0)

        val builderComplete = Notification.Builder(service, NOTIFICATION_CHANNEL_BOOT)
        builderComplete.setContentTitle(service.getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_tile_default)
                .setColor(service.getColor(R.color.colorAccent))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)

        val handler = Handler()

        Thread(Runnable {
            for (i in 0..delay) {
                builder.setContentText(service.getString(R.string.applying_values_on_boot, delay-i))
                        .setProgress(delay, i, false)
                service.startForeground(1, builder.build())
                try {
                    Thread.sleep(1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            builderComplete.setContentTitle("Settings applied successfully")
            val manager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(2, builderComplete.build())
            FileUtils.setFilePermissions().submit {
                ProfileUtils.applyProfile(service)
                handler.post {
                    Toast.makeText(service, "Settings applied successfully", Toast.LENGTH_SHORT).show()
                }
            }
            service.stopSelf()
        }).start()

        return true
    }

}