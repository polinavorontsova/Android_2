package com.breaktime.lab2.data

import android.graphics.Bitmap
import com.breaktime.lab2.util.toBitmap
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

open class ProductEntity(
    @PrimaryKey
    var id: Long = 0,
    var title: String = "",
    var price: Double = 0.0,
    var description: String = "",
    var category: String = "",
    var image: ByteArray? = null,
    var rating: Double = 0.0,
    var isInStock: Boolean = true,
    var label: String = "Add label"
) : RealmObject() {
    @Ignore
    var isVisible: Boolean = false

    @Ignore
    private var _bmp: Bitmap? = null
    val bmp: Bitmap?
        get() {
            if (_bmp == null)
                _bmp = image?.toBitmap()
            return _bmp
        }

    fun setFields(product: ProductEntity) {
        title = product.title
        price = product.price
        description = product.description
        category = product.category
        image = product.image
        rating = product.rating
        isInStock = product.isInStock
        label = product.label
    }

    fun copy(
        id: Long = this.id,
        title: String = this.title,
        price: Double = this.price,
        description: String = this.description,
        category: String = this.category,
        image: ByteArray? = this.image,
        rating: Double = this.rating,
        isInStock: Boolean = this.isInStock,
        label: String = this.label
    ) =
        ProductEntity(
            id,
            title,
            price,
            description,
            category,
            image,
            rating,
            isInStock,
            label
        )
}