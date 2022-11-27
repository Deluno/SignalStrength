package com.example.signalstrengthlab.ui.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signalstrengthlab.common.Resource
import com.example.signalstrengthlab.data.model.User
import com.example.signalstrengthlab.data.repository.SignalStrengthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: SignalStrengthRepository
) : ViewModel() {

    private val _users: MutableStateFlow<Resource<List<User>>> =
        MutableStateFlow(Resource.Loading())
    val users: StateFlow<Resource<List<User>>>
        get() = _users.asStateFlow()

    init {
        getUsers()
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(
                user = user,
                localDataSourceCall = { users -> _users.emit(users) },
                remoteDataSourceCall = { _users.emit(Resource.Loading()) }
            )
        }
    }

    fun updateUser(mac: String, user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(
                mac = mac,
                user = user,
                localDataSourceCall = { users -> _users.emit(users) },
                remoteDataSourceCall = { _users.emit(Resource.Loading()) }
            )
        }
    }

    fun deleteUser(mac: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(
                mac = mac,
                localDataSourceCall = { users ->
                    Log.d("deleteUserLocal", "$users")
                    _users.emit(users)
                                      },
                remoteDataSourceCall = {
                    Log.d("deleteUserRemote", "${it.message}")
                    _users.emit(Resource.Loading())
                }
            )
        }
    }

    private fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUsers(
                localDataSourceCall = { users -> _users.emit(users) },
                remoteDataSourceCall = { users -> _users.emit(users) }
            )
        }
    }
}