package com.secta9ine.didapp.di

import android.content.Context
import androidx.room.Room
import com.secta9ine.didapp.data.local.DidDao
import com.secta9ine.didapp.data.local.DidDatabase
import com.secta9ine.didapp.data.remote.DidApi
import com.secta9ine.didapp.system.FileLogger
import com.secta9ine.didapp.system.PowerScheduleManager
import com.secta9ine.didapp.system.TokenManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val API_BASE_URL = "http://10.120.44.88:14000/api/v1/"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DidDatabase {
        return Room.databaseBuilder(
            context,
            DidDatabase::class.java,
            "did_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideDidDao(database: DidDatabase): DidDao {
        return database.didDao()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: TokenManager, logger: FileLogger): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val req = chain.request()
                val token = tokenManager.getAccessToken()
                val newReq = if (token != null) {
                    req.newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    req
                }

                logger.d("HTTP", "→ ${newReq.method} ${newReq.url}")

                val response = chain.proceed(newReq)
                val responseBody = response.body
                val bodyString = responseBody?.source()?.let { source ->
                    source.request(Long.MAX_VALUE)
                    source.buffer.clone().readString(Charsets.UTF_8)
                }

                logger.d("HTTP", "← ${response.code} ${newReq.method} ${newReq.url} body=$bodyString")

                response
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideDidApi(okHttpClient: OkHttpClient): DidApi {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DidApi::class.java)
    }

    @Provides
    @Singleton
    fun providePowerScheduleManager(@ApplicationContext context: Context): PowerScheduleManager {
        return PowerScheduleManager(context)
    }

    @Provides
    @Singleton
    fun provideFileLogger(@ApplicationContext context: Context): FileLogger {
        return FileLogger(context)
    }
}
