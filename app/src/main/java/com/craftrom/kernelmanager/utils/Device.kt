package com.craftrom.kernelmanager.utils

import android.os.Build
import android.os.Build.VERSION_CODES
import com.craftrom.kernelmanager.utils.root.RootUtils.getProp
import com.craftrom.kernelmanager.utils.root.RootUtils.runCommand
import java.lang.reflect.Field
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by willi on 31.12.15.
 */
object Device {
    fun getKernelVersion(extended: Boolean): String {
        return getKernelVersion(extended, true)
    }

    fun getKernelVersion(extended: Boolean, root: Boolean): String {
        val version: String = FileUtils.readFile("/proc/version", root).toString()
        if (extended) {
            return version
        }
        val matcher: Matcher = Pattern.compile("Linux version (\\S+).+").matcher(version)
        return if (matcher.matches() && matcher.groupCount() === 1) {
            matcher.group(1)
        } else "unknown"
    }

    val architecture: String
        get() = runCommand("uname -m").toString()
    val hardware: String
        get() = Build.HARDWARE
    val bootloader: String
        get() = Build.BOOTLOADER
    val baseBand: String
        get() = Build.getRadioVersion()
    val codename: String
        get() {
            var codeName = ""
            val fields: Array<Field> = VERSION_CODES::class.java.fields
            for (field in fields) {
                val fieldName: String = field.getName()
                var fieldValue = -1
                try {
                    fieldValue = field.getInt(Any())
                } catch (ignored: IllegalArgumentException) {
                } catch (ignored: IllegalAccessException) {
                } catch (ignored: NullPointerException) {
                }
                if (fieldValue == Build.VERSION.SDK_INT) {
                    codeName = fieldName
                    break
                }
            }
            return codeName
        }

    val buildDisplayId: String
        get() = Build.DISPLAY
    val fingerprint: String
        get() = Build.FINGERPRINT
    val version: String
        get() = Build.VERSION.RELEASE
    val vendor: String
        get() = Build.MANUFACTURER
    val deviceName: String
        get() = PropUtils.readProp("ro.product.device")
    val model: String
        get() = Build.MODEL

    class ROMInfo private constructor() {
        var version: String? = null

        companion object {
            private var sInstance: ROMInfo? = null
            val instance: ROMInfo?
                get() {
                    if (sInstance == null) {
                        sInstance = ROMInfo()
                    }
                    return sInstance
                }
            private val sProps = arrayOf(
                "ro.cm.version",
                "ro.pa.version",
                "ro.pac.version",
                "ro.carbon.version",
                "ro.slim.version",
                "ro.mod.version"
            )
        }

        init {
            for (prop in sProps) {
                this.version = getProp(prop)
                if (this.version != null && version!!.isEmpty()) {
                    break
                }
            }
        }
    }

    class MemInfo private constructor() {
        private val MEMINFO: String
        val totalMem: Long
            get() = try {
                getItem("MemTotal").replace("[^\\d]".toRegex(), "").toLong() / 1024L
            } catch (ignored: NumberFormatException) {
                0
            }
        val availableMem: Long
            get() {
                return try {
                    getItem("MemAvailable").replace("[^\\d]".toRegex(), "").toLong() / 1024L
                } catch (ignored: NumberFormatException) {
                    0
                }
            }
        val items: List<String>
            get() {
                val list: MutableList<String> = ArrayList()
                try {
                    for (line in MEMINFO.split("\\r?\\n".toRegex()).toTypedArray()) {
                        list.add(line.split(":".toRegex()).toTypedArray()[0])
                    }
                } catch (ignored: Exception) {
                }
                return list
            }

        fun getItem(prefix: String?): String {
            try {
                for (line in MEMINFO.split("\\r?\\n".toRegex()).toTypedArray()) {
                    if (line.startsWith(prefix!!)) {
                        return line.split(":".toRegex()).toTypedArray()[1].trim { it <= ' ' }
                    }
                }
            } catch (ignored: Exception) {
            }
            return ""
        }

        companion object {
            private var sInstance: MemInfo? = null
            val instance: MemInfo?
                get() {
                    if (sInstance == null) {
                        sInstance = MemInfo()
                    }
                    return sInstance
                }
            private const val MEMINFO_PROC = "/proc/meminfo"
        }

        init {
            MEMINFO = FileUtils.readFile(MEMINFO_PROC).toString()
        }
    }

    class CPUInfo private constructor() {
        val cpuInfo: String
        val features: String
            get() {
                val features = getString("Features")
                return if (!features.isEmpty()) features else getString("flags")
            }
        val processor: String
            get() {
                val pro = getString("Processor")
                return if (!pro.isEmpty()) pro else getString("model name")
            }
        val vendor: String
            get() {
                val vendor = getString("Hardware")
                return if (!vendor.isEmpty()) vendor else getString("vendor_id")
            }

        private fun getString(prefix: String): String {
            try {
                for (line in cpuInfo.split("\\r?\\n".toRegex()).toTypedArray()) {
                    if (line.startsWith(prefix)) {
                        return line.split(":".toRegex()).toTypedArray()[1].trim { it <= ' ' }
                    }
                }
            } catch (ignored: Exception) {
            }
            return ""
        }

        companion object {
            private var sInstance: CPUInfo? = null
            val instance: CPUInfo?
                get() {
                    if (sInstance == null) {
                        sInstance = CPUInfo()
                    }
                    return sInstance
                }
            private const val CPUINFO_PROC = "/proc/cpuinfo"
        }

        init {
            cpuInfo = FileUtils.readFile(CPUINFO_PROC, false).toString()
        }
    }
}
