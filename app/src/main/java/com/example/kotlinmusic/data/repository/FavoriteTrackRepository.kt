package com.example.kotlinmusic.data.repository

import android.util.Log
import com.example.kotlinmusic.data.dao.FavoriteTrackDAO
import com.example.kotlinmusic.data.entities.FavoriteTrack
import kotlinx.coroutines.flow.Flow
/*
Repository to call methods from the DAO to manipulate data in UI
 */
class FavoriteTrackRepository(private val favoriteTrackDAO: FavoriteTrackDAO) : IFavoriteTrackRepository {
    override fun getAllFavorites(): Flow<List<FavoriteTrack>> = favoriteTrackDAO.getAllFavorites()

    override fun deleteFavoriteTrack(favoriteTrack: FavoriteTrack) {
        favoriteTrackDAO.delete(favoriteTrack)
    }

    override suspend fun insertFavoriteTrack(favoriteTrack: FavoriteTrack) {
        if (!favoriteTrackDAO.isAlreadyInFavorite(favoriteTrack.uid)) {
            favoriteTrackDAO.insertAll(favoriteTrack)
        } else {
            Log.d("Repository", "Track is already in favorites.")
        }
    }
}
