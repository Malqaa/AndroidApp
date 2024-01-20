package com.malqaa.androidappp.newPhase.data.network.service

interface SetOnProductItemListeners {
    fun onProductSelect(position: Int,productID:Int,categoryID:Int)
    fun onAddProductToFav(position: Int,productID:Int,categoryID:Int)
    fun onShowMoreSetting(position: Int,productID:Int,categoryID:Int)
}