package com.example.demo

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
object HelperClass {

    fun checkPermissionForCorporateUsers(context: Context): Boolean {
       return hasPermissions(context, Constant.corporatePermissions)
    }

    fun requestPermissionForCorporateUsers(context: ComponentActivity?) {
        if (context != null) {
            ActivityCompat.requestPermissions(
                context,
                Constant.corporatePermissions,
                1
            )
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        var hasAllPermission = true
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(
                    context,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasAllPermission = false
                return@forEach
            }
        }
        return hasAllPermission
    }


    fun isInternetAvailable(context: Context?): Boolean {
        var result = false
        val connectivityManager: ConnectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}

object Constant {
    val corporatePermissions = arrayOf(permission.CAMERA, permission.RECORD_AUDIO,permission.READ_PHONE_STATE, permission.BLUETOOTH_CONNECT)
}

