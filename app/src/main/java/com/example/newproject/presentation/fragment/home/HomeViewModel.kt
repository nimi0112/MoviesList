package com.example.newproject.presentation.fragment.home

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.newproject.data.models.Movie
import com.example.newproject.domain.FeedService
import com.example.newproject.presentation.base.AbstractViewModel
import com.example.newproject.presentation.base.NavigationAction.DisplayToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Nimish Nandwana on 07/09/2021.
 * Description -
 */

data class HomeState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val hasMoreData: Boolean = true,
    val currentPageCount: Int = 0,
    val movies: List<Movie> = emptyList(),
) : MavericksState

class HomeViewModel(
    initialState: HomeState,
    private val feedService: FeedService,
) : AbstractViewModel<HomeState>(initialState) {

    private var currentPageNumber = 1
    private lateinit var currentKeyword: String

    fun searchMovies(keyword: String) {
        currentKeyword = keyword
        if (keyword.length < 3) {
            setState { copy(error = "Please type 3 characters or more to search") }
            return
        }

        feedService.searchMovies(keyword, currentPageNumber)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState {
                    copy(
                        isLoading = true,
                        isPaginationLoading = false,
                        error = null,
                        movies = emptyList()
                    )
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val moreData = if (it.search.isNullOrEmpty()) false else it.search.size <= it.totalResults!!.toInt()
                setState {
                    copy(
                        isLoading = false,
                        movies = it.search.orEmpty(),
                        hasMoreData = moreData,
                        isPaginationLoading = false,
                        error = it.error
                    )
                }

                viewModelScope.launch {
                    Timber.e(awaitState().toString())
                }
            }, {
                setState { copy(error = it.message, isLoading = false) }
                Timber.e(it)
                onNavigationAction(DisplayToast(it.message.orEmpty()))
            }).bindToLifecycle()
    }

    fun getMoreData() = withState { state ->

        if (!state.hasMoreData || state.isPaginationLoading || state.isLoading) {
            return@withState
        }

        currentPageNumber++

        feedService.searchMovies(currentKeyword, currentPageNumber)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isPaginationLoading = true) }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setState {
                    copy(
                        movies = state.movies.toMutableList().apply {
                            addAll(it.search.orEmpty())
                        },
                        hasMoreData = if (it.search.isNullOrEmpty()) false else it.search.size <= it.totalResults!!
                            .toInt(),
                        isPaginationLoading = false,
                        error = it.error
                    )
                }
            }, {
                setState { copy(error = it.message, isLoading = false) }
                Timber.e(it)
                onNavigationAction(DisplayToast(it.message.orEmpty()))
            }).bindToLifecycle()

    }

    /**
     * If you implement MvRxViewModelFactory in your companion object, MvRx will use that to create
     * your ViewModel. You can use this to achieve constructor dependency injection with Mavericks.
     *
     * @see MavericksViewModelFactory
     */
    companion object : MavericksViewModelFactory<HomeViewModel, HomeState> {
        override fun create(viewModelContext: ViewModelContext, state: HomeState): HomeViewModel {
            return HomeViewModel(state, feedService = FeedService)
        }
    }
}

