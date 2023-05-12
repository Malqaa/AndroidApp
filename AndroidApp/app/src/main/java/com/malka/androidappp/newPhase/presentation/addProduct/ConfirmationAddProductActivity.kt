package com.malka.androidappp.newPhase.presentation.addProduct

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.CreateAdvMainModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.CreateAdvResponseBack
import com.malka.androidappp.newPhase.presentation.addProduct.activity6.ListingDetailsActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity6.PricingActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity7.ListingDurationActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity8.PromotionalActivity
import com.malka.androidappp.newPhase.presentation.addProduct.viewmodel.AddProductViewModel
import kotlinx.android.synthetic.main.activity_confirmation_add_product.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ConfirmationAddProductActivity : BaseActivity() {
    private lateinit var addProductViewModel: AddProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation_add_product)
        toolbar_title.text = getString(R.string.distinguish_your_product)
        setViewClickListeners()
        setData()
        setUpViewModel()
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
        addProductViewModel.confirmAddPorductRespObserver.observe(this) { confirmAddPorductRespObserver ->
            if (confirmAddPorductRespObserver.status_code == 200) {
//                HelpFunctions.ShowLongToast(
//                    getString(R.string.Success),
//                    this
//                )
                try {
                    val productId: Int = confirmAddPorductRespObserver.data
                    resetAddProductObject()
                    var inten = Intent(
                        this@ConfirmationAddProductActivity, SuccessProductActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    }
                    inten.putExtra(ConstantObjects.productIdKey, productId)

                    startActivity(inten)
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
    }

    private fun resetAddProductObject() {
        /**activity 1*/
        AddProductObjectData.selectedCategory = null
        AddProductObjectData.selectedCategoryId = 0
        AddProductObjectData.selectedCategoryName = ""
        AddProductObjectData.video = ""

        AddProductObjectData.itemTitleAr = ""
        AddProductObjectData.itemTitleEn = ""
        AddProductObjectData.subtitleAr = ""
        AddProductObjectData.subtitleEn = ""
        AddProductObjectData.itemDescriptionAr = ""
        AddProductObjectData.itemDescriptionEn = ""

        /**used =1 , new =2*/
        AddProductObjectData.productCondition = 0
        AddProductObjectData.quantity = ""
        AddProductObjectData.country = null
        AddProductObjectData.region = null
        AddProductObjectData.city = null

        AddProductObjectData.phone = ""
        AddProductObjectData.phoneCountryCode = ""

        AddProductObjectData.price = ""
        AddProductObjectData.reservedPrice = ""
        AddProductObjectData.startingPrice = ""
        AddProductObjectData.isnegotiable = false
        AddProductObjectData.buyingType = ""
        AddProductObjectData.isvisapaid = false
        AddProductObjectData.isbankpaid = false
        AddProductObjectData.productSpecificationList = null
        AddProductObjectData.pickUpOption = false
        AddProductObjectData.selectedPakat = null
        AddProductObjectData.shippingOptionSelection = null
    }

    private fun setViewClickListeners() {
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
    }

    private fun confirmOrder() {
        var withFixedPrice = "false"
        var isMazad = "false"
        when (AddProductObjectData.buyingType) {
            "1" -> {
                withFixedPrice = true.toString()
                tvFixedPrice.show()
                purchasing_price_.show()
                purchasing_price_tv.text = AddProductObjectData.price
            }
            "2" -> {
                isMazad = true.toString()
                tvAuction.show()
                auction_start_price.show()
                minimum_price.show()
                auction_start_price_tv.text = AddProductObjectData.startingPrice
                minimum_price_tv.text = AddProductObjectData.reservedPrice

            }
            "12" -> {
                withFixedPrice = true.toString()
                isMazad = true.toString()
                tvFixedPrice.show()
                tvAuction.show()
                purchasing_price_.show()
                auction_start_price.show()
                minimum_price.show()
                purchasing_price_tv.text = AddProductObjectData.price
                auction_start_price_tv.text = AddProductObjectData.startingPrice
                minimum_price_tv.text = AddProductObjectData.reservedPrice
            }
        }
        var listImageFile: ArrayList<File> = ArrayList()
        var listImageByts: ArrayList<ByteArray> = ArrayList()
        var listImageUri: ArrayList<Uri> = ArrayList()
        var mainIndex = ""
        for (image in AddProductObjectData.images) {
            if (image.is_main) {
                mainIndex = AddProductObjectData.images.indexOf(image).toString()
            }
            listImageFile.add(HelpFunctions.getFileImage(image.uri, this))
            var bytes=HelpFunctions.getBytesImage(image.uri,this)
            if(bytes!=null){
                listImageByts.add(bytes)
            }
            listImageUri.add(image.uri)
        }
        println("hhh image file numer " + listImageFile.size+" "+listImageByts.size)

        var pakatId = ""
        AddProductObjectData.selectedPakat?.let {
            pakatId = it.id.toString()
        }

        /**********/
        addProductViewModel.getAddProduct2(
            this,
            nameAr = AddProductObjectData.itemTitleAr,
            nameEn = AddProductObjectData.itemTitleEn,
            subTitleAr = AddProductObjectData.subtitleAr,
            subTitleEn = AddProductObjectData.subtitleEn,
            descriptionAr = AddProductObjectData.itemDescriptionAr,
            descriptionEn = AddProductObjectData.itemDescriptionEn,
            qty = AddProductObjectData.quantity,
            price = AddProductObjectData.price,
            priceDisc = "0",
            acceptQuestion = false.toString(),
            isNegotiationOffers = AddProductObjectData.isnegotiable.toString(),
            withFixedPrice = withFixedPrice,
            isMazad = isMazad,
            isSendOfferForMazad = isMazad,
            startPriceMazad = "0",
            lessPriceMazad = "0",
            mazadNegotiatePrice = "0",
            mazadNegotiateForWhom = "0",
            appointment = "".toString(),
            productCondition = AddProductObjectData.productCondition.toString(),
            categoryId = AddProductObjectData.selectedCategoryId.toString(),
            countryId = AddProductObjectData.country!!.id.toString(),
            regionId = AddProductObjectData.region!!.id.toString(),
            neighborhoodId = AddProductObjectData.city!!.id.toString(),
            Street = "",
            GovernmentCode = "",
            pakatId = pakatId,
            productSep = AddProductObjectData.productSpecificationList,
//            listImageFile = listImageUri,//listImageFile
            listImageFile = listImageUri,//listImageFile
            MainImageIndex = mainIndex.toString(),
            videoUrl = AddProductObjectData.video,
            PickUpDelivery = AddProductObjectData.pickUpOption.toString(),
            DeliveryOption = "1",
        )


//            addProductViewModel.getAddProduct(
//                nameAr = AddProductObjectData.itemTitleAr,
//                nameEn = AddProductObjectData.itemTitleEn,
//                subTitleAr = AddProductObjectData.subtitleAr,
//                subTitleEn = AddProductObjectData.subtitleEn,
//                descriptionAr = AddProductObjectData.itemDescriptionAr,
//                descriptionEn = AddProductObjectData.itemDescriptionEn,
//                qty = AddProductObjectData.quantity,
//                price = AddProductObjectData.price,
//                priceDisc = "0",
//                acceptQuestion = false.toString(),
//                isNegotiationOffers = AddProductObjectData.isnegotiable.toString(),
//                withFixedPrice = withFixedPrice,
//                isMazad = isMazad,
//                isSendOfferForMazad = isMazad,
//                startPriceMazad = "0",
//                lessPriceMazad = "0",
//                mazadNegotiatePrice = "0",
//                mazadNegotiateForWhom = "0",
//                appointment = "".toString(),
//                productCondition = AddProductObjectData.productCondition.toString(),
//                categoryId = AddProductObjectData.selectedCategoryId.toString(),
//                countryId = AddProductObjectData.country!!.id.toString(),
//                regionId = AddProductObjectData.region!!.id.toString(),
//                neighborhoodId = AddProductObjectData.city!!.id.toString(),
//                Street = "",
//                GovernmentCode = "",
//                pakatId=pakatId,
//                productSep = "",
//                listImageFile = listImageFile,//listImageFile
//                MainImageIndex = mainIndex.toString(),
//                videoUrl = AddProductObjectData.video,
//                PickUpDelivery = AddProductObjectData.pickUpOption.toString(),
//                DeliveryOption = "1",
        //          )

        /*************/
//            val paymentMethodList: ArrayList<Selection> = ArrayList()
//            paymentMethodList.apply {
//                clear()
//                add(Selection(getString(R.string.Saudiabankdeposit)))
//                add(Selection(getString(R.string.visa_mastercard)))
//            }
//            CommonBottomSheet().showPaymentOption(getString(R.string.PaymentOptions),paymentMethodList,this) {
//                when (it.name) {
//                    getString(R.string.saudi_bank_deposit) -> {
//
//                    }
//                    getString(R.string.visa_mastercard) -> {
//                        CommonAPI().GetUserCreditCards(this) {
//                            CommonBottomSheet().showCardSelection(this, it, {
//                                mainModelToJSON()
//                            }) {
//                                btn_confirm_details.performClick()
//                            }
//                        }
//
//                    }
//                }
//
//
//            }
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
        AddProductObjectData.images.filter {
            it.is_main == true
        }.let {
            if (it.size > 0) {
                selectedImages.setImageURI(it[0].uri)
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

        when (AddProductObjectData.buyingType) {
            "1" -> {
                tvFixedPrice.show()
                purchasing_price_.show()
                purchasing_price_tv.text = AddProductObjectData.price
            }
            "2" -> {
                tvAuction.show()
                auction_start_price.show()
                minimum_price.show()
                auction_start_price_tv.text = AddProductObjectData.startingPrice
                minimum_price_tv.text = AddProductObjectData.reservedPrice

            }
            "12" -> {
                tvFixedPrice.show()
                tvAuction.show()
                purchasing_price_.show()
                auction_start_price.show()
                minimum_price.show()
                purchasing_price_tv.text = AddProductObjectData.price
                auction_start_price_tv.text = AddProductObjectData.startingPrice
                minimum_price_tv.text = AddProductObjectData.reservedPrice
            }
        }

        if (AddProductObjectData.isnegotiable) {
            negotiable_tv.text = getString(R.string.Yes)
        } else {
            negotiable_tv.text = getString(R.string.No)
        }

        if (AddProductObjectData.isbankpaid) {
            saudi_bank_deposit.show()
        }
        if (AddProductObjectData.isvisapaid) {
            Visa.show()
        }
        if (AddProductObjectData.pickUpOption) {
            tvPickupOptionData.text = getString(R.string.Yes)
        } else {
            tvPickupOptionData.text = getString(R.string.No)
        }
        tvShippingOption.text = AddProductObjectData.shippingOptionSelection?.name ?: ""

        if (AddProductObjectData.selectedPakat == null) {
            tv_package_price.text = getString(R.string.notSpecified)
            tv_package_name.text = getString(R.string.notSpecified)
            calculation(0f)
        } else {
            tv_package_price.text = AddProductObjectData.selectedPakat?.price.toString()
            tv_package_name.text = AddProductObjectData.selectedPakat?.name.toString()
            AddProductObjectData.selectedPakat?.let {
                calculation(it.price)
            }

//            val package_cost = AddProductObjectData.selectPromotiion!!.packageprice.toInt()
//            calculation(package_cost)
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


    }


    @SuppressLint("SetTextI18n")
    private fun calculation(package_cost: Float) {
        val TaxAmount = package_cost * 12 / 100
        val total = package_cost + TaxAmount
        package_cost_tv.text = "${package_cost} ${getString(R.string.rial)}"
        added_tax.text = "${TaxAmount} ${getString(R.string.rial)}"
        total_tv.text = "${total} ${getString(R.string.rial)}"
        discount_tv.text = "${"0"} ${getString(R.string.rial)}"
    }


    // Methods to handle dynamic ad creation
    private fun mainModelToJSON() {
        HelpFunctions.startProgressBar(this)

//        if (StaticClassAdCreate.brand_new_item.equals(getString(R.string.New))) {
//            StaticClassAdCreate.brand_new_item = "on"
//        } else if (StaticClassAdCreate.brand_new_item.equals(getString(R.string.used))) {
//            StaticClassAdCreate.brand_new_item = "Off"
//
//        }


        val mainModel = CreateAdvMainModel(
            Id = null,
            City = AddProductObjectData.city!!.title,
            Country = AddProductObjectData.country!!.title,
            name = AddProductObjectData.name,
            slug = AddProductObjectData.slug,
            Template = AddProductObjectData.template,
            Region = AddProductObjectData.region!!.title,
            Urgentexpirydate = AddProductObjectData.urgentexpirydate,
            title = AddProductObjectData.itemTitleAr,
            Price = AddProductObjectData.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = AddProductObjectData.startingPrice,
            ReservePrice = AddProductObjectData.reservedPrice,

            Duration = AddProductObjectData.duration,
            EndTime = AddProductObjectData.endtime,
            FixLength = AddProductObjectData.fixLength,
            Timepicker = AddProductObjectData.timepicker,
            DateTime = AddProductObjectData.endtime,

            isActive = false,
            isWatching = false,
            Isuserfavorite = false,
            listingType = AddProductObjectData.buyingType,
            quantity = AddProductObjectData.quantity.toInt(),
            featureexpirydate = AddProductObjectData.featureexpirydate,
            highlightexpirydate = AddProductObjectData.highlightexpirydate,
            phone = AddProductObjectData.phone,
            address = AddProductObjectData.address,
            pickupOption = AddProductObjectData.shippingOptionSelection!!.name,
            shippingOption = AddProductObjectData.shippingOptionSelection!!.name,
            description = AddProductObjectData.itemDescriptionAr,
            subtitle = AddProductObjectData.subtitleAr,
            // producttitle = AddProductObjectData.productTitle,
            enddate = AddProductObjectData.endtime,
            platform = "Android",
            iscashpaid = AddProductObjectData.iscashpaid,
            isvisapaid = AddProductObjectData.isvisapaid,
            isbankpaid = AddProductObjectData.isbankpaid,
            isnegotiable = AddProductObjectData.isnegotiable,
            subcatone = AddProductObjectData.subcatone,
            subcattwo = AddProductObjectData.subcattwo,
            subcatthree = AddProductObjectData.subcatthree,
            subcatfour = AddProductObjectData.subcatfour,
            subcatfive = AddProductObjectData.subcatfive,
            subcatsix = AddProductObjectData.subcatsix,
            subcatonekey = AddProductObjectData.subcatonekey,
            subcattwokey = AddProductObjectData.subcattwokey,
            subcatthreekey = AddProductObjectData.subcatthreekey,
            subcatfourkey = AddProductObjectData.subcatfourkey,
            subcatfivekey = AddProductObjectData.subcatfivekey,
            subcatsixkey = AddProductObjectData.subcatsixkey,
            category = AddProductObjectData.subCategoryPath[0],
            Video = AddProductObjectData.video,
            brand_new_item = AddProductObjectData.brand_new_item,
        )
//        if(AddProductObjectData.selectPromotiion==null){
//            mainModel.pack4=""
//        }else{
//            mainModel.pack4= AddProductObjectData.selectPromotiion!!.packageprice
//
//        }


        val imageList: ArrayList<String> = ArrayList()

        AddProductObjectData.images.forEach {
            if (it.is_main == true) {
                imageList.add(it.base64)
            }
        }

        AddProductObjectData.images.forEach {
            if (it.is_main != true) {
                imageList.add(it.base64)
            }
        }

//        imageList.clear()
//        imageList.add("data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUA" +
//                "    AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO" +
//                "        9TXL0Y4OHwAAAABJRU5ErkJggg==")

        mainModel.images = imageList

        val jsonString = Gson().toJson(mainModel)

        var map: Map<String, String> = HashMap()
        map = Gson().fromJson(jsonString, map.javaClass)

        ConstantObjects.dynamic_json_dictionary.putAll(map)
//        val testing = Gson().toJson(map)
//        print(testing)

        createAllAds(ConstantObjects.dynamic_json_dictionary)

    }

    private fun createAllAds(data: HashMap<String, String>) {

        val malqaa = RetrofitBuilder.GetRetrofitBuilder()

        val call: Call<CreateAdvResponseBack> = malqaa.createAllAd(data)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {
                    AddProductObjectData.buyingType = ""
                    AddProductObjectData.brand_new_item = ""
                    AddProductObjectData.shippingOptionSelection = null
//                    AddProductObjectData.selectPromotiion=null
                    AddProductObjectData.subCategoryPath.clear()
                    Extension.clearPath()

                    val AdvId = response.body()!!.data
                    HelpFunctions.dismissProgressBar()
                    startActivity(
                        Intent(
                            this@ConfirmationAddProductActivity,
                            SuccessProductActivity::class.java
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            putExtra("AdvId", AdvId)
                            putExtra("Template", AddProductObjectData.template)
                            putExtra("sellerID", ConstantObjects.logged_userid)
                        })
                    finish()


                } else {
                    HelpFunctions.dismissProgressBar()
                    HelpFunctions.ShowLongToast(
                        getString(R.string.something_went_wrong),
                        this@ConfirmationAddProductActivity
                    )
                }

            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                HelpFunctions.ShowLongToast(
                    t.message + " " + getString(R.string.failed),
                    this@ConfirmationAddProductActivity
                )
            }
        })
    }

}