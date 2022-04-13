package com.breaktime.lab2.view.explore

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.breaktime.lab2.R
import com.breaktime.lab2.api.model.Product
import com.breaktime.lab2.databinding.SearchItemBinding
import com.breaktime.lab2.repository.Repository
import com.breaktime.lab2.util.ResourcesProvider
import com.breaktime.lab2.util.toProductEntity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecyclerProductsAdapter(
    private val resourcesProvider: ResourcesProvider,
    private val repository: Repository
) :
    RecyclerView.Adapter<RecyclerProductsAdapter.ViewHolder>() {
    var favoriteList = repository.getFavoriteProducts().toMutableList()
    var items = emptyList<Product>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value.map { product ->
                if (favoriteList.firstOrNull { it.id == product.id } != null)
                    product.isFavorite = true
                product
            }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SearchItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.search_item, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    inner class ViewHolder(private val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product, position: Int) = with(binding) {
            binding.product = product
            image.setImageDrawable(resourcesProvider.getDrawable(R.drawable.loading))
            MainScope().launch {
                product.bitmap.collect {
                    if (it != null)
                        image.setImageBitmap(it)
                }
            }
            if (product.isVisible) {
                expand.visibility = View.VISIBLE
            } else {
                expand.visibility = View.GONE
            }
            root.setOnClickListener {
                product.isVisible = !product.isVisible
                notifyItemChanged(position)
            }
            more.setOnClickListener {
                product.isVisible = !product.isVisible
                notifyItemChanged(position)
            }
            favorite.setTextColor(Color.WHITE)
            favorite.activate(product.isFavorite)
            favorite.setOnClickListener {
                if (product.isFavorite) {
                    product.isFavorite = false
                    deleteFavorite(product)
                    favorite.activate(false)
                } else {
                    addFavorite(product)
                    product.isFavorite = true
                    favorite.activate(true)
                }
            }
        }

        private fun TextView.activate(activate: Boolean) {
            if (activate) {
                this.setBackgroundColor(Color.BLACK)
            } else {
                this.setBackgroundColor(Color.GRAY)
            }
        }

        private fun addFavorite(product: Product) {
            val entity = product.toProductEntity()
            repository.addFavorite(entity)
            favoriteList.add(entity)
        }

        private fun deleteFavorite(product: Product) {
            val entity = product.toProductEntity()
            repository.deleteFavorite(entity)
            favoriteList.remove(entity)
        }
    }
}
