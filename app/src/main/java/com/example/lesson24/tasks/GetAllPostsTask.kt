package com.example.lesson24.tasks

import bolts.CancellationToken
import bolts.Task
import com.example.lesson24.models.PostInfo
import com.example.lesson24.repositories.DataRepository

class GetAllPostsTask {

    fun startTask(
        cancellationToken: CancellationToken,
        dataRepository: DataRepository
    ): Task<ArrayList<PostInfo>> {
        return Task
            .callInBackground({
                dataRepository.getAllPosts()
            }, cancellationToken)
    }
}