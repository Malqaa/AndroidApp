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
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show

class PromotionalActivity : BaseActivity<ActivityPromotionalBinding>(),
    PakatAdapter.SetOnPakatSelected {

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

        // Initialize view binding
        binding = ActivityPromotionalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarPromotional.toolbarTitle.text = getString(R.string.distinguish_your_product)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)

        setClickViewListenrs()
        setPromotionalAdaptor2()
        setUpViewModel()

        setFreePakageData()
        addProductViewModel.getPakatList(AddProductObjectData.selectedCategoryId)
    }

    private fun setFreePakageData() {
        binding.containerExtraProductImageCount.hide()
        binding.containerExtraProductVideoCount.hide()
        binding.containerExtraPakage.hide()
        AddProductObjectData.selectedCategory?.let { selectedCategory ->
            var extraImages = 0
            AddProductObjectData.images?.let {
                if (it.size > selectedCategory.freeProductImagesCount) {
                    extraImages =
                        it.size - selectedCategory.freeProductImagesCount
                    binding.btnExtraProductImageCount.text = extraImages.toString()
                    binding.containerExtraProductImageCount.show()
                } else {
                    binding.containerExtraProductImageCount.hide()
                }
            }

            var extraVideos = 0
            AddProductObjectData.videoList?.let {
                if (it.size > selectedCategory.freeProductVidoesCount) {
                    extraVideos = it.size - selectedCategory.freeProductVidoesCount
                    binding.btnExtraProductVideoCount.text = extraVideos.toString()
                    binding.containerExtraProductVideoCount.show()
                } else {
                    binding.containerExtraProductVideoCount.hide()
                }
            }


            if (extraVideos != 0 || extraImages != 0) {
                binding.containerExtraPakage.show()
                val totalFee =
                    (selectedCategory.extraProductVidoeFee * extraVideos) + (selectedCategory.extraProductImageFee * extraImages)
                binding.tvFreePakagePrice.text = "${totalFee} ${getString(R.string.Rayal)}"
            } else {
                binding.containerExtraPakage.hide()
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
        binding.rvPakat.apply {
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
        binding.toolbarPromotional.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.button16611.setOnClickListener {
            confirmpromotion()
        }
        binding.noThankYou.setOnClickListener {
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
        if (pakatList.isNotEmpty() && !validatepromotion() && (binding.containerExtraPakage.visibility != View.VISIBLE)) {
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
