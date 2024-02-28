package com.example.kotlinmusic.data.repository

import com.example.kotlinmusic.data.entities.FavoriteTrack
import kotlinx.coroutines.flow.Flow
/*
Defining available methods to handle database transactions for favorite tracks
 */
interface IFavoriteTrackRepository {
    suspend fun insertFavoriteTrack(favoriteTrack: FavoriteTrack)
    fun getAllFavorites(): Flow<List<FavoriteTrack>>
    fun deleteFavoriteTrack(favoriteTrack: FavoriteTrack)
}
