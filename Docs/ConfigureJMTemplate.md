## Configure JioMeet Template UI inside your app

i. **Step 1** : Generate a Personal Access Token for GitHub

- Settings -> Developer Settings -> Personal Access Tokens -> Generate new token
- Make sure you select the following scopes (“ read:packages”) and Generate a token
- After Generating make sure to copy your new personal access token. You cannot see it again! The only option is to generate a new key.

ii. Update build.gradle inside the application module

```kotlin
    repositories {
    maven {
        credentials {
            <!--github user name-->
                username = ""
            <!--github user token-->
                password = ""
        }
        url = uri("https://maven.pkg.github.com/JioMeet/JioMeetCoreTemplateSDK_ANDROID")
    }
    google()
    mavenCentral()
}
```

iii. In Gradle Scripts/build.gradle (Module: <projectname>) add the Template UI dependency. The dependencies section should look like the following:

```gradle
dependencies {
    ...
    implementation "com.jiomeet.platform:jiomeetcoretemplatesdk:<version>"
    ...
}
```

Find the [Latest version](https://github.com/JioMeet/JioMeetCoreTemplateSDK_ANDROID/releases) of the UI Kit and replace <version> with the one you want to use. For example: 2.1.8.

### Add permissions for network and device access.

In /app/Manifests/AndroidManifest.xml, add the following permissions after </application>:

```gradle
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- The SDK requires Bluetooth permissions in case users are using Bluetooth devices. -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<!-- For Android 12 and above devices, the following permission is also required. -->
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
```

To support Screen Share add following lines in Android Manifest

```gradle
    <service
            android:name="com.jiomeet.core.mediaEngine.agora.screenshare.impl.ScreenSharingService"
            android:exported="false"
            android:foregroundServiceType="mediaProjection"
            android:process=":screensharingsvc">
            <intent-filter>
                <action android:name="android.intent.action.screenshare" />
            </intent-filter>
        </service>

        <service
            android:name="org.jio.sdk.service.OnGoingScreenShareService"
            android:foregroundServiceType="mediaProjection"
            android:stopWithTask="false" />


    <activity
        android:name="com.jiomeet.core.mediaEngine.agora.screenshare.impl.ScreenCapture$ScreenCaptureAssistantActivity"
        android:autoRemoveFromRecents="true"
        android:excludeFromRecents="true"
        android:process=":screensharingsvc"
        android:screenOrientation="fullUser"
        android:theme="@android:style/Theme.Translucent" />
```

### Requesting run time permissions

it's crucial to request some permissions like **_CAMERA ,RECORD_AUDIO, READ_PHONE_STATE_** at runtime since these are critical device access permissins to ensure a seamless and secure user experience. Follow these steps

1. Check Permissions

```kotlin
if (checkPermissions()) {
    // Proceed with using the features.
} else {
    // Request critical permissions at runtime.
}
```

2. Request Runtime Permissions:

```kotlin
private void requestCriticalPermissions() {
    ActivityCompat.requestPermissions(this,
        new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        },
        PERMISSION_REQUEST_CODE);

}
```

3. Handle Permission Results

```kotlin
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == PERMISSION_REQUEST_CODE) {
        if (areAllPermissionsGranted(grantResults)) {
            // Proceed with using the features that require critical permissions.
        } else {
            // Handle denied permissions, especially for camera and phone state, which are essential.
        }
    }
}
```

### Initiliazing Hilt in Application Class

1. Create a Custom Application Class: If your users don't already have a custom Application class in their Android project, they should create one. This class will be used to initialize Hilt.

```kotlin
import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
class MyApplication : Application {
    // ...
}
```

2. Modify AndroidManifest.xml: In the AndroidManifest.xml file of their app, users should specify the custom Application class they created as the application name. This tells Android to use their custom Application class when the app starts.

```xml
<application
    android:name=".MyApplication" <!-- Specify the name of your custom Application class -->
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:theme="@style/AppTheme">
    <!-- ... -->
    </application>
```

### Start your App

In /app/java/com.example.<projectname>/MainActivity, add @AndroidEntryPoint to enable Hilt injection. Here's an example:

```kotlin
   import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // ...
}
```

update onCreate to run LaunchCore() when the app starts. The updated code should like the provided code sample:

```kotlin
    private val jioMeetConnectionListener = object : JioMeetConnectionListener {
    override fun onLeaveMeeting() {
        finish()
    }

    override fun onRemoteParticipantJoined(jmMeetingUser: JMMeetingUser) {
        super.onRemoteParticipantJoined(jmMeetingUser)
        Log.d("Listener onRemoteParticipantJoined", "UID: $jmMeetingUser")
    }

    override fun onRemoteUserLeftMeeting(jmMeetingUser: JMMeetingUser) {
        super.onRemoteUserLeftMeeting(jmMeetingUser)
        Log.d("Listener onRemoteUserLeftMeeting", "UID: $jmMeetingUser")
    }

    override fun onLocalJoinedRoom(jmMeetingUser: JMMeetingUser) {
        super.onLocalJoinedRoom(jmMeetingUser)
        Log.d("Listener onLocalJoinedRoom", "UID: 0 $jmMeetingUser")
    }

    override fun onLocalLeftRoom() {
        super.onLocalLeftRoom()
        Log.d("Listener onLocalLeftRoom", "onLocalLeftRoom")
    }

    override fun onLoudestParticipantIsLocalSDK(
        isLocalParticipant: Boolean,
        loudSpeaker: Int,
        listActiveParticipant: List<ActiveParticipant>,
        totalVolume: Int
    ) {
        super.onLoudestParticipantIsLocalSDK(
            isLocalParticipant,
            loudSpeaker,
            listActiveParticipant,
            totalVolume
        )
        Log.d("Listener onLoudestParticipantIsLocalSDK", "onLoudestParticipantIsLocalSDK")

    }

    override fun onShareInviteClicked(meetingId: String, meetingPin: String, name: String) {
        super.onShareInviteClicked(meetingId, meetingPin, name)
        Log.d("Listener onShareInviteClicked", "onShareInviteClicked")
    }

    override fun isUserSpeakingWhileMute(isUserSpeaking: Boolean) {
        super.isUserSpeakingWhileMute(isUserSpeaking)
        Log.d("Listener isUserSpeakingWhileMute", "isUserSpeakingWhileMute:$isUserSpeaking")
    }

    override fun onAnalyticsEvent(analyticsEvent: AnalyticsEvent) {
        super.onAnalyticsEvent(analyticsEvent)
        Log.d("Listener onAnalyticsEvent", "onAnalyticsEvent")
    }

    override fun onLocalAudioStateChange(isMicMuted: Boolean) {
        super.onLocalAudioStateChange(isMicMuted)
        Log.d("Listener onLocalAudioStateChange", "onLocalAudioStateChange: $isMicMuted")
    }

    override fun onLocalVideoStateChange(isCameraMutes: Boolean) {
        super.onLocalVideoStateChange(isCameraMutes)
        Log.d("Listener onLocalVideoStateChange", "onLocalVideoStateChange: $isCameraMutes")
    }

    override fun toggleScreenShare(isScreenShared: Boolean) {
        super.toggleScreenShare(isScreenShared)
        Log.d("Listener toggleScreenShare", "toggleScreenShare: $isScreenShared")
    }

    override fun onHostSwitchClientRole(isMovedToAudience: Boolean,jmMeetingUser: JMMeetingUser) {
        super.onHostSwitchClientRole(isMovedToAudience,jmMeetingUser)
        Log.d("Listener onHostSwitchClientRole", "onHostSwitchClientRole: $isMovedToAudience")
    }

    override fun onHostRemoveParticipant() {
        super.onHostRemoveParticipant()
        Log.d("Listener onHostRemoveParticipant", "onHostRemoveParticipant: ")
    }

    override fun onHandRaised(isHandRaised: Boolean,jmMeetingUser: JMMeetingUser) {
        super.onHandRaised(isHandRaised,jmMeetingUser)
        Log.d("Listener onHandRaised", "onHandRaised: ")
    }

    override fun onRemoveRemoteParticipant(jmMeetingUser: JMMeetingUser) {
        Log.d("Listener onRemoveRemoteParticipant", "onRemoveRemoteParticipant: $jmMeetingUser")
    }


    override fun onErrorFromSDK(error: String) {
        super.onErrorFromSDK(error)
        Log.d("Listener onErrorFromSDK", "onErrorFromSDK: ")
    }

    override fun onSwitchRemoteClientRole(
        isMovedToAudience: Boolean,
        jmMeetingUser: JMMeetingUser
    ) {
        super.onSwitchRemoteClientRole(isMovedToAudience, jmMeetingUser)
        Log.d(
            "Listener onSwitchRemoteClientRole",
            "onSwitchRemoteClientRole: $isMovedToAudience uid:$jmMeetingUser"
        )
    }

}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val joinMeetingData = JMJoinMeetingData(
        meetingId: "8591303436",
        meetingPin: "KkMK1",
        displayName: "John wick")

    val jmJoinMeetingConfig = JMJoinMeetingConfig(
        userRole = userRole, //  // Specify the user role using JMUserRole (e.g., Host("host-token"), Speaker, Audience)
        isInitialAudioOn = true,
        isInitialVideoOn = true
    )


    setContent {
        val joinCallIntent = Intent()
        joinCallIntent.putExtra(
            JioMeetSdkManager.MEETING_ID,
            "meetingID"
        )
        joinCallIntent.putExtra(
            JioMeetSdkManager.MEETING_PIN,
            "meetingPin"
        )
        joinCallIntent.putExtra(JioMeetSdkManager.GUEST_NAME, "guestName")
        joinCallIntent.putExtra(
            JioMeetSdkManager.HOST_TOKEN,
            "hostToken"
        )
        LaunchCore(
            intent = joinCallIntent,
            jioMeetConnectionListener = jioMeetConnectionListener,
            jmJoinMeetingConfig,
            jmJoinMeetingData
        )
    }
}
```

The JioMeetConnectionListener interface allows you to receive important events and callbacks related to a Jio-Meet session. You can implement this interface to handle various events that occur during a meeting, such as participants joining or leaving, errors, analytics events, and more. Below are the available callbacks, use can use these callbacks to implement custom behaviour

- **_onShareInviteClicked_**(meetingId: String, meetingPin: String, name: String)
  ```Kotlin
  override fun onShareInviteClicked(meetingId: String, meetingPin: String, name: String) {
      // Implement custom behavior for sharing meeting details
  }
  ```
- **_leaveMeeting_**()
  ```Kotlin
  override fun onLeaveMeeting() {
      finish() // Close the meeting
  }
  ```
- **_onAnalyticsEvent_**(analyticsEvent: AnalyticsEvent)
  ```Kotlin
  override fun onAnalyticsEvent(analyticsEvent: AnalyticsEvent) {
      // Log or track analytics event information
  }
  ```
- **_Other Callbacks_**
  Depending on your use case, there are additional callbacks available in the JioMeetConnectionListener interface for handling various aspects of the meeting, such as **_microphone and camera status_**, **_remote participant actions_**, and more.
  Please implement the necessary callbacks in your JioMeetConnectionListener implementation to customize the behavior of your JioMeet integration as per your application's requirements.
