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
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity1.ProductsTagsForAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.utils.BadgeUtils
import kotlinx.android.synthetic.main.activity_bottmmm.*


class MainActivity : BaseActivity() {

var numBadge=MutableLiveData<Int>(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottmmm)
        //====nav control
        getFcmToken()
//        val deviceIdHelper = DeviceIdHelper(this)
//        val deviceId = deviceIdHelper.getDeviceId()
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
        //============
//        val isBid = intent.getBooleanExtra(ConstantObjects.isBid, false)
        val isMyOrder = intent.getBooleanExtra(ConstantObjects.isMyOrder, false)
//        if (isBid) {
//            nav_view_!!.selectedItemId = R.id.navigation_account
//            myBidTrigger = true
//        }
        if (isMyOrder) {
            nav_view!!.selectedItemId = R.id.navigation_account
            myOrderTrigger = true
        }

        //notification numberBadge

        val productId=intent.getIntExtra("productId",0)
        if(productId!=0){
            startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productId)

            })
        }
        numBadge.observe(this, Observer {
            nav_view.getOrCreateBadge(R.id.navigation_notifications).apply {
                isVisible = it != 0
                number = it
                backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.bg)
            }
        })

    }


    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get the FCM token
                    val token = task.result

                    SharedPreferencesStaticClass.saveFcmToken(token)
                    Log.d("FCM Token", "Token: $token")

                    // Now you have the FCM token, you can use it as needed
                } else {
                    Log.w("FCM Token", "Fetching FCM registration token failed", task.exception)
                }
            }
    }

//    override fun onResume() {
//        super.onResume()
//        nav_view.selectedItemId=R.id.navigation_home
//    }
    companion object {
//        var nav_view_: BottomNavigationView? = null
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

