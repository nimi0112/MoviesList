package com.example.newproject.presentation.fragment.home

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.newproject.R
import com.example.newproject.databinding.FragmentHomeBinding
import com.example.newproject.presentation.adapter.MoviesAdapter
import com.example.newproject.presentation.base.AbstractFragment
import com.example.newproject.presentation.base.BindFragment
import com.example.newproject.presentation.extensions.hideSoftKeyboard
import com.example.newproject.presentation.extensions.showToast
import com.example.newproject.presentation.extensions.textChanges
import com.example.newproject.presentation.extensions.visibleOrGone
import com.example.newproject.presentation.listeners.PaginationListener
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeFragment : AbstractFragment<HomeState, HomeViewModel>() {

    override val viewModel: HomeViewModel by fragmentViewModel()

    override val binding: FragmentHomeBinding by BindFragment(R.layout.fragment_home)

    private val moviesAdapter = MoviesAdapter(::goToNextScreen)
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    override fun setupView(isVeryFirstRun: Boolean): Unit = withState(viewModel) { state ->
        binding.run {
            rcvMovies.apply {
                adapter = moviesAdapter
                layoutManager = linearLayoutManager
                addOnScrollListener(object : PaginationListener(linearLayoutManager) {
                    override fun loadMoreItems() {
                        //hide keyboard when calling pagination
                        hideSoftKeyboard()
                        viewModel.getMoreData()
                    }

                    override fun isLastPage() = !state.hasMoreData

                    override fun isLoading() = state.isLoading || state.isPaginationLoading
                })
            }

            etMovieName.textChanges()
                .distinctUntilChanged()
                .debounce(300)
                .onEach {
                    viewModel.searchMovies(it.toString())
                }
                .launchIn(lifecycleScope)

            ivClear.setOnClickListener {
                etMovieName.setText("")
            }
        }
    }

    override fun invalidate(): Unit = withState(viewModel) { state ->
        moviesAdapter.updateItems(state.movies)
        binding.run {
            state.error?.let {
                showErrorState()
            } ?: hideErrorState()
            progressBarCenter.visibleOrGone(state.isLoading)
            progressBarPagination.visibleOrGone(state.isPaginationLoading)
        }
    }

    override fun onPause() {
        //hide keyboard when fragment goes to paused state
        hideSoftKeyboard()
        super.onPause()
    }

    private fun showErrorState() = withState(viewModel) { state ->
        binding.run {
            rcvMovies.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = state.error

        }
    }

    private fun hideErrorState() {
        binding.errorTextView.visibility = View.GONE
        binding.rcvMovies.visibility = View.VISIBLE
    }

    private fun goToNextScreen(movieName: String) {
        showToast(movieName)
    }
}
