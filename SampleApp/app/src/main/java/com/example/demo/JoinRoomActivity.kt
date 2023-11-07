package com.example.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jiomeet.core.constant.Constant
import com.jiomeet.core.main.models.JMJoinMeetingConfig
import com.jiomeet.core.main.models.JMJoinMeetingData
import com.jiomeet.core.main.models.JMMeetingUser
import com.jiomeet.core.main.models.Speaker
import com.jiomeet.core.utils.BaseUrl
import dagger.hilt.android.AndroidEntryPoint
import org.jio.sdk.sdkmanager.JioMeetConnectionListener
import org.jio.sdk.sdkmanager.JioMeetSdkManager
import org.jio.sdk.templates.core.LaunchCore

@AndroidEntryPoint
class JoinRoomActivity : ComponentActivity() {

    companion object {
        const val TAG = "RoomActivity"
    }

    private val jmJoinMeetingConfig = JMJoinMeetingConfig(
        userRole = Speaker,
        isInitialAudioOn = true,
        isInitialVideoOn = true,
        isShareScreen = false,
        isShareWhiteBoard = false
    )

    private val jioMeetConnectionListener = object : JioMeetConnectionListener {
        override fun onLeaveMeeting() {
            finish()
        }

        override fun onRemoveRemoteParticipant(jmMeetingUser: JMMeetingUser) {

        }

        override fun onShareInviteClicked(meetingId: String, meetingPin: String, name: String) {}
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseUrl.initializedNetworkInformation(Constant.Environment.PROD)
        joinVideoCall()
    }

    private fun joinVideoCall() {
        setContent {
            val jmJoinMeetingData = JMJoinMeetingData(
                meetingId = intent.getStringExtra(JioMeetSdkManager.MEETING_ID) ?: "",
                meetingPin = intent.getStringExtra(JioMeetSdkManager.MEETING_PIN) ?: "",
                displayName = intent.getStringExtra(JioMeetSdkManager.GUEST_NAME) ?: "",
                version = "",
                deviceId = ""
            )
            LaunchCore(
                intent = intent,
                jioMeetConnectionListener = jioMeetConnectionListener,
                jmJionMeetingConfig = jmJoinMeetingConfig,
                jmJoinMeetingData = jmJoinMeetingData
            )
        }
    }
}
