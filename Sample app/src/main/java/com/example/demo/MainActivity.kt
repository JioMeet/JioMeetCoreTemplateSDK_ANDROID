package com.example.demo

import android.app.UiModeManager
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.demo.view.ui.CoreLoginView
import com.example.demo.viewModel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.jio.sdk.sdkmanager.JioMeetSdkManager

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        if (uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        setContent {
            CoreLoginView(onJoinMeetingClick = {
                val joinCallIntent = Intent(this, JoinRoomActivity::class.java)
                joinCallIntent.putExtra(
                    JioMeetSdkManager.MEETING_ID,
                    viewModel.loginState.value.meetingID
                )
                joinCallIntent.putExtra(
                    JioMeetSdkManager.MEETING_PIN,
                    viewModel.loginState.value.meetingPin
                )
                joinCallIntent.putExtra(
                    JioMeetSdkManager.GUEST_NAME,
                    viewModel.loginState.value.userName
                )
                startActivity(joinCallIntent)
            }, viewModel)

        }


    }
}
