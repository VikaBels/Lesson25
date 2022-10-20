package com.example.lesson24.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bolts.CancellationTokenSource
import bolts.Task
import com.example.lesson24.models.PostInfo
import com.example.lesson24.models.UIError
import com.example.lesson24.repositories.DataRepository
import com.example.lesson24.utils.getIdError

class MainViewModel(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val cancellationTokenSourceMain = CancellationTokenSource()

    private var listPostInfo = MutableLiveData<List<PostInfo>>()
    private var uiError = MutableLiveData<UIError>()

    val listPost: LiveData<List<PostInfo>>
        get() = listPostInfo

    val error: LiveData<UIError>
        get() = uiError

    init {
        startMainTask()
    }

    override fun onCleared() {
        super.onCleared()
        cancellationTokenSourceMain.cancel()
    }

    private fun startMainTask() {
        dataRepository.getAllPostsTask(
            cancellationTokenSourceMain.token,
        ).continueWith({

            if(it.result != null){
                listPostInfo.value = it.result

            }

            if (it.error != null) {
                uiError.value = UIError(getIdError(it.error))
            }

        }, Task.UI_THREAD_EXECUTOR)
    }
}