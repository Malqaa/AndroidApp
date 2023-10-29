package com.malka.androidappp.newPhase.presentation.addProduct

import android.content.Intent
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.presentation.MainActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import kotlinx.android.synthetic.main.fragment_continue.*


class SuccessProductActivity : BaseActivity() {

    var AdvId: Int = 0
    var template: String = ""
    var comeFrom =""

    override fun onBackPressed() {
        back_to_main.performClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_continue)

        AdvId = intent?.getIntExtra(ConstantObjects.productIdKey,0)?:0
        comeFrom= intent?.getStringExtra("comeFrom")?:""

        println("hhhh product id " + AdvId)
        //template = intent?.getStringExtra("Template").toString()

        if(comeFrom == "RatingShipping"){
            txtTitleSuccess.text=getString(R.string.doneRate)
            btnProduct.text=getString(R.string.back_to_main)
            back_to_main.hide()
        }else{
            back_to_main.show()
            btnProduct.text=getString(R.string.view_the_product)
            txtTitleSuccess.text=getString(R.string.your_product_has_been_successfully_added)

        }

        btnProduct.setOnClickListener() {
            if(comeFrom == "RatingShipping"){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
                    putExtra(ConstantObjects.productIdKey, AdvId)
                    putExtra(ConstantObjects.isSuccess, true)
                })
                finish()
            }

        }
        back_to_main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {})
            finish()
        }

    }


}