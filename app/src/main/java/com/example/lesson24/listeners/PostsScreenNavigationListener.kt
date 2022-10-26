package com.example.lesson24.listeners

import com.example.lesson24.models.PostInfo

interface PostsScreenNavigationListener {

    fun showStatistic()

    fun showDetailPost(postInfo: PostInfo)
}