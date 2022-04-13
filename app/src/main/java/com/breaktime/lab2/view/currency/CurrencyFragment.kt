package com.breaktime.lab2.view.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.breaktime.lab2.R
import com.breaktime.lab2.currency_parser.CurrencyParser
import com.breaktime.lab2.databinding.FragmentCurrencyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyFragment : Fragment() {
    private lateinit var binding: FragmentCurrencyBinding

    @Inject
    lateinit var parser: CurrencyParser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_currency, container, false
        )
        binding.update.setOnClickListener {
            updateDate()
        }
        updateDate()
        return binding.root
    }

    private fun updateDate() {
        lifecycleScope.launch {
            var data: Map<String, Pair<Double, Double>>
            binding.loading.visibility = View.VISIBLE
            withContext(Dispatchers.Default) {
                parser.update()
                data = parser.data
            }
            binding.loading.visibility = View.GONE
            binding.setUsd(data["usd"])
            binding.setEur(data["eur"])
            binding.setRub(data["rub"])
        }
    }
}