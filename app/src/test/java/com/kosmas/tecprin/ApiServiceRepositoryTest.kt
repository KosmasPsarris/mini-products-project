package com.kosmas.tecprin

import com.kosmas.tecprin.network.ApiService
import com.kosmas.tecprin.network.entities.ProductResponse
import com.kosmas.tecprin.network.models.DetailedProduct
import com.kosmas.tecprin.network.models.Product
import com.kosmas.tecprin.network.repository.ApiServiceRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ApiServiceRepositoryTest {

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var repository: ApiServiceRepository

    @Before
    fun setup() {
        repository = ApiServiceRepository(apiService)
    }

    @Test
    fun getProducts_success() = runBlocking {
        // Mock the API response
        val productResponse = ProductResponse(
            listOf(
                Product(
                    id = 1,
                    title = "title",
                    thumbnail = "Thumbnail",
                    price = "10.00",
                    brand = "brand"
                )
            )
        ) // Create a sample response
        `when`(apiService.getProducts(skip = 10)).thenReturn(
            Response.success(productResponse)
        )

        // Call the repository function
        val result = repository.getProducts(skip = 10)

        // Verify the result
        assertEquals(productResponse, result)
    }

    @Test
    fun getProducts_error() = runBlocking {
        // Mock an error response
        val errorResponseBody =
            "{}".toByteArray().toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse = Response.error<ProductResponse>(404, errorResponseBody)
        `when`(apiService.getProducts(skip = anyInt())).thenReturn(errorResponse)

        // Call the repository function
        val result = repository.getProducts(skip = 10)

        // Verify the result (expecting null in case of error)
        assertNull(result)
    }

    @Test
    fun getDetailedProduct_success() = runBlocking {
        val detailedProduct = DetailedProduct(
            thumbnail = "thumbnail_url",
            title = "title",
            description = "This is a sample product",
            price = "9.99",
            category = "category",
            images = listOf("image1.jpg", "image2.jpg"),
            reviews = listOf()
        )
        `when`(apiService.getDetailedProduct(productId = anyInt())).thenReturn(
            Response.success(detailedProduct)
        )
        val result = repository.getDetailedProduct(id = 123)

        assertEquals(detailedProduct, result)
    }

    @Test
    fun getDetailedProduct_error() = runBlocking {
        val errorResponseBody =
            "{}".toByteArray().toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse = Response.error<DetailedProduct>(500, errorResponseBody)
        `when`(apiService.getDetailedProduct(productId = anyInt())).thenReturn(errorResponse)

        val result = repository.getDetailedProduct(id = 123)

        assertNull(result)
    }
}
