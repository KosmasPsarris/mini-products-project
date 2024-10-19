package com.kosmas.tecprin.ui.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kosmas.tecprin.databinding.ItemProductReviewBinding
import com.kosmas.tecprin.network.models.Review
import com.kosmas.tecprin.utils.reformatDate

class ProductReviewsAdapter(
    private var productReviews: List<Review>
) : RecyclerView.Adapter<ProductReviewsAdapter.ViewHolder>() {

    lateinit var myContext: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductReviewsAdapter.ViewHolder {
        val binding =
            ItemProductReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        myContext = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductReviewsAdapter.ViewHolder, position: Int) {
        val currentItem = productReviews[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return productReviews.size
    }

    inner class ViewHolder(private val binding: ItemProductReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.apply {
                ratingBar.rating = review.rating.toFloat()
                reviewerName.text = review.reviewerName
                reviewDate.text = review.date.reformatDate()
                reviewComment.text = review.comment
                reviewerEmail.text = review.reviewerEmail
            }
        }
    }

}
