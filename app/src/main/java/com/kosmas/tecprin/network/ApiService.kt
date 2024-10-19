package com.kosmas.tecprin.network

import com.kosmas.tecprin.network.entities.ProductResponse
import com.kosmas.tecprin.network.models.DetailedProduct
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int
    ): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getDetailedProduct(@Path("id") productId: Int = 1): Response<DetailedProduct>

}
