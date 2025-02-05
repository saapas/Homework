package com.example.homework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModel(
    private val dao: UserDao
): ViewModel() {
    private val _state = MutableStateFlow(ChangeState())
    val state: StateFlow<ChangeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val user = dao.getUser() // Fetch latest user
            user?.let {
                _state.value = ChangeState(
                    userName = it.userName,
                    profilePic = it.profilePic
                )
            }
        }
    }

    fun onEvent(event: ChangeEvent) {
        when (event) {
            is ChangeEvent.SetUserName -> {
                _state.update { it.copy(userName = event.userName) }
                viewModelScope.launch {
                    dao.upsert(User(1, event.userName, _state.value.profilePic))
                }
            }
            is ChangeEvent.SetProfilePic -> {
                _state.update { it.copy(profilePic = event.profilePic) }
                viewModelScope.launch {
                    dao.upsert(User(1, _state.value.userName, event.profilePic))
                }
            }
        }
    }
}
