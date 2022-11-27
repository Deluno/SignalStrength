package com.example.signalstrengthlab.data.repository

import com.example.signalstrengthlab.common.Resource
import com.example.signalstrengthlab.data.api.SignalStrengthRemoteDataSource
import com.example.signalstrengthlab.data.db.SignalStrengthDatabase
import com.example.signalstrengthlab.data.model.Measurement
import com.example.signalstrengthlab.data.model.User
import com.example.signalstrengthlab.utils.dbPriorityOperationFactory
import com.example.signalstrengthlab.utils.networkPriorityOperationFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignalStrengthRepository @Inject constructor(
    private val remoteDataSource: SignalStrengthRemoteDataSource,
    private val localDataSource: SignalStrengthDatabase
) {
    suspend fun getMeasurements(
        localDataSourceCall: suspend (measurements: Resource<List<Measurement>>) -> Unit = {},
        remoteDataSourceCall: suspend (measurements: Resource<List<Measurement>>) -> Unit = {}
    ) = dbPriorityOperationFactory(
        databaseQuery = { localDataSource.measurementDao.getMeasurements() },
        networkCall = { remoteDataSource.getMeasurements() },
        onSuccess = { localDataSource.measurementDao.insertAll(it) }
    ).invoke(localDataSourceCall, remoteDataSourceCall)

    suspend fun getUsers(
        localDataSourceCall: suspend (users: Resource<List<User>>) -> Unit = {},
        remoteDataSourceCall: suspend (users: Resource<List<User>>) -> Unit = {}
    ) = dbPriorityOperationFactory(
        databaseQuery = { localDataSource.userDao.getUsers() },
        networkCall = { remoteDataSource.getUsers() },
        onSuccess = {
            localDataSource.userDao.deleteAll()
            localDataSource.userDao.insertAll(it)
        }
    ).invoke(localDataSourceCall, remoteDataSourceCall)

    suspend fun addUser(
        user: User,
        localDataSourceCall: suspend (user: Resource<List<User>>) -> Unit = {},
        remoteDataSourceCall: suspend (users: Resource<User>) -> Unit = {},
    ) = networkPriorityOperationFactory(
        networkCall = { remoteDataSource.addUser(user) },
        databaseQuery = { localDataSource.userDao.getUsers() },
        onSuccess = { response ->
            response?.let { localDataSource.userDao.insert(it) }
        },
    ).invoke(localDataSourceCall, remoteDataSourceCall)

    suspend fun updateUser(
        mac: String,
        user: User,
        localDataSourceCall: suspend (user: Resource<List<User>>) -> Unit = {},
        remoteDataSourceCall: suspend (users: Resource<User>) -> Unit = {},
    ) = networkPriorityOperationFactory(
        networkCall = { remoteDataSource.updateUser(mac, user) },
        onSuccess = { response ->
            localDataSource.userDao.deleteUser(mac)
            response?.let { localDataSource.userDao.insert(it) }
        },
        databaseQuery = { localDataSource.userDao.getUsers() },
    ).invoke(localDataSourceCall, remoteDataSourceCall)

    suspend fun deleteUser(
        mac: String,
        localDataSourceCall: suspend (user: Resource<List<User>>) -> Unit = {},
        remoteDataSourceCall: suspend (response: Resource<Unit>) -> Unit = {},
    ) = networkPriorityOperationFactory(
        networkCall = { remoteDataSource.deleteUser(mac) },
        onSuccess = { localDataSource.userDao.deleteUser(mac) },
        databaseQuery = { localDataSource.userDao.getUsers() }
    ).invoke(localDataSourceCall, remoteDataSourceCall)
}