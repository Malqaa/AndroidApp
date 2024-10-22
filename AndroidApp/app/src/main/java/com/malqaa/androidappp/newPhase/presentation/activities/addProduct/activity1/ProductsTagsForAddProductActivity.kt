package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity1

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityProductTageForAddProductBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity2.ChooseCategoryActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity4.AddPhotoActivity
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsTagsForAddProductActivity : BaseActivity<ActivityProductTageForAddProductBinding>(),
    ProductTagsAdapter.SetOnSelectedListeners {
    private var selectTag: Category? = null
    private var listCategoryViewModel: ListCategoryViewModel? = null
    private var productTagsAdapter: ProductTagsAdapter? = null
    private var productTagsList: ArrayList<Category>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductTageForAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.add_product)
        setViewClickListeners()
        setUpViewModel()
        setProductTagsAdapters()
    }

    private fun setProductTagsAdapters() {
        productTagsList = ArrayList()
        productTagsAdapter = ProductTagsAdapter(productTagsList ?: arrayListOf(), this)
        binding.recyclerSuggestedCategory.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = productTagsAdapter
        }
    }

    private fun setUpViewModel() {
        listCategoryViewModel = ViewModelProvider(this).get(ListCategoryViewModel::class.java)
        listCategoryViewModel!!.isLoading.observe(this) {
            if (it)
                binding.progressBar.show()
            else
                binding.progressBar.hide()
        }
        listCategoryViewModel!!.isNetworkFail.observe(this) {
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
        listCategoryViewModel!!.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {

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
        }
        listCategoryViewModel!!.getListCategoriesByProductNameObserver.observe(this) { categoyProductTagResp ->
            if (categoyProductTagResp.status_code == 200) {
                lifecycleScope.launch(Dispatchers.IO) {
                    productTagsList?.clear()
                    categoyProductTagResp.tagsList?.let { productTagsList?.addAll(it) }
                    withContext(Dispatchers.Main) {
                        productTagsAdapter?.notifyDataSetChanged()
                        if ((productTagsList ?: arrayListOf()).size > 0) {
                            binding.recyclerSuggestedCategory.show()
                            productTagsAdapter?.notifyDataSetChanged()
                        } else {
                            binding.recyclerSuggestedCategory.hide()
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
        productTagsList?.forEach {
            it.isSelect = false
        }
        productTagsList?.get(position)?.isSelect = true
        selectTag = productTagsList?.get(position)
        productTagsAdapter?.notifyDataSetChanged()

    }

    private fun setViewClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }

        binding.button2.setOnClickListener {
            startActivity(Intent(this, ChooseCategoryActivity::class.java))
            //confirmListItem()
        }
        binding.button2.setOnClickListener {
            if ((productTagsList ?: arrayListOf()).size > 0 && selectTag != null) {
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

        binding.textInputLayout11._view2()
            .setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (validateitem()) {
                        listCategoryViewModel!!.getListCategoriesByProductName(binding.textInputLayout11.getText())
                        // getCategoryTags(textInputLayout11.getText())
                    }
                    return@OnEditorActionListener true
                }
                true
            })
        binding.textInputLayout11._attachInfoClickListener {
            if (validateitem()) {
                listCategoryViewModel!!.getListCategoriesByProductName(binding.textInputLayout11.getText())
                // getCategoryTags(textInputLayout11.getText())
            }
        }
    }


    private fun validateitem(): Boolean {

        val Inputname = binding.textInputLayout11.getText().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            binding.textInputLayout11.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            binding.textInputLayout11.error = null
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listCategoryViewModel?.closeAllCall()
        listCategoryViewModel?.baseCancel()
        listCategoryViewModel = null
        productTagsList = null
        productTagsAdapter = null
    }

}