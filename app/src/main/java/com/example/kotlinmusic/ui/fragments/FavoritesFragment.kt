package com.example.kotlinmusic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinmusic.data.entities.DetailData
import com.example.kotlinmusic.data.entities.FavoriteTrack
import com.example.kotlinmusic.databinding.FavoritesFragmentBinding
import com.example.kotlinmusic.ui.activities.MusicDetailActivity
import com.example.kotlinmusic.ui.adapters.TracksAdapter
import com.example.kotlinmusic.ui.viewmodels.FavoritesViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
FavoritesFragment is responsible for displaying favorite tracks coming from the RoomDB
*/
class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModel()
    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.allFavorites.observe(viewLifecycleOwner) { favorites ->
            binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewFavorites.adapter = TracksAdapter(requireContext(), favorites, {
                deleteFavorite(it)
            }, {
                displayDetail(it)
            })
        }
    }

    private fun deleteFavorite(favoriteTrack: FavoriteTrack) {
        viewModel.deleteFavorite(favoriteTrack) {
            activity?.runOnUiThread {
                Snackbar.make(binding.root,
                    "${favoriteTrack.title} removed from favorites", Snackbar.LENGTH_SHORT).show()

            }
        }
    }

    private fun displayDetail(data: FavoriteTrack) {
        val detailData = DetailData(cover = data.coverUrl, preview = data.preview, title = data.title)
        startActivity(MusicDetailActivity.newIntent(context, detailData))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
