package com.example.kotlinmusic.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinmusic.data.entities.FavoriteTrack
import kotlinx.coroutines.flow.Flow

/*
Interface to define the available methods to interact with RoomDB
 */
@Dao
interface FavoriteTrackDAO {
    @Query("SELECT * FROM favoritetrack")
    fun getAllFavorites(): Flow<List<FavoriteTrack>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg favoriteTrack: FavoriteTrack)

    @Delete
    fun delete(favoriteTrack: FavoriteTrack)

    @Query("SELECT EXISTS(SELECT 1 FROM favoritetrack WHERE uid = :uid LIMIT 1)")
    suspend fun isAlreadyInFavorite(uid: Long): Boolean
}
