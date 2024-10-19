package com.kosmas.tecprin.ui.details

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kosmas.tecprin.R
import com.kosmas.tecprin.databinding.FragmentDetailsBinding
import com.kosmas.tecprin.network.models.DetailedProduct
import com.kosmas.tecprin.ui.base.BaseBottomSheetFragment
import com.kosmas.tecprin.utils.ExceptionUtils
import com.kosmas.tecprin.utils.UIState
import com.kosmas.tecprin.utils.hide
import com.kosmas.tecprin.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsBottomSheetFragment(private val productId: Int) : BaseBottomSheetFragment() {

    companion object {
        const val TAG = "DetailsBottomSheetFragment"
    }

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loader.show()
        viewModel.getProductDetails(productId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productDetails.collect {
                    when (it) {
                        is UIState.Result -> {
                            binding.loader.hide()

                            populateUI(it.data)
                        }

                        is UIState.InProgress -> {
                            binding.loader.show()
                        }

                        is UIState.Error -> {
                            binding.loader.hide()
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
                                        mandatoryButton = getString(R.string.dialog_back),
                                        mandatoryAction = { this@DetailsBottomSheetFragment.dismiss() }
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
                                        mandatoryButton = getString(R.string.dialog_back),
                                        mandatoryAction = { this@DetailsBottomSheetFragment.dismiss() }
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
                                        mandatoryButton = getString(R.string.dialog_back),
                                        mandatoryAction = { this@DetailsBottomSheetFragment.dismiss() }
                                    )
                                }
                            }
                        }

                        is UIState.CANCELED -> {}

                        else -> {
                            binding.loader.hide()
                        }
                    }
                }
            }
        }

        binding.back.setOnClickListener {
            dismiss()
        }
    }

    private fun populateUI(product: DetailedProduct) {
        binding.apply {
            Glide.with(requireActivity())
                .load(product.thumbnail)
                .placeholder(R.drawable.placeholder_image) // Placeholder while loading
                .error(R.drawable.error_image) // Error image if loading fails
                .fallback(R.drawable.placeholder_image) // Fallback if URL is null
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(productThumbnail)

            productTitle.text = product.title
            productPrice.text = getString(R.string.product_price, product.price)
            productCategory.text = getString(R.string.product_category, product.category)
            productDescription.text = product.description

            imagesRecyclerView.adapter = ProductImagesAdapter(
                if (product.images.count() >= 4)
                    product.images.take(4)
                else
                    product.images
            )
            reviewsRecyclerView.adapter = ProductReviewsAdapter(
                if (product.reviews.count() >= 3)
                    product.reviews.take(3)
                else
                    product.reviews
            )
        }
    }

    private fun retry(){
        binding.loader.show()
        viewModel.getProductDetails(productId)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { layout ->
                val behavior = BottomSheetBehavior.from(layout)
                setupFullScreen(layout)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = false
            }
        }
        return dialog
    }

    private fun setupFullScreen(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

}
