package com.example.lesson24.listeners

import com.example.lesson24.models.PostInfo

interface PostListener {

    fun onPostClick(postInfo: PostInfo)
}