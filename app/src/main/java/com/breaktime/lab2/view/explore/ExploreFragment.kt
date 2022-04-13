package com.breaktime.lab2.view.explore

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.breaktime.lab2.R
import com.breaktime.lab2.api.model.Product
import com.breaktime.lab2.databinding.FragmentExploreBinding
import com.breaktime.lab2.repository.Repository
import com.breaktime.lab2.util.ResourcesProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private val viewModel: ExploreViewModel by viewModels()

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    @Inject
    lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_explore, container, false
        )
        binding.apply {
            currency.setOnClickListener {
                findNavController().navigate(R.id.currencyFragment)
            }
            search.setOnClickListener {
                findNavController().navigate(R.id.searchFragment)
            }
            noConnection.setOnClickListener {
                loadList()
            }
            noApi.setOnClickListener {
                loadList()
            }
            val adapter = RecyclerProductsAdapter(resourcesProvider, repository)
            list.layoutManager = LinearLayoutManager(context)
            list.adapter = adapter
            loadList()
            reset.setOnClickListener { resetList() }
        }
        return binding.root
    }

    private fun loadList() {
        if (isInternetAvailable()) {
            showNoInternet(true)
            binding.loading.visibility = View.VISIBLE
            val products = viewModel.getAllProducts()
            (binding.list.adapter as RecyclerProductsAdapter).items = modifyList(products)
            binding.loading.visibility = View.GONE
        } else {
            showNoInternet(false)
        }
    }

    private fun modifyList(list: List<Product>): List<Product> {
        if (arguments?.isEmpty != false)
            return list
        val args = requireArguments()
        binding.reset.visibility = View.VISIBLE
        val sequence =
            list.asSequence()
                .filter { it.isInStock == args.getBoolean("isInStock") }
                .filter { it.price in args.getFloat("minPrice")..args.getFloat("maxPrice") }
                .filter {
                    it.title.uppercase(Locale.getDefault())
                        .contains(
                            args.getString("searchText")!!.uppercase(Locale.getDefault())
                        )
                }
                .filter {
                    if (args.getString("category")!! == "Any")
                        true
                    else
                        it.category == args.getString("category")!!
                }
        return sequence.toList()
    }

    private fun resetList() {
        binding.reset.visibility = View.INVISIBLE
        arguments?.clear()
        loadList()
    }

    private fun showNoInternet(isConnected: Boolean) {
        if (isConnected) {
            binding.list.visibility = View.VISIBLE
            binding.noConnection.visibility = View.GONE
            binding.noApi.visibility = View.GONE
        } else {
            binding.noConnection.visibility = View.VISIBLE
            binding.list.visibility = View.GONE
            binding.noApi.visibility = View.GONE
        }
    }

    private fun showApiProblem() {
        binding.noApi.visibility = View.VISIBLE
        binding.list.visibility = View.GONE
        binding.noConnection.visibility = View.GONE
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork != null
    }
}