package com.example.lesson24.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.lesson24.models.PostInfo

class DiffUtilsPostCallBack(
    private val oldList: List<PostInfo>,
    private val newList: List<PostInfo>
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
        return oldList[oldItemPosition].body == newList[newItemPosition].body &&
                oldList[oldItemPosition].email == newList[newItemPosition].email &&
                oldList[oldItemPosition].title == newList[newItemPosition].title &&
                oldList[oldItemPosition].fullName == newList[newItemPosition].fullName
    }
}