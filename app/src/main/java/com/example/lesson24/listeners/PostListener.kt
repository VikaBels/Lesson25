package com.example.lesson24.listeners

import com.example.lesson24.models.PostInfo

interface PostListener {

    fun onClickPost(postItem: PostInfo)
}
