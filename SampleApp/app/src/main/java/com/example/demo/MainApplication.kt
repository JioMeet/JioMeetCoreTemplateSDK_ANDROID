package com.example.demo

import android.app.Application
import com.jio.sdksampleapp.R
import dagger.hilt.android.HiltAndroidApp
import org.jio.sdk.sdkmanager.JioMeetSdkManager

@HiltAndroidApp
class MainApplication:Application() {

    override fun onCreate() {
        super.onCreate()
       setTheme(R.style.Theme_Demo)

        JioMeetSdkManager.instance?.initialize(this, true, JioMeetSdkManager.ENVIRONMENT.PROD)
    }


}