package com.kosmas.tecprin.network.repository

import com.kosmas.tecprin.network.ApiService
import com.kosmas.tecprin.network.entities.ProductResponse
import com.kosmas.tecprin.network.models.DetailedProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiServiceRepository @Inject constructor(
    private val service: ApiService
) {
    suspend fun getProducts(skip: Int): ProductResponse? = withContext(Dispatchers.IO) {
        val response = service.getProducts(skip = skip)
        response.body()
    }

    suspend fun getDetailedProduct(id: Int): DetailedProduct? = withContext(Dispatchers.IO) {
        val response = service.getDetailedProduct(productId = id)
        response.body()
    }
}
