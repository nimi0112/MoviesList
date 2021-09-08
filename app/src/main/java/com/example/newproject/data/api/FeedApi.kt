package com.example.newproject.data.api

import com.example.newproject.data.models.MovieResults
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Nimish Nandwana on 07/09/2021.
 * Description -
 */

//http://www.omdbapi.com/?i=tt3896198&apikey=175a54fd

//https://www.omdbapi.com/
//    @GET("?apikey=175a54fd&s={word}&page=1")

interface FeedApi {

    @GET("/")
    fun searchMovies(
        @Query("s") keyword: String,
        @Query("apikey") apikey: String = "175a54fd",
        @Query("page") page: Int = 1
    ): Single<MovieResults>

}
