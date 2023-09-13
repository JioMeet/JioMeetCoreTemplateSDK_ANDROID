package com.example.demo.viewModel

sealed class CreateJmRoomState {

    data class Error(val errorMessage: String) : CreateJmRoomState()
    data class Success(val meetingID: String, val meetingPin: String,val hostToken:String) : CreateJmRoomState()

    data class Loading(val isLoading: Boolean) : CreateJmRoomState()

    object EnableWatchPartyButton : CreateJmRoomState()

}
