package com.example.didapp.di

import android.content.Context
import androidx.room.Room
import com.example.didapp.data.local.DidDao
import com.example.didapp.data.local.DidDatabase
import com.example.didapp.data.remote.DidApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DidDatabase {
        return Room.databaseBuilder(
            context,
            DidDatabase::class.java,
            "did_database"
        ).build()
    }

    @Provides
    fun provideDidDao(database: DidDatabase): DidDao {
        return database.didDao()
    }

    @Provides
    @Singleton
    fun provideDidApi(): DidApi {
        return Retrofit.Builder()
            .baseUrl("https://example.com/api/") // Placeholder URL for initial development. This will cause 404 until real backend is ready.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DidApi::class.java)
    }
}
