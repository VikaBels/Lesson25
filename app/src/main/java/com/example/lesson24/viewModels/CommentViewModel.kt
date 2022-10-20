package com.example.lesson24.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bolts.CancellationTokenSource
import bolts.Task
import com.example.lesson24.R
import com.example.lesson24.models.CommentInfo
import com.example.lesson24.models.UIError
import com.example.lesson24.repositories.DataRepository
import com.example.lesson24.utils.getIdError

class CommentViewModel(
    private val dataRepository: DataRepository,
    private val idCurrentPost: Long?
) : ViewModel() {
    private val cancellationTokenSourceComment = CancellationTokenSource()

    private val listCommentInfo = MutableLiveData<List<CommentInfo>>()
    private val uiError = MutableLiveData<UIError>()

    val listComment: LiveData<List<CommentInfo>>
        get() = listCommentInfo

    val error: LiveData<UIError>
        get() = uiError

    init {
        startCommentTask()
    }

    override fun onCleared() {
        super.onCleared()
        cancellationTokenSourceComment.cancel()
    }

    private fun startCommentTask() {
        if (idCurrentPost != null) {
           dataRepository.getAllCommentTask(
                cancellationTokenSourceComment.token,
                idCurrentPost
            ).continueWith({

                if (it.result != null) {
                    listCommentInfo.value = it.result
                }

                if (it.error != null) {
                    uiError.value = UIError(getIdError(it.error))
                }

            }, Task.UI_THREAD_EXECUTOR)
        } else {
            uiError.value = UIError(R.string.error_no_id)
        }
    }

    fun upDateRateComment(idComment: Long, commentRate: Long) {
        dataRepository.updateCurrentCommentTask(
            cancellationTokenSourceComment.token,
            idComment,
            commentRate
        ).continueWith({

            if (it.error != null) {
                uiError.value = UIError(getIdError(it.error))
            }

        }, Task.UI_THREAD_EXECUTOR)
    }
}