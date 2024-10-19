package com.kosmas.tecprin.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosmas.tecprin.network.models.Product
import com.kosmas.tecprin.network.repository.ApiServiceRepository
import com.kosmas.tecprin.utils.ExceptionUtils
import com.kosmas.tecprin.utils.PagingScrollingState
import com.kosmas.tecprin.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ApiServiceRepository) :
    ViewModel() {

    var skip = 0 // Start from the first page
    var productsPagingState = PagingScrollingState.START

    private var _products = MutableStateFlow<UIState<List<Product>>>(UIState.IDLE)
    val products: StateFlow<UIState<List<Product>>> = _products

    fun getProducts() {
        if (productsPagingState == PagingScrollingState.START) _products.value = UIState.InProgress
        productsPagingState = PagingScrollingState.FETCHING

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getProducts(skip)
                _products.value = if (response == null) {
                    UIState.Error(ExceptionUtils.GenericErrorException())
                } else {
                    response.products?.let { products ->
                        // Success api call
                        val productsCount = products.count()
                        if (productsCount == 0) {
                            productsPagingState =
                                PagingScrollingState.STOP // We got last page so no more products fetching
                            UIState.COMPLETE
                        } else {
                            skip += products.count()
                            productsPagingState = PagingScrollingState.FETCH_MORE
                            UIState.Result(products)
                        }
                    } ?: run {
                        UIState.Error(ExceptionUtils.GenericErrorException(response.message)) // Generic error
                    }
                }
            } catch (ex: Exception) {
                _products.value = when (ex) {
                    is ConnectException, is UnknownHostException -> UIState.Error(ExceptionUtils.NetworkErrorException()) // Network error
                    is CancellationException -> UIState.CANCELED // If the job is cancelled, ignore it
                    else -> UIState.Error(ExceptionUtils.GenericErrorException(ex.message)) // Generic error
                }
            }
        }
    }

}
