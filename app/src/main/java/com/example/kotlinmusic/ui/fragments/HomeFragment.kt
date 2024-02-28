package com.example.kotlinmusic.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinmusic.data.entities.Data
import com.example.kotlinmusic.data.entities.DetailData
import com.example.kotlinmusic.databinding.HomeFragmentBinding
import com.example.kotlinmusic.ui.activities.MusicDetailActivity
import com.example.kotlinmusic.ui.adapters.MusicAdapter
import com.example.kotlinmusic.ui.viewmodels.MusicSearchViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
/*
HomeFragment is responsible for displaying music search results from the API calls.
*/
class HomeFragment : Fragment() {

    private val viewModel: MusicSearchViewModel by viewModel()
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        setupSearchView()
        observeViewModel()
        return binding.root
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.fetchMusicData(it)
                    startShimmerEffect()
                    //close
                    closeKeyBoard();
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    private fun closeKeyBoard() {
        val inputMethodManager =
           context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
    }

    /*
    There basically should be a shimmering effect replacing the music item when its loading
    But due to lack of time during the development phase, it hasn't been correctly implemented.
    Refer to functions line 83 and 89 of this file
     */
    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        viewModel.musicData.observe(viewLifecycleOwner) { musicData ->
            if (musicData == null) {
                binding.shimmerViewContainer.visibility = View.GONE
                binding.tvEmptyMessage.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.tvEmptyMessage.text = "Use the search bar to lookup for an artist discography!"
            } else if (musicData.isNotEmpty()) {
                stopShimmerEffect()
                binding.recyclerView.visibility = View.VISIBLE
                binding.tvEmptyMessage.visibility = View.GONE
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = MusicAdapter(requireContext(), musicData,
                { dataItem ->
                    viewModel.addToFavorites(dataItem) {
                        activity?.runOnUiThread {
                            Snackbar.make(binding.root,
                                "${it.title} added to favorites", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }, {
                    displayDetail(it)
                    })
            } else {
                stopShimmerEffect()
                binding.recyclerView.visibility = View.GONE
                binding.tvEmptyMessage.visibility = View.VISIBLE
                binding.tvEmptyMessage.text = "No results found. Try a different search."
            }
        }
    }

    private fun displayDetail(data: Data) {
        val detailData = DetailData(cover = data.album.cover, preview = data.preview, title = data.title)
        startActivity(MusicDetailActivity.newIntent(context, detailData))
        closeKeyBoard()
    }

    private fun startShimmerEffect() {
        binding.shimmerViewContainer.startShimmer()
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun stopShimmerEffect() {
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
