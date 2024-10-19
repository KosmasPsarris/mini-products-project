package com.kosmas.tecprin.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kosmas.tecprin.R
import com.kosmas.tecprin.databinding.FragmentMainBinding
import com.kosmas.tecprin.ui.base.BaseFragment
import com.kosmas.tecprin.ui.details.DetailsBottomSheetFragment
import com.kosmas.tecprin.utils.ExceptionUtils
import com.kosmas.tecprin.utils.PagingScrollingState
import com.kosmas.tecprin.utils.UIState
import com.kosmas.tecprin.utils.hide
import com.kosmas.tecprin.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding

    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProductList()
        viewModel.getProducts()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collect {
                    when (it) {
                        is UIState.Result -> {
                            binding.moreLoader.hide()
                            binding.loader.hide()
                            binding.swipeRefreshLayout.isRefreshing = false

                            productAdapter.updateProducts(it.data)
                        }

                        is UIState.InProgress -> {
                            binding.loader.show()
                        }

                        is UIState.Error -> {
                            binding.moreLoader.hide()
                            binding.loader.hide()
                            binding.swipeRefreshLayout.isRefreshing = false
                            when (it.error) {
                                is ExceptionUtils.GenericErrorException -> {
                                    showDialog(
                                        title = getString(R.string.dialog_general_error_title),
                                        message = if (it.error.message.isNullOrEmpty().not())
                                            it.error.message
                                        else getString(R.string.dialog_general_error_message),
                                        isCancelable = false,
                                        optionalAction = {
                                            retry()
                                        },
                                        optionalButton = getString(R.string.dialog_retry),
                                        mandatoryButton = getString(R.string.dialog_ok),
                                        mandatoryAction = { viewModel.productsPagingState = PagingScrollingState.FETCH_MORE }
                                    )
                                }

                                is ExceptionUtils.NetworkErrorException -> {
                                    showDialog(
                                        title = getString(R.string.dialog_no_internet_title),
                                        message = getString(R.string.dialog_no_internet_message),
                                        isCancelable = false,
                                        optionalAction = {
                                            retry()
                                        },
                                        optionalButton = getString(R.string.dialog_retry),
                                        mandatoryButton = getString(R.string.dialog_ok),
                                        mandatoryAction = { viewModel.productsPagingState = PagingScrollingState.FETCH_MORE }
                                    )
                                }

                                else -> {
                                    showDialog(
                                        title = getString(R.string.dialog_general_error_title),
                                        message = getString(R.string.dialog_general_error_message),
                                        isCancelable = false,
                                        optionalAction = {
                                            retry()
                                        },
                                        optionalButton = getString(R.string.dialog_retry),
                                        mandatoryButton = getString(R.string.dialog_ok),
                                        mandatoryAction = { viewModel.productsPagingState = PagingScrollingState.FETCH_MORE }
                                    )
                                }
                            }
                        }

                        is UIState.COMPLETE -> {
                            binding.moreLoader.hide()
                        }

                        else -> {}
                    }
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            retry()
            binding.swipeRefreshLayout.isRefreshing = true
        }
    }

    private fun initProductList() {
        binding.productsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            productAdapter = ProductAdapter(emptyList(), onItemClick = { productId: Int ->

                if (parentFragmentManager.fragments.none { it is DetailsBottomSheetFragment }) {
                    val filtersSheet = DetailsBottomSheetFragment(productId)
                    filtersSheet.show(parentFragmentManager, DetailsBottomSheetFragment.TAG)
                }
            })
            adapter = productAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val llm = this@apply.layoutManager as LinearLayoutManager
                    if (llm.findLastCompletelyVisibleItemPosition() != -1 &&
                        llm.findLastCompletelyVisibleItemPosition() >= llm.itemCount - 2 &&
                        viewModel.productsPagingState in listOf(
                            PagingScrollingState.FETCH_MORE,
                            PagingScrollingState.START
                        )
                    ) {
                        // call more items
                        binding.moreLoader.show()
                        viewModel.getProducts()
                    }
                }
            })
        }
    }

    private fun retry() {
        viewModel.skip = 0
        productAdapter.deleteProducts()
        viewModel.productsPagingState = PagingScrollingState.START

        viewModel.getProducts()
    }

}
