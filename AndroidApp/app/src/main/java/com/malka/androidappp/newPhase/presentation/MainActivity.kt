package com.malka.androidappp.newPhase.presentation

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.addProduct.activity1.ProductsTagsForAddProductActivity
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import kotlinx.android.synthetic.main.activity_bottmmm.*


class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottmmm)
        //====nav control
        nav_view.setupWithNavController(findNavController(R.id.nav_host_fragment))
        nav_view.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_notifications -> {
                    if (!HelpFunctions.isUserLoggedIn()) {
                      startActivity(Intent(this, SignInActivity::class.java))
                    } else {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_notifications)
                    }
                }
                R.id.navigation_account -> {

                    if (!HelpFunctions.isUserLoggedIn()) {
                        startActivity(Intent(this, SignInActivity::class.java))
                    } else {
                        myOrderTrigger = false
                        myBidTrigger = false
                        findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_account)
                    }
                }
                R.id.navigation_watchlist -> {
                    if (!HelpFunctions.isUserLoggedIn()) {
                        startActivity(Intent(this, SignInActivity::class.java))
                    } else {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_watchlist)
                    }
                }
                R.id.navigation_home -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_home)
                }

            }
            return@setOnItemSelectedListener true;

        }
        floatingActionButtonBottm.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                startActivity(Intent(this, ProductsTagsForAddProductActivity::class.java))
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
        nav_view_ = nav_view
        //============
//        val isBid = intent.getBooleanExtra(ConstantObjects.isBid, false)
        val isMyOrder = intent.getBooleanExtra(ConstantObjects.isMyOrder, false)
//        if (isBid) {
//            nav_view_!!.selectedItemId = R.id.navigation_account
//            myBidTrigger = true
//        }
        if (isMyOrder) {
            nav_view_!!.selectedItemId = R.id.navigation_account
            myOrderTrigger = true
        }

        //notification numberBadge
//        nav_view.getOrCreateBadge(R.id.navigation_notifications).apply {
//            isVisible = true
//            number = 99
//            backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.bg)
//        }
        var productId=intent.getIntExtra("productId",0)
        if(productId!=0){
            startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productId)

            })
        }

    }

    companion object {
        var nav_view_: BottomNavigationView? = null
        var myOrderTrigger = false
        var myBidTrigger = false
    }

//    val loginLuncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                checkPriceLayout()
//            }
//        }

}

