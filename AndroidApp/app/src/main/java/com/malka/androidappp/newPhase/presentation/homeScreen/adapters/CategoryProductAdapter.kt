package com.malka.androidappp.newPhase.presentation.homeScreen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malka.androidappp.databinding.ParenetCategoryItemBinding
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.homeCategoryProductResp.CategoryProductItem
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners

class CategoryProductAdapter(var categoryProductHomeList: ArrayList<CategoryProductItem>,var setOnSelectedProductInCategory:SetOnSelectedProductInCategory) :
    RecyclerView.Adapter<CategoryProductAdapter.CategoryProductHolder>(),
    SetOnProductItemListeners {
    lateinit var context: Context
    class CategoryProductHolder(var viewBinding: ParenetCategoryItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductHolder {
        context = parent.context
        return CategoryProductHolder(
            ParenetCategoryItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = categoryProductHomeList.size

    override fun onBindViewHolder(holder: CategoryProductHolder, position: Int) {
//        when (categoryProductHomeList[position].typeName) {
//            "category" -> {
//                holder.viewBinding.categoryHeaderContainer.show()
//                holder.viewBinding.productListLayout.hide()
//            }
//            "list" -> {
//                holder.viewBinding.categoryHeaderContainer.hide()
//                holder.viewBinding.productListLayout.show()
//            }
//        }
        holder.viewBinding.categoryHeaderContainer.show()
        holder.viewBinding.productListLayout.hide()
        Extension.loadThumbnail(
            context,
            categoryProductHomeList[position].image,
            holder.viewBinding.categoryIconIv,
            null
        )
       // holder.viewBinding.detailTv.text = categoryProductHomeList[position].detail
        holder.viewBinding.detailTv.text = ""
        holder.viewBinding.categoryNameTv.text = categoryProductHomeList[position].name
        holder.viewBinding.categoryNameTv2.text = categoryProductHomeList[position].name
//                        viewAllCategoriesProduct.setOnClickListener {
//                            startActivity(
//                                Intent(requireContext(), SearchCategoryActivity::class.java).apply {
//                                    putExtra("CategoryDesc", category_id)
//                                    putExtra("SearchQuery", "")
//                                    putExtra("isMapShow", category_id.equals("Property"))
//                                })
        // }
        setHomeProductAdaptor(categoryProductHomeList[position].listProducts, holder.viewBinding.productRcv,categoryProductHomeList[position].catId)
    }

    private fun setHomeProductAdaptor(
        product: List<Product>?,
        productRcv: RecyclerView,
        categoryId: Int
    ) {
        val porductList:ArrayList<Product> = ArrayList()
        if(product!=null){
            porductList.clear()
            porductList.addAll(product)
        }
      var porductAdatper = ProductHorizontalAdapter(porductList, this,categoryId,true)
        productRcv.apply {
            adapter = porductAdatper
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
        }
    }

    override fun onProductSelect(position: Int, productID: Int,categoryID:Int) {
        setOnSelectedProductInCategory.onSelectedProductInCategory(position,productID, categoryID)
    }

    override fun onAddProductToFav(position: Int, productID: Int,categoryID:Int) {
        setOnSelectedProductInCategory.onAddProductInCategoryToFav(position,productID, categoryID)
    }

    override fun onShowMoreSetting(position: Int, productID: Int, categoryID: Int) {

    }


    interface SetOnSelectedProductInCategory{
        fun onSelectedProductInCategory(position:Int,productID:Int,categoryID:Int)
        fun onAddProductInCategoryToFav(position:Int,productID:Int,categoryID:Int)
    }
}