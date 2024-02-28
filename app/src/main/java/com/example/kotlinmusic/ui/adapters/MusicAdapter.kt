package com.example.kotlinmusic.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmusic.data.entities.Data
import com.example.kotlinmusic.R
import com.squareup.picasso.Picasso

/*
Music adapter is to access data items from the HomeView
Here, we define behaviors that can happen in the view, such as playing music
or adding a track to the favorites section of the application
*/
class MusicAdapter(
    private val context: Context,
    private val data: List<Data>,
    private val onFavoriteClicked: (Data) -> Unit,
    private val onItemClicked: (Data) -> Unit
) : RecyclerView.Adapter<MusicAdapter.MusicItemViewHolder>() {

    // Keep track of the currently playing MediaPlayer instance
    private var currentMediaPlayer: MediaPlayer? = null
    private var currentPlayingPosition: Int = -1

    class MusicItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var albumCover: ImageView = itemView.findViewById(R.id.albumCover)
        val musicTitle: TextView = itemView.findViewById(R.id.musicTitle)
        val playButton: ImageButton = itemView.findViewById(R.id.btnPlay)
        val pauseButton: ImageButton = itemView.findViewById(R.id.btnPause)
        val favButton: ImageButton = itemView.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicItemViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false)
        return MusicItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MusicItemViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentData = data[position]
        holder.musicTitle.text = currentData.title
        Picasso.get().load(currentData.album.cover).into(holder.albumCover)

        // Set click listeners
        holder.albumCover.setOnClickListener {
            onItemClicked(currentData)
        }

        holder.playButton.setOnClickListener {
            // Check if another track is playing
            if (currentPlayingPosition != position) {
                stopPlayer()
            }

            // Start playback
            val mediaPlayer = MediaPlayer.create(context, currentData.preview.toUri())
            mediaPlayer.start()
            currentMediaPlayer = mediaPlayer
            currentPlayingPosition = position
        }

        holder.pauseButton.setOnClickListener {
            // Stop playback
            stopPlayer(resetPosition = true)
        }

        holder.favButton.setOnClickListener {
            onFavoriteClicked(currentData)
        }
    }

    override fun onViewRecycled(holder: MusicItemViewHolder) {
        super.onViewRecycled(holder)
        // Release MediaPlayer when the item is recycled
        if (holder.adapterPosition == currentPlayingPosition) {
            stopPlayer(resetPosition = true)
        }
    }

    private fun stopPlayer(resetPosition: Boolean = false){
        currentMediaPlayer?.stop()
        currentMediaPlayer?.release()
        currentMediaPlayer = null
        if (resetPosition) {
            currentPlayingPosition = -1
        }
    }
}

