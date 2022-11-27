package com.example.signalstrengthlab.ui.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signalstrengthlab.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {

    var userReference by mutableStateOf(User())
        private set

    var userState by mutableStateOf(User())
        private set

    var isEditUser by mutableStateOf(false)
        private set

    fun changeUserState(user: User) {
        userState = user
    }

    fun switchToEditUserState(user: User) {
        userReference = user
        userState = user
        isEditUser = true
    }

    fun switchToAddUserState() = resetState()

    fun resetState() {
        userState = User()
        userReference = User()
        isEditUser = false
    }
}