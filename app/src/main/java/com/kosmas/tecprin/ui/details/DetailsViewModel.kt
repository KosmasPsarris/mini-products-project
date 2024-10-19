package com.kosmas.tecprin.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosmas.tecprin.network.models.DetailedProduct
import com.kosmas.tecprin.network.repository.ApiServiceRepository
import com.kosmas.tecprin.utils.ExceptionUtils
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
class DetailsViewModel @Inject constructor(private val repository: ApiServiceRepository) :
    ViewModel() {

    private var _productDetails = MutableStateFlow<UIState<DetailedProduct>>(UIState.IDLE)
    val productDetails: StateFlow<UIState<DetailedProduct>> = _productDetails

    fun getProductDetails(productId: Int) {
        _productDetails.value = UIState.InProgress
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getDetailedProduct(productId)
                _productDetails.value = if (response == null) {
                    UIState.Error(ExceptionUtils.GenericErrorException())
                } else {
                    UIState.Result(response)
                }
            } catch (ex: Exception) {
                _productDetails.value = when (ex) {
                    is ConnectException, is UnknownHostException -> UIState.Error(ExceptionUtils.NetworkErrorException()) // Network error
                    is CancellationException -> UIState.CANCELED // If the job is cancelled, ignore it
                    else -> UIState.Error(ExceptionUtils.GenericErrorException(ex.message)) // Generic error
                }
            }
        }
    }

}
