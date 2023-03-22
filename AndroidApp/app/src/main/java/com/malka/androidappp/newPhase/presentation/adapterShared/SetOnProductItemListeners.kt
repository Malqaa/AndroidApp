package com.malka.androidappp.newPhase.presentation.adapterShared

import java.text.FieldPosition

interface SetOnProductItemListeners {
    fun onProductSelect(position: Int,productID:Int,categoryID:Int)
    fun onAddProductToFav(position: Int,productID:Int,categoryID:Int)
}