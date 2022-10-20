package com.example.lesson24.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson24.App.Companion.getDataRepository
import com.example.lesson24.R
import com.example.lesson24.adapters.PostStatisticAdapter
import com.example.lesson24.databinding.ActivityStatisticBinding
import com.example.lesson24.factories.StatisticViewModelFactory
import com.example.lesson24.viewModels.StatisticViewModel

class StatisticActivity : AppCompatActivity() {
    private var bindingStatistic: ActivityStatisticBinding? = null
    private val postStatisticAdapter = PostStatisticAdapter()

    private val statisticViewModel by viewModels<StatisticViewModel> {
        StatisticViewModelFactory(
            getDataRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bindingStatistic = ActivityStatisticBinding.inflate(layoutInflater)
        setContentView(bindingStatistic.root)

        this.bindingStatistic = bindingStatistic

        setUpAdapter()

        observeErrorDB()

        observeListPost()
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingStatistic = null
    }

    private fun setUpAdapter() {
        bindingStatistic?.listPostStatistic?.apply {
            adapter = postStatisticAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeErrorDB() {
        statisticViewModel.error.observe(this) { error ->
            setTextError(error.textId)
        }
    }

    private fun observeListPost() {
        statisticViewModel.listPostStatistic.observe(this) { listPostStatistic ->
            if (listPostStatistic.isEmpty()) {
                setTextError(R.string.txt_no_info_about_post)
            } else {
                postStatisticAdapter.setListStatistic(listPostStatistic)
            }
        }
    }

    private fun setTextError(idError: Int) {
        bindingStatistic?.apply {
            textError.text = resources.getString(idError)
            textError.isVisible = true
        }
    }
}