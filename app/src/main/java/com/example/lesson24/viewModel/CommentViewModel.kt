package com.example.lesson24.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bolts.CancellationTokenSource
import bolts.Task
import com.example.lesson24.models.CommentInfo
import com.example.lesson24.models.UIError
import com.example.lesson24.repositories.DataRepository
import com.example.lesson24.tasks.CommentTasks
import com.example.lesson24.utils.getIdError

class CommentViewModel(
    private val dataRepository: DataRepository,
    private val idCurrentPost: Long?
) : ViewModel() {
    private val cancellationTokenSourceComment = CancellationTokenSource()
    private val cancellationTokenSourceCommentUpdate = CancellationTokenSource()

    val listComment = MutableLiveData<ArrayList<CommentInfo>>()
    var error = MutableLiveData<UIError>()

    init {
        startCommentTask()
    }

    override fun onCleared() {
        super.onCleared()
        cancellationTokenSourceComment.cancel()
        cancellationTokenSourceCommentUpdate.cancel()
    }

    private fun startCommentTask() {
        CommentTasks().getAllCommentTask(
            cancellationTokenSourceComment.token,
            dataRepository,
            idCurrentPost
        ).continueWith({

            listComment.value = it.result

            if (it.error != null) {
                error.value?.textId = getIdError(it.error)
            }

        }, Task.UI_THREAD_EXECUTOR)
    }

    fun upDateRateComment(idComment: Long, commentRate: Long) {
        CommentTasks().updateCurrentCommentTask(
            cancellationTokenSourceCommentUpdate.token,
            dataRepository,
            idComment,
            commentRate
        ).continueWith({

            if (it.error != null) {
                error.value?.textId = getIdError(it.error)
            }

        }, Task.UI_THREAD_EXECUTOR)
    }
}