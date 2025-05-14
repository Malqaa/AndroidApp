package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity8

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityPromotionalBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.pakatResp.PakatDetails
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.PromotionModel
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.confirmationAddProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.utils.toStyledSpannable

class PromotionalActivity : BaseActivity<ActivityPromotionalBinding>(),
    PackagesAdapter.SetOnPackageSelected {

    val list: ArrayList<PromotionModel> = ArrayList()
    var isEdit: Boolean = false
    lateinit var packagesAdapter: PackagesAdapter
    var pakatList: ArrayList<PakatDetails> = ArrayList()
    private lateinit var addProductViewModel: AddProductViewModel
    var closed = false
    override fun onBackPressed() {
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                putExtra("whereCome", "Add")
                putExtra("closed", closed)
            })
            finish()
        } else {
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityPromotionalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarPromotional.toolbarTitle.text = getString(R.string.distinguish_your_product)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        closed = intent.getBooleanExtra("closed", false)

        setClickViewListeners()
        setPromotionalAdaptor2()
        setUpViewModel()

        setFreePackageData()
        addProductViewModel.getPakatList(AddProductObjectData.selectedCategoryId)
    }

    private fun setFreePackageData() {
        binding.apply {
            containerExtraProductImageCount.hide()
            containerExtraProductVideoCount.hide()
            containerExtraPakage.hide()
        }
        AddProductObjectData.selectedCategory?.let { category ->
            val imageCount = AddProductObjectData.images?.size ?: 0
            val videoCount = AddProductObjectData.videoList?.size ?: 0

            val extraImages = maxOf(imageCount - category.freeProductImagesCount, b = 0)
            val extraVideos = maxOf(videoCount - category.freeProductVidoesCount, b = 0)

            binding.apply {
                if (extraImages > 0) {
                    btnExtraProductImageCount.text = extraImages.toString()
                    containerExtraProductImageCount.show()
                }

                if (extraVideos > 0) {
                    btnExtraProductVideoCount.text = extraVideos.toString()
                    containerExtraProductVideoCount.show()
                }

                if (extraImages > 0 || extraVideos > 0) {
                    val totalFee = (category.extraProductImageFee * extraImages) +
                            (category.extraProductVidoeFee * extraVideos)
                    val currency = getString(R.string.Rayal)
                    val feeText = "$totalFee $currency"

                    binding.tvFreePakagePrice.text = feeText.toStyledSpannable(
                        highlightPart = totalFee.toString(),
                        sizeInSp = 20
                    )
                    containerExtraPakage.show()
                }
            }
        }
    }

    private fun setUpViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        addProductViewModel.isLoading.observe(this) {
            if (it)
                binding.progressBar.show()
            else
                binding.progressBar.hide()
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
                                val countImagePackage = item.countImage
                                val countVideoPackage = item.countVideo
                                updateExtraPackageUI(
                                    countImagePackage = countImagePackage,
                                    countVideoPackage = countVideoPackage
                                )
                                break
                            }
                        }
                    }
                    packagesAdapter.notifyDataSetChanged()
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.noPackagesFound), this)
                }
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.noPackagesFound), this)
            }
        }
    }

    private fun setPromotionalAdaptor2() {
        packagesAdapter = PackagesAdapter(pakatList, this)
        binding.rvPakat.apply {
            adapter = packagesAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    override fun onSelectPackage(position: Int) {
        val selectedItem = pakatList[position]

        if (selectedItem.isSelected) {
            // If it's already selected, unselect it
            selectedItem.isSelected = false
            AddProductObjectData.selectedPakat = null
            updateExtraPackageUI(countImagePackage = 0, countVideoPackage = 0)
        } else {
            // Unselect all, then select this one
            pakatList.forEach { it.isSelected = false }
            selectedItem.isSelected = true
            AddProductObjectData.selectedPakat = selectedItem
            updateExtraPackageUI(
                countImagePackage = selectedItem.countImage,
                countVideoPackage = selectedItem.countVideo
            )
        }

        packagesAdapter.notifyDataSetChanged()
    }


    private fun updateExtraPackageUI(countImagePackage: Int, countVideoPackage: Int) {
        AddProductObjectData.selectedCategory?.let { category ->
            val imageCount = AddProductObjectData.images?.size ?: 0
            val videoCount = AddProductObjectData.videoList?.size ?: 0

            val currentImageCount = maxOf(imageCount - category.freeProductImagesCount, b = 0)
            val currentVideoCount = maxOf(videoCount - category.freeProductVidoesCount, b = 0)

            val extraImages = maxOf(currentImageCount - countImagePackage, b = 0)
            val extraVideos = maxOf(currentVideoCount - countVideoPackage, b = 0)

            if (extraImages > 0) {
                binding.btnExtraProductImageCount.text = extraImages.toString()
                binding.containerExtraProductImageCount.show()
            } else {
                binding.containerExtraProductImageCount.hide()
            }

            if (extraVideos > 0) {
                binding.btnExtraProductVideoCount.text = extraVideos.toString()
                binding.containerExtraProductVideoCount.show()
            } else {
                binding.containerExtraProductVideoCount.hide()
            }

            if (extraImages > 0 || extraVideos > 0) {
                binding.containerExtraPakage.show()

                val extraProductImageFee =
                    extraImages * AddProductObjectData.selectedCategory?.extraProductImageFee!!
                val extraProductVideoFree =
                    extraVideos * AddProductObjectData.selectedCategory?.extraProductVidoeFee!!

                val totalFee = extraProductImageFee + extraProductVideoFree
                val currency = getString(R.string.Rayal)
                val feeText = "$totalFee $currency"
                binding.tvFreePakagePrice.text = feeText.toStyledSpannable(
                    highlightPart = totalFee.toString(),
                    sizeInSp = 20
                )
            } else {
                binding.containerExtraPakage.hide()
            }

        }
    }

    private fun setClickViewListeners() {
        binding.toolbarPromotional.backBtn.setOnClickListener { onBackPressed() }
        binding.button16611.setOnClickListener { confirmPromotion() }
    }

    private fun goNextActivity() {
        startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
            putExtra("whereCome", "Add")
            putExtra("closed", closed)
        })
    }

    private fun validatePromotion() = pakatList.filter { it.isSelected }.isNotEmpty()

    private fun confirmPromotion() {
        if (pakatList.isNotEmpty() && !validatePromotion() && (binding.containerExtraPakage.visibility != View.VISIBLE)) {
            AddProductObjectData.selectedPakat = null
            goNextActivity()
        } else {
            if (isEdit) {
                startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                    putExtra("whereCome", "Add")
                    putExtra("closed", closed)
                    finish()
                })
            } else {
                goNextActivity()

            }
        }
    }
}
