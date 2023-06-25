package com.malka.androidappp.newPhase.presentation.addProduct.activity1

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.productTags.Tags
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.presentation.addProduct.activity2.ChooseCategoryActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity4.AddPhotoActivity
import com.malka.androidappp.newPhase.presentation.addProduct.viewmodel.AddProductViewModel
import kotlinx.android.synthetic.main.activity_product_tage_for_add_product.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProductsTagsForAddProductActivity : BaseActivity(),
    ProductTagsAdapter.SetOnSelectedListeners {
    var selectTag: Category? = null
    var lists: List<Tags> = ArrayList()
    private lateinit var addProductViewModel: AddProductViewModel
    private lateinit var productTagsAdapter: ProductTagsAdapter
    private lateinit var productTagsList: ArrayList<Category>
    private var addedProductObjectData: AddProductObjectData = AddProductObjectData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_tage_for_add_product)
        toolbar_title.text = getString(R.string.add_product)
        setViewClickListeners()
        setUpViewModel()
        setProductTagsAdapters()

    }

    private fun setProductTagsAdapters() {
        productTagsList = ArrayList()
        productTagsAdapter = ProductTagsAdapter(productTagsList, this)
        recycler_suggested_category.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = productTagsAdapter
        }
    }

    private fun setUpViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        addProductViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        addProductViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        addProductViewModel.errorResponseObserver.observe(this) {
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message!!,
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        addProductViewModel.getListCategoriesByProductNameObserver.observe(this) { categoyProductTagResp ->
            if (categoyProductTagResp.status_code == 200) {
                lifecycleScope.launch(Dispatchers.IO) {
                    productTagsList.clear()
                    categoyProductTagResp.tagsList?.let { productTagsList.addAll(it) }
//                    for (tag in categoyProductTagResp.tagsList) {
////                        productTagsList.add(
////                            SearchTagItem(
////                                tag.productCategoryId,
////                                tag.category
////                            )
////                            //  "${tag.productTitle}-${tagCategoryItem}"
////                        )
//                        productTagsList.add(tag)
//
//                    }
                    withContext(Dispatchers.Main) {
                        productTagsAdapter.notifyDataSetChanged()
                        if (productTagsList.size > 0) {
                            recycler_suggested_category.show()
                            productTagsAdapter.notifyDataSetChanged()
                        } else {
                            recycler_suggested_category.hide()
                            HelpFunctions.ShowLongToast(
                                getString(R.string.no_tag_found),
                                this@ProductsTagsForAddProductActivity
                            )
                        }
                    }

                }



            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.no_tag_found),
                    this
                )
            }
        }
    }

    override fun onSelectTagItem(position: Int) {
        productTagsList.forEach {
            it.isSelect = false
        }
        productTagsList[position].isSelect = true
        selectTag = productTagsList[position]
        productTagsAdapter.notifyDataSetChanged()

    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }

        button2.setOnClickListener {
            startActivity(Intent(this, ChooseCategoryActivity::class.java))
            //confirmListItem()
        }
        button_2.setOnClickListener {
            if (productTagsList.size > 0 && selectTag != null) {
                AddProductObjectData.selectedCategoryId = selectTag!!.id
                AddProductObjectData.selectedCategoryName = selectTag!!.category.toString()
                AddProductObjectData.selectedCategory = selectTag
                startActivity(
                    Intent(
                        this@ProductsTagsForAddProductActivity,
                        AddPhotoActivity::class.java
                    )
                )

            } else {
                startActivity(Intent(this, ChooseCategoryActivity::class.java))
            }

        }

        textInputLayout11._view2()
            .setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (validateitem()) {
                        addProductViewModel.getListCategoriesByProductName(textInputLayout11.getText())
                        // getCategoryTags(textInputLayout11.getText())
                    }
                    return@OnEditorActionListener true
                }
                true
            })
        textInputLayout11._attachInfoClickListener {
            if (validateitem()) {
                addProductViewModel.getListCategoriesByProductName(textInputLayout11.getText())
                // getCategoryTags(textInputLayout11.getText())
            }
        }
    }



    private fun validateitem(): Boolean {

        val Inputname = textInputLayout11.getText().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            textInputLayout11.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textInputLayout11.error = null
            true
        }
    }



}