package com.example.newproject.data.models

import com.google.gson.annotations.SerializedName

data class MovieResults(
    @SerializedName("Response")
    val response: String,
    @SerializedName("Error")
    val error: String?,
    @SerializedName("Search")
    val search: List<Movie>?,
    @SerializedName("totalResults")
    val totalResults: String?
)

data class Movie(
    @SerializedName("Poster")
    val poster: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("imdbID")
    val imdbID: String
)