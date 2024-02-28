package com.example.kotlinmusic.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmusic.data.entities.Data
import com.example.kotlinmusic.data.entities.FavoriteTrack
import com.example.kotlinmusic.data.repository.IFavoriteTrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
FavoriteTrackViewModel is responsible for managing data of the favorite tracks from RoomBD
*/
class FavoritesViewModel(
    val favoriteTrackRepository: IFavoriteTrackRepository
) : ViewModel() {
    val allFavorites: LiveData<List<FavoriteTrack>> = favoriteTrackRepository
        .getAllFavorites()
        .asLiveData()

    fun deleteFavorite(favoriteTrack: FavoriteTrack, onTrackDeleted: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteTrackRepository.deleteFavoriteTrack(favoriteTrack)
            onTrackDeleted()
        }
    }
}
