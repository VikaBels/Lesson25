package com.example.lesson24.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson24.App.Companion.getInstance
import com.example.lesson24.R
import com.example.lesson24.databinding.ItemPostStatisticBinding
import com.example.lesson24.models.PostStatistic

class PostStatisticAdapter : RecyclerView.Adapter<PostStatisticAdapter.PostStatisticViewHolder>() {
    private var postStatisticList = ArrayList<PostStatistic>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostStatisticViewHolder {
        val binding =
            ItemPostStatisticBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostStatisticViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostStatisticViewHolder, position: Int) {
        val postStatisticItem = postStatisticList[position]
        holder.bind(postStatisticItem)
    }

    override fun getItemCount(): Int {
        return postStatisticList.size
    }

    fun setListStatistic(listStatistic: ArrayList<PostStatistic>) {
        postStatisticList = listStatistic
        notifyDataSetChanged()
    }

    class PostStatisticViewHolder(
        private val binding: ItemPostStatisticBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val resources = getInstance().resources

        fun bind(postStatisticItem: PostStatistic) {
            binding.title.text = resources.getString(R.string.post_title, postStatisticItem.title)

            binding.quantity.text = resources.getString(
                R.string.quantity_comment,
                postStatisticItem.quantityComment.toString()
            )

            binding.averageRating.text = resources.getString(
                R.string.average_comment_rating,
                postStatisticItem.averageCommentRating.toString()
            )
        }
    }
}