package com.example.lesson24.tasks

import bolts.CancellationToken
import bolts.Task
import com.example.lesson24.models.PostStatistic
import com.example.lesson24.repositories.DataRepository

class GetAllPostStatisticTask {

    fun startTask(
        cancellationToken: CancellationToken,
        dataRepository: DataRepository
    ): Task<ArrayList<PostStatistic>> {
        return Task
            .callInBackground({
                dataRepository.getAllPostsStatistic()
            }, cancellationToken)
    }
}