package com.example.kotlinmusic.ui.activities

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.kotlinmusic.data.entities.DetailData
import com.example.kotlinmusic.databinding.MusicDetailActivityBinding
import com.squareup.picasso.Picasso

class MusicDetailActivity: AppCompatActivity() {
    private var currentMediaPlayer: MediaPlayer? = null

    private lateinit var binding: MusicDetailActivityBinding
    private lateinit var data: DetailData

    companion object {
        const val dataExtra = "dataExtra"
        fun newIntent(context: Context?, data: DetailData): Intent{
            val intent = Intent(context, MusicDetailActivity::class.java)
            intent.putExtra(dataExtra, data)
            return intent
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MusicDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        displayItem()
    }

    private fun displayItem() {
        intent.extras?.getParcelable<DetailData>(dataExtra)?.let {
            data = it
            Picasso.get().load(it.cover).into(binding.albumCover)
            binding.musicTitle.text = it.title
        }


        binding.btnPlay.setOnClickListener {
            // Start playback
            val mediaPlayer = MediaPlayer.create(this, data.preview?.toUri())
            mediaPlayer.start()
            currentMediaPlayer = mediaPlayer
        }

        binding.btnPause.setOnClickListener {
            // Stop playback
            stopPlayer()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        stopPlayer()
    }

    private fun stopPlayer(){
        currentMediaPlayer?.stop()
        currentMediaPlayer?.release()
        currentMediaPlayer = null
    }

}
