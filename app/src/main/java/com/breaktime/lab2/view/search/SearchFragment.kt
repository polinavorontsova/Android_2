package com.breaktime.lab2.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.breaktime.lab2.R
import com.breaktime.lab2.databinding.FragmentSearchBinding
import com.breaktime.lab2.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var repository: Repository
    private val categories = mutableListOf("Any")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentSearchBinding?>(
            inflater, R.layout.fragment_search, container, false
        ).apply {
            val arrayAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, categories
            )
            categories.addAll(repository.allCategories)
            category.adapter = arrayAdapter

            search.setOnClickListener {
                val strMinPrice = fromPrice.text.toString()
                val minPrice = if (strMinPrice.isNotEmpty()) strMinPrice.toFloat() else 0f
                val strMaxPrice = toPrice.text.toString()
                val maxPrice =
                    if (strMaxPrice.isNotEmpty()) strMaxPrice.toFloat() else Float.MAX_VALUE
                val isInStock = binding.isInStock.isChecked
                val searchText = searchText.text.toString()
                findNavController().navigate(
                    R.id.exploreFragment, bundleOf(
                        "category" to categories[category.selectedItemPosition],
                        "searchText" to searchText,
                        "minPrice" to minPrice,
                        "maxPrice" to maxPrice,
                        "isInStock" to isInStock
                    )
                )
            }
        }
        return binding.root
    }
}