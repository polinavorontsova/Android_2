package com.breaktime.lab2.data

import io.realm.Realm

class FavoriteProductDao(private val realm: Realm) {
    fun addProduct(product: ProductEntity) {
        realm.executeTransactionAsync(
            {
                val prod = it.createObject(ProductEntity::class.java, product.id)
                prod.setFields(product)
            },
            { println("success") },
            { println("error   " + it.message) })
    }

    fun updateProduct(product: ProductEntity) {
        realm.executeTransactionAsync(
            {
                it.insertOrUpdate(product)
            },
            { println("success") },
            { println("error   " + it.message) })
    }

    fun deleteProduct(product: ProductEntity) {
        realm.beginTransaction()
        val rows = realm.where(ProductEntity::class.java).equalTo("id", product.id).findAll()
        rows.deleteAllFromRealm()
        realm.commitTransaction()
    }

    fun readAllData(): List<ProductEntity> {
        realm.beginTransaction()
        val list = realm.where(ProductEntity::class.java).findAll().toList()
        realm.commitTransaction()
        return list
    }
}