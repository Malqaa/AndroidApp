package com.malqaa.androidappp.newPhase.presentation.activities.addProduct

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.helper.*
import com.malqaa.androidappp.newPhase.utils.helper.widgets.searchdialog.SearchListItem
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.TimeAuctionSelection
import com.malqaa.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionObject
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity2.ChooseCategoryActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity4.AddPhotoActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity6.PricingActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity7.ListingDurationActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity8.PromotionalActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.android.synthetic.main.activity_confirmation_add_product.*
import kotlinx.android.synthetic.main.my_product_details.containerMainProduct
import kotlinx.android.synthetic.main.my_product_details.containerShareAndFav
import kotlinx.android.synthetic.main.toolbar_main.*
import java.io.File

class ConfirmationAddProductActivity : BaseActivity() {
    private lateinit var addProductViewModel: AddProductViewModel
    private var pakagePrice = 0f
    private var fixedPriceFee = 0f
    private var auctionEnableFee = 0f
    private var negotiationFee = 0f
    private var subTitleFee = 0f
    private var extraImageFee = 0f
    private var extraVideoFee = 0f
    private var totalPrice = 0f
    private var productPublishPriceFee = 0f
    private var couponId = 0
    private var couponDiscountValue = 0f
    private var totalAfterDiscount = 0f
    private var productDetails: Product? = null
    private var shippingOptionText: StringBuilder? = null
    private var pickup: StringBuilder? = null

    var pakatId = ""

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setData()
        if (intent.getStringExtra("whereCome").equals("repost")) {
            addProductViewModel.getProductPaymentOptions(intent.getIntExtra("productID", 0))
            addProductViewModel.getProductShippingOptions(intent.getIntExtra("productID", 0))
            addProductViewModel.getProductBankAccounts(intent.getIntExtra("productID", 0))
            addProductViewModel.getProductDetailsById(intent.getIntExtra("productID", 0))
        } else setSummeryData()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation_add_product)
//        containerAuctionFee.hide()
//        containerNegotiationFee.hide()
//        containerFixedPriceFee.hide()
        toolbar_title.text = getString(R.string.distinguish_your_product)
        setViewClickListeners()
        setData()
        setUpViewModel()
        // println("hhhh " + AddProductObjectData.selectedPakat?.id + " " + AddProductObjectData.selectedCategoryId)
        //  getCartSummery()
        if (intent.getStringExtra("whereCome").equals("repost")) {
            addProductViewModel.getProductPaymentOptions(intent.getIntExtra("productID", 0))
            addProductViewModel.getProductShippingOptions(intent.getIntExtra("productID", 0))
            addProductViewModel.getProductBankAccounts(intent.getIntExtra("productID", 0))
            addProductViewModel.getProductDetailsById(intent.getIntExtra("productID", 0))
        } else setSummeryData()
    }

    @SuppressLint("SetTextI18n")
    fun setData() {
        tvQuantityData.text = null
        tvTitleData.text = null
        tvSubTitleData.text = null
        tvProductDetail.text = null
        product_type.text = null
        tvItemCondition.text = null
        purchasing_price_tv.text = null
        auction_start_price_tv.text = null
        minimum_price_tv.text = null
        tvShippingOption.text = null
        tv_package_price.text = null

        if (ConstantObjects.isModify) {
            edit_selected_package.hide()
            titleCoupon.hide()
            layEdtCoupon.hide()
        }
        AddProductObjectData.images?.let { images ->
            images.filter {
                if (it.uri != null)
                    (it.is_main || it.isMainMadia)
                else
                    (it.is_main || it.isMainMadia)
            }.let {
                if (it.isNotEmpty()) {
                    if (it[0].uri != null)
                        selectedImages.setImageURI(it[0].uri)
                    else {
                        getPicassoInstance().load(it[0].url?.replace("http", "https"))
                            .placeholder(R.drawable.splash_logo).error(R.drawable.splash_logo)
                            .into(selectedImages)
                    }

                }
            }
        }

        product_type.text = AddProductObjectData.selectedCategoryName

        if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
            tvTitleData.text = AddProductObjectData.itemTitleAr
            tvSubTitleData.text = AddProductObjectData.subtitleAr
            tvProductDetail.text = AddProductObjectData.itemDescriptionAr
        } else {
            tvTitleData.text = AddProductObjectData.itemTitleEn
            tvSubTitleData.text = AddProductObjectData.subtitleEn
            tvProductDetail.text = AddProductObjectData.itemDescriptionEn
        }
        tvItemCondition.text =
            if (AddProductObjectData.productCondition == 1) getString(R.string.used) else getString(
                R.string.New
            )

        tvQuantityData.text = AddProductObjectData.quantity
        if (AddProductObjectData.priceFixedOption) {
            tvFixedPriceOptionSale.show()
        } else {
            tvFixedPriceOptionSale.hide()
        }
        if (AddProductObjectData.auctionOption) {
            tvAuctionOpitonSale.show()
        } else {
            tvAuctionOpitonSale.hide()
        }

        if (AddProductObjectData.isNegotiablePrice) {
            negotiable_tv.text = getString(R.string.Yes)
            tvNegotiationPriceOptionSale.hide()
        } else {
            negotiable_tv.text = getString(R.string.No)
            tvNegotiationPriceOptionSale.hide()
        }
        /**Back**/
        tvCashOptionPayment.hide()
        tvSaudiBankDepositOptionPayment.hide()
        tvMadaOptionPayment.hide()
        tvCardOptionPayment.hide()
        if(AddProductObjectData.paymentOptionList?.isEmpty() == true){
            layPaymentOption.hide()
        }else{
            layPaymentOption.show()
        }

        AddProductObjectData.paymentOptionList?.let { paymentOptionList ->
            for (item in paymentOptionList) {
                when (item) {
                    AddProductObjectData.PAYMENT_OPTION_BANk -> {
                        tvSaudiBankDepositOptionPayment.show()
                    }

                    AddProductObjectData.PAYMENT_OPTION_CASH -> {
                        tvCashOptionPayment.show()
                    }

                    AddProductObjectData.PAYMENT_OPTION_Mada -> {
                        tvMadaOptionPayment.show()
                    }

                    AddProductObjectData.PAYMENT_OPTION_MasterCard -> {
                        tvCardOptionPayment.show()
                    }
                }
            }
        }

//        val shippingOptionText = StringBuilder("")
//        tvShippingOption.text = shippingOptionText
        AddProductObjectData.shippingOptionSelections?.let {

//            for (item in it) {
//                shippingOptionText += "${item.name},"
//            }
//            shippingOptionText = it[0].name

            tvShippingOption.text = ""
            tvPickupOptionData.text = ""
            for (i in it) {
//                tvShippingOption.text = shippingOptionText
                when (i) {
                    ConstantObjects.pickUp_Must -> {
                        tvPickupOptionData.append(getString(R.string.mustPickUp))
                    }

                    ConstantObjects.pickUp_No -> {
                        tvPickupOptionData.append(getString(R.string.noPickUp))
                    }

                    ConstantObjects.pickUp_Available -> {
                        tvPickupOptionData.append(getString(R.string.pickUpAvaliable))
                    }

                    ConstantObjects.shippingOption_integratedShippingCompanyOptions -> {
                        tvShippingOption.append(getString(R.string.integratedShippingCompanies))
                    }

                    ConstantObjects.shippingOption_freeShippingWithinSaudiArabia -> {
                        tvShippingOption.append(getString(R.string.free_shipping_within_Saudi_Arabia))
                    }

                    ConstantObjects.shippingOption_arrangementWillBeMadeWithTheBuyer -> {
                        tvShippingOption.append(getString(R.string.arrangementWillBeMadeWithTheBuyer))
                    }
                }
            }

        }

        if (AddProductObjectData.auctionOption) {
            containerAuction.show()
            if (AddProductObjectData.selectTimeAuction?.customOption == true) {
                tvDateAuction.text = AddProductObjectData.selectTimeAuction?.text
            } else {
                tvDateAuction.text =
                    HelpFunctions.getAuctionClosingTime(AddProductObjectData.selectTimeAuction?.endTime.toString())
//                AddProductObjectData.selectTimeAuction?.endTimeUTC
            }

        } else {
            containerAuction.hide()
        }

        // timingData.text = "${AddProductObjectData.endtime} ${AddProductObjectData.timepicker}"
//        timingData.text = "${AddProductObjectData.endtime}"

//        if (AddProductObjectData.selectPromotiion == null) {
//            select_package_layout.hide()
//
//        } else {
//            package_name_tv.text = AddProductObjectData.selectPromotiion!!.packagename
//            package_price_tv.text =
//                "${AddProductObjectData.selectPromotiion!!.packageprice} ${getString(R.string.rial)}"
//        }
        //=====setSum
        setSummeryData()

    }

    private fun showPaymentMethod(paymentOptionList: ArrayList<ShippingOptionObject>) {
        val paymentList = ArrayList<Int>()
        if(paymentOptionList.isEmpty()){
            layPaymentOption.hide()
        }else{
            layPaymentOption.show()
        }
        for (item in paymentOptionList) {
            when (item.paymentOptionId) {
                AddProductObjectData.PAYMENT_OPTION_CASH -> {
                    tvCashOptionPayment.show()
                    paymentList.add(AddProductObjectData.PAYMENT_OPTION_CASH)
                }

                AddProductObjectData.PAYMENT_OPTION_BANk -> {
                    tvSaudiBankDepositOptionPayment.show()
                    paymentList.add(AddProductObjectData.PAYMENT_OPTION_BANk)
                }

                AddProductObjectData.PAYMENT_OPTION_Mada -> {
                    tvMadaOptionPayment.show()
                    paymentList.add(AddProductObjectData.PAYMENT_OPTION_Mada)
                }

                AddProductObjectData.PAYMENT_OPTION_MasterCard -> {
                    tvCardOptionPayment.show()
                    paymentList.add(AddProductObjectData.PAYMENT_OPTION_MasterCard)
                }
            }
        }

        AddProductObjectData.paymentOptionList = paymentList
    }

    private fun showShoppingOption(shippingOptionObject: ShippingOptionObject) {
        val shippingList = ArrayList<Int>()

        when (shippingOptionObject.shippingOptionId) {
            ConstantObjects.shippingOption_integratedShippingCompanyOptions -> {
                AddProductObjectData.pickUpOption =
                    ConstantObjects.shippingOption_integratedShippingCompanyOptions
                shippingList.add(ConstantObjects.shippingOption_integratedShippingCompanyOptions)
                shippingOptionText?.append(getString(R.string.integratedShippingCompanies))
            }

            ConstantObjects.shippingOption_freeShippingWithinSaudiArabia -> {
                AddProductObjectData.pickUpOption =
                    ConstantObjects.shippingOption_freeShippingWithinSaudiArabia
                shippingList.add(ConstantObjects.shippingOption_freeShippingWithinSaudiArabia)
                shippingOptionText?.append(getString(R.string.free_shipping_within_Saudi_Arabia))
            }

            ConstantObjects.shippingOption_arrangementWillBeMadeWithTheBuyer -> {
                AddProductObjectData.pickUpOption =
                    ConstantObjects.shippingOption_arrangementWillBeMadeWithTheBuyer
                shippingList.add(ConstantObjects.shippingOption_arrangementWillBeMadeWithTheBuyer)
                shippingOptionText?.append(getString(R.string.arrangementWillBeMadeWithTheBuyer))
            }

            ConstantObjects.pickUp_Must -> {
                AddProductObjectData.shippingOption = ConstantObjects.pickUp_Must
                shippingList.add(ConstantObjects.pickUp_Must)
                pickup?.append(getString(R.string.mustPickUp))
            }

            ConstantObjects.pickUp_No -> {
                AddProductObjectData.shippingOption = ConstantObjects.pickUp_No
                shippingList.add(ConstantObjects.pickUp_No)
                pickup?.append(getString(R.string.noPickUp))
            }

            ConstantObjects.pickUp_Available -> {
                AddProductObjectData.shippingOption = ConstantObjects.pickUp_Available
                shippingList.add(ConstantObjects.pickUp_Available)
                pickup?.append(getString(R.string.pickUpAvaliable))
            }

        }
        tvShippingOption.text = shippingOptionText
        tvPickupOptionData.text = pickup


        AddProductObjectData.shippingOptionSelections?.addAll(shippingList)


    }

    @SuppressLint("SetTextI18n")
    private fun setUpViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        addProductViewModel.isLoading.observe(this) {
            if (it) HelpFunctions.startProgressBar(this)
            else HelpFunctions.dismissProgressBar()
        }
        addProductViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError), this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError), this
                )
            }

        }
        addProductViewModel.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null && it.message != "") {
                    HelpFunctions.ShowLongToast(
                        it.message!!, this
                    )
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.serverError), this
                    )
                }
            }

        }
        addProductViewModel.confirmAddPorductRespObserver.observe(this) { confirmAddPorductRespObserver ->
            if (confirmAddPorductRespObserver.status_code == 200) {
                try {

                    val productId: Int = confirmAddPorductRespObserver.productId
                    println(
                        "hhhh product id $productId " + Gson().toJson(
                            confirmAddPorductRespObserver
                        )
                    )
                    resetAddProductObject()
                    val intent = Intent(
                        this@ConfirmationAddProductActivity, SuccessProductActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        putExtra(ConstantObjects.productIdKey, productId)
                        putExtra("comeFrom", "AddProduct")

                    }

                    startActivity(intent)
                    finish()
                } catch (e: java.lang.Exception) {

                }

            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.failed), this
                )
            }
        }
        addProductViewModel.cartPriceSummeryObserver.observe(this) { cartSummery ->
            if (cartSummery.status_code == 200 && cartSummery.priceSummery != null) {
                package_cost_tv.text =
                    "${cartSummery.priceSummery.pakatPrice} ${getString(R.string.Rayal)}"
                if(cartSummery.priceSummery.pakatPrice==0f){
                    layPackage.visibility=View.GONE
                }
//                tv_package_price.text =
//                    "${cartSummery.priceSummery.pakatPrice} ${getString(R.string.Rayal)}"
//                tv_package_name.text = AddProductObjectData.selectedPakat?.name ?: ""


                val discount: Float =
                    cartSummery.priceSummery.totalPriceBeforeCoupon - cartSummery.priceSummery.totalPriceAfterCoupon
                discount_tv.text = "$discount ${getString(R.string.Rayal)}"
                if (discount != 0f) {
                    tvTotal.text =
                        "${cartSummery.priceSummery.totalPriceBeforeCoupon} ${getString(R.string.Rayal)}"
                } else {
                    tvTotal.text =
                        "${cartSummery.priceSummery.totalPriceAfterCoupon} ${getString(R.string.Rayal)}"
                }
                if (cartSummery.priceSummery.enableFixedPriceSaleFee != 0f) {
                    containerFixedPriceFee.show()
                    tvFixedPriceFee.text =
                        "${cartSummery.priceSummery.enableFixedPriceSaleFee} ${getString(R.string.Rayal)}"
                } else {
                    containerFixedPriceFee.hide()
                }
                if (cartSummery.priceSummery.enableAuctionFee != 0f) {
                    containerAuctionFee.show()
                    tvAuctionFee.text =
                        "${cartSummery.priceSummery.enableAuctionFee} ${getString(R.string.Rayal)}"
                } else {
                    containerAuctionFee.hide()
                }
                if (cartSummery.priceSummery.enableNegotiationFee != 0f) {
                    containerNegotiationFee.show()
                    tvNegotiationFee.text =
                        "${cartSummery.priceSummery.enableNegotiationFee} ${getString(R.string.Rayal)}"
                } else {
                    containerNegotiationFee.hide()

                }

            }
        }

        addProductViewModel.productDetailsObservable.observe(this) { productResp ->
            if (productResp.productDetails != null) {
                productDetails = productResp.productDetails
                AddProductObjectData.productId = productDetails?.id
//                        productPrice = productResp.productDetails.priceDisc
                setProductData(productDetails!!)
            } else {
                showProductApiError(productResp.message)
            }
        }

        addProductViewModel.shippingOptionObserver.observe(this) {
            if (it.status_code == 200) {
                if (!it.shippingOptionObject.isNullOrEmpty()) {
                    shippingOptionText = StringBuilder("")
                    pickup = StringBuilder("")
                    tvShippingOption.text = ""
                    tvPickupOptionData.text = ""
//                    if (it.shippingOptionObject.size > 3) {
//                        for (i in (it.shippingOptionObject?.subList(
//                            3,
//                            it.shippingOptionObject.size - 1
//                        )))
//                            showShoppingOption(i)
//                        for (i in (it.shippingOptionObject?.subList(0, 3)))
//                            showShoppingOption(i)
//                    } else {
                    for (i in (it.shippingOptionObject))
                        showShoppingOption(i)
//                    }

                } else {
                    tvPickupOptionData.text = getString(R.string.mustPickUp)
                    AddProductObjectData.shippingOptionSelections?.add(1)
                }
            }
        }
        addProductViewModel.paymentOptionObserver.observe(this) {
            if (it.status_code == 200) {
                it.shippingOptionObject?.let { list ->
                    showPaymentMethod(list)

                }
            }
        }
        addProductViewModel.bankOptionObserver.observe(this) {
            if (it.status_code == 200) {
                if (!it.accountsList.isNullOrEmpty()) {
                    AddProductObjectData.selectedAccountDetails = arrayListOf()
                    for (item in it.accountsList) {
                        item.id = item.bankAccountId
                    }
                    AddProductObjectData.selectedAccountDetails?.addAll(it.accountsList)
                }

            }
        }


        addProductViewModel.couponByCodeObserver.observe(this) { couponDiscountResp ->
            if (couponDiscountResp.status_code == 200) {
                if (couponDiscountResp.discountCouponObject != null) {
                    couponId = couponDiscountResp.discountCouponObject.id
                    if (couponDiscountResp.discountCouponObject.discountTypeID == "FixedAmount") {
                        couponDiscountValue = couponDiscountResp.discountCouponObject.discountValue
                        totalAfterDiscount = totalPrice - couponDiscountValue
                    } else {
                        val disc =
                            ((couponDiscountResp.discountCouponObject.discountValue * totalPrice) / 100)
                        couponDiscountValue = disc
                        totalAfterDiscount = totalPrice - disc
                    }
                    tvTotalAfterDiscount.show()
                    tvTotalAfterDiscount.text = "$totalAfterDiscount ${getString(R.string.SAR)}"
                    tvTotal.paintFlags = tvTotal.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.invalidCoupon), this)
                }
            }
        }
    }

    //    private fun getCartSummery() {
//        AddProductObjectData.selectedCategory?.let { selectedCategory ->
////            btnFreeProductImagesCount.text = it.freeProductImagesCount.toString()
////            btnFreeProductVidoesCount.text = it.freeProductVidoesCount.toString()
////            btnExtraProductVideoFee.text = "${it.extraProductVidoeFee} ${getString(R.string.Rayal)}"
////            btnExtraProductImageFee.text = "${it.extraProductImageFee} ${getString(R.string.Rayal)}"
////            btnSubTitleFeeFee.text = "${it.subTitleFee} ${getString(R.string.Rayal)}
//
//            AddProductObjectData.images?.let {
//                var extraImagesFee = 0F
//                if (it.size > selectedCategory.freeProductImagesCount) {
//                    extraImagesFee =
//                        selectedCategory.extraProductImageFee * (it.size - selectedCategory.freeProductImagesCount)
//                }
//                var extraVideosFee: Float = 0f
//                AddProductObjectData.videoList?.let {
//                    if (it.size > selectedCategory.freeProductVidoesCount) {
//                        extraVideosFee =
//                            selectedCategory.extraProductVidoeFee * (it.size - selectedCategory.freeProductVidoesCount)
//
//                    }
//                }
//                addProductViewModel.checkOutAdditionalPakat(
//                    AddProductObjectData.selectedPakat?.id ?: 0,
//                    AddProductObjectData.selectedCategoryId,
//                    extraImagesFee,
//                    extraVideosFee, 0f
//                )
//            }
//        }
//    }

    @SuppressLint("SetTextI18n")
    private fun setProductData(productDetails: Product) {
        AddProductObjectData.selectedCategoryId = productDetails.categoryId
        AddProductObjectData.selectedCategoryName = productDetails.category.toString()
        pakatId = productDetails.pakatId.toString()

        AddProductObjectData.selectedCategory = productDetails.categoryDto
        /**productPublishFeee**/
        if (productDetails.categoryDto?.productPublishPrice?.toDouble() != 0.0) {
            containerProductPublishPriceFee.show()
            AddProductObjectData.selectedCategory?.productPublishPrice =
                productDetails.categoryDto?.productPublishPrice ?: 0f
            productPublishPriceFee = productDetails.categoryDto?.productPublishPrice ?: 0f
            tvProductPublishPriceFee.text = "$productPublishPriceFee ${getString(R.string.SAR)}"
        }else{
            containerProductPublishPriceFee.hide()
        }
        /**package fee*/
        if (productDetails.selectedPacket != null) {
            AddProductObjectData.selectedPakat = productDetails.selectedPacket
            ContainerPackge.show()
            layPackage.show()
            pakagePrice = AddProductObjectData.selectedPakat?.price ?: 0f
            package_cost_tv.text = "$pakagePrice ${getString(R.string.SAR)}"
            tv_package_price.text = "$pakagePrice ${getString(R.string.SAR)}"
            tv_package_name.text = productDetails.selectedPacket?.name
        }else{
            layPackage.hide()
        }

        AddProductObjectData.priceFixed = productDetails.price.toString()
        /**fixedPrice fee*/
        if (productDetails.isFixedPriceEnabled && productDetails.price != 0f) {
            containerFixedPriceFee.show()
            AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee =
                productDetails.categoryDto?.enableFixedPriceSaleFee ?: 0f
            fixedPriceFee = AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee ?: 0f
            tvFixedPriceFee.text = "$fixedPriceFee ${getString(R.string.SAR)}"
        }

        /**AuctionFee fee*/
        if (productDetails.isAuctionEnabled && productDetails.auctionStartPrice != 0f) {
            AddProductObjectData.selectedCategory?.enableAuctionFee =
                productDetails.categoryDto?.enableAuctionFee ?: 0f
            containerAuctionFee.show()
            AddProductObjectData.auctionStartPrice = productDetails.auctionStartPrice.toString()
            AddProductObjectData.auctionMinPrice = productDetails.auctionMinimumPrice.toString()
            auctionEnableFee = AddProductObjectData.selectedCategory?.enableAuctionFee ?: 0f
            tvAuctionFee.text = "${productDetails.auctionStartPrice} ${getString(R.string.SAR)}"
        }
        /**negotiation fee*/
        if (productDetails.isNegotiationEnabled && productDetails.auctionNegotiatePrice != 0f) {
            AddProductObjectData.isNegotiablePrice = productDetails.isNegotiationEnabled
            AddProductObjectData.selectedCategory?.enableNegotiationFee =
                productDetails.categoryDto?.enableNegotiationFee ?: 0f
            containerNegotiationFee.show()
            negotiationFee = AddProductObjectData.selectedCategory?.enableNegotiationFee ?: 0f
            tvNegotiationFee.text = "$negotiationFee ${getString(R.string.SAR)}"
        }
        /**subTitle fee*/
        if (productDetails.subTitle != "" && productDetails.categoryDto?.subTitleFee != 0f) {
            AddProductObjectData.selectedCategory?.subTitleFee =
                productDetails.categoryDto?.subTitleFee ?: 0f
            containerSubTitleFee.show()
            subTitleFee = AddProductObjectData.selectedCategory?.subTitleFee ?: 0f
            tvSubTitleFee.text = "$subTitleFee ${getString(R.string.SAR)}"
        }
        /**ExtreImageFee*/
//        productDetails.categoryDto?.let { selectedCategory ->
//            var extraImages = 0
//            AddProductObjectData.images?.let {
//                if (it.size > selectedCategory.freeProductImagesCount) {
//                    extraImages = it.size - selectedCategory.freeProductImagesCount
//                }
//            }
//            if (extraImages > 0) {
//                extraImageFee = selectedCategory.extraProductImageFee * extraImages
//                if (extraImageFee != 0f) {
//                    containerImageExtraFee.show()
//                    tvImageExtraFee.text = "$extraImageFee ${getString(R.string.SAR)}"
//                }
//            }
//        }
        /**ExtreVideoFee*/
        if (!productDetails.listMedia.isNullOrEmpty()) productDetails.listMedia.let {
            val list = productDetails.listMedia.filter { it.type == 2 }.map { it.url }

            AddProductObjectData.videoList = list as ArrayList<String>
//            AddProductObjectData.listMedia = arrayListOf()
//            AddProductObjectData.listMedia = productDetails.listMedia
        }
//        AddProductObjectData.selectedCategory?.let { selectedCategory ->
//            var extraVideo = 0
//            AddProductObjectData.videoList?.let {
//                if (it.size > selectedCategory.freeProductVidoesCount) {
//                    extraVideo = it.size - selectedCategory.freeProductVidoesCount
//                }
//            }
//            if (extraVideo > 0) {
//                extraVideoFee = selectedCategory.extraProductVidoeFee * extraVideo
//                if (extraVideoFee != 0f) {
//                    containerVideoExtraFee.show()
//                    tvVideoExtraFee.text = "$extraVideoFee ${getString(R.string.SAR)}"
//                }
//            }
//        }

        /**total*/
        totalPrice =
            (pakagePrice + fixedPriceFee + auctionEnableFee + negotiationFee + subTitleFee + extraImageFee + extraVideoFee + productPublishPriceFee)
        tvTotal.text = "$totalPrice ${getString(R.string.SAR)}"


        AddProductObjectData.productCondition = productDetails.status

        tvItemCondition.text =
            if (AddProductObjectData.productCondition == 1) getString(R.string.used) else getString(
                R.string.New
            )

        AddProductObjectData.productCondition = productDetails.status
        tvQuantityData.text = productDetails.qty.toString()
        AddProductObjectData.quantity = productDetails.qty.toString()

        if (productDetails.isAuctionEnabled) {
            tvAuctionOpitonSale.show()
        } else {
            tvAuctionOpitonSale.hide()
        }
        if (productDetails.isFixedPriceEnabled) {
            tvFixedPriceOptionSale.show()
        } else {
            tvFixedPriceOptionSale.hide()
        }

        AddProductObjectData.isNegotiablePrice = productDetails.isNegotiationEnabled
        AddProductObjectData.priceFixedOption = productDetails.isFixedPriceEnabled
        AddProductObjectData.auctionOption = productDetails.isAuctionEnabled

        if (productDetails.isNegotiationEnabled) {
            negotiable_tv.text = getString(R.string.Yes)
        } else {
            negotiable_tv.text = getString(R.string.No)
        }

        if (productDetails.isAuctionEnabled) {
            containerAuction.show()
            if (productDetails.auctionClosingTime != null && (productDetails.auctionClosingTime != "")) {

//                if (productDetails.e== true) {
//                    tvDateAuction.text = AddProductObjectData.selectTimeAuction?.text
//                } else {
//                    tvDateAuction.text = AddProductObjectData.selectTimeAuction?.endTime
//                }

                AddProductObjectData.isAuctionClosingTimeFixed=productDetails.isAuctionClosingTimeFixed
                if (!productDetails.isAuctionClosingTimeFixed)
                    AddProductObjectData.selectTimeAuction = TimeAuctionSelection(
                        "",
                        productDetails.auctionClosingTime,
                        HelpFunctions.getAuctionClosingTime2(productDetails.auctionClosingTime),
                        0,
                        customOption = true
                    )
                else {
                    AddProductObjectData.selectTimeAuction = TimeAuctionSelection(
                        "1",
                        productDetails.auctionClosingTime,
                        HelpFunctions.getAuctionClosingTime2(productDetails.auctionClosingTime),
                        0,
                        customOption = false
                    )
                }

                tvDateAuction.text =
                    HelpFunctions.getAuctionClosingTime2(productDetails.auctionClosingTime)
            }
        } else {
            containerAuction.hide()
        }


        AddProductObjectData.country =
            SearchListItem(productDetails.countryId, productDetails.country)
        AddProductObjectData.region =
            SearchListItem(productDetails.regionId, productDetails.regionName)
        AddProductObjectData.city =
            SearchListItem(productDetails.neighborhoodId, productDetails.neighborhood)

        if (!productDetails.listMedia.isNullOrEmpty()) productDetails.listMedia.let {
            val obj = productDetails.listMedia.find { it.isMainMadia }
            if (obj?.type == 2) {
                selectedImages.setImageResource(R.mipmap.ic_launcher)
            } else {
                getPicassoInstance().load(obj?.url?.replace("http", "https"))
                    .placeholder(R.drawable.splash_logo).error(R.drawable.splash_logo)
                    .into(selectedImages)
            }
            AddProductObjectData.images = productDetails.listMedia
//            AddProductObjectData.listMedia = arrayListOf()
//            AddProductObjectData.listMedia = productDetails.listMedia
        }

        AddProductObjectData.productSpecificationList =
            productDetails.listProductSep ?: arrayListOf()

        AddProductObjectData.selectedCategoryName = productDetails.category.toString()
        product_type.text = productDetails.category

//        if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
        AddProductObjectData.itemTitleAr = productDetails.nameAr.toString()
        AddProductObjectData.subtitleAr = productDetails.subTitleAr ?: ""
        AddProductObjectData.itemDescriptionAr = productDetails.descriptionAr ?: ""

//        tvTitleData.text = AddProductObjectData.itemTitleAr
//        tvSubTitleData.text = AddProductObjectData.subtitleAr
//        tvProductDetail.text = AddProductObjectData.itemDescriptionAr ?: ""
//        } else {
        AddProductObjectData.itemTitleEn = productDetails.nameEn.toString()
        AddProductObjectData.subtitleEn = productDetails.subTitle ?: ""
        AddProductObjectData.itemDescriptionEn = productDetails.descriptionEn ?: ""

        tvTitleData.text = productDetails.name
        tvSubTitleData.text = productDetails.subTitle
        tvProductDetail.text = productDetails.description ?: ""
//        }

        ContainerPackge.hide()
//        containerFixedPriceFee.hide()
//        containerAuctionFee.hide()
//        containerNegotiationFee.hide()
//        containerSubTitleFee.hide()
        containerImageExtraFee.hide()
        containerVideoExtraFee.hide()
//        containerProductPublishPriceFee.hide()
    }

    private fun showProductApiError(message: String) {
        if (productDetails == null) {
            containerMainProduct.hide()
            containerShareAndFav.hide()
        }
        HelpFunctions.ShowLongToast(message, this)
    }

    @SuppressLint("SetTextI18n")
    private fun setSummeryData() {
        ContainerPackge.hide()
        containerFixedPriceFee.hide()
        containerAuctionFee.hide()
        containerNegotiationFee.hide()
        containerSubTitleFee.hide()
        containerImageExtraFee.hide()
        containerVideoExtraFee.hide()
        containerProductPublishPriceFee.hide()
        /**productPublishFeee**/
        if (AddProductObjectData.selectedCategory?.productPublishPrice?.toDouble() != 0.0) {
            containerProductPublishPriceFee.show()
            productPublishPriceFee =
                AddProductObjectData.selectedCategory?.productPublishPrice ?: 0f
            tvProductPublishPriceFee.text = "$productPublishPriceFee ${getString(R.string.SAR)}"
        }else{
            containerProductPublishPriceFee.hide()
        }
        /**package fee*/
        if (AddProductObjectData.selectedPakat != null) {
            layPackage.show()
            ContainerPackge.show()
            pakagePrice = AddProductObjectData.selectedPakat?.price ?: 0f
            package_cost_tv.text = "$pakagePrice ${getString(R.string.SAR)}"
            tv_package_price.text = "$pakagePrice ${getString(R.string.SAR)}"
            tv_package_name.text = AddProductObjectData.selectedPakat?.name
        }else{
            ContainerPackge.hide()
            layPackage.hide()
        }
        /**fixedPrice fee*/
        if (AddProductObjectData.priceFixedOption && AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee != 0f) {
            containerFixedPriceFee.show()
            fixedPriceFee = AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee ?: 0f
            tvFixedPriceFee.text = "$fixedPriceFee ${getString(R.string.SAR)}"
        }
        /**AuctionFee fee*/
        if (AddProductObjectData.auctionOption && AddProductObjectData.selectedCategory?.enableAuctionFee != 0f) {
            containerAuctionFee.show()
            auctionEnableFee = AddProductObjectData.selectedCategory?.enableAuctionFee ?: 0f
            tvAuctionFee.text = "$auctionEnableFee ${getString(R.string.SAR)}"
        }
        /**negotiation fee*/
        if (AddProductObjectData.isNegotiablePrice && AddProductObjectData.selectedCategory?.enableNegotiationFee != 0f) {
            containerNegotiationFee.show()
            negotiationFee = AddProductObjectData.selectedCategory?.enableNegotiationFee ?: 0f
            tvNegotiationFee.text = "$negotiationFee ${getString(R.string.SAR)}"
        }
        /**subTitle fee*/
        if (AddProductObjectData.subtitleAr != "" && AddProductObjectData.subtitleEn != "" && AddProductObjectData.selectedCategory?.subTitleFee != 0f) {
            containerSubTitleFee.show()
            subTitleFee = AddProductObjectData.selectedCategory?.subTitleFee ?: 0f
            tvSubTitleFee.text = "$subTitleFee ${getString(R.string.SAR)}"
        }
        /**ExtreImageFee*/
        AddProductObjectData.selectedCategory?.let { selectedCategory ->
            var extraImages = 0
            AddProductObjectData.images?.let {
                if (it.size > selectedCategory.freeProductImagesCount) {
                    extraImages = it.size - selectedCategory.freeProductImagesCount
                }
            }
            if (extraImages > 0) {
                extraImageFee = selectedCategory.extraProductImageFee * extraImages
                if (extraImageFee != 0f) {
                    containerImageExtraFee.show()
                    tvImageExtraFee.text = "$extraImageFee ${getString(R.string.SAR)}"
                }
            }
        }
        /**ExtreVideoFee*/
        AddProductObjectData.selectedCategory?.let { selectedCategory ->
            var extraVideo = 0
            AddProductObjectData.videoList?.let {
                if (it.size > selectedCategory.freeProductVidoesCount) {
                    extraVideo = it.size - selectedCategory.freeProductVidoesCount
                }
            }
            if (extraVideo > 0) {
                extraVideoFee = selectedCategory.extraProductVidoeFee * extraVideo
                if (extraVideoFee != 0f) {
                    containerVideoExtraFee.show()
                    tvVideoExtraFee.text = "$extraVideoFee ${getString(R.string.SAR)}"
                }
            }
        }

        /**total*/
        totalPrice =
            (pakagePrice + fixedPriceFee + auctionEnableFee + negotiationFee + subTitleFee + extraImageFee + extraVideoFee + productPublishPriceFee)
        tvTotal.text = "$totalPrice ${getString(R.string.SAR)}"
    }

    private fun resetAddProductObject() {
        AddProductObjectData.selectedCategory = null
        AddProductObjectData.selectedCategoryId = 0
        AddProductObjectData.selectedCategoryName = ""
        AddProductObjectData.videoList = null
        AddProductObjectData.images = null
        AddProductObjectData.productSpecificationList = null
        AddProductObjectData.itemTitleAr = ""
        AddProductObjectData.itemTitleEn = ""
        AddProductObjectData.subtitleAr = ""
        AddProductObjectData.subtitleEn = ""
        AddProductObjectData.itemDescriptionAr = ""
        AddProductObjectData.itemDescriptionEn = ""
        AddProductObjectData.productCondition = 0
        AddProductObjectData.quantity = ""
        AddProductObjectData.country = null
        AddProductObjectData.region = null
        AddProductObjectData.city = null
        AddProductObjectData.phone = ""
        AddProductObjectData.priceFixed = ""
        AddProductObjectData.priceFixedOption = false
        AddProductObjectData.auctionOption = false
        AddProductObjectData.auctionMinPrice = "0"
        AddProductObjectData.auctionStartPrice = "0"
        AddProductObjectData.isNegotiablePrice = false
        AddProductObjectData.selectedAccountDetails = null
        AddProductObjectData.selectTimeAuction = null
        AddProductObjectData.paymentOptionList = null
        AddProductObjectData.shippingOption = 0
        AddProductObjectData.pickUpOption = 0
        AddProductObjectData.selectedPakat = null
        AddProductObjectData.shippingOptionSelection = null
        AddProductObjectData.imagesListRemoved = null
        AddProductObjectData.shippingOptionSelections = null
        AddProductObjectData.productId = null
    }

    private fun setViewClickListeners() {
        btnCoupon.setOnClickListener {
            if (etCoupon.text.toString().trim() != "") {
                addProductViewModel.getCouponByCode(etCoupon.text.toString().trim(),"1")
            } else {
                etCoupon.error = getString(R.string.enter_the_coupon)
            }
        }
        back_btn.setOnClickListener {
            finish()
        }
        tvEditProductDetails.setOnClickListener {
            if (ConstantObjects.isModify) {
                startActivity(Intent(this, AddPhotoActivity::class.java).apply {
                    putExtra(ConstantObjects.isEditKey, true)
                })
            } else {
                startActivity(Intent(this, ChooseCategoryActivity::class.java).apply {
                    putExtra(ConstantObjects.isEditKey, true)
                })
                finish()
            }


        }
        edit_item_payment.setOnClickListener {
            startActivity(Intent(this, PricingActivity::class.java).apply {
                putExtra(ConstantObjects.isEditKey, true)
            })
            finish()
        }
        edit_selected_package.setOnClickListener {
            startActivity(Intent(this, PromotionalActivity::class.java).apply {
                putExtra(ConstantObjects.isEditKey, true)
            })
            finish()
        }
        edit_shoping_option.setOnClickListener {
            startActivity(Intent(this, ListingDurationActivity::class.java).apply {
                putExtra(ConstantObjects.isEditKey, true)
            })
            finish()
        }
        btn_confirm_details.setOnClickListener {
            //  HelpFunctions.ShowLongToast("not implemented yet",this)
            if (ConstantObjects.isRepost || ConstantObjects.isModify)
                confirmOrder(true)
            else
                confirmOrder(false)
        }
        btn_cancel.setOnClickListener {
            //  HelpFunctions.ShowLongToast("not implemented yet",this)
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }

    private fun confirmOrder(isEdit: Boolean) {
        val listImageFile: ArrayList<File> = ArrayList()
        var mainIndex = ""

        if (AddProductObjectData.images.isNullOrEmpty()) {
            val listImages = ArrayList<ImageSelectModel>()
            listImages.add(
                ImageSelectModel(
                    "content://media/external/images/media/29".toUri(),
                    "",
                    true
                )
            )
            for (image in listImages) {
                if (image.is_main) {
                    mainIndex = listImages.indexOf(image).toString()
                }
                try {
                    val file = HelpFunctions.getFileImage(image.uri!!, this)
                    listImageFile.add(file)
                } catch (e: java.lang.Exception) {
                    //
                }
            }
        } else {
            AddProductObjectData.images?.let { imageList ->
                for (image in imageList) {
                    if (image.uri != null && image.is_main) {
                        mainIndex = imageList.indexOf(image).toString()
                    } else if (image.url != null && image.isMainMadia) {
                        mainIndex = imageList.indexOf(image).toString()
                    }
                    try {
                        // val file = File(image.uri.path)
                        val file = HelpFunctions.getFileImage(image.uri!!, this)

                        listImageFile.add(file)
                    } catch (e: java.lang.Exception) {

                    }

                    // listImageFile.add(HelpFunctions.getFileImage(image.uri, this))
                    //var bytes=HelpFunctions.getBytesImage(image.uri,this)
//               var   bytes:ByteArray=HelpFunctions.getFileImage(image.uri, this).readBytes()
//               if(bytes!=null){
//                   listImageByts.add(bytes)
//               }
//               listImageUri.add(image.uri)
                }
            }
        }

        AddProductObjectData.selectedPakat?.let {
            pakatId = it.id.toString()
        }
        val shippingOption: ArrayList<String> = ArrayList()
        AddProductObjectData.shippingOptionSelection?.let {
            shippingOption.add(it[0].id.toString())
        }

        val bankList = AddProductObjectData.selectedAccountDetails?.map { it.id }
//        addProductViewModel.callAddProduct(listImageFile,AddProductObjectData.shippingOptionSelections ?: arrayListOf(),AddProductObjectData.paymentOptionList
//            ,AddProductObjectData.videoList,tvDateAuction.text.toString(),
//            couponId,
//            couponDiscountValue.toDouble(),totalAfterDiscount.toDouble(),totalPrice.toDouble())

        addProductViewModel.getAddProduct3(
            isEdit,
            productDetails?.id ?: 0,
            this,
            nameAr = AddProductObjectData.itemTitleAr,
            nameEn = AddProductObjectData.itemTitleEn,
            subTitleAr = AddProductObjectData.subtitleAr,
            subTitleEn = AddProductObjectData.subtitleEn,
            descriptionAr = AddProductObjectData.itemDescriptionAr,
            descriptionEn = AddProductObjectData.itemDescriptionEn,
            qty = AddProductObjectData.quantity,
            productCondition = AddProductObjectData.productCondition.toString(),
            categoryId = AddProductObjectData.selectedCategoryId.toString(),
            countryId = AddProductObjectData.country?.id.toString(),
            regionId = AddProductObjectData.region?.id.toString(),
            neighborhoodId = AddProductObjectData.city?.id.toString(),
            Street = "",
            GovernmentCode = "",
            pakatId = pakatId,
            productSep = AddProductObjectData.productSpecificationList,
            listImageFile = listImageFile,//listImageFile
            MainImageIndex = mainIndex,
            videoUrl = AddProductObjectData.videoList,
            PickUpDelivery = AddProductObjectData.pickUpOption.toString(),
            DeliveryOption = AddProductObjectData.shippingOptionSelections ?: arrayListOf(),
            isFixedPriceEnabled = AddProductObjectData.priceFixedOption,
            isAuctionEnabled = AddProductObjectData.auctionOption,
            isNegotiationEnabled = AddProductObjectData.isNegotiablePrice,
            price = AddProductObjectData.priceFixed,
            priceDisc = AddProductObjectData.priceFixed,
            paymentOptionIdList = AddProductObjectData.paymentOptionList,
            isCashEnabled = "false", //same as  paymentOptionId
            disccountEndDate = "",
            auctionStartPrice = AddProductObjectData.auctionStartPrice,
            auctionMinimumPrice = AddProductObjectData.auctionMinPrice,
            auctionClosingTime =tvDateAuction.text.toString()
           ,
            productBankAccounts = bankList,
            ProductPaymentDetailsDto_AdditionalPakatId = pakatId,
            ProductPaymentDetailsDto_ProductPublishPrice = productPublishPriceFee,
            ProductPaymentDetailsDto_EnableAuctionFee = auctionEnableFee,
            ProductPaymentDetailsDto_EnableNegotiationFee = negotiationFee,
            ProductPaymentDetailsDto_ExtraProductImageFee = extraImageFee,
            ProductPaymentDetailsDto_ExtraProductVidoeFee = extraVideoFee,
            ProductPaymentDetailsDto_SubTitleFee = subTitleFee,
            ProductPaymentDetailsDto_CouponId = couponId,
            ProductPaymentDetailsDto_CouponDiscountValue = couponDiscountValue,
            ProductPaymentDetailsDto_TotalAmountAfterCoupon = totalAfterDiscount,
            ProductPaymentDetailsDto_TotalAmountBeforeCoupon = totalPrice,

            )
    }


    override fun onDestroy() {
        super.onDestroy()
        shippingOptionText = null
        pickup = null
        addProductViewModel.closeAllCall()
        addProductViewModel.baseCancel()
    }


}