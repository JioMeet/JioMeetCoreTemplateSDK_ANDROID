package com.example.demo.model

data class AppData(
    var appId: String = "",
    var appName: String = "",
    var roomName: String = "",
    var descriptionName: String = "",
    var secretKey: String = "",
    val isAppIDError: Boolean = false,
    val isAppNameError: Boolean = false,
    val isRoomNameError: Boolean = false,
    val isDescriptionError: Boolean = false,
    val isSecretKeyError: Boolean = false
)