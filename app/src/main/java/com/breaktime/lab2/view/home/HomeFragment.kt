package com.breaktime.lab2.view.home

import android.app.ActionBar
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.breaktime.lab2.R
import com.breaktime.lab2.databinding.FragmentHomeBinding
import com.breaktime.lab2.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        binding.apply {
            viewModel.getCategories().observeForever { categories ->
                categoryList.removeAllViews()
                if (categories?.isEmpty() == true)
                    showAlert()
                else {
                    categories!!.onEach {
                        categoryList.addView(createButton(it))
                    }
                }
            }
            web.setOnClickListener {
                findNavController()
                    .navigate(
                        R.id.webFragment,
                        bundleOf("path" to "https://fakestoreapi.com/")
                    )
            }
            currency.setOnClickListener {
                findNavController().navigate(R.id.currencyFragment)
            }
            val adapter = RecyclerFavoriteAdapter(repository)
            adapter.items = viewModel.getAllProducts()
            list.layoutManager = LinearLayoutManager(context)
            list.adapter = adapter
        }
        return binding.root
    }

    private fun showAlert() = with(binding) {
        list.visibility = View.GONE
        alert.visibility = View.VISIBLE
    }

    private fun createButton(text: String): Button {
        val button = Button(requireContext())
        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(10, 0, 10, 0)
        button.layoutParams = params
        button.text = text
        button.tag = text
        button.setTextColor(Color.WHITE)
        button.setBackgroundColor(Color.GRAY)
        button.setOnClickListener {
            binding.categoryList.children.forEach { btn ->
                btn.setBackgroundColor(Color.GRAY)
                if (btn.tag != it.tag)
                    btn.isActivated = false
            }
            if (!it.isActivated) {
                it.setBackgroundColor(Color.BLACK)
                it.isActivated = true
                (binding.list.adapter as RecyclerFavoriteAdapter).apply {
                    items = items.filter { product ->
                        product.category == button.tag
                    }
                }
            } else {
                it.isActivated = false
                (binding.list.adapter as RecyclerFavoriteAdapter).items = viewModel.getAllProducts()
            }
        }
        return button
    }
}