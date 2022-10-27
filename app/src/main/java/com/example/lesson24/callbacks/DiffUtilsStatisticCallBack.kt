package com.example.lesson24.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.lesson24.models.PostStatistic

class DiffUtilsStatisticCallBack(
    private val oldList: List<PostStatistic>,
    private val newList: List<PostStatistic>

) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return isContentTheSame(oldItemPosition, newItemPosition)
    }

    private fun isContentTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].quantityComment == newList[newItemPosition].quantityComment &&
                oldList[oldItemPosition].averageCommentRating == newList[newItemPosition].averageCommentRating
    }
}