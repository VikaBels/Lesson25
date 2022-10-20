package com.example.lesson24.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson24.App
import com.example.lesson24.R
import com.example.lesson24.activities.DetailPostActivity.Companion.KEY_SEND_ID_POST
import com.example.lesson24.adapters.CommentAdapter
import com.example.lesson24.databinding.ActivityListCommentBinding
import com.example.lesson24.factories.CommentViewModelFactory
import com.example.lesson24.listeners.CommentListener
import com.example.lesson24.repositories.DataRepository
import com.example.lesson24.viewModel.CommentViewModel

class ListCommentActivity : AppCompatActivity(), CommentListener {
    private var bindingListComment: ActivityListCommentBinding? = null
    private var commentAdapter: CommentAdapter? = null

    private val commentViewModel by viewModels<CommentViewModel> {
        CommentViewModelFactory(
            DataRepository(App.getDb()),
            getIdCurrentPost()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bindingListComment = ActivityListCommentBinding.inflate(layoutInflater)
        setContentView(bindingListComment.root)

        this.bindingListComment = bindingListComment
        commentAdapter = CommentAdapter(this)

        setUpAdapter()

        observeErrorDB()

        observeListComment()
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingListComment = null
        commentAdapter = null
    }

    override fun onClickRate(idComment: Long, commentRate: Long) {
        commentViewModel.upDateRateComment(idComment, commentRate)
    }

    private fun observeErrorDB() {
        commentViewModel.error.observe(this) { error ->
            setTextError(error.textId)
        }
    }

    private fun observeListComment() {
        commentViewModel.listComment.observe(this) { listComment ->
            if (listComment.isEmpty()) {
                setTextError(R.string.text_no_comment)
            } else {
                commentAdapter?.setListComment(listComment)
            }
        }
    }

    private fun setTextError(idError: Int) {
        bindingListComment?.apply {
            textNoComment.text = resources.getString(idError)
            textNoComment.isVisible = true
        }
    }

    private fun getIdCurrentPost(): Long? {
        return intent.extras?.getLong(KEY_SEND_ID_POST)
    }

    private fun setUpAdapter() {
        bindingListComment?.listComment?.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

}