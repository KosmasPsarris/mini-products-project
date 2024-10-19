package com.kosmas.tecprin.network.models

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("date") val date: String,
    @SerializedName("reviewerName") val reviewerName: String,
    @SerializedName("reviewerEmail") val reviewerEmail: String
)
