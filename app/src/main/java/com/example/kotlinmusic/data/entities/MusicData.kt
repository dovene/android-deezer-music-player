package com.example.kotlinmusic.data.entities

/*
Entity to define the root of a GET /search json response of the Deezer Rapid API
 */
data class MusicData(
    val `data`: List<Data>,
    val next: String,
    val total: Int
)