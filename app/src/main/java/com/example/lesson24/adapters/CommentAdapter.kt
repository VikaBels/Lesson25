package com.example.lesson24.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson24.databinding.ItemCommentBinding
import com.example.lesson24.listeners.CommentListener
import com.example.lesson24.models.CommentInfo

class CommentAdapter(
    private val commentListener: CommentListener,
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private var commentList = ArrayList<CommentInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding, commentListener)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentItem = commentList[position]
        holder.bind(commentItem)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun setListComment(listComment: ArrayList<CommentInfo>) {
        commentList = listComment
        notifyDataSetChanged()
    }

    class CommentViewHolder(
        private val binding: ItemCommentBinding,
        private val commentListener: CommentListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(commentItem: CommentInfo) {
            binding.email.text = commentItem.email
            binding.text.text = commentItem.text
            binding.rate.text = commentItem.rate.toString()

            binding.btnLike.setOnClickListener {
                setItem(commentItem, true)
                sendItem(commentItem)
            }

            binding.btnDislike.setOnClickListener {
                setItem(commentItem, false)
                sendItem(commentItem)
            }
        }

        private fun setItem(commentItem: CommentInfo, isLike: Boolean) {
            when {
                isLike -> {
                    commentItem.rate++
                }
                else -> {
                    commentItem.rate--
                }
            }

            binding.rate.text = commentItem.rate.toString()
        }

        private fun sendItem(commentItem: CommentInfo) {
            commentListener.onClickRate(commentItem.id, commentItem.rate)
        }
    }
}