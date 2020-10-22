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

import com.craftrom.kernelmanager.utils.FileUtils
import com.topjohnwu.superuser.Shell
import java.io.File
import java.util.*

/**
 * Created by willi on 30.12.15.
 */
// TODO: 22/04/20 Perhaps use com.github.topjohnwu.libsu:io
class RootFile(private val mFile: String?) {
    val name: String
        get() = File(mFile).name

    fun mkdir() {
        Shell.su("mkdir -p '$mFile'").exec()
    }

    fun mv(newPath: String): RootFile {
        Shell.su("mv -f '$mFile' '$newPath'").exec()
        return RootFile(newPath)
    }

    fun cp(path: String) {
        Shell.su("cp -r '$mFile' '$path'").exec()
    }

    fun write(text: String?, append: Boolean) {
        val array = text!!.split("\\r?\\n".toRegex()).toTypedArray()
        if (!append) delete()
        for (line in array) {
            Shell.su("echo '$line' >> $mFile").exec()
        }
        RootUtils.chmod(mFile.toString(), "755")
    }

    fun execute() {
        RootUtils.runCommand("sh $mFile")
    }

    fun delete() {
        Shell.su("rm -r '$mFile'").exec()
    }

    fun list(): List<String> {
        val list: MutableList<String> = ArrayList()
        val files = RootUtils.runAndGetOutput("ls '$mFile/'")
        if (!files.isEmpty()) {
            // Make sure the files exists
            for (file in files.split("\\r?\\n".toRegex()).toTypedArray()) {
                if (file != null && !file.isEmpty() && FileUtils.existFile(
                        "$mFile/$file"
                    )
                ) {
                    list.add(file)
                }
            }
        }
        return list
    }

    fun listFiles(): List<RootFile> {
        val list: MutableList<RootFile> = ArrayList()
        val files = RootUtils.runAndGetOutput("ls '$mFile/'")
        if (!files.isEmpty()) {
            // Make sure the files exists
            for (file in files.split("\\r?\\n".toRegex()).toTypedArray()) {
                if (file != null && !file.isEmpty() && FileUtils.existFile(
                        "$mFile/$file"
                    )
                ) {
                    list.add(RootFile(if (mFile == "/") mFile + file else "$mFile/$file"))
                }
            }
        }
        return list
    }

    val isEmpty: Boolean
        get() = "false" == RootUtils.runAndGetOutput("find '$mFile' -mindepth 1 | read || echo false")

    fun exists(): Boolean {
        val output = RootUtils.runAndGetOutput("[ -e $mFile ] && echo true")
        return !output.isEmpty() && output == "true"
    }

    fun readFile(): String {
        return RootUtils.runAndGetOutput("cat '$mFile'")
    }

    override fun toString(): String {
        return mFile.toString()
    }
}