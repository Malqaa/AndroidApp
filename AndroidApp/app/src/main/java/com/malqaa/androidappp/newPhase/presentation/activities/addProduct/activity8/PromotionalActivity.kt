package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity8

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.pakatResp.PakatDetails
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.PromotionModel
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.android.synthetic.main.activity_promotional.*
import kotlinx.android.synthetic.main.toolbar_main.*


class PromotionalActivity : BaseActivity(), PakatAdapter.SetOnPakatSelected {


    val list: ArrayList<PromotionModel> = ArrayList()
    var isEdit: Boolean = false
    lateinit var pakatAdapter: PakatAdapter
    var pakatList: ArrayList<PakatDetails> = ArrayList()
    private lateinit var addProductViewModel: AddProductViewModel
    override fun onBackPressed() {
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                putExtra("whereCome", "Add")
            })
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
        setFreePakageData()
        addProductViewModel.getPakatList(AddProductObjectData.selectedCategoryId)
    }

    private fun setFreePakageData() {
        containerExtraProductImageCount.hide()
        containerExtraProductVideoCount.hide()
        containerExtraPakage.hide()
        AddProductObjectData.selectedCategory?.let { selectedCategory ->
//            btnFreeProductImagesCount.text = it.freeProductImagesCount.toString()
//            btnFreeProductVidoesCount.text = it.freeProductVidoesCount.toString()
//            btnExtraProductVideoFee.text = "${it.extraProductVidoeFee} ${getString(R.string.Rayal)}"
//            btnExtraProductImageFee.text = "${it.extraProductImageFee} ${getString(R.string.Rayal)}"
//            btnSubTitleFeeFee.text = "${it.subTitleFee} ${getString(R.string.Rayal)}
            var extraImages = 0
            AddProductObjectData.images?.let {
                if (it.size > selectedCategory.freeProductImagesCount) {
                    extraImages =
                        it.size - selectedCategory.freeProductImagesCount
                    btnExtraProductImageCount.text = extraImages.toString()
                    containerExtraProductImageCount.show()
                } else {
                    containerExtraProductImageCount.hide()
                }
            }

            var extraVideos = 0
            AddProductObjectData.videoList?.let {
                if (it.size > selectedCategory.freeProductVidoesCount) {
                    extraVideos = it.size - selectedCategory.freeProductVidoesCount
                    btnExtraProductVideoCount.text = extraVideos.toString()
                    containerExtraProductVideoCount.show()
                } else {
                    containerExtraProductVideoCount.hide()
                }
            }


            if (extraVideos != 0 || extraImages != 0) {
                containerExtraPakage.show()
                val totalFee =
                    (selectedCategory.extraProductVidoeFee * extraVideos) + (selectedCategory.extraProductImageFee * extraImages)
                tvFreePakagePrice.text = "${totalFee} ${getString(R.string.Rayal)}"
            } else {
                containerExtraPakage.hide()

            }

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
                    // goNextActivity()
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
        println("hhhh " + pakatList[position].id)
    }

    private fun setClickViewListenrs() {
        back_btn.setOnClickListener {
            onBackPressed()
        }
        button16611.setOnClickListener {
                confirmpromotion()
        }
        no_thank_you.setOnClickListener {
//            AddProductObjectData.selectPromotiion = null
            AddProductObjectData.selectedPakat = null
            goNextActivity()
        }
    }

    private fun goNextActivity() {
        startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
            putExtra("whereCome", "Add")
        })
    }


    //Promotion Validation
    private fun validatepromotion(): Boolean {
        val list = pakatList.filter { it.isSelected }
        return list.isNotEmpty()
    }

    fun confirmpromotion() {
        if (pakatList.isNotEmpty()&& !validatepromotion() && (containerExtraPakage.visibility != View.VISIBLE)) {
            showError(getString(R.string.choose_one_of_our_special_packages))
        } else {
            if (isEdit) {
                startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                    putExtra("whereCome", "Add")
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

