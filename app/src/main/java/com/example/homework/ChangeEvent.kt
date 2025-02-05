package com.example.homework

sealed interface ChangeEvent {
    data class SetUserName(val userName: String): ChangeEvent
    data class SetProfilePic(val profilePic: String): ChangeEvent
}
