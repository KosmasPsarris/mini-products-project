package com.kosmas.tecprin.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kosmas.tecprin.R
import com.kosmas.tecprin.databinding.ItemProductBinding
import com.kosmas.tecprin.network.models.Product

class ProductAdapter(
    private var products: List<Product>,
    private val onItemClick: (id: Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    lateinit var myContext: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        myContext = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        val currentItem = products[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateProducts(products: List<Product>) {
        this.products += products
        notifyDataSetChanged()
    }

    fun deleteProducts(){
        this.products = listOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                Glide.with(myContext)
                    .load(product.thumbnail)
                    .placeholder(R.drawable.placeholder_image) // Placeholder while loading
                    .error(R.drawable.error_image) // Error image if loading fails
                    .fallback(R.drawable.placeholder_image) // Fallback if URL is null
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(productThumbnail)

                productTitle.text = product.title
                productPrice.text = myContext.getString(R.string.product_price, product.price)
                product.brand?.let{
                    productBrand.text = myContext.getString(R.string.product_brand, it)
                } ?: run{
                    productBrand.text = ""
                }
                root.setOnClickListener {
                    onItemClick(product.id)
                }
            }
        }
    }

}
