/*
 * Copyright (C) 2015-2016 Willi Ye <williye97@gmail.com>
 *
 * This file is part of Kernel Adiutor.
 *
 * Kernel Adiutor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Adiutor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Adiutor.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.craftrom.kernelmanager.utils.root

import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by willi on 02.05.16.
 */
class Control {
    private val mProfileMode = false
    private val mProfileCommands = LinkedHashMap<String, String>()
    private val mExecutor: Executor = Executors.newSingleThreadExecutor()

    companion object {
        private val TAG = Control::class.java.simpleName
        private var sControl: Control? = null
        fun setProp(prop: String, value: String): String {
            return "setprop $prop $value"
        }

        fun startService(prop: String): String {
            return setProp("ctl.start", prop) + ";start " + prop
        }

        fun stopService(prop: String): String {
            return setProp("ctl.stop", prop) + ";stop " + prop
        }

        fun write(text: String, path: String): String {
            return "echo '$text' > $path"
        }

        fun chmod(permission: String, file: String): String {
            return "chmod $permission $file"
        }

        fun chown(group: String, file: String): String {
            return "chown $group $file"
        }

        private val instance: Control?
            private get() {
                if (sControl == null) {
                    sControl = Control()
                }
                return sControl
            }
    }
}