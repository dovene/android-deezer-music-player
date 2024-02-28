package com.example.kotlinmusic.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailData (val cover: String?, val title: String?, val preview: String?) : Parcelable