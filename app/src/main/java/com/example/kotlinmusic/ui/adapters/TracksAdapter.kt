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
import com.example.kotlinmusic.R
import com.example.kotlinmusic.data.entities.Data
import com.example.kotlinmusic.data.entities.FavoriteTrack
import com.squareup.picasso.Picasso

/*
Music adapter is to handle interactions with the FavoriteTrackViewModel
*/
class TracksAdapter(
    private val context: Context,
    private var data: List<FavoriteTrack>,
    private val addToFavorites: (FavoriteTrack) -> Unit,
    private val onItemClicked: (FavoriteTrack) -> Unit
) : RecyclerView.Adapter<TracksAdapter.TrackViewHolder>() {

    // Keep track of the currently playing MediaPlayer instance
    private var currentMediaPlayer: MediaPlayer? = null
    private var currentPlayingPosition: Int = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = data[position]
        holder.bind(item)

        holder.itemView.findViewById<ImageView>(R.id.albumCover)?.setOnClickListener {
            onItemClicked(item)
        }

        holder.itemView.findViewById<ImageButton>(R.id.btnFavorite)?.setOnClickListener {
            addToFavorites(item)
        }

        holder.itemView.findViewById<ImageButton>(R.id.btnPlay)?.setOnClickListener {
            // Check if another track is playing
            if (currentPlayingPosition != position) {
                stopPlayer()
            }

            // Start playback
            val mediaPlayer = MediaPlayer.create(context, item.preview?.toUri())
            mediaPlayer.start()
            currentMediaPlayer = mediaPlayer
            currentPlayingPosition = position
        }

        holder.itemView.findViewById<ImageButton>(R.id.btnPause)?.setOnClickListener {
            // Stop playback
            stopPlayer(resetPosition = true)
        }
    }


    override fun onViewRecycled(holder: TrackViewHolder) {
        super.onViewRecycled(holder)
        if (holder.adapterPosition == currentPlayingPosition) {
            stopPlayer(resetPosition = true)
        }
    }

    override fun getItemCount() = data.size
    private fun stopPlayer(resetPosition: Boolean = false){
        currentMediaPlayer?.stop()
        currentMediaPlayer?.release()
        currentMediaPlayer = null
        if (resetPosition) {
            currentPlayingPosition = -1
        }
    }

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.musicTitle)
        private val cover: ImageView = itemView.findViewById(R.id.albumCover)

        fun bind(track: FavoriteTrack) {
            title.text = track.title
            Picasso.get().load(track.coverUrl).into(cover)
        }
    }
}
