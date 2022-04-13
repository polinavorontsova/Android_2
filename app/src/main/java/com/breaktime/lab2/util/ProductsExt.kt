package com.breaktime.lab2.util

import com.breaktime.lab2.api.model.Product
import com.breaktime.lab2.data.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun Product.toProductEntity(): ProductEntity {
    var _bmp: ByteArray?
    runBlocking(Dispatchers.IO) {
        _bmp = bmp?.toByteArray()
    }
    return ProductEntity(
        id,
        title,
        price,
        description,
        category,
        _bmp,
        rating.rate,
        isInStock
    )
}