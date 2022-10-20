package com.example.lesson24.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson24.databinding.ItemBinding
import com.example.lesson24.models.PostInfo
import com.example.lesson24.listeners.PostListener

class PostAdapter(
    private val postListener: PostListener,
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var postList = listOf<PostInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, postListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postItem = postList[position]
        holder.bind(postItem)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun setListItems(listPost: List<PostInfo>) {
        postList = listPost
        notifyDataSetChanged()
    }

    class PostViewHolder(
        private val binding: ItemBinding,
        private val postListener: PostListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(postItem: PostInfo) {
            binding.title.text = postItem.title
            binding.email.text = postItem.email
            binding.body.text = postItem.body

            binding.oneItem.setOnClickListener {
                notifyPostListener(postItem)
            }
        }

        private fun notifyPostListener(postItem: PostInfo) {
            postListener.onClickPost(postItem)
        }
    }
}