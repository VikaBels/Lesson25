package com.example.lesson24.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lesson24.repositories.DataRepository
import com.example.lesson24.viewModels.CommentViewModel

class CommentViewModelFactory(
    private val dataRepository: DataRepository,
    private val idCurrentPost: Long?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == CommentViewModel::class.java) {
            return CommentViewModel(dataRepository, idCurrentPost) as T
        }
        return super.create(modelClass)
    }
}