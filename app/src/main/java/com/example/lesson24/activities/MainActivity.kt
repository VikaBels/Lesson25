package com.example.lesson24.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson24.App
import com.example.lesson24.R
import com.example.lesson24.adapters.PostAdapter
import com.example.lesson24.databinding.ActivityMainBinding
import com.example.lesson24.factories.MainViewModelFactory
import com.example.lesson24.listeners.PostListener
import com.example.lesson24.models.PostInfo
import com.example.lesson24.repositories.DataRepository
import com.example.lesson24.viewModel.MainViewModel

class MainActivity : AppCompatActivity(), PostListener {
    companion object {
        const val KEY_SEND_POST = "KEY_SEND_POST"
    }

    private var bindingMain: ActivityMainBinding? = null
    private var postAdapter: PostAdapter? = null

    private val mainViewModel by viewModels<MainViewModel> {
        MainViewModelFactory(
            DataRepository(App.getDb())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        this.bindingMain = bindingMain
        postAdapter = PostAdapter(this)

        setUpAdapter()

        observeErrorDB()

        observeListPost()

        setUpListeners(bindingMain)
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingMain = null
        postAdapter = null
    }

    override fun onClickPost(postItem: PostInfo) {
        startDetailPostActivity(postItem)
    }

    private fun startDetailPostActivity(postItem: PostInfo) {
        val intent = Intent(this, DetailPostActivity::class.java)
        intent.putExtra(KEY_SEND_POST, postItem)
        startActivity(intent)
    }

    private fun setUpAdapter() {
        bindingMain?.listPost?.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeErrorDB() {
        mainViewModel.error.observe(this) { error ->
            setTextError(error.textId)
        }
    }

    private fun observeListPost() {
        mainViewModel.listPost.observe(this) { listPost ->
            if (listPost.isEmpty()) {
                setTextError(R.string.txt_no_post)
            } else {
                postAdapter?.setListItems(listPost)
            }
        }
    }

    private fun setTextError(idError: Int) {
        bindingMain?.apply {
            textError.text = resources.getString(idError)
            textError.isVisible = true
        }
    }

    private fun setUpListeners(bindingMain: ActivityMainBinding) {
        bindingMain.buttonStatistic.setOnClickListener {
            startStatisticActivity()
        }
    }

    private fun startStatisticActivity() {
        val intent = Intent(this, StatisticActivity::class.java)
        startActivity(intent)
    }
}