package com.malqaa.androidappp.newPhase.presentation.activities.addProduct

import android.content.Intent
import android.os.Bundle
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentContinueBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class SuccessProductActivity : BaseActivity<FragmentContinueBinding>() {

    var AdvId: Int = 0
    var template: String = ""
    var comeFrom = ""

    override fun onBackPressed() {
        binding.backToMain.performClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = FragmentContinueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AdvId = intent?.getIntExtra(ConstantObjects.productIdKey, 0) ?: 0
        comeFrom = intent?.getStringExtra("comeFrom") ?: ""

        if (comeFrom == "RatingShipping") {
            binding.txtTitleSuccess.text = getString(R.string.doneRate)
            binding.btnProduct.text = getString(R.string.back_to_main)
            binding.backToMain.hide()
        } else {
            binding.backToMain.show()
            binding.btnProduct.text = getString(R.string.view_the_product)
            binding.txtTitleSuccess.text =
                getString(R.string.your_product_has_been_successfully_added)
        }

        binding.btnProduct.setOnClickListener() {
            if (comeFrom == "RatingShipping") {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
                    putExtra(ConstantObjects.productIdKey, AdvId)
                    putExtra(ConstantObjects.isSuccess, true)
                })
                finish()
            }

        }
        binding.backToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {})
            finish()
        }
    }
}