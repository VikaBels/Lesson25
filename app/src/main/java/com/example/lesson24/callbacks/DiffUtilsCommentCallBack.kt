package com.example.lesson24.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.lesson24.models.CommentInfo

class DiffUtilsCommentCallBack(
    private val oldList: List<CommentInfo>,
    private val newList: List<CommentInfo>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return isContentTheSame(oldItemPosition, newItemPosition)
    }

    private fun isContentTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].rate == newList[newItemPosition].rate &&
                oldList[oldItemPosition].email == newList[newItemPosition].email &&
                oldList[oldItemPosition].text == newList[newItemPosition].text
    }
}