package com.craftrom.kernelmanager.services

import android.content.Context
import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.craftrom.kernelmanager.R
import com.craftrom.kernelmanager.utils.FileUtils
import com.craftrom.kernelmanager.utils.ProfileUtils

class QSTileService : TileService() {

    private var curr = 1

    override fun onClick() {
        super.onClick()

        curr = (curr + 1) % 4
        val tile = qsTile
        when(curr) {
            0 -> {
                //Battery
                tile.label = applicationContext.getString(R.string.profile_battery)
                tile.state = Tile.STATE_ACTIVE
                tile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_qs_battery)
            }
            1 -> {
                //chidori
                tile.label = applicationContext.getString(R.string.chidori_profile)
                tile.state = Tile.STATE_ACTIVE
                tile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_tile_default)
            }
            2 -> {
                //Performance
                tile.label = applicationContext.getString(R.string.profile_performance)
                tile.state = Tile.STATE_ACTIVE
                tile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_qs_performance)
            }
            3 -> {
                //Game
                tile.label = applicationContext.getString(R.string.profile_gaming)
                tile.state = Tile.STATE_ACTIVE
                tile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_qs_game)
            }
            else -> {
                //Custom
                tile.label = applicationContext.getString(R.string.custom_warn_head)
                tile.state = Tile.STATE_ACTIVE
                tile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_qs_custom)
            }
        }
        tile.updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()

        curr = getCurrentProfile()
        val tile = qsTile
        when(curr) {
            0 -> {
                //Battery
                tile.label = applicationContext.getString(R.string.profile_battery)
                tile.state = Tile.STATE_ACTIVE
                tile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_qs_battery)
            }
            1 -> {
                //chidori
                tile.label = applicationContext.getString(R.string.chidori_profile)
                tile.state = Tile.STATE_ACTIVE
                tile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_tile_default)
            }
            2 -> {
                //Performance
                tile.label = applicationContext.getString(R.string.profile_performance)
                tile.state = Tile.STATE_ACTIVE
                tile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_qs_performance)
            }
            3 -> {
                //Game
                tile.label = applicationContext.getString(R.string.profile_gaming)
                tile.state = Tile.STATE_ACTIVE
                tile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_qs_game)
            }
            else -> {
                //Custom
                tile.label = applicationContext.getString(R.string.custom_warn_head)
                tile.state = Tile.STATE_ACTIVE
                tile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_qs_custom)
            }
        }
        tile.updateTile()
    }

    override fun onStopListening() {
        super.onStopListening()
        setCurrentProfile(curr)
    }

    private fun getCurrentProfile(): Int {
        return applicationContext.getSharedPreferences("profile", Context.MODE_PRIVATE).getInt("current", 1)
    }

    private fun setCurrentProfile(n: Int) {
        if (getCurrentProfile() != n) {
            FileUtils.setFilePermissions().submit {
                if (applicationContext.getSharedPreferences("profile", Context.MODE_PRIVATE).edit().putInt("current", n).commit())
                    ProfileUtils.applyProfile(applicationContext)
            }
        }
    }

}