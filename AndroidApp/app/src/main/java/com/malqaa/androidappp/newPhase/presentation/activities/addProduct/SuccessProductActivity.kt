package com.malqaa.androidappp.newPhase.presentation.activities.addProduct

import android.content.Intent
import android.os.Bundle
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
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