package com.breaktime.lab2.view.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.breaktime.lab2.R
import com.breaktime.lab2.data.ProductEntity
import com.breaktime.lab2.databinding.FavoriteItemBinding
import com.breaktime.lab2.repository.Repository

class RecyclerFavoriteAdapter(
    private val repository: Repository
) :
    RecyclerView.Adapter<RecyclerFavoriteAdapter.ViewHolder>() {
    var items = emptyList<ProductEntity>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FavoriteItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.favorite_item, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    inner class ViewHolder(private val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity, position: Int) = with(binding) {
            binding.product = product
            image.setImageBitmap(product.bmp)
            if (product.isVisible) {
                expand.visibility = View.VISIBLE
            } else {
                expand.visibility = View.GONE
            }
            root.setOnClickListener {
                product.isVisible = !product.isVisible
                notifyItemChanged(position)
            }
            web.setOnClickListener {
                root.findNavController()
                    .navigate(
                        R.id.webFragment,
                        bundleOf("path" to "https://fakestoreapi.com/products/${product.id}")
                    )
            }
            more.setOnClickListener {
                product.isVisible = !product.isVisible
                notifyItemChanged(position)
            }
            label.setOnClickListener {
                dialogComment(label.context, product)?.show()
            }
            delete.setTextColor(Color.WHITE)
            delete.setOnClickListener {
                repository.deleteFavorite(product)
                items = repository.getFavoriteProducts()
            }
        }
    }

    private fun dialogComment(context: Context, product: ProductEntity): AlertDialog? {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Comment")
        val comment = EditText(context)
        comment.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(comment)

        builder.setPositiveButton("add") { dialog, _ ->
            repository.updateFavorite(product.copy(label = comment.text.toString()))
            items = repository.getFavoriteProducts()
            dialog.dismiss()

        }
        builder.setNegativeButton("cancel") { dialog, _ -> dialog.dismiss() }
        return builder.create()
    }
}
