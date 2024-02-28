package com.example.kotlinmusic.network

import com.example.kotlinmusic.util.Constants
import com.example.kotlinmusic.data.entities.MusicData
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
/*
Defining the query we need to search tracks for an Artiste using the Deezer Rapid API
 */
interface ApiInterface {
    @Headers("X-RapidAPI-Key: ${Constants.API_KEY}", "X-RapidAPI-Host: ${Constants.API_HOST}")
    @GET("search")
    suspend fun getData(@Query("q") queryString: String): MusicData
}
