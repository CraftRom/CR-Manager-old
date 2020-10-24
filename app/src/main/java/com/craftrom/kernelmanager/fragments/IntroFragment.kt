package com.craftrom.kernelmanager.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.card.MaterialCardView
import com.craftrom.kernelmanager.R
import com.craftrom.kernelmanager.activities.Choice
import com.craftrom.kernelmanager.activities.MainActivity
import com.craftrom.kernelmanager.utils.*
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class IntroFragment : Fragment() {

    private lateinit var updateCard: MaterialCardView
    private lateinit var device: TextView
    private lateinit var chidoriVersion: TextView
    private lateinit var buildType: TextView
    private lateinit var kernVer: TextView
    private lateinit var buildDate: TextView
    private lateinit var updStatus: TextView
    private lateinit var updCheckDate: TextView
    private lateinit var updLatestVersion: TextView
    private lateinit var updChannel: TextView
    private var dID: Long = 0L

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Choice.choice = 0
        val sharedPreferences = context?.getSharedPreferences("update", Context.MODE_PRIVATE)
        val updateSharedPreferences = context?.getSharedPreferences("update", Context.MODE_PRIVATE)
        val view = inflater.inflate(R.layout.fragment_intro, container, false)

        updateCard = view.findViewById(R.id.upd_card)
        device = view.findViewById(R.id.device_name)
        chidoriVersion = view.findViewById(R.id.chidori_version)
        buildType = view.findViewById(R.id.build_type)
        kernVer = view.findViewById(R.id.kernel_version)
        buildDate = view.findViewById(R.id.build_date)
        updStatus = view.findViewById(R.id.update_status)
        updCheckDate = view.findViewById(R.id.last_check_date)
        updLatestVersion = view.findViewById(R.id.latest_version)
        updChannel = view.findViewById(R.id.update_channel)

        context!!.registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        device.text = Device.deviceName
        chidoriVersion.text = JsonUtils.getJsonObject(FileUtils.readFile("/proc/chidori_kernel") ?: "{}")?.getString("version") ?: "unknown"
        buildType.text = JsonUtils.getJsonObject(FileUtils.readFile("/proc/cidori_kernel") ?: "{}")?.getString("type") ?: "unknown"
        kernVer.text = PropUtils.kernelVersion()
        buildDate.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(SimpleDateFormat("yyyyMMdd-HHmm", Locale.getDefault()).parse(JsonUtils.getJsonObject(FileUtils.readFile("/proc/chidori_kernel") ?: "")?.getString("buildtime") ?: "Unknown") ?: "") ?: "Unknown"

        updCheckDate.text = sharedPreferences?.getString("lastCheck", "Never")
        updChannel.text = sharedPreferences?.getString("channel", "stable")
        val latestVersion = context?.getSharedPreferences("latest", Context.MODE_PRIVATE)?.getFloat("version", -1f) ?: -1f
        updLatestVersion.text =  latestVersion.toString()

        updStatus.text = sharedPreferences?.getString("updateStatus", "Updated")
        updateCard.strokeColor = context!!.getColor(if(sharedPreferences?.getString("updateStatus", "Updated").equals("Updated", true))  R.color.colorChidori else R.color.colorBattery)

        updateCard.setOnClickListener {
            if(!sharedPreferences?.getString("updateStatus", "Updated").equals("updated", true)) {
                /*Toast.makeText(context, "Starting Download", Toast.LENGTH_SHORT).show()
                startDownload()*/
                val builder = AlertDialog.Builder(context)
                val customLayout = layoutInflater.inflate(R.layout.dialog_update_details, null)
                builder.setView(customLayout)
                builder.setCancelable(true)

                val latest = context?.getSharedPreferences("latest", Context.MODE_PRIVATE)
                val downloadBtn = customLayout.findViewById<TextView>(R.id.download)
                val cancelBtn = customLayout.findViewById<TextView>(R.id.cancel)
                val version = customLayout.findViewById<TextView>(R.id.vers)
                val dSize = customLayout.findViewById<TextView>(R.id.size_value)

                version.text = context?.getString(R.string.dialog_version, latest?.getFloat("version", 0f).toString())
                val size = (if(latest!!.getInt("size", 0) == 0) 1 else latest.getInt("size", 0))/1000000
                val df = DecimalFormat()
                df.maximumFractionDigits = 2
                dSize.text = context?.getString(R.string.size_value, df.format(size))

                val dialog = builder.create()
                dialog.show()
                downloadBtn.setOnClickListener {
                    startDownload()
                    dialog.dismiss()
                }
                cancelBtn.setOnClickListener {
                    dialog.cancel()
                }
            }
        }

        if((updateSharedPreferences?.getBoolean("startup", true) != false && Choice.isMainRedrawn == 1) || Choice.isMainRedrawn == 1) {
            updateUI(sharedPreferences)
        }

        (activity as MainActivity).fab.setOnClickListener {
            updateUI(sharedPreferences)
        }

        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onDestroy() {
        super.onDestroy()
        context!!.unregisterReceiver(onDownloadComplete)
    }

    private fun startDownload() {
        val obj = JsonUtils.getJsonObject(FileUtils.readFile("/proc/chidori_kernel") ?: "{}")
        val kernel = obj?.getString("kernel-name") ?: "Generic"
        val device = Device.deviceName
        val channel = context?.getSharedPreferences("update", Context.MODE_PRIVATE)?.getString("channel", "stable") ?: "generic"
        val direc = File(context!!.getExternalFilesDir(null)!!, "Updates")
        direc.mkdir()
        val file = File(direc, "$kernel-$device-$channel-${context?.getSharedPreferences("latest", Context.MODE_PRIVATE)?.getFloat("version", -1f).toString()}.zip")
        val request = DownloadManager.Request(Uri.parse(context!!.getSharedPreferences("latest", Context.MODE_PRIVATE).getString("url", "")))
                .setTitle("Chidori Kernel")
                .setDescription("Downloading Update")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(file))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        val downloadManager = context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dID = downloadManager.enqueue(request)
    }

    private fun updateUI(sharedPreferences: SharedPreferences?) {
        Choice.isMainRedrawn = 0
        updStatus.text = context!!.getText(R.string.upd_checking)
        updateCard.strokeColor = context!!.getColor(R.color.colorStroke)
        Thread(Runnable {
            UpdateUtils.checkUpd(context!!)
            (activity as MainActivity).runOnUiThread {
                val temp = SimpleDateFormat("dd MMM yyyy hh:mm", Locale.getDefault()).format(Date())
                sharedPreferences?.edit()?.putString("lastCheck", temp)?.apply()
                updCheckDate.text = temp
                updStatus.text = sharedPreferences?.getString("updateStatus", "Updated")
                val latestVersion = context?.getSharedPreferences("latest", Context.MODE_PRIVATE)?.getFloat("version", -1f) ?: -1f
                updLatestVersion.text = if(latestVersion == -1f) "Unknown" else latestVersion.toString()
                updateCard.strokeColor = context!!.getColor(if(sharedPreferences?.getString("updateStatus", "Updated").equals("updated", true))  R.color.colorChidori else R.color.colorBattery)
            }
        }).start()
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if(dID == id) {
                //TODO: Check for MD5 and do shit!
            }

        }
    }

}
