package com.example.lesson24.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.lesson24.activities.MainActivity.Companion.KEY_SEND_POST
import com.example.lesson24.databinding.ActivityDetailPostBinding
import com.example.lesson24.models.PostInfo

class DetailPostActivity : AppCompatActivity() {
    companion object {
        const val KEY_SEND_ID_POST = "KEY_SEND_ID_POST"
    }

    private var bindingDetailPost: ActivityDetailPostBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bindingDetailPost = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(bindingDetailPost.root)

        this.bindingDetailPost = bindingDetailPost

        checkItemPost(bindingDetailPost, readObjectPost())
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingDetailPost = null
    }

    private fun checkItemPost(bindingDetailPost: ActivityDetailPostBinding, itemPost: PostInfo?) {
        if (itemPost != null) {
            setDetailPost(itemPost)
            setupListeners(bindingDetailPost, itemPost.id)
        } else {
            changeVisibility(bindingDetailPost)
        }
    }

    private fun readObjectPost(): PostInfo? {
        return intent.extras?.getParcelable(KEY_SEND_POST)
    }

    private fun setDetailPost(itemPost: PostInfo) {
        bindingDetailPost?.apply {
            name.text = itemPost.fullName
            email.text = itemPost.email
            title.text = itemPost.title
            body.text = itemPost.body
        }
    }

    private fun setupListeners(bindingDetailPost: ActivityDetailPostBinding, id: Long) {
        bindingDetailPost.buttonComments.setOnClickListener {
            startListCommentActivity(id)
        }
    }

    private fun startListCommentActivity(id: Long) {
        val intent = Intent(this, ListCommentActivity::class.java)
        intent.putExtra(KEY_SEND_ID_POST, id)
        startActivity(intent)
    }

    private fun changeVisibility(bindingDetailPost: ActivityDetailPostBinding) {
        bindingDetailPost.noInfo.isVisible = true
    }
}