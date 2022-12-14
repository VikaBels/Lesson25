package com.example.lesson24.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.lesson24.R
import com.example.lesson24.databinding.ActivityMainBinding
import com.example.lesson24.fragments.DetailPostFragment
import com.example.lesson24.fragments.ListCommentFragment
import com.example.lesson24.fragments.ListPostFragment
import com.example.lesson24.fragments.StatisticFragment
import com.example.lesson24.listeners.*
import com.example.lesson24.models.PostInfo

class MainActivity : AppCompatActivity(),
    PostsScreenNavigationListener,
    DetailScreenNavigationListener {
    companion object {
        private const val TAG_FOR_LIST_POST = "TAG_FOR_LIST_POST"
        private const val TAG_FOR_STATISTIC = "TAG_FOR_STATISTIC"
        private const val TAG_FOR_DETAIL_POST = "TAG_FOR_DETAIL_POST"
        private const val TAG_FOR_LIST_COMMENT = "TAG_FOR_LIST_COMMENT"
    }

    private var bindingMain: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        this.bindingMain = bindingMain

        if (savedInstanceState == null) {
            showListPost()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingMain = null
    }

    override fun showStatistic() {
        val fragmentStatistic = StatisticFragment.newInstance()
        showFragment(TAG_FOR_STATISTIC, fragmentStatistic)
    }

    override fun showDetailPost(postInfo: PostInfo) {
        val fragmentDetailPost = DetailPostFragment.newInstance(postInfo)
        showFragment(TAG_FOR_DETAIL_POST, fragmentDetailPost)
    }

    override fun showComments(idPost: Long) {
        val fragmentListComment = ListCommentFragment.newInstance(idPost)
        showFragment(TAG_FOR_LIST_COMMENT, fragmentListComment)
    }

    private fun showListPost() {
        val fragment = ListPostFragment.newInstance()
        showFragment(TAG_FOR_LIST_POST, fragment, false)
    }

    private fun showFragment(
        tag: String,
        fragment: Fragment,
        isAddToBackStack: Boolean = true
    ) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        when {
            isAddToBackStack -> {
                transaction.replace(R.id.container, fragment, tag)
                    .addToBackStack(tag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
            else -> {
                transaction.replace(R.id.container, fragment, tag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
        }
    }
}