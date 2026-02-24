package com.secta9ine.didapp.di

import android.content.Context
import androidx.room.Room
import com.secta9ine.didapp.data.local.DidDao
import com.secta9ine.didapp.data.local.DidDatabase
import com.secta9ine.didapp.data.remote.DidApi
import com.secta9ine.didapp.v2.data.local.V2SnapshotDao
import com.secta9ine.didapp.v2.data.remote.V2PlayerApi
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
//    private const val DEV_API_BASE_URL = "http://10.0.2.2:8080/api/"
    private const val DEV_API_BASE_URL = "http://10.212.44.212:8080/api/"

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
    fun provideV2SnapshotDao(database: DidDatabase): V2SnapshotDao {
        return database.v2SnapshotDao()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideDidApi(): DidApi {
        return Retrofit.Builder()
            .baseUrl(DEV_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DidApi::class.java)
    }

    @Provides
    @Singleton
    fun provideV2PlayerApi(okHttpClient: OkHttpClient, gson: Gson): V2PlayerApi {
        return Retrofit.Builder()
            .baseUrl(DEV_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(V2PlayerApi::class.java)
    }
}
