package com.kosmas.tecprin.ui.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kosmas.tecprin.R
import com.kosmas.tecprin.databinding.ItemProductImageBinding

class ProductImagesAdapter(
    private var productImages: List<String>
) : RecyclerView.Adapter<ProductImagesAdapter.ViewHolder>() {

    private lateinit var myContext: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductImagesAdapter.ViewHolder {
        val binding =
            ItemProductImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        myContext = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductImagesAdapter.ViewHolder, position: Int) {
        val currentItem = productImages[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return productImages.size
    }

    inner class ViewHolder(private val binding: ItemProductImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String) {
            binding.apply {
                Glide.with(myContext)
                    .load(image)
                    .placeholder(R.drawable.placeholder_image) // Placeholder while loading
                    .error(R.drawable.error_image) // Error image if loading fails
                    .fallback(R.drawable.placeholder_image) // Fallback if URL is null
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(productImage)
            }
        }
    }

}
