package com.example.lesson24.tasks

import bolts.CancellationToken
import bolts.Task
import com.example.lesson24.models.CommentInfo
import com.example.lesson24.repositories.DataRepository

class CommentTasks {

    fun getAllCommentTask(
        cancellationToken: CancellationToken,
        dataRepository: DataRepository,
        idCurrentPost: Long?
    ): Task<ArrayList<CommentInfo>> {
        return Task
            .callInBackground({
                dataRepository.getAllComment(idCurrentPost)
            }, cancellationToken)
    }

    fun updateCurrentCommentTask(
        cancellationToken: CancellationToken,
        dataRepository: DataRepository,
        idComment: Long,
        commentRate: Long
    ): Task<Unit> {
        return Task.callInBackground({
            dataRepository.updateRateComment(idComment, commentRate)
        }, cancellationToken)
    }
}