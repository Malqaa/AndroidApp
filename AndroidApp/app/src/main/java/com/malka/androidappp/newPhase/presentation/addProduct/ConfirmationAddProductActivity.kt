package com.malka.androidappp.newPhase.presentation.addProduct

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.presentation.MainActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity6.ListingDetailsActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity6.PricingActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity7.ListingDurationActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity8.PromotionalActivity
import com.malka.androidappp.newPhase.presentation.addProduct.viewmodel.AddProductViewModel
import kotlinx.android.synthetic.main.activity_confirmation_add_product.*
import kotlinx.android.synthetic.main.activity_my_product_details2.tvShippingOptions
import kotlinx.android.synthetic.main.toolbar_main.*
import java.io.File

class ConfirmationAddProductActivity : BaseActivity() {

    private lateinit var addProductViewModel: AddProductViewModel
    var pakagePrice = 0f
    var fixedPriceFee = 0f
    var auctionEnableFee = 0f
    var negotiationFee = 0f
    var subTitleFee = 0f
    var extraImageFee = 0f
    var extraVideoFee = 0f
    var totalPrice = 0f
    var productPublishPriceFee = 0f
    var couponId = 0
    var couponDiscountValue = 0f
    var totalAfterDiscount = 0f
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
        setSummeryData()
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
        AddProductObjectData.images?.let { images ->
            images.filter {
                it.is_main == true
            }.let {
                if (it.size > 0) {
                    selectedImages.setImageURI(it[0].uri)
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
        } else {
            negotiable_tv.text = getString(R.string.No)
        }
        /**Back**/
        tvCashOptionPayment.hide()
        tvSaudiBankDepositOptionPayment.hide()
        tvMadaOptionPayment.hide()
        tvCardOptionPayment.hide()
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

        var shippingOptionText = StringBuilder("")
        tvShippingOption.text = shippingOptionText
        AddProductObjectData.shippingOptionSelections?.let {

//            for (item in it) {
//                shippingOptionText += "${item.name},"
//            }
//            shippingOptionText = it[0].name

            for (i in it) {
                tvShippingOption.text = shippingOptionText
                when (i) {
                    ConstantObjects.pickUp_Must -> {
                        shippingOptionText.append(getString(R.string.mustPickUp))
                    }
                    ConstantObjects.pickUp_No -> {
                        shippingOptionText.append(getString(R.string.noPickUp))
                    }
                    ConstantObjects.pickUp_Available -> {
                        shippingOptionText.append(getString(R.string.pickUpAvaliable))
                    }
                    ConstantObjects.shippingOption_integratedShippingCompanyOptions -> {
                        shippingOptionText.append(getString(R.string.integratedShippingCompanies))
                    }
                    ConstantObjects.shippingOption_freeShippingWithinSaudiArabia -> {
                        shippingOptionText.append(getString(R.string.free_shipping_within_Saudi_Arabia))
                    }
                    ConstantObjects.shippingOption_arrangementWillBeMadeWithTheBuyer -> {
                        shippingOptionText.append(getString(R.string.arrangementWillBeMadeWithTheBuyer))
                    }
                }
            }

//                if(i == ConstantObjects.pickUp_Must){
//                    contianerPickUp.hide()
//                }else if(i ==ConstantObjects.pickUp_No){
//                    contianerPickUp.show()
//                    when (AddProductObjectData.pickUpOption) {
//                        ConstantObjects.pickUp_Must -> {
//                            tvShippingOption.text = getString(R.string.mustPickUp)
//                        }
//
//                        ConstantObjects.pickUp_No -> {
//                            tvShippingOption.text = getString(R.string.noPickUp)
//                        }
//
//                        ConstantObjects.pickUp_Available -> {
//                            tvShippingOption.text = getString(R.string.pickUpAvaliable)
//                        }
//                    }
//                }else if(i ==ConstantObjects.pickUp_Available){
//                    contianerPickUp.show()
//                    when (AddProductObjectData.pickUpOption) {
//                        ConstantObjects.pickUp_Must -> {
//                            tvShippingOption.text = getString(R.string.mustPickUp)
//                        }
//
//                        ConstantObjects.pickUp_No -> {
//                            tvShippingOption.text = getString(R.string.noPickUp)
//                        }
//
//                        ConstantObjects.pickUp_Available -> {
//                            tvShippingOption.text = getString(R.string.pickUpAvaliable)
//                        }
//                    }
//                }
        }
//            when (it) {
//                ConstantObjects.pickUp_Must -> {
//                    contianerPickUp.hide()
//                }
//
//                ConstantObjects.pickUp_No -> {
//                    contianerPickUp.show()
//                    when (AddProductObjectData.pickUpOption) {
//                        ConstantObjects.pickUp_Must -> {
//                            tvPickupOptionData.text = getString(R.string.mustPickUp)
//                        }
//
//                        ConstantObjects.pickUp_No -> {
//                            tvPickupOptionData.text = getString(R.string.noPickUp)
//                        }
//
//                        ConstantObjects.pickUp_Available -> {
//                            tvPickupOptionData.text = getString(R.string.pickUpAvaliable)
//                        }
//                    }
//                }
//
//                ConstantObjects.pickUp_Available -> {
//                    contianerPickUp.show()
//                    when (AddProductObjectData.pickUpOption) {
//                        ConstantObjects.pickUp_Must -> {
//                            tvPickupOptionData.text = getString(R.string.mustPickUp)
//                        }
//
//                        ConstantObjects.pickUp_No -> {
//                            tvPickupOptionData.text = getString(R.string.noPickUp)
//                        }
//
//                        ConstantObjects.pickUp_Available -> {
//                            tvPickupOptionData.text = getString(R.string.pickUpAvaliable)
//                        }
//                    }
//                }
//            }


        if (AddProductObjectData.auctionOption) {
            containerAuction.show()
            if (AddProductObjectData.selectTimeAuction?.customOption == true) {
                tvDateAuction.text = AddProductObjectData.selectTimeAuction?.text
            } else {
                tvDateAuction.text = AddProductObjectData.selectTimeAuction?.endTime
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


    private fun setUpViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        addProductViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
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
        addProductViewModel.confirmAddPorductRespObserver.observe(this) { confirmAddPorductRespObserver ->
            if (confirmAddPorductRespObserver.status_code == 200) {
//                HelpFunctions.ShowLongToast(
//                    getString(R.string.Success),
//                    this
//                )
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
                    getString(R.string.failed),
                    this
                )
            }
        }
        addProductViewModel.cartPriceSummeryObserver.observe(this) { cartSummery ->
            if (cartSummery.status_code == 200 && cartSummery.priceSummery != null) {
                package_cost_tv.text =
                    "${cartSummery.priceSummery.pakatPrice} ${getString(R.string.Rayal)}"
                tv_package_price.text =
                    "${cartSummery.priceSummery.pakatPrice} ${getString(R.string.Rayal)}"
                tv_package_name.text = AddProductObjectData.selectedPakat?.name ?: ""
                var discount: Float =
                    cartSummery.priceSummery.totalPriceBeforeCoupon - cartSummery.priceSummery.totalPriceAfterCoupon
                discount_tv.text = "${discount} ${getString(R.string.Rayal)}"
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


//                if (AddProductObjectData.selectedPakat == null) {
//                    tv_package_price.text = getString(R.string.notSpecified)
//                    tv_package_name.text = getString(R.string.notSpecified)
//                    calculation(0f)
//                } else {
//                    tv_package_price.text = AddProductObjectData.selectedPakat?.price.toString()
//                    tv_package_name.text = AddProductObjectData.selectedPakat?.name.toString()
//                    AddProductObjectData.selectedPakat?.let {
//                        calculation(it.price)
//                    }
//
////            val package_cost = AddProductObjectData.selectPromotiion!!.packageprice.toInt()
////            calculation(package_cost)
//                }
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
                        var disc =
                            ((couponDiscountResp.discountCouponObject.discountValue * totalPrice) / 100)
                        couponDiscountValue = disc
                        totalAfterDiscount = totalPrice - disc
                    }
                    tvTotalAfterDiscount.show()
                    tvTotalAfterDiscount.text = "${totalAfterDiscount} ${getString(R.string.SAR)}"
                    tvTotal.paintFlags = tvTotal.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.invalidCoupon), this)
                }
            }
        }
    }

    private fun getCartSummery() {
        AddProductObjectData.selectedCategory?.let { selectedCategory ->
//            btnFreeProductImagesCount.text = it.freeProductImagesCount.toString()
//            btnFreeProductVidoesCount.text = it.freeProductVidoesCount.toString()
//            btnExtraProductVideoFee.text = "${it.extraProductVidoeFee} ${getString(R.string.Rayal)}"
//            btnExtraProductImageFee.text = "${it.extraProductImageFee} ${getString(R.string.Rayal)}"
//            btnSubTitleFeeFee.text = "${it.subTitleFee} ${getString(R.string.Rayal)}

            AddProductObjectData.images?.let {
                var extraImagesFee = 0F
                if (it.size > selectedCategory.freeProductImagesCount) {
                    extraImagesFee =
                        selectedCategory.extraProductImageFee * (it.size - selectedCategory.freeProductImagesCount)
                }
                var extraVideosFee: Float = 0f
                AddProductObjectData.videoList?.let {
                    if (it.size > selectedCategory.freeProductVidoesCount) {
                        extraVideosFee =
                            selectedCategory.extraProductVidoeFee * (it.size - selectedCategory.freeProductVidoesCount)

                    }
                }
                addProductViewModel.checkOutAdditionalPakat(
                    AddProductObjectData.selectedPakat?.id ?: 0,
                    AddProductObjectData.selectedCategoryId,
                    extraImagesFee,
                    extraVideosFee, 0f
                )
            }


        }

    }

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
        if (AddProductObjectData.selectedCategory?.productPublishPrice != 0f) {
            containerProductPublishPriceFee.show()
            productPublishPriceFee =
                AddProductObjectData.selectedCategory?.productPublishPrice ?: 0f
            tvProductPublishPriceFee.text = "$productPublishPriceFee ${getString(R.string.SAR)}"
        }
        /**package fee*/
        if (AddProductObjectData.selectedPakat != null) {
            ContainerPackge.show()
            pakagePrice = AddProductObjectData.selectedPakat?.price ?: 0f
            package_cost_tv.text = "$pakagePrice ${getString(R.string.SAR)}"
            tv_package_price.text = "$pakagePrice ${getString(R.string.SAR)}"
        }
        /**fixedPrice fee*/
        if (AddProductObjectData.priceFixedOption
            && AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee != 0f
        ) {
            containerFixedPriceFee.show()
            fixedPriceFee = AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee ?: 0f
            tvFixedPriceFee.text =
                "$fixedPriceFee ${getString(R.string.SAR)}"
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
        totalPrice = (pakagePrice + fixedPriceFee + auctionEnableFee
                + negotiationFee + subTitleFee + extraImageFee + extraVideoFee + productPublishPriceFee)
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
        AddProductObjectData.auctionMinPrice = ""
        AddProductObjectData.auctionStartPrice = ""
        AddProductObjectData.isNegotiablePrice = false
        AddProductObjectData.selectedAccountDetails = null
        AddProductObjectData.selectTimeAuction = null
        AddProductObjectData.paymentOptionList = null
        AddProductObjectData.pickUpOption = 0
        AddProductObjectData.selectedPakat = null
        AddProductObjectData.shippingOptionSelection = null
    }

    private fun setViewClickListeners() {
        btnCoupon.setOnClickListener {
            if (etCoupon.text.toString().trim() != "") {
                addProductViewModel.getCouponByCode(etCoupon.text.toString().trim())
            } else {
                etCoupon.error = getString(R.string.enter_the_coupon)
            }
        }
        back_btn.setOnClickListener {
            finish()
        }
        tvEditProductDetails.setOnClickListener {
            startActivity(Intent(this, ListingDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.isEditKey, true)
            })
            finish()
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
            confirmOrder()
        }
        btn_cancel.setOnClickListener {
            //  HelpFunctions.ShowLongToast("not implemented yet",this)
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }

    private fun confirmOrder() {
        var listImageFile: ArrayList<File> = ArrayList()
        var mainIndex = ""
        AddProductObjectData.images?.let { imageList ->
            for (image in imageList) {
                if (image.is_main) {
                    mainIndex = imageList.indexOf(image).toString()
                }
                try {
                    // val file = File(image.uri.path)
                    val file = HelpFunctions.getFileImage(image.uri, this)

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

        var pakatId = ""
        AddProductObjectData.selectedPakat?.let {
            pakatId = it.id.toString()
        }
        var shippingOption: ArrayList<String> = ArrayList()
        AddProductObjectData.shippingOptionSelection?.let {
            shippingOption.add(it[0].id.toString())
        }

        val bankList = AddProductObjectData.selectedAccountDetails?.map { it.id }
        addProductViewModel.getAddProduct3(
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
            isCashEnabled = "false".toString(), //same as  paymentOptionId
            disccountEndDate = "",
            auctionStartPrice = AddProductObjectData.auctionStartPrice,
            auctionMinimumPrice = AddProductObjectData.auctionMinPrice,
            auctionClosingTime = AddProductObjectData.selectTimeAuction?.endTime ?: "",
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

        /**********/
//        addProductViewModel.getAddProduct2(
//            this,
//            nameAr = AddProductObjectData.itemTitleAr,
//            nameEn = AddProductObjectData.itemTitleEn,
//            subTitleAr = AddProductObjectData.subtitleAr,
//            subTitleEn = AddProductObjectData.subtitleEn,
//            descriptionAr = AddProductObjectData.itemDescriptionAr,
//            descriptionEn = AddProductObjectData.itemDescriptionEn,
//            qty = AddProductObjectData.quantity,
//            price = AddProductObjectData.priceFixed,
//            priceDisc = "0",
//            acceptQuestion = false.toString(),
//            isNegotiationOffers = AddProductObjectData.isNegotiablePrice.toString(),
//            withFixedPrice = withFixedPrice,
//            isMazad = isMazad,
//            isSendOfferForMazad = isMazad,
//            startPriceMazad = "0",
//            lessPriceMazad = "0",
//            mazadNegotiatePrice = "0",
//            mazadNegotiateForWhom = "0",
//            appointment = "".toString(),
//            productCondition = AddProductObjectData.productCondition.toString(),
//            categoryId = AddProductObjectData.selectedCategoryId.toString(),
//            countryId = AddProductObjectData.country!!.id.toString(),
//            regionId = AddProductObjectData.region!!.id.toString(),
//            neighborhoodId = AddProductObjectData.city!!.id.toString(),
//            Street = "",
//            GovernmentCode = "",
//            pakatId = pakatId,
//            productSep = AddProductObjectData.productSpecificationList,
////            listImageFile = listImageUri,//listImageFile
//            listImageFile = listImageFile,//listImageFile
//            MainImageIndex = mainIndex.toString(),
//            videoUrl = AddProductObjectData.videoList,
//            PickUpDelivery = AddProductObjectData.pickUpOption.toString(),
//            DeliveryOption = "1",
//        )

    }


//    @SuppressLint("SetTextI18n")
//    private fun calculation(package_cost: Float) {
//        val TaxAmount = package_cost * 12 / 100
//        val total = package_cost + TaxAmount
//        package_cost_tv.text = "${package_cost} ${getString(R.string.rial)}"
//        //added_tax.text = "${TaxAmount} ${getString(R.string.rial)}"
//        total_tv.text = "${total} ${getString(R.string.rial)}"
//        discount_tv.text = "${"0"} ${getString(R.string.rial)}"
//    }

//
//    // Methods to handle dynamic ad creation
//    private fun mainModelToJSON() {
//        HelpFunctions.startProgressBar(this)
//
////        if (StaticClassAdCreate.brand_new_item.equals(getString(R.string.New))) {
////            StaticClassAdCreate.brand_new_item = "on"
////        } else if (StaticClassAdCreate.brand_new_item.equals(getString(R.string.used))) {
////            StaticClassAdCreate.brand_new_item = "Off"
////
////        }
//
//
//        val mainModel = CreateAdvMainModel(
//            Id = null,
//            City = AddProductObjectData.city!!.title,
//            Country = AddProductObjectData.country!!.title,
//            name = AddProductObjectData.name,
//            slug = AddProductObjectData.slug,
//            Template = AddProductObjectData.template,
//            Region = AddProductObjectData.region!!.title,
//            Urgentexpirydate = AddProductObjectData.urgentexpirydate,
//            title = AddProductObjectData.itemTitleAr,
//            Price = AddProductObjectData.priceFixed,
//            user = ConstantObjects.logged_userid,
//            StartingPrice = AddProductObjectData.auctionStartPrice,
//            ReservePrice = AddProductObjectData.auctionMinPrice,
//
//            Duration = AddProductObjectData.duration,
//            EndTime = AddProductObjectData.endtime,
//            FixLength = AddProductObjectData.fixLength,
//            Timepicker = AddProductObjectData.timepicker,
//            DateTime = AddProductObjectData.endtime,
//
//            isActive = false,
//            isWatching = false,
//            Isuserfavorite = false,
//            //listingType = AddProductObjectData.buyingType,
//            listingType = "",
//            quantity = AddProductObjectData.quantity.toInt(),
//            featureexpirydate = AddProductObjectData.featureexpirydate,
//            highlightexpirydate = AddProductObjectData.highlightexpirydate,
//            phone = AddProductObjectData.phone,
//            address = AddProductObjectData.address,
//            pickupOption = AddProductObjectData.pickUpOption.toString(),
//            shippingOption = AddProductObjectData.shippingOptionSelection,
//            description = AddProductObjectData.itemDescriptionAr,
//            subtitle = AddProductObjectData.subtitleAr,
//            // producttitle = AddProductObjectData.productTitle,
//            enddate = AddProductObjectData.endtime,
//            platform = "Android",
//            iscashpaid = AddProductObjectData.iscashpaid,
////            isvisapaid = AddProductObjectData.isvisapaid,
////            isbankpaid = AddProductObjectData.isbankpaid,
//            isvisapaid = true,
//            isbankpaid = true,
//            isnegotiable = AddProductObjectData.isNegotiablePrice,
//            subcatone = AddProductObjectData.subcatone,
//            subcattwo = AddProductObjectData.subcattwo,
//            subcatthree = AddProductObjectData.subcatthree,
//            subcatfour = AddProductObjectData.subcatfour,
//            subcatfive = AddProductObjectData.subcatfive,
//            subcatsix = AddProductObjectData.subcatsix,
//            subcatonekey = AddProductObjectData.subcatonekey,
//            subcattwokey = AddProductObjectData.subcattwokey,
//            subcatthreekey = AddProductObjectData.subcatthreekey,
//            subcatfourkey = AddProductObjectData.subcatfourkey,
//            subcatfivekey = AddProductObjectData.subcatfivekey,
//            subcatsixkey = AddProductObjectData.subcatsixkey,
//            category = AddProductObjectData.subCategoryPath[0],
//            Video = AddProductObjectData.videoList,
//            brand_new_item = AddProductObjectData.brand_new_item,
//        )
////        if(AddProductObjectData.selectPromotiion==null){
////            mainModel.pack4=""
////        }else{
////            mainModel.pack4= AddProductObjectData.selectPromotiion!!.packageprice
////
////        }
//
//
//        val imageList: ArrayList<String> = ArrayList()
//
//        AddProductObjectData.images.forEach {
//            if (it.is_main == true) {
//                imageList.add(it.base64)
//            }
//        }
//
//        AddProductObjectData.images.forEach {
//            if (it.is_main != true) {
//                imageList.add(it.base64)
//            }
//        }
//
////        imageList.clear()
////        imageList.add("data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUA" +
////                "    AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO" +
////                "        9TXL0Y4OHwAAAABJRU5ErkJggg==")
//
//        mainModel.images = imageList
//
//        val jsonString = Gson().toJson(mainModel)
//
//        var map: Map<String, String> = HashMap()
//        map = Gson().fromJson(jsonString, map.javaClass)
//
//        ConstantObjects.dynamic_json_dictionary.putAll(map)
////        val testing = Gson().toJson(map)
////        print(testing)
//
//        createAllAds(ConstantObjects.dynamic_json_dictionary)
//
//    }

//    private fun createAllAds(data: HashMap<String, String>) {
//
//        val malqaa = RetrofitBuilder.GetRetrofitBuilder()
//
//        val call: Call<CreateAdvResponseBack> = malqaa.createAllAd(data)
//        call.enqueue(object : Callback<CreateAdvResponseBack> {
//
//            override fun onResponse(
//                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
//            ) {
//                if (response.isSuccessful) {
//                   // AddProductObjectData.buyingType = ""
//                 //   AddProductObjectData.buyingType = ""
//                    AddProductObjectData.brand_new_item = ""
//                    AddProductObjectData.shippingOptionSelection = null
////                    AddProductObjectData.selectPromotiion=null
//                    AddProductObjectData.subCategoryPath.clear()
//                    Extension.clearPath()
//
//                    val AdvId = response.body()!!.data
//                    HelpFunctions.dismissProgressBar()
//                    startActivity(
//                        Intent(
//                            this@ConfirmationAddProductActivity,
//                            SuccessProductActivity::class.java
//                        ).apply {
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            putExtra("AdvId", AdvId)
//                            putExtra("Template", AddProductObjectData.template)
//                            putExtra("sellerID", ConstantObjects.logged_userid)
//                        })
//                    finish()
//
//
//                } else {
//                    HelpFunctions.dismissProgressBar()
//                    HelpFunctions.ShowLongToast(
//                        getString(R.string.something_went_wrong),
//                        this@ConfirmationAddProductActivity
//                    )
//                }
//
//            }
//
//            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//                HelpFunctions.ShowLongToast(
//                    t.message + " " + getString(R.string.failed),
//                    this@ConfirmationAddProductActivity
//                )
//            }
//        })
//    }

}