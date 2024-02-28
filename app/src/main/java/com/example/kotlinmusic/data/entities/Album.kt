package com.example.kotlinmusic.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
Entity matching the object Album from a Json Response of the Deezer Rapid API
 */

data class Album(
    val cover: String,
    val coverBig: String,
    val coverMedium: String,
    val coverSmall: String,
    val coverXl: String,
    val id: Int,
    val md5Image: String,
    val title: String,
    val trackList: String,
    val type: String
)