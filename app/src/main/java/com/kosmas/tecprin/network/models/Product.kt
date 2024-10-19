package com.kosmas.tecprin.network.models

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id : Int,
    @SerializedName("thumbnail") val thumbnail : String,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price : String,
    @SerializedName("brand") val brand: String? = ""
)