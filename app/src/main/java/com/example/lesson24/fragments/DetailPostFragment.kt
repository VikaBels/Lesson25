package com.example.lesson24.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.example.lesson24.R
import com.example.lesson24.databinding.FragmentDetailPostBinding
import com.example.lesson24.listeners.DetailScreenNavigationListener
import com.example.lesson24.models.PostInfo

class DetailPostFragment : Fragment() {
    companion object {
        private const val KEY_SEND_POST = "KEY_SEND_POST"

        fun newInstance(postInfo: PostInfo): Fragment {
            val detailPostFragment = DetailPostFragment()
            detailPostFragment.arguments = bundleOf(KEY_SEND_POST to postInfo)
            return detailPostFragment
        }
    }

    private var detailScreenNavigationListener: DetailScreenNavigationListener? = null
    private var bindingDetailPostFragment: FragmentDetailPostBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        detailScreenNavigationListener = context as? DetailScreenNavigationListener
            ?: error("$context${resources.getString(R.string.exceptionInterface)}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingDetailPostFragment = FragmentDetailPostBinding.inflate(layoutInflater)
        this.bindingDetailPostFragment = bindingDetailPostFragment

        checkItemPost(bindingDetailPostFragment, readObjectPost())

        return bindingDetailPostFragment.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingDetailPostFragment = null
    }

    override fun onDetach() {
        super.onDetach()
        detailScreenNavigationListener = null
    }

    private fun checkItemPost(bindingDetailPost: FragmentDetailPostBinding, itemPost: PostInfo?) {
        if (itemPost != null) {
            setDetailPost(itemPost)
            setupListeners(bindingDetailPost, itemPost.id)
        } else {
            changeVisibility(bindingDetailPost)
        }
    }

    private fun readObjectPost(): PostInfo? {
        return arguments?.getParcelable(KEY_SEND_POST)
    }

    private fun setDetailPost(itemPost: PostInfo) {
        bindingDetailPostFragment?.apply {
            name.text = itemPost.fullName
            email.text = itemPost.email
            title.text = itemPost.title
            body.text = itemPost.body
        }
    }

    private fun setupListeners(bindingDetailPost: FragmentDetailPostBinding, id: Long) {
        bindingDetailPost.buttonComments.setOnClickListener {
            startListCommentActivity(id)
        }
    }

    private fun startListCommentActivity(id: Long) {
        detailScreenNavigationListener?.showComments(id)
    }

    private fun changeVisibility(bindingDetailPost: FragmentDetailPostBinding) {
        bindingDetailPost.noInfo.isVisible = true
    }
}