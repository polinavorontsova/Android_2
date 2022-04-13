package com.breaktime.lab2.repository

import androidx.lifecycle.MutableLiveData
import com.breaktime.lab2.api.ProductApi
import com.breaktime.lab2.api.model.Product
import com.breaktime.lab2.data.FavoriteProductDao
import com.breaktime.lab2.data.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.random.Random

class Repository(private var productApi: ProductApi, private var productDao: FavoriteProductDao) {
    lateinit var allProducts: List<Product>
    lateinit var allCategories: List<String>
    val favoriteCategories = MutableLiveData<List<String>?>()

    init {
        MainScope().launch(Dispatchers.IO) {
            val products = async { getProducts() }
            val categories = async { getCategories() }
            allProducts = products.await()
            allCategories = categories.await()
        }
    }

    private suspend fun getProducts(): List<Product> {
        val list = productApi.getProducts().map {
            it.copy(isInStock = Random.nextBoolean())
        }
        return list
    }

    private suspend fun getCategories(): List<String> {
        return productApi.getCategories()
    }

    fun getFavoriteProducts(): List<ProductEntity> {
        val list = productDao.readAllData()
        favoriteCategories.value = list.map { it.category }.distinct()
        return list
    }

    fun addFavorite(productEntity: ProductEntity) {
        productDao.addProduct(productEntity)
    }

    fun deleteFavorite(productEntity: ProductEntity) {
        productDao.deleteProduct(productEntity)
    }

    fun updateFavorite(productEntity: ProductEntity) {
        productDao.updateProduct(productEntity)
    }
}