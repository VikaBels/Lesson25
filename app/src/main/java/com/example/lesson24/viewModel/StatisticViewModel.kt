package com.example.lesson24.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bolts.CancellationTokenSource
import bolts.Task
import com.example.lesson24.models.PostStatistic
import com.example.lesson24.models.UIError
import com.example.lesson24.repositories.DataRepository
import com.example.lesson24.tasks.GetAllPostStatisticTask
import com.example.lesson24.utils.getIdError

class StatisticViewModel(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val cancellationTokenSourceStatistic = CancellationTokenSource()

    var listPostStatistic = MutableLiveData<ArrayList<PostStatistic>>()
    var error = MutableLiveData<UIError>()

    init {
        startStatisticTask()
    }

    override fun onCleared() {
        super.onCleared()
        cancellationTokenSourceStatistic.cancel()
    }

    private fun startStatisticTask() {
        GetAllPostStatisticTask().startTask(
            cancellationTokenSourceStatistic.token,
            dataRepository
        ).continueWith({

            listPostStatistic.value = it.result

            if (it.error != null) {
                error.value?.textId = getIdError(it.error)
            }

        }, Task.UI_THREAD_EXECUTOR)

    }
}