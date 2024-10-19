package com.kosmas.tecprin.network.entities

import com.google.gson.annotations.SerializedName
import com.kosmas.tecprin.network.models.Product

data class ProductResponse(
    @SerializedName("products") val products: List<Product>?
) : BaseResponse()
