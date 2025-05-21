package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.confirmationAddProduct

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityConfirmationAddProductBinding
import com.malqaa.androidappp.databinding.AddCardBinding
import com.malqaa.androidappp.databinding.AllCardsLayoutBinding
import com.malqaa.androidappp.databinding.ItemCardBinding
import com.malqaa.androidappp.databinding.MyProductDetailsBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.enums.PaymentAccountType
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountDetails
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData.Companion.selectedCategory
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.TimeAuctionSelection
import com.malqaa.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionObject
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.SuccessProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity2.ChooseCategoryActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity4.AddPhotoActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity6.PricingActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity7.ListingDurationActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity8.PromotionalActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.ShowAlert
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance
import com.malqaa.androidappp.newPhase.utils.formatAsCardNumber
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.utils.helper.widgets.searchdialog.SearchListItem
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import java.io.File
import java.util.Calendar

class ConfirmationAddProductActivity : BaseActivity<ActivityConfirmationAddProductBinding>() {

    private lateinit var myProductDetailsBinding: MyProductDetailsBinding
    private lateinit var _binding: ActivityConfirmationAddProductBinding

    private lateinit var addProductViewModel: AddProductViewModel
    private var pakagePrice = 0f
    private var fixedPriceFee = 0f
    private var auctionEnableFee = 0f
    private var closingAuctionEnableFee = 0f
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
    private var showPublishPrice = true

    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var selectedPaymentType: PaymentAccountType
    private var pointsNumber: Double? = null
    private var selectedAccountDetails: ArrayList<AccountDetails> = ArrayList()
    private var accountDetails: AccountDetails? = null
    private lateinit var adapterList: GenericListAdapter<AccountDetails>


    var pakatId = ""
    var closed = false

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


        // Initialize view binding for the main activity
        binding = ActivityConfirmationAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        _binding = binding
        closed = intent.getBooleanExtra("closed", false)

        setupAdapter()

        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        addProductViewModel.listBackAccountObserver.observe(this) {
            if (it.status_code == 200) {
                if (it.accountsList != null) {
                    newAllCardsBottomSheetDialog(it.accountsList)
                    if (AddProductObjectData.paymentOptionList != null) {
                        AddProductObjectData.paymentOptionList?.let { paymentOptionList ->
                            if (paymentOptionList.contains(AddProductObjectData.PAYMENT_OPTION_BANk) && AddProductObjectData.selectedAccountDetails != null) {
                                addAllCardsAdaptor(it.accountsList)
                            } else
                                addAllCardsAdaptor(it.accountsList)
                        }
                    } else {
                        addAllCardsAdaptor(it.accountsList)
                    }
                }
            }
        }
        addProductViewModel.addBackAccountObserver.observe(this) {
            if (it.status_code == 200) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog!!.dismiss()
                }
                addProductViewModel.getBankAccountsList(selectedPaymentType.value)
            }
        }
        addProductViewModel.pointsBalance.observe(this) {
            if (it.status_code == 200) {
                val pointsBalance = it.pointsBalance?.pointsBalance ?: 0
                val pointsCountToTransfer = it.pointsBalance?.pointsCountToTransfer ?: 0
                val moneyOfPointsTransferred = it.pointsBalance?.moneyOfPointsTransferred ?: 0.0
                val totalToPay = totalPrice

                // Check to avoid division by zero
                if (pointsCountToTransfer > 0 && moneyOfPointsTransferred > 0) {
                    // How many points equal one pound
                    val pointsPerMoney = pointsCountToTransfer.toDouble() / moneyOfPointsTransferred

                    // The number of points required to pay the product price
                    val pointsRequired = totalToPay * pointsPerMoney

                    if (pointsBalance >= pointsRequired) {
                        Log.d("test #1", "✅ You can pay with points!")

                        // We calculate the value of the points available in the pound
                        val moneyBalance =
                            (pointsBalance.toDouble() / pointsCountToTransfer) * moneyOfPointsTransferred

                        val productPriceSar =
                            getString(R.string.product_price_sar, moneyBalance.toString())
                        val myPointsBalance =
                            getString(R.string.the_points_balance_equals, productPriceSar)

                        binding.selectedMyPoints.textMyPointsBalance.text = myPointsBalance
                        pointsNumber = pointsRequired
                        selectedPaymentType = PaymentAccountType.Points

                        binding.layoutMyPointsPayment.background =
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.field_selection_border_enable
                            )
                        binding.tvMyPointsPayment.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.bg
                            )
                        )
                        binding.selectedMyPoints.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE
                    } else {
                        binding.switchMyPointsPayment.isChecked = false

                        // Calculate available money value of points
                        val moneyBalance =
                            (pointsBalance.toDouble() / pointsCountToTransfer) * moneyOfPointsTransferred

                        // Format the money balance as SAR
                        val productPriceSar =
                            getString(R.string.product_price_sar, moneyBalance.toString())

                        // Full message
                        val pointsBalanceText =
                            getString(R.string.points_balance_equals, productPriceSar)
                        val insufficientMessage = getString(R.string.balance_insufficient)
                        val finalMessage = "$pointsBalanceText\n$insufficientMessage"

                        ShowAlert(
                            context = this,
                            alertTitle = getString(R.string.my_points),
                            icon = R.drawable.failed_message,
                            alertMessage = finalMessage
                        )
                    }
                } else {
                    binding.switchMyPointsPayment.isChecked = false
                    Log.e(
                        "test #1",
                        "❌ Invalid point conversion configuration (division by zero risk)"
                    )
                }

            } else {
                binding.switchMyPointsPayment.isChecked = false
            }
        }
        addProductViewModel.getShowProductPrice()
        addProductViewModel.showProductPriceResp.observe(this) {
            if (it.status_code == 200) {
                showPublishPrice = it.data!!
                setSummeryData()
            }
        }

        // Initialize view binding for the product details layout
        myProductDetailsBinding = MyProductDetailsBinding.inflate(layoutInflater)

        binding.toolbarConfirmation.toolbarTitle.text = getString(R.string.distinguish_your_product)
        setViewClickListeners()
        setData()
        setUpViewModel()
        if (intent.getStringExtra("whereCome").equals("repost")) {
            addProductViewModel.getProductPaymentOptions(intent.getIntExtra("productID", 0))
            addProductViewModel.getProductShippingOptions(intent.getIntExtra("productID", 0))
            addProductViewModel.getProductBankAccounts(intent.getIntExtra("productID", 0))
            addProductViewModel.getProductDetailsById(intent.getIntExtra("productID", 0))
        }/* else setSummeryData()*/
    }

    @SuppressLint("SetTextI18n")
    fun setData() {
        binding.tvQuantityData.text = null
        binding.tvTitleData.text = null
        binding.tvSubTitleData.text = null
        binding.tvProductDetail.text = null
        binding.productType.text = null
        binding.tvItemCondition.text = null
        binding.purchasingPriceTv.text = null
        binding.auctionStartPriceTv.text = null
        binding.minimumPriceTv.text = null
        binding.tvShippingOption.text = null
        binding.tvPackagePrice.text = null

        if (ConstantObjects.isModify) {
            binding.editSelectedPackage.hide()
            binding.titleCoupon.hide()
            binding.layEdtCoupon.hide()
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
                        binding.selectedImages.setImageURI(it[0].uri)
                    else {
                        getPicassoInstance().load(it[0].url?.replace("http", "https"))
                            .placeholder(R.drawable.splash_logo).error(R.drawable.splash_logo)
                            .into(binding.selectedImages)
                    }

                }
            }
        }

        binding.productType.text = AddProductObjectData.selectedCategoryName

        if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
            binding.tvTitleData.text = AddProductObjectData.itemTitleAr
            binding.tvSubTitleData.text = AddProductObjectData.subtitleAr
            binding.tvProductDetail.text = AddProductObjectData.itemDescriptionAr
        } else {
            binding.tvTitleData.text = AddProductObjectData.itemTitleEn
            binding.tvSubTitleData.text = AddProductObjectData.subtitleEn
            binding.tvProductDetail.text = AddProductObjectData.itemDescriptionEn
        }
        binding.tvItemCondition.text =
            if (AddProductObjectData.productCondition == 1) getString(R.string.used) else getString(
                R.string.New
            )

        binding.tvQuantityData.text = AddProductObjectData.quantity
        if (AddProductObjectData.priceFixedOption) {
            binding.tvFixedPriceOptionSale.show()
        } else {
            binding.tvFixedPriceOptionSale.hide()
        }
        if (AddProductObjectData.auctionOption) {
            binding.tvAuctionOpitonSale.show()
        } else {
            binding.tvAuctionOpitonSale.hide()
        }

        if (AddProductObjectData.isNegotiablePrice) {
            binding.negotiableTv.text = getString(R.string.Yes)
            binding.tvNegotiationPriceOptionSale.hide()
        } else {
            binding.negotiableTv.text = getString(R.string.No)
            binding.tvNegotiationPriceOptionSale.hide()
        }
        if (AddProductObjectData.auctionOption || productDetails?.isFixedPriceEnabled == true) {
            binding.linearSaleType.show()
        } else {
            binding.linearSaleType.hide()
        }
        /**Back**/
        binding.tvCashOptionPayment.hide()
        binding.tvSaudiBankDepositOptionPayment.hide()
        binding.tvMadaOptionPayment.hide()
        binding.tvCardOptionPayment.hide()
        if (AddProductObjectData.paymentOptionList?.isEmpty() == true) {
            binding.layPaymentOption.hide()
        } else {
            binding.layPaymentOption.show()
        }

        AddProductObjectData.paymentOptionList?.let { paymentOptionList ->
            for (item in paymentOptionList) {
                when (item) {
                    AddProductObjectData.PAYMENT_OPTION_BANk -> {
                        binding.tvSaudiBankDepositOptionPayment.show()
                    }

                    AddProductObjectData.PAYMENT_OPTION_CASH -> {
                        binding.tvCashOptionPayment.show()
                    }

                    AddProductObjectData.PAYMENT_OPTION_Mada -> {
                        binding.tvMadaOptionPayment.show()
                    }

                    AddProductObjectData.PAYMENT_OPTION_MasterCard -> {
                        binding.tvCardOptionPayment.show()
                    }
                }
            }
        }

        AddProductObjectData.shippingOptionSelections?.let {

            binding.tvShippingOption.text = ""
            binding.tvPickupOptionData.text = ""
            for (i in it) {
//                tvShippingOption.text = shippingOptionText
                when (i) {
                    ConstantObjects.pickUp_Must -> {
                        binding.tvPickupOptionData.append(getString(R.string.mustPickUp))
                    }

                    ConstantObjects.pickUp_No -> {
                        binding.tvPickupOptionData.append(getString(R.string.noPickUp))
                    }

                    ConstantObjects.pickUp_Available -> {
                        binding.tvPickupOptionData.append(getString(R.string.pickUpAvaliable))
                    }

                    ConstantObjects.shippingOption_integratedShippingCompanyOptions -> {
                        binding.tvShippingOption.append(getString(R.string.integratedShippingCompanies))
                    }

                    ConstantObjects.shippingOption_freeShippingWithinSaudiArabia -> {
                        binding.tvShippingOption.append(getString(R.string.free_shipping_within_Saudi_Arabia))
                    }

                    ConstantObjects.shippingOption_arrangementWillBeMadeWithTheBuyer -> {
                        binding.tvShippingOption.append(getString(R.string.arrangementWillBeMadeWithTheBuyer))
                    }
                }
            }

        }

        if (AddProductObjectData.auctionOption) {
            binding.containerAuction.show()
            if (AddProductObjectData.selectTimeAuction?.customOption == true) {
                binding.tvDateAuction.text = AddProductObjectData.selectTimeAuction?.text
            } else {
                binding.tvDateAuction.text =
                    HelpFunctions.getAuctionClosingTime(AddProductObjectData.selectTimeAuction?.endTime.toString())
//                AddProductObjectData.selectTimeAuction?.endTimeUTC
            }

        } else {
            binding.containerAuction.hide()
        }
    }

    private fun showPaymentMethod(paymentOptionList: ArrayList<ShippingOptionObject>) {
        val paymentList = ArrayList<Int>()
        if (paymentOptionList.isEmpty()) {
            binding.layPaymentOption.hide()
        } else {
            binding.layPaymentOption.show()
        }
        for (item in paymentOptionList) {
            when (item.paymentOptionId) {
                AddProductObjectData.PAYMENT_OPTION_CASH -> {
                    binding.tvCashOptionPayment.show()
                    paymentList.add(AddProductObjectData.PAYMENT_OPTION_CASH)
                }

                AddProductObjectData.PAYMENT_OPTION_BANk -> {
                    binding.tvSaudiBankDepositOptionPayment.show()
                    paymentList.add(AddProductObjectData.PAYMENT_OPTION_BANk)
                }

                AddProductObjectData.PAYMENT_OPTION_Mada -> {
                    binding.tvMadaOptionPayment.show()
                    paymentList.add(AddProductObjectData.PAYMENT_OPTION_Mada)
                }

                AddProductObjectData.PAYMENT_OPTION_MasterCard -> {
                    binding.tvCardOptionPayment.show()
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
        binding.tvShippingOption.text = shippingOptionText
        binding.tvPickupOptionData.text = pickup


        AddProductObjectData.shippingOptionSelections?.addAll(shippingList)


    }

    @SuppressLint("SetTextI18n")
    private fun setUpViewModel() {
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
                } catch (e: Exception) {

                }

            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.failed), this
                )
            }
        }
        addProductViewModel.cartPriceSummeryObserver.observe(this) { cartSummery ->
            if (cartSummery.status_code == 200 && cartSummery.priceSummery != null) {
                binding.packageCostTv.text =
                    "${cartSummery.priceSummery.pakatPrice} ${getString(R.string.Rayal)}"
                if (cartSummery.priceSummery.pakatPrice == 0f) {
                    binding.layPackage.visibility = View.GONE
                }
//                tv_package_price.text =
//                    "${cartSummery.priceSummery.pakatPrice} ${getString(R.string.Rayal)}"
//                tv_package_name.text = AddProductObjectData.selectedPakat?.name ?: ""


                val discount: Float =
                    cartSummery.priceSummery.totalPriceBeforeCoupon - cartSummery.priceSummery.totalPriceAfterCoupon
                binding.discountTv.text = "$discount ${getString(R.string.Rayal)}"
                if (discount != 0f) {
                    binding.tvTotal.text =
                        "${cartSummery.priceSummery.totalPriceBeforeCoupon} ${getString(R.string.Rayal)}"
                    totalPrice = cartSummery.priceSummery.totalPriceBeforeCoupon
                } else {
                    binding.tvTotal.text =
                        "${cartSummery.priceSummery.totalPriceAfterCoupon} ${getString(R.string.Rayal)}"
                }
                if (cartSummery.priceSummery.enableFixedPriceSaleFee != 0f) {
                    binding.containerFixedPriceFee.show()
                    binding.tvFixedPriceFee.text =
                        "${cartSummery.priceSummery.enableFixedPriceSaleFee} ${getString(R.string.Rayal)}"
                } else {
                    binding.containerFixedPriceFee.hide()
                }
                if (cartSummery.priceSummery.enableAuctionFee != 0f) {
                    binding.containerAuctionFee.show()
                    binding.tvAuctionFee.text =
                        "${cartSummery.priceSummery.enableAuctionFee} ${getString(R.string.Rayal)}"
                } else {
                    binding.containerAuctionFee.hide()
                }
                if (cartSummery.priceSummery.enableNegotiationFee != 0f) {
                    binding.containerNegotiationFee.show()
                    binding.tvNegotiationFee.text =
                        "${cartSummery.priceSummery.enableNegotiationFee} ${getString(R.string.Rayal)}"
                } else {
                    binding.containerNegotiationFee.hide()

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
                    binding.tvShippingOption.text = ""
                    binding.tvPickupOptionData.text = ""
                    for (i in (it.shippingOptionObject))
                        showShoppingOption(i)
                } else {
                    binding.tvPickupOptionData.text = getString(R.string.mustPickUp)
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
                    binding.tvTotalAfterDiscount.show()
                    binding.tvTotalAfterDiscount.text =
                        "$totalAfterDiscount ${getString(R.string.SAR)}"
                    binding.tvTotal.paintFlags =
                        binding.tvTotal.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.invalidCoupon), this)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProductData(productDetails: Product) {

        binding.ContainerPackge.hide()
        binding.containerFixedPriceFee.hide()
        binding.containerAuctionFee.hide()
        binding.containerNegotiationFee.hide()
        binding.containerSubTitleFee.hide()
        binding.containerImageExtraFee.hide()
        binding.containerVideoExtraFee.hide()
        binding.containerProductPublishPriceFee.hide()
        binding.containerClosingAuctionFee.hide()


        AddProductObjectData.selectedCategoryId = productDetails.categoryId
        AddProductObjectData.selectedCategoryName = productDetails.category.toString()
        pakatId = productDetails.pakatId.toString()

        selectedCategory = productDetails.categoryDto
        /**productPublishFeee**/
        //implement here productPublishFeee and productPublishPrice
        val text = productDetails.categoryDto?.productPublishPrice.toString()
        if(showPublishPrice){
            binding.containerProductPublishPriceFee.show()
            binding.publishLine.hide()
            selectedCategory?.productPublishPrice =
                productDetails.categoryDto?.productPublishPrice ?: 0f
            productPublishPriceFee = productDetails.categoryDto?.productPublishPrice ?: 0f
            binding.tvProductPublishPriceFee.text =
                "$productPublishPriceFee ${getString(R.string.SAR)}"
        }else{
            binding.containerProductPublishPriceFee.show()
            binding.publishLine.show()
            productPublishPriceFee = 0f
            binding.tvProductPublishPriceFee.text =
                "$text ${getString(R.string.SAR)}"
        }
        /**package fee*/
        if (productDetails.selectedPacket != null) {
            AddProductObjectData.selectedPakat = productDetails.selectedPacket
            binding.ContainerPackge.show()
            binding.layPackage.show()
            pakagePrice = AddProductObjectData.selectedPakat?.price ?: 0f
            binding.packageCostTv.text = "$pakagePrice ${getString(R.string.SAR)}"
            binding.tvPackagePrice.text = "$pakagePrice ${getString(R.string.SAR)}"
            binding.tvPackageName.text = productDetails.selectedPacket?.name
        } else {
            binding.ContainerPackge.hide()
            binding.layPackage.hide()
        }

        AddProductObjectData.priceFixed = productDetails.price.toString()
        /**fixedPrice fee*/
        if (productDetails.isFixedPriceEnabled && productDetails.price != 0f) {
            binding.containerFixedPriceFee.show()
            selectedCategory?.enableFixedPriceSaleFee =
                productDetails.categoryDto?.enableFixedPriceSaleFee ?: 0f
            fixedPriceFee = selectedCategory?.enableFixedPriceSaleFee ?: 0f
            binding.tvFixedPriceFee.text = "$fixedPriceFee ${getString(R.string.SAR)}"
        }

        /**AuctionFee fee*/
        if (productDetails.isAuctionEnabled && productDetails.auctionStartPrice != 0f) {
            selectedCategory?.enableAuctionFee =
                productDetails.categoryDto?.enableAuctionFee ?: 0f
            binding.containerAuctionFee.show()
            AddProductObjectData.auctionStartPrice = productDetails.auctionStartPrice.toString()
            AddProductObjectData.auctionMinPrice = productDetails.auctionMinimumPrice.toString()
            auctionEnableFee = selectedCategory?.enableAuctionFee ?: 0f
            binding.tvAuctionFee.text =
                "${productDetails.auctionStartPrice} ${getString(R.string.SAR)}"
        }
        /**AuctionClosingFee*/
        if (closed) {
            selectedCategory?.auctionClosingTimeFee =
                productDetails.categoryDto?.auctionClosingTimeFee ?: 0f
            binding.containerClosingAuctionFee.show()
            var closeText = selectedCategory?.auctionClosingTimeFee
            closingAuctionEnableFee = selectedCategory?.auctionClosingTimeFee ?: 0f
            binding.tvClosingAuctionFee.text = "$closeText ${getString(R.string.SAR)}"
        }
        /**negotiation fee*/
        if (productDetails.isNegotiationEnabled && productDetails.auctionNegotiatePrice != 0f) {
            AddProductObjectData.isNegotiablePrice = productDetails.isNegotiationEnabled
            selectedCategory?.enableNegotiationFee =
                productDetails.categoryDto?.enableNegotiationFee ?: 0f
            binding.containerNegotiationFee.show()
            negotiationFee = selectedCategory?.enableNegotiationFee ?: 0f
            binding.tvNegotiationFee.text = "$negotiationFee ${getString(R.string.SAR)}"
        }

        /**subTitle fee*/
        if (productDetails.subTitle != "" && productDetails.categoryDto?.subTitleFee != 0f) {
            selectedCategory?.subTitleFee =
                productDetails.categoryDto?.subTitleFee ?: 0f
            binding.containerSubTitleFee.show()
            subTitleFee = selectedCategory?.subTitleFee ?: 0f
            binding.tvSubTitleFee.text = "$subTitleFee ${getString(R.string.SAR)}"
        }

        if (!productDetails.listMedia.isNullOrEmpty()) productDetails.listMedia.let {
            val list = productDetails.listMedia.filter { it.type == 2 }.map { it.url }

            AddProductObjectData.videoList = list as ArrayList<String>
            AddProductObjectData.images = it
        }

        /**ExtreImageFee*/
        productDetails.categoryDto?.let { selectedCategory ->
            var extraImages = 0
            AddProductObjectData.images?.let {
                if (it.size > selectedCategory.freeProductImagesCount) {
                    extraImages = it.size - selectedCategory.freeProductImagesCount
                }
            }
            if (extraImages > 0) {
                extraImageFee = selectedCategory.extraProductImageFee * extraImages
                if (extraImageFee != 0f) {
                    binding.containerImageExtraFee.show()
                    binding.tvImageExtraFee.text = "$extraImageFee ${getString(R.string.SAR)}"
                }
            }
        }
        /**ExtreVideoFee*/
        productDetails.categoryDto?.let { selectedCategory ->
            var extraVideo = 0
            AddProductObjectData.videoList?.let {
                if (it.size > selectedCategory.freeProductVidoesCount) {
                    extraVideo = it.size - selectedCategory.freeProductVidoesCount
                }
            }
            if (extraVideo > 0) {
                extraVideoFee = selectedCategory.extraProductVidoeFee * extraVideo
                if (extraVideoFee != 0f) {
                    binding.containerVideoExtraFee.show()
                    binding.tvVideoExtraFee.text = "$extraVideoFee ${getString(R.string.SAR)}"
                }
            }
        }

        // Step 1: Calculate Subtotal
        val subtotal = pakagePrice + fixedPriceFee + auctionEnableFee + negotiationFee +
                subTitleFee + extraImageFee + extraVideoFee + productPublishPriceFee +closingAuctionEnableFee

        // Step 2: Calculate Tax (15%)
        val taxAmount = subtotal * 0.15

        // Step 3: Calculate Total with VAT
        val totalWithVat = subtotal + taxAmount

        // Step 4: Display the values
        binding.tvSubtotal.text = String.format("%.2f", subtotal)  // e.g., "150.00"
        binding.tvVat.text = String.format("%.2f", taxAmount)       // e.g., "22.50"
        binding.tvTotal.text = "${String.format("%.2f", totalWithVat)} ${getString(R.string.SAR)}"
        totalPrice = totalWithVat.toFloat()


        AddProductObjectData.productCondition = productDetails.status

        binding.tvItemCondition.text =
            if (AddProductObjectData.productCondition == 1) getString(R.string.used) else getString(
                R.string.New
            )

        AddProductObjectData.productCondition = productDetails.status
        binding.tvQuantityData.text = productDetails.qty.toString()
        AddProductObjectData.quantity = productDetails.qty.toString()

        if (productDetails.isAuctionEnabled) {
            binding.tvAuctionOpitonSale.show()
        } else {
            binding.tvAuctionOpitonSale.hide()
        }
        if (AddProductObjectData.auctionOption || productDetails.isFixedPriceEnabled) {
            binding.linearSaleType.show()
        } else {
            binding.linearSaleType.hide()
        }
        if (productDetails.isFixedPriceEnabled) {
            binding.tvFixedPriceOptionSale.show()
        } else {
            binding.tvFixedPriceOptionSale.hide()
        }

        AddProductObjectData.isNegotiablePrice = productDetails.isNegotiationEnabled
        AddProductObjectData.priceFixedOption = productDetails.isFixedPriceEnabled
        AddProductObjectData.auctionOption = productDetails.isAuctionEnabled

        if (productDetails.isNegotiationEnabled) {
            binding.negotiableTv.text = getString(R.string.Yes)
        } else {
            binding.negotiableTv.text = getString(R.string.No)
        }

        if (productDetails.isAuctionEnabled) {
            binding.containerAuction.show()
            if (productDetails.auctionClosingTime != null && (productDetails.auctionClosingTime != "")) {
                AddProductObjectData.isAuctionClosingTimeFixed =
                    productDetails.isAuctionClosingTimeFixed
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

                binding.tvDateAuction.text =
                    HelpFunctions.getAuctionClosingTime2(productDetails.auctionClosingTime)
            }
        } else {
            binding.containerAuction.hide()
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
                binding.selectedImages.setImageResource(R.mipmap.ic_launcher)
            } else {
                getPicassoInstance().load(obj?.url?.replace("http", "https"))
                    .placeholder(R.drawable.splash_logo).error(R.drawable.splash_logo)
                    .into(binding.selectedImages)
            }
            AddProductObjectData.images = productDetails.listMedia
        }

        AddProductObjectData.productSpecificationList =
            productDetails.listProductSep ?: arrayListOf()

        AddProductObjectData.selectedCategoryName = productDetails.category.toString()
        binding.productType.text = productDetails.category

        AddProductObjectData.itemTitleAr = productDetails.nameAr.toString()
        AddProductObjectData.subtitleAr = productDetails.subTitleAr ?: ""
        AddProductObjectData.itemDescriptionAr = productDetails.descriptionAr ?: ""
        AddProductObjectData.itemTitleEn = productDetails.nameEn.toString()
        AddProductObjectData.subtitleEn = productDetails.subTitle ?: ""
        AddProductObjectData.itemDescriptionEn = productDetails.descriptionEn ?: ""

        binding.tvTitleData.text = productDetails.name
        binding.tvSubTitleData.text = productDetails.subTitle
        binding.tvProductDetail.text = productDetails.description ?: ""
    }

    private fun showProductApiError(message: String) {
        if (productDetails == null) {
            // Get the container from the included layout
            val containerMainProduct =
                myProductDetailsBinding.root.findViewById<View>(R.id.containerMainProduct)
            containerMainProduct.hide()
            myProductDetailsBinding.containerShareAndFav.hide()
        }
        HelpFunctions.ShowLongToast(message, this)
    }

    @SuppressLint("SetTextI18n")
    private fun setSummeryData() {
        binding.ContainerPackge.hide()
        binding.containerFixedPriceFee.hide()
        binding.containerAuctionFee.hide()
        binding.containerNegotiationFee.hide()
        binding.containerSubTitleFee.hide()
        binding.containerImageExtraFee.hide()
        binding.containerVideoExtraFee.hide()
        binding.containerProductPublishPriceFee.hide()
        binding.containerClosingAuctionFee.hide()


        val text = selectedCategory?.productPublishPrice.toString()
        if(showPublishPrice){
            binding.containerProductPublishPriceFee.show()
            binding.publishLine.hide()
            productPublishPriceFee =
                selectedCategory?.productPublishPrice ?: 0f
            binding.tvProductPublishPriceFee.text =
                "$productPublishPriceFee ${getString(R.string.SAR)}"
        }else{
            binding.containerProductPublishPriceFee.show()
            binding.publishLine.show()
            productPublishPriceFee = 0f
            binding.tvProductPublishPriceFee.text =
                "$text ${getString(R.string.SAR)}"
        }
        /**package fee*/
        if (AddProductObjectData.selectedPakat != null) {
            binding.layPackage.show()
            binding.ContainerPackge.show()
            pakagePrice = AddProductObjectData.selectedPakat?.price ?: 0f
            binding.packageCostTv.text = "$pakagePrice ${getString(R.string.SAR)}"
            binding.tvPackagePrice.text = "$pakagePrice ${getString(R.string.SAR)}"
            binding.tvPackageName.text = AddProductObjectData.selectedPakat?.name
        } else {
            binding.ContainerPackge.hide()
            binding.layPackage.hide()
        }

        if (AddProductObjectData.priceFixedOption && selectedCategory?.enableFixedPriceSaleFee != 0f) {
            binding.containerFixedPriceFee.show()
            fixedPriceFee = selectedCategory?.enableFixedPriceSaleFee ?: 0f
            binding.tvFixedPriceFee.text = "$fixedPriceFee ${getString(R.string.SAR)}"
        }
        /**AuctionFee fee*/
        if (AddProductObjectData.auctionOption && selectedCategory?.enableAuctionFee != 0f) {
            binding.containerAuctionFee.show()
            auctionEnableFee=selectedCategory?.enableAuctionFee ?: 0f
            binding.tvAuctionFee.text = "$auctionEnableFee ${getString(R.string.SAR)}"
        }
        /**AuctionClosingFee*/
        if (closed) {
            binding.containerClosingAuctionFee.show()
            var closeText = selectedCategory?.auctionClosingTimeFee
            closingAuctionEnableFee = selectedCategory?.auctionClosingTimeFee ?: 0f
            binding.tvClosingAuctionFee.text = "$closeText ${getString(R.string.SAR)}"
        }
        /**negotiation fee*/
        if (AddProductObjectData.isNegotiablePrice && selectedCategory?.enableNegotiationFee != 0f) {
            binding.containerNegotiationFee.show()
            negotiationFee = selectedCategory?.enableNegotiationFee ?: 0f
            binding.tvNegotiationFee.text = "$negotiationFee ${getString(R.string.SAR)}"
        }
        /**subTitle fee*/
        if ((AddProductObjectData.subtitleAr != "" || AddProductObjectData.subtitleEn != "") && selectedCategory?.subTitleFee != 0f) {
            binding.containerSubTitleFee.show()
            subTitleFee = selectedCategory?.subTitleFee ?: 0f
            binding.tvSubTitleFee.text = "$subTitleFee ${getString(R.string.SAR)}"

            if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
                binding.tvTitleData.text = if (AddProductObjectData.itemTitleAr != "")
                    AddProductObjectData.itemTitleAr
                else
                    AddProductObjectData.itemTitleEn

                binding.tvSubTitleData.text = if (AddProductObjectData.subtitleAr != "")
                    AddProductObjectData.subtitleAr
                else
                    AddProductObjectData.subtitleEn

                binding.tvProductDetail.text = if (AddProductObjectData.itemDescriptionAr != "")
                    AddProductObjectData.itemDescriptionAr
                else
                    AddProductObjectData.itemDescriptionEn


            } else {
                binding.tvTitleData.text = if (AddProductObjectData.itemTitleEn != "")
                    AddProductObjectData.itemTitleEn
                else
                    AddProductObjectData.itemTitleAr

                binding.tvSubTitleData.text = if (AddProductObjectData.subtitleEn != "")
                    AddProductObjectData.subtitleEn
                else
                    AddProductObjectData.subtitleAr

                binding.tvProductDetail.text = if (AddProductObjectData.itemDescriptionEn != "")
                    AddProductObjectData.itemDescriptionEn
                else
                    AddProductObjectData.itemDescriptionAr

            }
        }

        val countImagePackage = AddProductObjectData.selectedPakat?.countImage ?: 0
        val countVideoPackage = AddProductObjectData.selectedPakat?.countVideo ?: 0

        updateExtraPackageUI(
            countImagePackage = countImagePackage,
            countVideoPackage = countVideoPackage
        )

        // Step 1: Calculate Subtotal
        val subtotal = pakagePrice + fixedPriceFee + auctionEnableFee + negotiationFee +
                subTitleFee + extraImageFee + extraVideoFee + productPublishPriceFee + closingAuctionEnableFee

        // Step 2: Calculate Tax (15%)
        val taxAmount = subtotal * 0.15

        // Step 3: Calculate Total with VAT
        val totalWithVat = subtotal + taxAmount
        totalPrice = totalWithVat.toFloat()

        // Step 4: Display the values
        binding.tvSubtotal.text = String.format("%.2f", subtotal)  // e.g., "150.00"
        binding.tvVat.text = String.format("%.2f", taxAmount)       // e.g., "22.50"
        binding.tvTotal.text = "${String.format("%.2f", totalWithVat)} ${getString(R.string.SAR)}"
    }

    private fun updateExtraPackageUI(countImagePackage: Int, countVideoPackage: Int) {
        selectedCategory?.let { category ->
            val imageCount = AddProductObjectData.images?.size ?: 0
            val videoCount = AddProductObjectData.videoList?.size ?: 0

            val currentImageCount = maxOf(imageCount - category.freeProductImagesCount, b = 0)
            val currentVideoCount = maxOf(videoCount - category.freeProductVidoesCount, b = 0)

            val extraImages = maxOf(currentImageCount - countImagePackage, b = 0)
            val extraVideos = maxOf(currentVideoCount - countVideoPackage, b = 0)

            if (extraImages > 0) {
                extraImageFee = category.extraProductImageFee * extraImages
                if (extraImageFee != 0f) {
                    binding.containerImageExtraFee.show()
                    binding.tvImageExtraFee.text = "$extraImageFee ${getString(R.string.SAR)}"
                }
            }

            if (extraVideos > 0) {
                extraVideoFee = category.extraProductVidoeFee * extraVideos
                if (extraVideoFee != 0f) {
                    binding.containerVideoExtraFee.show()
                    binding.tvVideoExtraFee.text = "$extraVideoFee ${getString(R.string.SAR)}"
                }
            }
        }
    }

    private fun resetAddProductObject() {
        selectedCategory = null
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
        binding.btnCoupon.setOnClickListener {
            if (binding.etCoupon.text.toString().trim() != "") {
                addProductViewModel.getCouponByCode(binding.etCoupon.text.toString().trim(), "1")
            } else {
                binding.etCoupon.error = getString(R.string.enter_the_coupon)
            }
        }
        binding.toolbarConfirmation.backBtn.setOnClickListener {
            finish()
        }
        binding.tvEditProductDetails.setOnClickListener {
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
        binding.editItemPayment.setOnClickListener {
            startActivity(Intent(this, PricingActivity::class.java).apply {
                putExtra(ConstantObjects.isEditKey, true)
            })
            finish()
        }
        binding.editSelectedPackage.setOnClickListener {
            startActivity(Intent(this, PromotionalActivity::class.java).apply {
                putExtra(ConstantObjects.isEditKey, true)
            })
            finish()
        }
        binding.editShopingOption.setOnClickListener {
            startActivity(Intent(this, ListingDurationActivity::class.java).apply {
                putExtra(ConstantObjects.isEditKey, true)
            })
            finish()
        }

        // =========================================================================================
        binding.switchVisaCreditCard.setOnCheckedChangeListener { _, b ->
            if (b) {
                //allCardsBottomSheetDialog()
                addProductViewModel.getBankAccountsList(paymentAccountType = PaymentAccountType.VisaMasterCard.value)
                selectedPaymentType = PaymentAccountType.VisaMasterCard

                // hide other
                binding.switchMadaPayment.isChecked = false
                binding.switchMyPointsPayment.isChecked = false
            } else {
                binding.selectedVisaCreditCard.linearLayoutSelectedPaymentOptions.visibility =
                    View.GONE
            }
        }

        binding.switchMadaPayment.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.layoutMadaPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                binding.tvMadaPayment.setTextColor(ContextCompat.getColor(this, R.color.bg))
                //allCardsBottomSheetDialog()
                addProductViewModel.getBankAccountsList(paymentAccountType = PaymentAccountType.Mada.value)
                selectedPaymentType = PaymentAccountType.Mada

                // hide other
                binding.switchVisaCreditCard.isChecked = false
                binding.switchMyPointsPayment.isChecked = false
            } else {
                binding.layoutMadaPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                binding.tvMadaPayment.setTextColor(ContextCompat.getColor(this, R.color.text_color))
                binding.selectedMada.linearLayoutSelectedPaymentOptions.visibility = View.GONE
            }
        }

        binding.switchMyPointsPayment.setOnCheckedChangeListener { _, b ->
            if (b) {
                addProductViewModel.getPointsBalance()
                selectedPaymentType = PaymentAccountType.Points
                Log.i("test #1", "selectedPaymentType: $selectedPaymentType")

                // hide other
                binding.switchVisaCreditCard.isChecked = false
                binding.switchMadaPayment.isChecked = false
            } else {
                binding.layoutMyPointsPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                binding.tvMyPointsPayment.setTextColor(
                    ContextCompat.getColor(this, R.color.text_color)
                )
                binding.selectedMyPoints.linearLayoutSelectedPaymentOptions.visibility = View.GONE
            }
        }

        binding.selectedVisaCreditCard.btnChooseAnotherCard.setOnClickListener {
            allCardsBottomSheetDialog()
        }

        binding.selectedMada.btnChooseAnotherCard.setOnClickListener {
            allCardsBottomSheetDialog()
        }
        // =========================================================================================


        binding.btnConfirmDetails.setOnClickListener {
            if (ConstantObjects.isRepost || ConstantObjects.isModify)
                confirmOrder(isEdit = true)
            else
                confirmOrder(isEdit = false)
        }
        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun confirmOrder(isEdit: Boolean) {

        val isVisaEnabled = binding.switchVisaCreditCard.isChecked
        val isMadaEnabled = binding.switchMadaPayment.isChecked
        val isPointsEnabled = binding.switchMyPointsPayment.isChecked

        if (!isVisaEnabled && !isMadaEnabled && !isPointsEnabled) {
            showError(error = getString(R.string.please_activate_at_least_one_payment_method))
            return
        }

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
                } catch (e: Exception) {
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
                        val file = HelpFunctions.getFileImage(image.uri!!, this)
                        listImageFile.add(file)
                    } catch (e: Exception) {
                    }
                }
            }
        }

        AddProductObjectData.selectedPakat?.let { pakatId = it.id.toString() }
        val shippingOption: ArrayList<String> = ArrayList()
        AddProductObjectData.shippingOptionSelection?.let { shippingOption.add(it[0].id.toString()) }

        val bankList = AddProductObjectData.selectedAccountDetails?.map { it.id }
        val accountDetails = accountDetails?.copy(paymentAccountType = selectedPaymentType)

        addProductViewModel.getAddProduct3(
            isEdit,
            productId = productDetails?.id ?: 0,
            context = this,
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
            auctionClosingTime = binding.tvDateAuction.text.toString(),
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
            accountDetails = accountDetails,
            totalAmount = totalPrice,
            pointsNumber = pointsNumber,
            selectedPaymentType = selectedPaymentType
        )
    }

    private lateinit var allCardsLayoutBinding: AllCardsLayoutBinding
    private lateinit var allCardsBottomSheetDialog: BottomSheetDialog

    private fun allCardsBottomSheetDialog() {
        allCardsLayoutBinding = AllCardsLayoutBinding.inflate(layoutInflater)
        allCardsBottomSheetDialog = BottomSheetDialog(this)
        allCardsBottomSheetDialog.setContentView(allCardsLayoutBinding.root)

        if (adapterList.itemCount >0){
            allCardsLayoutBinding.recyclerViewAllCards.show()
            allCardsLayoutBinding.descText.hide()
            allCardsLayoutBinding.recyclerViewAllCards.apply {
                layoutManager = LinearLayoutManager(this@ConfirmationAddProductActivity)
                adapter = adapterList
            }
        }else{
            allCardsLayoutBinding.recyclerViewAllCards.hide()
            allCardsLayoutBinding.descText.show()
        }


        // Flag to check if Done was clicked
        var isDoneButtonClicked = false

        allCardsLayoutBinding.apply {
            buttonAddNew.setOnClickListener { addCardBottomSheetDialog() }

            buttonDone.setOnClickListener {
                val cvv = accountDetails?.cvv

                if (accountDetails == null) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_choose_a_card),
                        context = this@ConfirmationAddProductActivity
                    )
                    return@setOnClickListener
                } else if (cvv.toString().length !in 3..4) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_enter_cvv),
                        context = this@ConfirmationAddProductActivity
                    )
                    return@setOnClickListener
                }

                isDoneButtonClicked = true // Mark as completed
                allCardsBottomSheetDialog.dismiss()

                when (selectedPaymentType) {
                    PaymentAccountType.VisaMasterCard -> {
                        _binding.selectedVisaCreditCard.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedVisaCreditCard.apply {
                            textCardHoldersName.text = accountDetails?.bankHolderName
                            textCardNumber.text = accountDetails?.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails?.expiaryDate
                        }
                    }

                    PaymentAccountType.Mada -> {
                        _binding.selectedMada.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedMada.apply {
                            textCardHoldersName.text = accountDetails?.bankHolderName
                            textCardNumber.text = accountDetails?.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails?.expiaryDate
                        }
                    }
                }
            }
        }

        addProductViewModel.isLoadingBackAccountList.observe(this) {
            if (it) {
                allCardsLayoutBinding.progressBarAllCards.show()
            } else {
                allCardsLayoutBinding.progressBarAllCards.hide()
            }
        }

        // Set background to transparent
        allCardsBottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 🔥 Set a dismiss listener to run command if nothing was selected
        allCardsBottomSheetDialog.setOnDismissListener {
            if (!isDoneButtonClicked && (accountDetails?.cvv ?: 0) == 0) {
                handleBottomSheetDismissedWithoutAction()
            }
        }

        // Show the dialog
        allCardsBottomSheetDialog.show()
    }

    private fun newAllCardsBottomSheetDialog(list: ArrayList<AccountDetails>?) {
        allCardsLayoutBinding = AllCardsLayoutBinding.inflate(layoutInflater)
        allCardsBottomSheetDialog = BottomSheetDialog(this)
        allCardsBottomSheetDialog.setContentView(allCardsLayoutBinding.root)

        if (list?.size!! >0){
            allCardsLayoutBinding.recyclerViewAllCards.show()
            allCardsLayoutBinding.descText.hide()
            allCardsLayoutBinding.recyclerViewAllCards.apply {
                layoutManager = LinearLayoutManager(this@ConfirmationAddProductActivity)
                adapter = adapterList
            }
        }else{
            allCardsLayoutBinding.recyclerViewAllCards.hide()
            allCardsLayoutBinding.descText.show()
        }


        // Flag to check if Done was clicked
        var isDoneButtonClicked = false

        allCardsLayoutBinding.apply {
            buttonAddNew.setOnClickListener { addCardBottomSheetDialog() }

            buttonDone.setOnClickListener {
                val cvv = accountDetails?.cvv

                if (accountDetails == null) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_choose_a_card),
                        context = this@ConfirmationAddProductActivity
                    )
                    return@setOnClickListener
                } else if (cvv.toString().length !in 3..4) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_enter_cvv),
                        context = this@ConfirmationAddProductActivity
                    )
                    return@setOnClickListener
                }

                isDoneButtonClicked = true // Mark as completed
                allCardsBottomSheetDialog.dismiss()

                when (selectedPaymentType) {
                    PaymentAccountType.VisaMasterCard -> {
                        _binding.selectedVisaCreditCard.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedVisaCreditCard.apply {
                            textCardHoldersName.text = accountDetails?.bankHolderName
                            textCardNumber.text = accountDetails?.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails?.expiaryDate
                        }
                    }

                    PaymentAccountType.Mada -> {
                        _binding.selectedMada.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedMada.apply {
                            textCardHoldersName.text = accountDetails?.bankHolderName
                            textCardNumber.text = accountDetails?.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails?.expiaryDate
                        }
                    }
                }
            }
        }

        addProductViewModel.isLoadingBackAccountList.observe(this) {
            if (it) {
                allCardsLayoutBinding.progressBarAllCards.show()
            } else {
                allCardsLayoutBinding.progressBarAllCards.hide()
            }
        }

        // Set background to transparent
        allCardsBottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 🔥 Set a dismiss listener to run command if nothing was selected
        allCardsBottomSheetDialog.setOnDismissListener {
            if (!isDoneButtonClicked && (accountDetails?.cvv ?: 0) == 0) {
                handleBottomSheetDismissedWithoutAction()
            }
        }

        // Show the dialog
        allCardsBottomSheetDialog.show()
    }


    // Your custom logic here
    private fun handleBottomSheetDismissedWithoutAction() {
        // Do whatever you need — show a toast, log, open another dialog, etc.
        val isVisaEnabled = binding.switchVisaCreditCard.isChecked
        val isMadaEnabled = binding.switchMadaPayment.isChecked
        when (selectedPaymentType) {
            PaymentAccountType.VisaMasterCard -> if (isVisaEnabled) binding.switchVisaCreditCard.isChecked =
                false

            PaymentAccountType.Mada -> if (isMadaEnabled) binding.switchMadaPayment.isChecked =
                false

            else -> {}
        }
    }

    private lateinit var addCardBinding: AddCardBinding
    private lateinit var addCardBottomSheetDialog: BottomSheetDialog

    private fun addCardBottomSheetDialog() {
        // Inflate the layout using ViewBinding
        addCardBinding = AddCardBinding.inflate(layoutInflater)

        // Initialize the BottomSheetDialog and set the view using binding.root
        addCardBottomSheetDialog = BottomSheetDialog(this)
        addCardBottomSheetDialog.setContentView(addCardBinding.root)

        addCardBinding.cardExpiryTv.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return

                val input = s.toString().replace("/", "")
                if (input == current || input.length > 6) return

                val formatted = buildString {
                    for (i in input.indices) {
                        append(input[i])
                        if (i == 1 && input.length > 2) append("/")
                    }
                }

                current = input
                addCardBinding.cardExpiryTv.removeTextChangedListener(this)
                addCardBinding.cardExpiryTv.setText(formatted)
                addCardBinding.cardExpiryTv.setSelection(formatted.length)
                addCardBinding.cardExpiryTv.addTextChangedListener(this)
            }
        })


        // Handle button click event using ViewBinding
        addCardBinding.buttonAdd.setOnClickListener {
            val isSaveLater = addCardBinding.switchSaveLater.isChecked

            // Check and add bank account
            checkAddCard(
                bottomSheetDialog = addCardBottomSheetDialog,
                binding = addCardBinding,
                isSaveLater = isSaveLater
            )

        }

        addCardBinding.buttonCancel.setOnClickListener {
            addCardBottomSheetDialog.dismiss()
        }

        // Set background to transparent
        addCardBottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Show the dialog
        addCardBottomSheetDialog.show()
    }

    private fun checkAddCard(
        bottomSheetDialog: BottomSheetDialog,
        binding: AddCardBinding,
        isSaveLater: Boolean
    ) {
        var readyToAdd = true

        val holderName = binding.cardHolderTv.text.toString().trim()
        val cardNumber = binding.CardnoTv.text.toString().trim()
        val expiryText = binding.cardExpiryTv.text.toString().trim()
        val cvv = binding.cvvTv.text.toString().trim()

        if (holderName.isEmpty()) {
            readyToAdd = false
            binding.cardHolderTv.error =
                "${getString(R.string.enter)} ${getString(R.string.account_holder_s_name)}"
        }

        if (cardNumber.isEmpty()) {
            readyToAdd = false
            binding.CardnoTv.error =
                "${getString(R.string.enter)} ${getString(R.string.Cardno)}"
        } else if (cardNumber.filter { it.isDigit() }.length < 16) {
            readyToAdd = false
            binding.CardnoTv.error = getString(R.string.card_number_must_be_16_digits)
        }

        if (expiryText.isEmpty()) {
            readyToAdd = false
            binding.cardExpiryTv.error =
                "${getString(R.string.enter)} ${getString(R.string.ExpiryDate)}"
        } else {
            // Validate format: MM/YYYY
            val regex = Regex("""^(0[1-9]|1[0-2])/(\d{4})$""")
            if (!regex.matches(expiryText)) {
                readyToAdd = false
                binding.cardExpiryTv.error =
                    getString(R.string.invalid_date_format) // add this string resource
            } else {
                // Check if expiry date is in the future
                val (monthStr, yearStr) = expiryText.split("/")
                val enteredMonth = monthStr.toInt()
                val enteredYear = yearStr.toInt()

                val current = Calendar.getInstance()
                val currentYear = current.get(Calendar.YEAR)
                val currentMonth = current.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-based

                if (enteredYear < currentYear || (enteredYear == currentYear && enteredMonth < currentMonth)) {
                    readyToAdd = false
                    binding.cardExpiryTv.error =
                        getString(R.string.expiry_date_passed) // add this string resource
                }
            }
        }

        if (cvv.isEmpty()||cvv.length<3) {
            readyToAdd = false
            binding.cvvTv.error = getString(R.string.please_enter_cvv)
        }

        if (readyToAdd) {
            if (isSaveLater) {
                addProductViewModel.addBackAccountData(
                    bankHolderName = holderName,
                    accountNumber = cardNumber,
                    expiryDate = expiryText,
                    saveForLaterUse = true,
                    paymentAccountType = selectedPaymentType.value.toString()
                )
            }

            accountDetails = AccountDetails(
                id = 1,
                bankAccountId = 1,
                isSelected = true,
                bankHolderName = holderName,
                accountNumber = cardNumber,
                expiaryDate = expiryText,
                cvv = cvv.toInt(),
                paymentAccountType = selectedPaymentType
            )

            allCardsLayoutBinding.apply {
                when (selectedPaymentType) {
                    PaymentAccountType.VisaMasterCard -> {
                        _binding.selectedVisaCreditCard.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedVisaCreditCard.apply {
                            textCardHoldersName.text = accountDetails!!.bankHolderName
                            textCardNumber.text =
                                accountDetails!!.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails!!.expiaryDate
                        }
                    }

                    PaymentAccountType.Mada -> {
                        _binding.selectedMada.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedMada.apply {
                            textCardHoldersName.text = accountDetails!!.bankHolderName
                            textCardNumber.text =
                                accountDetails!!.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails!!.expiaryDate
                        }
                    }
                }

            }

            bottomSheetDialog.dismiss()
            allCardsBottomSheetDialog.dismiss()
        }
    }

    private fun setupAdapter() {
        adapterList = object : GenericListAdapter<AccountDetails>(
            R.layout.item_card,
            bind = { element, holder, itemCount, position ->
                val itemBinding = ItemCardBinding.bind(holder.itemView)

                holder.view.run {
                    element.run {
                        itemBinding.textCardHoldersName.text = bankHolderName
                        itemBinding.textCardNumber.text = accountNumber.formatAsCardNumber()
                        itemBinding.textExpiryDate.text = expiaryDate
                        itemBinding.radioButtonCard.isSelected = isSelected
                        itemBinding.radioButtonCard.isChecked = isSelected

                        itemBinding.editTextCvv.setText(
                            if (cvv != 0) cvv.toString() else ""
                        )

                        // Enable/disable based on selection
                        itemBinding.editTextCvv.isEnabled = element.isSelected
                        itemBinding.editTextCvv.isFocusable = element.isSelected
                        itemBinding.editTextCvv.isFocusableInTouchMode = element.isSelected
                    }

                    itemBinding.editTextCvv.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            val newCvv = s?.toString()?.toIntOrNull()
                            if (newCvv != null && newCvv.toString().length <= 3) {
                                element.cvv = newCvv
                            }
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                        }
                    })

                    itemBinding.radioButtonCard.setOnClickListener {
                        val enteredCvv = itemBinding.editTextCvv.text.toString().toIntOrNull()
                        if (enteredCvv != null) {
                            element.cvv = enteredCvv
                        }

                        val previousSelectedPosition =
                            selectedAccountDetails.indexOfFirst { it.isSelected }
                        selectedAccountDetails.forEach { it.isSelected = false }
                        element.isSelected = true
                        accountDetails = element

                        if (previousSelectedPosition != -1) {
                            adapterList.notifyItemChanged(previousSelectedPosition)
                        }
                        adapterList.notifyItemChanged(position)
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun addAllCardsAdaptor(list: ArrayList<AccountDetails>) {
        selectedAccountDetails = list
        adapterList.updateAdapter(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        shippingOptionText = null
        pickup = null
        addProductViewModel.closeAllCall()
        addProductViewModel.baseCancel()
    }

}