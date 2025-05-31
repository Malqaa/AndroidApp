package com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.databinding.ParenetCategoryItemBinding
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.models.homeCategoryProductResp.CategoryProductItem
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.SearchCategoryActivity
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show

class CategoryProductAdapter(
    private var categoryProductHomeList: ArrayList<CategoryProductItem>,
    private var setOnSelectedProductInCategory: SetOnSelectedProductInCategory
) :
    RecyclerView.Adapter<CategoryProductAdapter.CategoryProductHolder>(),
    SetOnProductItemListeners {
    lateinit var context: Context

    class CategoryProductHolder(var viewBinding: ParenetCategoryItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductHolder {
        context = parent.context
        return CategoryProductHolder(
            ParenetCategoryItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = categoryProductHomeList.size

    override fun onBindViewHolder(holder: CategoryProductHolder, position: Int) {
        holder.viewBinding.categoryHeaderContainer.show()
        holder.viewBinding.productListLayout.hide()
        holder.viewBinding.detailTv.text = ""
        holder.viewBinding.categoryNameTv.text = categoryProductHomeList[position].name
        holder.viewBinding.categoryNameTv2.text = categoryProductHomeList[position].name
        holder.viewBinding.viewAllCategoriesProduct.setOnClickListener {
            context.startActivity(Intent(context, SearchCategoryActivity::class.java).apply {
                putExtra("CategoryDesc", categoryProductHomeList[position].name)
                putExtra("CategoryID", categoryProductHomeList[position].catId)
                putExtra("ComeFrom", ConstantObjects.search_categoriesDetails)
                putExtra("SearchQuery", "")

            })
        }
        setHomeProductAdaptor(
            categoryProductHomeList[position].listProducts,
            holder.viewBinding.productRcv,
            categoryProductHomeList[position].catId
        )
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setHomeProductAdaptor(
        product: List<Product>?,
        productRcv: RecyclerView,
        categoryId: Int
    ) {
        val productList: ArrayList<Product> = ArrayList()
        if (product != null) {
            productList.clear()
            productList.addAll(product)
        }
        val productAdapter = ProductHorizontalAdapter(productList, this, categoryId, true)
        productRcv.apply {
            adapter = productAdapter
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
        }
    }


    override fun onProductSelect(position: Int,productID:Int,categoryID:Int,userId:String,providerId:String,businessAccountId:String) {
        setOnSelectedProductInCategory.onSelectedProductInCategory(position, productID, categoryID)
    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        setOnSelectedProductInCategory.onAddProductInCategoryToFav(position, productID, categoryID)
    }

    override fun onShowMoreSetting(position: Int, productID: Int, categoryID: Int) {
    }


    interface SetOnSelectedProductInCategory {
        fun onSelectedProductInCategory(position: Int, productID: Int, categoryID: Int)
        fun onAddProductInCategoryToFav(position: Int, productID: Int, categoryID: Int)
    }
}