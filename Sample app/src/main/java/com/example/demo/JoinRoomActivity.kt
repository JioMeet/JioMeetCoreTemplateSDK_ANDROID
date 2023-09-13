package com.example.demo


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.demo.viewModel.AppViewModel
import com.jiomeet.core.main.models.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jio.sdk.common.customview.CustomView
import org.jio.sdk.sdkmanager.JioMeetConnectionListener
import org.jio.sdk.sdkmanager.JioMeetSdkManager
import org.jio.sdk.templates.core.LaunchCore


@AndroidEntryPoint
class JoinRoomActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()
    private var isUserJoined: Boolean = false



    companion object {
        const val TAG = "RoomActivity"
    }


    private val jioMeetConnectionListener = object : JioMeetConnectionListener {
        override fun closeWatchParty() {
            finish()
        }

        override fun onShareInviteClicked(meetingId: String, meetingPin: String, name: String) {}


    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        joinVideoCall()

    }


    private fun joinVideoCall() {
        if (HelperClass.checkPermissionForCorporateUsers(this)) {

                setContent {
                    LaunchCore(
                        intent = intent,
                        jioMeetConnectionListener = jioMeetConnectionListener
                    )
                }

        } else {
            HelperClass.requestPermissionForCorporateUsers(this)
        }
        lifecycleScope.launch {

                        setContent {
                            val joinCallIntent = Intent()
                            joinCallIntent.putExtra(
                                JioMeetSdkManager.MEETING_ID,
                                viewModel.loginState.value.meetingID
                            )
                            joinCallIntent.putExtra(
                                JioMeetSdkManager.MEETING_PIN,
                                viewModel.loginState.value.meetingPin
                            )
                            joinCallIntent.putExtra(JioMeetSdkManager.GUEST_NAME,
                              viewModel.loginState.value.userName
                                )

                            isUserJoined = true
                            LaunchCore(
                                intent = joinCallIntent,
                                jioMeetConnectionListener = jioMeetConnectionListener
                            )

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            setContent {
                LaunchCore(
                    intent = intent,
                    jioMeetConnectionListener = jioMeetConnectionListener
                )
            }
        } else {
            CustomView.ShowShortToast(applicationContext, " Provide permissions to proceed.")
        }
    }
}
