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
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.productTags.Tags
import com.malka.androidappp.newPhase.presentation.addProduct.activity4.AddPhotoActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity2.ChooseCategoryActivity
import com.malka.androidappp.newPhase.presentation.addProduct.viewmodel.AddProductViewModel
import kotlinx.android.synthetic.main.activity_product_tage_for_add_product.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProductsTagsForAddProductActivity : BaseActivity(),
    ProductTagsAdapter.SetOnSelectedListeners {
    var selectTag: SearchTagItem? = null
    var lists: List<Tags> = ArrayList()
    private lateinit var addProductViewModel: AddProductViewModel
    private lateinit var productTagsAdapter: ProductTagsAdapter
    private lateinit var productTagsList: ArrayList<SearchTagItem>
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
                lifecycleScope.launch(Dispatchers.IO){
                    productTagsList.clear()
                    for(tag in  categoyProductTagResp.tagsList){

                        if(tag.categories!=null){
                            if(tag.categories!!.isNotEmpty()) {
                                for (tagCategoryItem in tag.categories!!) {
                                    productTagsList.add(
                                        SearchTagItem(
                                            tag.categoryid,
                                            "${tag.productTitle}-${tagCategoryItem}"
                                        )
                                    )
                                }
                            }else{
                                productTagsList.add(SearchTagItem(tag.categoryid,tag.productTitle))
                            }
                        }else{
                            productTagsList.add(SearchTagItem(tag.categoryid,tag.productTitle))
                        }

                    }
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



//                lists = categoyProductTagResp.tagsList
//                if (lists.count() > 0) {
//                    recycler_suggested_category.visibility = View.VISIBLE
//                    setCategoryAdaptor(lists)
//                } else {
//                    recycler_suggested_category.visibility = View.GONE
//                    Toast.makeText(
//                        this@ProductsTagsForAddProductActivity,
//                        getString(R.string.no_tag_found),
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
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
                AddProductObjectData.selectedCategoryId = selectTag!!.categoryID
                AddProductObjectData.selectedCategoryName = selectTag!!.title.toString()
                startActivity(Intent(this@ProductsTagsForAddProductActivity, AddPhotoActivity::class.java))
//                    startActivity(Intent(this, ChooseCategoryActivity::class.java))
//                    selectTag!!.run {
//                        ConstantObjects.categoryList.filter {
//                            it.id == categoryID
//                        }.let {
//                            if (it.size > 0) {
//                                it.get(0).run {
//                                    AddProductObjectData.subCategoryPath.add(name.toString())
//                                    val templateName = Extension.truncateString(template.toString())
//                                    AddProductObjectData.template = templateName
//                                    startActivity(Intent(this@ProductsTagsForAddProductActivity, AddPhotoActivity::class.java).apply { putExtra("Title", name.toString()) })
//                                }
//
//                            }
//                        }
//
//
//                    }

            } else {
//                val producttitle: String = textInputLayout11.getText()
//                AddProductObjectData.productTitle = producttitle
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


//    fun confirmListItem() {
//        if (!validateitem()) {
//            return
//        } else {
//            val producttitle: String = textInputLayout11.getText()
//            AddProductObjectData.productTitle = producttitle
//            startActivity(Intent(this, ChooseCategoryActivity::class.java))
//        }
//
//    }

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






//    fun getCategoryTags(category: String) {
//        HelpFunctions.startProgressBar(this)
//        try {
//            val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//
//            val call: Call<CategoryTagsResp> = malqaa.getCategoryTags(category)
//
//            call.enqueue(object : Callback<CategoryTagsResp> {
//                override fun onResponse(
//                    call: Call<CategoryTagsResp>, response: Response<CategoryTagsResp>
//                ) {
//                    if (response.isSuccessful) {
//                        if (response.body() != null) {
//                            val resp: CategoryTagsResp = response.body()!!
//                            lists = resp.tagsList
//                            if (lists.count() > 0) {
//                                recycler_suggested_category.visibility = View.VISIBLE
//                                setCategoryAdaptor(lists)
//                            } else {
//                                recycler_suggested_category.visibility = View.GONE
//                                Toast.makeText(
//                                    this@ProductsTagsForAddProductActivity,
//                                    getString(R.string.no_tag_found),
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        }
//
//
//                    } else {
//                        Toast.makeText(
//                            this@ProductsTagsForAddProductActivity,
//                            "Failed to get tags",
//                            Toast.LENGTH_LONG
//                        )
//                            .show()
//                    }
//                    HelpFunctions.dismissProgressBar()
//
//                }
//
//                override fun onFailure(call: Call<CategoryTagsResp>, t: Throwable) {
//                    Toast.makeText(
//                        this@ProductsTagsForAddProductActivity,
//                        t.message,
//                        Toast.LENGTH_LONG
//                    ).show()
//                    HelpFunctions.dismissProgressBar()
//
//                }
//            })
//        } catch (ex: Exception) {
//            throw ex
//        }
//    }
//
//    private fun setCategoryAdaptor(list: List<Tags>) {
//        recycler_suggested_category.adapter = object : GenericListAdapter<Tags>(
//            R.layout.item_suggested_categories,
//            bind = { element, holder, itemCount, position ->
//                holder.view.run {
//                    element.run {
//                        category_name_tv.text = name
//                        radioButtonSelect.isChecked = isSelect
//                        radioButtonSelect.setOnCheckedChangeListener { buttonView, isChecked ->
//                            if (isChecked) {
//                                list.forEach {
//                                    it.isSelect = false
//                                }
//                                list.get(position).isSelect = true
//                                recycler_suggested_category.post {
//                                    recycler_suggested_category.adapter!!.notifyDataSetChanged()
//                                }
//                            }
//                            selectTag = element
//                        }
//                    }
//                }
//            }
//        ) {
//            override fun getFilter(): Filter {
//                TODO("Not yet implemented")
//            }
//
//        }.apply {
//            submitList(
//                list
//            )
//        }
//    }

    /**importnat comment code*/
//    button2.setOnClickListener {
//        confirmListItem()
//    }
//    button_2.setOnClickListener {
//        if (validateitem()) {
//            if (productTagsList.size > 0 && selectTag != null) {
//                selectTag!!.run {
//                    ConstantObjects.categoryList.filter {
//                        it.id == categoryid
//                    }.let {
//                        if (it.size > 0) {
//                            it.get(0).run {
//                                StaticClassAdCreate.subCategoryPath.add(name.toString())
//                                val templateName =
//                                    Extension.truncateString(template.toString())
//                                StaticClassAdCreate.template = templateName
//                                startActivity(
//                                    Intent(
//                                        this@ProductsTagsForAddProductActivity,
//                                        AddPhotoActivity::class.java
//                                    ).apply {
//                                        putExtra("Title", name.toString())
//                                    })
//                            }
//
//                        }
//                    }
//
//
//                }
//
//
//            } else {
//                val producttitle: String = textInputLayout11.getText()
//                StaticClassAdCreate.producttitle = producttitle
//                startActivity(Intent(this, ChooseCategoryActivity::class.java))
//            }
//
//
//        }
//
//    }

}