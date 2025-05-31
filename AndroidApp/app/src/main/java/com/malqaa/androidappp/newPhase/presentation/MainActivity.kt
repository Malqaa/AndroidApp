package com.malqaa.androidappp.newPhase.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityBottmmmBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity1.ProductsTagsForAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions

class MainActivity : BaseActivity<ActivityBottmmmBinding>() {

    var numBadge = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize view binding
        binding = ActivityBottmmmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setupWithNavController(findNavController(R.id.nav_host_fragment))

        binding.navView.setOnItemSelectedListener { item ->
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
            return@setOnItemSelectedListener true

        }


        binding.floatingActionButtonBottm.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                startActivity(Intent(this, ProductsTagsForAddProductActivity::class.java))
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
        //============
        val isMyOrder = intent.getBooleanExtra(ConstantObjects.isMyOrder, false)

        if (isMyOrder) {
            binding.navView!!.selectedItemId = R.id.navigation_account
            myOrderTrigger = true
        }

        val productId = intent.getIntExtra("productId", 0)
        if (productId != 0) {
            startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productId)

            })
        }
        numBadge.observe(this, Observer {
            binding.navView.getOrCreateBadge(R.id.navigation_notifications).apply {
                isVisible = it != 0
                number = it
                backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.bg)
            }
        })

    }


    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Get the FCM token
                val token = task.result

//                    SharedPreferencesStaticClass.saveFcmToken(token)
                Log.d("FCM Token", "Token: $token")

                // Now you have the FCM token, you can use it as needed
            } else {
                Log.w("FCM Token", "Fetching FCM registration token failed", task.exception)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("FCM Token", "Token: ")
    }

    companion object {
        var myOrderTrigger = false
        var myBidTrigger = false
    }

}
