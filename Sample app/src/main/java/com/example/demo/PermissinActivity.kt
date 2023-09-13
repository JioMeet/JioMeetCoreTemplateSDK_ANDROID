package com.example.demo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.demo.view.ui.CoreLoginView
import com.example.demo.viewModel.AppViewModel
import org.jio.sdk.common.customview.CustomView
import org.jio.sdk.common.utilities.Log

class PermissionActivity:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (HelperClass.checkPermissionForCorporateUsers(this) && HelperClass.isInternetAvailable(
                this
            )
        ){


        setContent {
            CoreLoginView(onJoinMeetingClick = {
                val joinCallIntent = Intent(this, MainActivity::class.java)
                startActivity(joinCallIntent)
            }, viewModel = AppViewModel())
        }}else {
            HelperClass.requestPermissionForCorporateUsers(this)
            Log.d("Main APP","Please check Permission")
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            val joinCallIntent = Intent(this, MainActivity::class.java)
            startActivity(joinCallIntent)
        } else {
            CustomView.ShowShortToast(applicationContext, " Provide necessary permissions to proceed.")
        }
    }
}