package com.example.lesson24.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lesson24.repositories.DataRepository
import com.example.lesson24.viewmodels.StatisticViewModel

class StatisticViewModelFactory(
    private val dataRepository: DataRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == StatisticViewModel::class.java) {
            return StatisticViewModel(dataRepository) as T
        }
        return super.create(modelClass)
    }
}