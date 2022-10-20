package com.example.lesson24.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bolts.CancellationTokenSource
import bolts.Task
import com.example.lesson24.models.PostInfo
import com.example.lesson24.models.UIError
import com.example.lesson24.repositories.DataRepository
import com.example.lesson24.tasks.GetAllPostsTask
import com.example.lesson24.utils.getIdError

class MainViewModel(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val cancellationTokenSourceMain = CancellationTokenSource()

    var listPost = MutableLiveData<ArrayList<PostInfo>>()
    var error = MutableLiveData<UIError>()

    init {
        startMainTask()
    }

    override fun onCleared() {
        super.onCleared()
        cancellationTokenSourceMain.cancel()
    }

    private fun startMainTask() {
        GetAllPostsTask().startTask(
            cancellationTokenSourceMain.token,
            dataRepository
        ).continueWith({

            listPost.value = it.result

            if (it.error != null) {
                error.value?.textId = getIdError(it.error)
            }

        }, Task.UI_THREAD_EXECUTOR)
    }
}