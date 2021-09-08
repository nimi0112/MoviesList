package com.example.newproject.domain

import com.example.newproject.data.models.MovieResults
import com.example.newproject.data.singleton.ApiProvider
import io.reactivex.Single

/**
 * Created by Nimish Nandwana on 07/09/2021.
 * Description -
 */

object FeedService {

    private val feedApi = ApiProvider.getFeedApi()

    fun searchMovies(keyword: String, page: Int): Single<MovieResults> {
        return feedApi.searchMovies(keyword, page = page)
    }
}