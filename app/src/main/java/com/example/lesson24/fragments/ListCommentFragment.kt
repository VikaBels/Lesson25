package com.example.lesson24.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson24.App
import com.example.lesson24.R
import com.example.lesson24.adapters.CommentAdapter
import com.example.lesson24.databinding.FragmentListCommentBinding
import com.example.lesson24.factories.CommentViewModelFactory
import com.example.lesson24.listeners.CommentListener
import com.example.lesson24.viewmodels.CommentViewModel

class ListCommentFragment : Fragment(), CommentListener {
    companion object {
        private const val KEY_SEND_ID_POST = "KEY_SEND_ID_POST"

        fun newInstance(idPost: Long): Fragment {
            val fragment = ListCommentFragment()
            fragment.arguments = bundleOf(KEY_SEND_ID_POST to idPost)
            return fragment
        }
    }

    private var bindingListCommentFragment: FragmentListCommentBinding? = null
    private var commentAdapter: CommentAdapter? = null

    private val commentViewModel by viewModels<CommentViewModel> {
        CommentViewModelFactory(
            App.getDataRepository(),
            getIdCurrentPost()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingListCommentFragment = FragmentListCommentBinding.inflate(layoutInflater)
        this.bindingListCommentFragment = bindingListCommentFragment

        return bindingListCommentFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commentAdapter = CommentAdapter(this)

        setUpAdapter()

        observeErrorDB()

        observeListComment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingListCommentFragment = null
        commentAdapter = null
    }

    override fun onClickRate(idComment: Long, commentRate: Long) {
        commentViewModel.upDateRateComment(idComment, commentRate)
    }

    private fun getIdCurrentPost(): Long? {
        return arguments?.getLong(KEY_SEND_ID_POST)
    }

    private fun setUpAdapter() {
        bindingListCommentFragment?.listComment?.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeErrorDB() {
        commentViewModel.error.observe(viewLifecycleOwner) { error ->
            setTextError(error.textId)
        }
    }

    private fun observeListComment() {
        commentViewModel.listComment.observe(viewLifecycleOwner) { listComment ->
            if (listComment.isEmpty()) {
                setTextError(R.string.text_no_comment)
            } else {
                commentAdapter?.setListComment(listComment)
            }
        }
    }

    private fun setTextError(idError: Int) {
        bindingListCommentFragment?.apply {
            textNoComment.text = resources.getString(idError)
            textNoComment.isVisible = true
        }
    }
}