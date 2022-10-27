package com.example.lesson24.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson24.databinding.ItemCommentBinding
import com.example.lesson24.listeners.CommentListener
import com.example.lesson24.models.CommentInfo
import com.example.lesson24.callbacks.DiffUtilsCommentCallBack

class CommentAdapter(
    private val commentListener: CommentListener,
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private var commentList = listOf<CommentInfo>()

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

    fun setListComment(listComment: List<CommentInfo>) {
        val result = DiffUtil.calculateDiff(
            DiffUtilsCommentCallBack(commentList, listComment), false
        )

        commentList = listComment
        result.dispatchUpdatesTo(this)
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
                updateCommentRate(commentItem, true)
                notifyCommentListener(commentItem)
            }

            binding.btnDislike.setOnClickListener {
                updateCommentRate(commentItem, false)
                notifyCommentListener(commentItem)
            }
        }

        private fun updateCommentRate(commentItem: CommentInfo, isLike: Boolean) {
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

        private fun notifyCommentListener(commentItem: CommentInfo) {
            commentListener.onClickRate(commentItem.id, commentItem.rate)
        }
    }
}