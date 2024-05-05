package com.malqaa.androidappp.fragments.follow_Up

import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity1.ListCategoryViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import kotlinx.android.synthetic.main.favorite_sub_category_layout.view.*
import kotlinx.android.synthetic.main.fragment_favorite_sub_category.*
import kotlinx.android.synthetic.main.fragment_sub_categories.progressBar
import kotlinx.android.synthetic.main.toolbar_main.*


class FavoriteSubCategory : BaseActivity() {
    val subCategory: ArrayList<Selection> = ArrayList()
    var allCategoryList: ArrayList<Category>? = null
    var categoryIdList: ArrayList<Int>? = null
    private var listCategoryViewModel: ListCategoryViewModel? = null
    var categoryid = 0
    var categoryName = ""
    var lastCategoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_favorite_sub_category)
        categoryIdList = arrayListOf()
        initView()

        allCategoryList = arrayListOf()

        back_btn.setOnClickListener {
            finish()
        }
        categoryid = intent.getIntExtra(ConstantObjects.categoryIdKey, 0)
        categoryName = intent.getStringExtra(ConstantObjects.categoryName).toString()
        setUpViewModel()

        btnDone.setOnClickListener {
            listCategoryViewModel!!.followCategoryAPI(categoryIdList!!, this)
        }

        listCategoryViewModel!!.getSubCategoriesByCategoryID(categoryid)
    }

    private fun setUpViewModel() {
        listCategoryViewModel = ViewModelProvider(this).get(ListCategoryViewModel::class.java)
        listCategoryViewModel!!.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
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
                lastCategoryId = if (!categoryListObserver.categoryList.isNullOrEmpty()) {
                    allCategoryList?.add(categoryListObserver.categoryList[0])
                    categoryListObserver.categoryList[0].id
                } else {
                    0
                }
                if (lastCategoryId != 0) {
                    listCategoryViewModel!!.getSubCategoriesByCategoryID(lastCategoryId)
                    subCategoryAdapter(allCategoryList!!)

                } else {
                    if (allCategoryList?.isEmpty() == true)
                        categoryIdList?.add(categoryid)
                }

                if (allCategoryList != null) {
                    if (allCategoryList?.size != 0) {
                        subCategoryAdapter(allCategoryList!!)
                    }
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
        toolbar_title.visibility = View.GONE
        listCategoryViewModel = ViewModelProvider(this).get(ListCategoryViewModel::class.java)

    }


    private fun subCategoryAdapter(list: ArrayList<Category>) {
        all_sub_category_rcv.adapter = object : GenericListAdapter<Category>(
            R.layout.favorite_sub_category_layout,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        category_name_tv.text = name
                        if (allCategoryList?.get(position)?.isSelected == true) {

                            bgline.show()
                            is_selectimage.show()
                            category_icon.borderColor = ContextCompat.getColor(
                                context,
                                R.color.bg
                            )

                            categoryIdList?.add(allCategoryList?.get(position)?.id ?: 0)
                        } else {
                            bgline.hide()
                            is_selectimage.hide()
                            category_icon.borderColor = ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        }

                        holder.itemView.setOnClickListener {

                            allCategoryList?.get(position)?.isSelected =
                                (allCategoryList?.get(position)?.isSelected != true)
                            all_sub_category_rcv.adapter?.notifyDataSetChanged()
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