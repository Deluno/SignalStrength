package com.example.signalstrengthlab.di

import android.content.Context
import androidx.room.Room
import com.example.signalstrengthlab.data.api.SignalStrengthRemoteDataSource
import com.example.signalstrengthlab.data.api.SignalStrengthService
import com.example.signalstrengthlab.data.db.SignalStrengthDatabase
import com.example.signalstrengthlab.data.repository.SignalStrengthRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModules {

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .baseUrl("https://signal-strength-api.herokuapp.com/api/")
            .build()

    @Provides
    fun provideSignalStrengthService(
        retrofit: Retrofit
    ): SignalStrengthService = retrofit.create(SignalStrengthService::class.java)

    @Singleton
    @Provides
    fun provideSignalStrengthDb(@ApplicationContext appContext: Context): SignalStrengthDatabase {
        return Room.databaseBuilder(
            appContext,
            SignalStrengthDatabase::class.java,
            "signal_strength_local_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideSignalStrengthRepository(
        remoteDataSource: SignalStrengthRemoteDataSource,
        localDataSource: SignalStrengthDatabase,
    ): SignalStrengthRepository = SignalStrengthRepository(
        remoteDataSource, localDataSource
    )
}