package com.malka.androidappp.newPhase.presentation.addProduct.activity8

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Filter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.pakatResp.PakatDetails
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.PromotionModel
import com.malka.androidappp.newPhase.presentation.addProduct.ConfirmationAddProductActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity1.SearchTagItem
import com.malka.androidappp.newPhase.presentation.addProduct.viewmodel.AddProductViewModel
import kotlinx.android.synthetic.main.activity_promotional.*
import kotlinx.android.synthetic.main.activity_promotional.progressBar
import kotlinx.android.synthetic.main.item_pakat_desgin.view.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PromotionalActivity : BaseActivity(), PakatAdapter.SetOnPakatSelected {


    val list: ArrayList<PromotionModel> = ArrayList()
    var isEdit: Boolean = false
    lateinit var pakatAdapter: PakatAdapter
    var pakatList: ArrayList<PakatDetails> = ArrayList()
    private lateinit var addProductViewModel: AddProductViewModel
    override fun onBackPressed() {
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java))
            finish()
        } else {
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotional)
        toolbar_title.text = getString(R.string.distinguish_your_product)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        setClickViewListenrs()
        setPromotionalAdaptor2()
        setUpViewModel()
//        if (AddProductObjectData.selectPromotiion != null) {
//            isEdit = true
//        }
//        if (isEdit) {
//
//            list.forEach {
//                it.is_select =
//                    it.packagename.equals(AddProductObjectData.selectPromotiion!!.packagename)
//            }
//
//        }

        addProductViewModel.getPakatList(AddProductObjectData.selectedCategoryId)
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
        addProductViewModel.getPakatRespObserver.observe(this) { pakatResp ->
            if (pakatResp.status_code == 200) {
                if (pakatResp.pakatList != null && pakatResp.pakatList.isNotEmpty()) {
                    pakatList.clear()
                    pakatList.addAll(pakatResp.pakatList)
                    if (isEdit && AddProductObjectData.selectedPakat != null) {
                        for (item in pakatList) {
                            if (item.id == AddProductObjectData.selectedPakat!!.id) {
                                item.isSelected = true
                                break
                            }
                        }
                    }
                    pakatAdapter.notifyDataSetChanged()
                } else {
                    goNextActivity()
                    HelpFunctions.ShowLongToast(
                        getString(R.string.noPackagesFound),
                        this
                    )
                }
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.noPackagesFound),
                    this
                )
            }
        }
    }

    private fun setPromotionalAdaptor2() {
        pakatAdapter = PakatAdapter(pakatList, this)
        rvPakat.apply {
            adapter = pakatAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    override fun onSelectPakat(position: Int) {
        pakatList.forEach { item ->
            item.isSelected = false
        }
        pakatList[position].isSelected = true
        pakatAdapter.notifyDataSetChanged()
        AddProductObjectData.selectedPakat = pakatList[position]
    }

    private fun setClickViewListenrs() {
        back_btn.setOnClickListener {
            onBackPressed()
        }
        button16611.setOnClickListener() {
            confirmpromotion()
        }
        no_thank_you.setOnClickListener {
//            AddProductObjectData.selectPromotiion = null
            AddProductObjectData.selectedPakat = null
            goNextActivity()
        }
    }

    private fun goNextActivity() {
        startActivity(Intent(this, ConfirmationAddProductActivity::class.java))
    }


    //Promotion Validation
    private fun validatepromotion(): Boolean {
        val list = pakatList.filter {
            it.isSelected == true
        }
        return list.size > 0
    }

    fun confirmpromotion() {
        if (!validatepromotion()) {
            showError(getString(R.string.choose_one_of_our_special_packages))
        } else {
            if (isEdit) {
                startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                    finish()
                })
            } else {
                goNextActivity()

            }
        }
    }

}

//    private fun packageSelection(list: List<PromotionModel>, position: Int) {
//        list.forEach {
//            it.is_select = false
//        }
//        list.get(position).is_select = true
//        rvPakat.post {
//            rvPakat.adapter!!.notifyDataSetChanged()
//        }
//    }
//
//

//    fun saveSelectedcheckbox() {
//        val list = list.filter {
//            it.is_select == true
//        }
//        list.forEach {
//            AddProductObjectData.selectedPakat = it
//        }
//    }

//    private fun setPromotionalAdaptor(
//        list: List<PromotionModel>
//    ) {
//        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        rvPakat.adapter = object : GenericListAdapter<PromotionModel>(
//            R.layout.item_pakat_desgin,
//            bind = { element, holder, itemCount, parent_position ->
//                holder.view.run {
//                    element.run {
//                        pkg_name.text = packagename
//                        pkg_price.text = "$packageprice ${getString(R.string.Rayal)}"
//                        parent_layout.removeAllViews()
//                        packageservice.forEach {
//                            val _view = inflater.inflate(R.layout.promotion_item, null)
//                            val pkg_service1: TextView = _view.findViewById(R.id.pkg_service1)
//                            pkg_service1.text = it
//                            parent_layout.addView(_view)
//                        }
//                        if (is_select) {
//                            bgline.setBackgroundResource(R.drawable.product_attribute_linebg)
//                            is_selectimage.show()
//
//                        } else {
//                            bgline.setBackgroundResource(R.drawable.product_attribute_bg4)
//                            is_selectimage.hide()
//                        }
//
//                        if (is_common) {
//                            common.show()
//                            is_selectimage.setImageResource(R.drawable.ic_check_black)
//                            item_bg.setBackgroundColor(
//                                ContextCompat.getColor(
//                                    this@PromotionalActivity,
//                                    R.color.bg
//                                )
//                            )
//                        } else {
//                            common.hide()
//                            is_selectimage.setImageResource(R.drawable.ic_check)
//
//                            item_bg.setBackgroundColor(
//                                ContextCompat.getColor(
//                                    this@PromotionalActivity,
//                                    R.color.textColor
//                                )
//                            )
//
//                        }
//                        main_layout.setOnClickListener {
//                            packageSelection(list, parent_position)
//                        }
//
//
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

//    list.add(
//    PromotionModel(
//    getString(R.string.Golden_Package),
//    "160",
//    arrayListOf(
//    getString(R.string.your_product_will_be_displayed_on_the_home_page),
//    getString(R.string.your_product_will_be_displayed_on_the_home_page),
//    getString(R.string.your_product_will_be_displayed_on_the_home_page)
//    ), is_common = true
//    )
//    )
//    list.add(
//    PromotionModel(
//    getString(R.string.Silver_Package),
//    "160",
//    arrayListOf(
//    getString(R.string.your_product_will_be_displayed_on_the_home_page),
//    getString(R.string.your_product_will_be_displayed_on_the_home_page),
//    getString(R.string.your_product_will_be_displayed_on_the_home_page)
//    )
//    )
//    )
//    list.add(
//    PromotionModel(
//    getString(R.string.Bronze_Package),
//    "160",
//    arrayListOf(
//    getString(R.string.your_product_will_be_displayed_on_the_home_page),
//    getString(R.string.your_product_will_be_displayed_on_the_home_page)
//    )
//    )
//    )
//    list.add(
//    PromotionModel(
//    getString(R.string.Standard_Package),
//    "160",
//    arrayListOf(getString(R.string.your_product_will_be_displayed_on_the_home_page))
//    )
//    )
//}

