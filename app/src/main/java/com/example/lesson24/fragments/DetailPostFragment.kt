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

        return bindingDetailPostFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkItemPost(readObjectPost())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingDetailPostFragment = null
    }

    override fun onDetach() {
        super.onDetach()
        detailScreenNavigationListener = null
    }

    private fun checkItemPost(itemPost: PostInfo?) {
        if (itemPost != null) {
            setDetailPost(itemPost)
            setupListeners(itemPost.id)
        } else {
            changeVisibility()
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

    private fun setupListeners(id: Long) {
        bindingDetailPostFragment?.apply {
            buttonComments.setOnClickListener {
                startListCommentActivity(id)
            }
        }
    }

    private fun startListCommentActivity(id: Long) {
        detailScreenNavigationListener?.showComments(id)
    }

    private fun changeVisibility() {
        bindingDetailPostFragment?.apply {
            noInfo.isVisible = true
        }
    }
}