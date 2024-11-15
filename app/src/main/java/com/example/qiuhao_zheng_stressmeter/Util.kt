package com.example.qiuhao_zheng_stressmeter

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


object Util {
    // check read, write, and camera permissions
    fun checkPermissions(activity: Activity?) {
        val permissionLists = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val unCheckedPermissions = permissionLists.any {
            ContextCompat.checkSelfPermission(activity!!, it) != PackageManager.PERMISSION_GRANTED
        }
        if (unCheckedPermissions) {
            ActivityCompat.requestPermissions(activity!!, permissionLists, 0)
        }

    }
    // request permissions
    fun requestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 0) {
            val reaskPermissions = mutableListOf<String>()
            // get a list of ungranted permissions
            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    reaskPermissions.add(permissions[i])
                }
            }
            // for ungranted permissions, reask the ungranted permissions (request the permissions)
            if (reaskPermissions.isNotEmpty()) {
                // reask permission
                ActivityCompat.requestPermissions(activity, reaskPermissions.toTypedArray(), 0)
            }
        }
    }
}