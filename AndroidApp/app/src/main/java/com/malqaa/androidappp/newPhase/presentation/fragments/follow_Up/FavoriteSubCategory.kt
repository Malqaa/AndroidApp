package com.malqaa.androidappp.fragments.follow_Up

import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FavoriteSubCategoryLayoutBinding
import com.malqaa.androidappp.databinding.FragmentFavoriteSubCategoryBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity1.ListCategoryViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class FavoriteSubCategory : BaseActivity<FragmentFavoriteSubCategoryBinding>() {
    val subCategory: ArrayList<Selection> = ArrayList()
    var allCategoryList: ArrayList<Category>? = null
    var categoryIdList: ArrayList<Int>? = null
    private var listCategoryViewModel: ListCategoryViewModel? = null
    var categoryid = 0
    var categoryName = ""
    var lastCategoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = FragmentFavoriteSubCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryIdList = arrayListOf()
        initView()

        allCategoryList = arrayListOf()

        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        categoryid = intent.getIntExtra(ConstantObjects.categoryIdKey, 0)
        categoryName = intent.getStringExtra(ConstantObjects.categoryName).toString()
        setUpViewModel()

        binding.btnDone.setOnClickListener {
            listCategoryViewModel!!.followCategoryAPI(categoryIdList!!, this)
        }

        listCategoryViewModel!!.getSubCategoriesByCategoryID(categoryid)
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
        listCategoryViewModel!!.error.observe(this) {
            HelpFunctions.ShowLongToast(it, this)

        }
        listCategoryViewModel!!.isFollowCategory.observe(this) { isFollow ->

            if (isFollow) {
                finish()
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
        listCategoryViewModel!!.categoryListObserver.observe(this) { categoryListObserver ->
            if (categoryListObserver.status_code == 200) {
                // Add all items to allCategoryList
                if (!categoryListObserver.categoryList.isNullOrEmpty()) {
                    // Add all categories
                    allCategoryList?.addAll(categoryListObserver.categoryList)
                    // Set the last category's id as the new lastCategoryId
                    lastCategoryId = categoryListObserver.categoryList.last().id
                } else {
                    lastCategoryId = 0
                }

                // Check if there are categories to process
                if (lastCategoryId != 0) {
                    listCategoryViewModel!!.getSubCategoriesByCategoryID(lastCategoryId)
                    // Pass the updated list of categories to the adapter
                    subCategoryAdapter(allCategoryList!!)
                } else {
                    if (allCategoryList?.isEmpty() == true) {
                        categoryIdList?.add(categoryid)
                    }
                }

                // Ensure to update the adapter with the full list of categories
                if (!allCategoryList.isNullOrEmpty()) {
                    subCategoryAdapter(allCategoryList!!)
                }
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.noSubCategoryFound),
                    this
                )
            }
        }
    }

    private fun initView() {
        binding.toolbarMain.toolbarTitle.visibility = View.GONE
        listCategoryViewModel = ViewModelProvider(this).get(ListCategoryViewModel::class.java)
    }


    private fun subCategoryAdapter(list: ArrayList<Category>) {
        binding.allSubCategoryRcv.adapter = object : GenericListAdapter<Category>(
            R.layout.favorite_sub_category_layout,
            bind = { element, holder, itemCount, position ->

                // Use view binding to bind each item in the RecyclerView
                val itemBinding = FavoriteSubCategoryLayoutBinding.bind(holder.itemView)

                holder.view.run {
                    element.run {
                        itemBinding.categoryNameTv.text = name
                        if (allCategoryList?.get(position)?.isSelected == true) {

                            itemBinding.bgline.show()
                            itemBinding.isSelectimage.show()
                            itemBinding.categoryIcon.borderColor = ContextCompat.getColor(
                                context,
                                R.color.bg
                            )

                            categoryIdList?.add(allCategoryList?.get(position)?.id ?: 0)
                        } else {
                            itemBinding.bgline.hide()
                            itemBinding.isSelectimage.hide()
                            itemBinding.categoryIcon.borderColor = ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        }

                        holder.itemView.setOnClickListener {

                            allCategoryList?.get(position)?.isSelected =
                                (allCategoryList?.get(position)?.isSelected != true)
                            binding.allSubCategoryRcv.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }


}