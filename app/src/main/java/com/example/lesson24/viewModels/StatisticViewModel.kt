package com.example.lesson24.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bolts.CancellationTokenSource
import bolts.Task
import com.example.lesson24.models.PostStatistic
import com.example.lesson24.models.UIError
import com.example.lesson24.repositories.DataRepository
import com.example.lesson24.utils.getIdError

class StatisticViewModel(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val cancellationTokenSourceStatistic = CancellationTokenSource()

    private var list = MutableLiveData<List<PostStatistic>>()
    private var uiError = MutableLiveData<UIError>()

    val listPostStatistic: LiveData<List<PostStatistic>>
        get() = list

    val error: LiveData<UIError>
        get() = uiError

    init {
        startStatisticTask()
    }

    override fun onCleared() {
        super.onCleared()
        cancellationTokenSourceStatistic.cancel()
    }

    private fun startStatisticTask() {
        dataRepository.getAllPostStatisticTask(
            cancellationTokenSourceStatistic.token,
        ).continueWith({

            if (it.result != null) {
                list.value = it.result

            }

            if (it.error != null) {
                uiError.value = UIError(getIdError(it.error))
            }

        }, Task.UI_THREAD_EXECUTOR)
    }
}