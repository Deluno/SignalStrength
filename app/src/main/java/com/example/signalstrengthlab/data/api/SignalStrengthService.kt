package com.example.signalstrengthlab.data.api

import com.example.signalstrengthlab.data.model.Measurement
import com.example.signalstrengthlab.data.model.Strength
import com.example.signalstrengthlab.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface SignalStrengthService {

    // Strengths

    @GET("strength")
    suspend fun getAllStrengths(): Response<List<Strength>>

    // Measurements

    @GET("measurement")
    suspend fun getAllMeasurements(): Response<List<Measurement>>

    // Users

    @GET("user")
    suspend fun getAllUsers(): Response<List<User>>

    @POST("user")
    suspend fun postUser(@Body user: User): Response<User>

    @PUT("user")
    suspend fun updateUser(@Query("mac") mac: String, @Body user: User): Response<User>

    @DELETE("user")
    suspend fun deleteUser(@Query("mac") mac: String): Response<Unit>
}