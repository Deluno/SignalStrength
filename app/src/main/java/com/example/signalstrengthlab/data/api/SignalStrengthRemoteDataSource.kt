package com.example.signalstrengthlab.data.api

import com.example.signalstrengthlab.common.BaseDataSource
import com.example.signalstrengthlab.data.model.User
import javax.inject.Inject

class SignalStrengthRemoteDataSource @Inject constructor(
    private val signalStrengthService: SignalStrengthService
) : BaseDataSource() {
    suspend fun getStrengths() = safeApiCall {
        signalStrengthService.getAllStrengths()
    }

    suspend fun getMeasurements() = safeApiCall {
        signalStrengthService.getAllMeasurements()
    }

    suspend fun getUsers() = safeApiCall {
        signalStrengthService.getAllUsers()
    }

    suspend fun addUser(user: User) = safeApiCall {
        signalStrengthService.postUser(user)
    }

    suspend fun updateUser(mac: String, user: User) = safeApiCall {
        signalStrengthService.updateUser(mac, user)
    }

    suspend fun deleteUser(mac: String) = safeApiCall {
        signalStrengthService.deleteUser(mac)
    }
}