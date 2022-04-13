package com.breaktime.lab2.view.home

import androidx.lifecycle.ViewModel
import com.breaktime.lab2.data.ProductEntity
import com.breaktime.lab2.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    fun getAllProducts(): List<ProductEntity> {
        return repository.getFavoriteProducts()
    }

    fun getCategories() = repository.favoriteCategories
}