package com.kosmas.tecprin.network.models

import com.google.gson.annotations.SerializedName
import com.kosmas.tecprin.network.entities.BaseResponse

data class DetailedProduct(
    @SerializedName("thumbnail") val thumbnail : String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description : String,
    @SerializedName("category") val category: String,
    @SerializedName("price") val price: String,
    @SerializedName("reviews") val reviews: List<Review>,
    @SerializedName("images") val images: List<String>
) : BaseResponse()
