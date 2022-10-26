package com.example.lesson24.fragments

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
import com.example.lesson24.adapters.PostStatisticAdapter
import com.example.lesson24.databinding.FragmentStatisticBinding
import com.example.lesson24.factories.StatisticViewModelFactory
import com.example.lesson24.viewModels.StatisticViewModel

class StatisticFragment : Fragment() {
    companion object {
        fun newInstance(): Fragment {
            return StatisticFragment()
        }
    }

    private var bindingStatistic: FragmentStatisticBinding? = null
    private val postStatisticAdapter = PostStatisticAdapter()

    private val statisticViewModel by viewModels<StatisticViewModel> {
        StatisticViewModelFactory(
            App.getDataRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingStatistic = FragmentStatisticBinding.inflate(layoutInflater)
        this.bindingStatistic = bindingStatistic

        setUpAdapter()

        observeErrorDB()

        observeListPost()

        return bindingStatistic.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingStatistic = null
    }

    private fun setUpAdapter() {
        bindingStatistic?.listPostStatistic?.apply {
            adapter = postStatisticAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeErrorDB() {
        statisticViewModel.error.observe(viewLifecycleOwner) { error ->
            setTextError(error.textId)
        }
    }

    private fun observeListPost() {
        statisticViewModel.listPostStatistic.observe(viewLifecycleOwner) { listPostStatistic ->
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