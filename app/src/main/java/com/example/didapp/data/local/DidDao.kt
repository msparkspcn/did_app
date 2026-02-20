package com.example.didapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DidDao {
    @Query("SELECT * FROM did_items ORDER BY timestamp ASC")
    fun getAllDidItems(): Flow<List<DidEntity>>

    @Query("SELECT * FROM did_items")
    suspend fun getAllDidItemsSync(): List<DidEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<DidEntity>)

    @Query("UPDATE did_items SET localPath = :localPath WHERE id = :id")
    suspend fun updateLocalPath(id: String, localPath: String)

    @Query("DELETE FROM did_items")
    suspend fun clearAll()
}
