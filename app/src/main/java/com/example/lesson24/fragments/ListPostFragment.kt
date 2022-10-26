package com.example.lesson24.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson24.App
import com.example.lesson24.R
import com.example.lesson24.adapters.PostAdapter
import com.example.lesson24.databinding.FragmentListPostBinding
import com.example.lesson24.factories.MainViewModelFactory
import com.example.lesson24.listeners.PostListener
import com.example.lesson24.listeners.PostsScreenNavigationListener
import com.example.lesson24.models.PostInfo
import com.example.lesson24.viewModels.MainViewModel

class ListPostFragment : Fragment(), PostListener {
    companion object {
        fun newInstance(): Fragment {
            return ListPostFragment()
        }
    }

    private var postsScreenNavigationListener: PostsScreenNavigationListener? = null

    private var bindingListPostFragment: FragmentListPostBinding? = null
    private var postAdapter: PostAdapter? = null

    private val mainViewModel by viewModels<MainViewModel> {
        MainViewModelFactory(
            App.getDataRepository()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        postsScreenNavigationListener = context as? PostsScreenNavigationListener
            ?: error("$context${resources.getString(R.string.exceptionInterface)}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingListPostBinding = FragmentListPostBinding.inflate(layoutInflater)
        this.bindingListPostFragment = bindingListPostBinding

        postAdapter = PostAdapter(this)

        setUpAdapter()

        observeErrorDB()

        observeListPost()

        setUpListeners(bindingListPostBinding)

        return bindingListPostBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingListPostFragment = null
    }

    override fun onDetach() {
        super.onDetach()
        postsScreenNavigationListener = null
    }

    override fun onPostClick(postInfo: PostInfo) {
        postsScreenNavigationListener?.showDetailPost(postInfo)
    }

    private fun setUpAdapter() {
        bindingListPostFragment?.listPost?.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeErrorDB() {
        mainViewModel.error.observe(viewLifecycleOwner) { error ->
            setTextError(error.textId)
        }
    }

    private fun observeListPost() {
        mainViewModel.listPost.observe(viewLifecycleOwner) { listPost ->
            if (listPost.isEmpty()) {
                setTextError(R.string.txt_no_post)
            } else {
                postAdapter?.setListItems(listPost)
            }
        }
    }

    private fun setTextError(idError: Int) {
        bindingListPostFragment?.apply {
            textError.text = resources.getString(idError)
            textError.isVisible = true
        }
    }

    private fun setUpListeners(bindingMain: FragmentListPostBinding) {
        bindingMain.buttonStatistic.setOnClickListener {
            postsScreenNavigationListener?.showStatistic()
        }
    }
}