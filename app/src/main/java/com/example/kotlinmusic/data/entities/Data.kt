package com.example.kotlinmusic.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/*
Entity matching the object Data from a Json Response of the Deezer Rapid API
 */

data class Data(
    val album: Album,
    val artist: Artist,
    val duration: Int,
    val explicitContentCover: Int,
    val explicitContentLyrics: Int,
    val explicitLyrics: Boolean,
    val id: Long,
    val link: String,
    val md5Image: String,
    val preview: String,
    val rank: Int,
    val readable: Boolean,
    val title: String,
    val titleShort: String,
    val titleVersion: String,
    val type: String
)