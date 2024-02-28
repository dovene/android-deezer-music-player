package com.example.kotlinmusic.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmusic.data.entities.Data
import com.example.kotlinmusic.data.entities.FavoriteTrack
import com.example.kotlinmusic.data.repository.IFavoriteTrackRepository
import com.example.kotlinmusic.network.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
/*
MusicSearchViewModel is responsible for handling music search functionality
It is managing data related to music search results from the API calls
*/
class MusicSearchViewModel(private val apiInterface: ApiInterface, private val favoriteTrackRepository: IFavoriteTrackRepository) : ViewModel() {
    private val _musicData = MutableLiveData<List<Data>?>(null)
    val musicData: LiveData<List<Data>?> = _musicData


    fun fetchMusicData(query: String) {
        _musicData.postValue(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiInterface.getData(query)
                _musicData.postValue(response.data)
            } catch (e: Exception) {
                Log.e("Artist Search", "Error: ", e)
                _musicData.postValue(emptyList())
            }
        }
    }

    fun addToFavorites(data: Data, onAdded: (Data) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteTrack = FavoriteTrack(
                uid = data.id,
                title = data.title,
                artist = data.artist.name,
                coverUrl = data.album.cover,
                preview = data.preview
            )
            favoriteTrackRepository.insertFavoriteTrack(favoriteTrack)
            onAdded(data)
        }
    }
}

