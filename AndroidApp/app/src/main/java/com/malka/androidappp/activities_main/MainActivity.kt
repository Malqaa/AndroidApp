package com.malka.androidappp.activities_main

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.add_product.ListanItem
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_bottmmm.*


class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottmmm)
        nav_view.setupWithNavController(findNavController(R.id.nav_host_fragment))
        nav_view.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_notifications -> {
                    if (!HelpFunctions.IsUserLoggedIn()) {
                        startActivity(Intent(this, SignInActivity::class.java))
                    } else {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_notifications)
                    }
                }
                R.id.navigation_account -> {

                    if (!HelpFunctions.IsUserLoggedIn()) {
                        startActivity(Intent(this, SignInActivity::class.java))
                    } else {
                        myOrderTrigger = false
                        myBidTrigger = false
                        findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_account)
                    }
                }
                R.id.navigation_watchlist -> {
                    if (!HelpFunctions.IsUserLoggedIn()) {
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
        nav_view.getOrCreateBadge(R.id.navigation_notifications).apply {
            isVisible = true
            number = 99
            backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.bg)
        }


        floatingActionButtonBottm.setOnClickListener {
            if (HelpFunctions.IsUserLoggedIn()) {
                startActivity(Intent(this, ListanItem::class.java))
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
        nav_view_ = nav_view
        val isBid = intent.getBooleanExtra(ConstantObjects.isBid, false)
        val isMyOrder = intent.getBooleanExtra(ConstantObjects.isMyOrder, false)
        if (isBid) {
            nav_view_!!.selectedItemId = R.id.navigation_account
            myBidTrigger = true
        } else if (isMyOrder) {
            nav_view_!!.selectedItemId = R.id.navigation_account
            myOrderTrigger = true
        }
    }

    companion object {
        var nav_view_: BottomNavigationView? = null
        var myOrderTrigger = false
        var myBidTrigger = false
    }

}

