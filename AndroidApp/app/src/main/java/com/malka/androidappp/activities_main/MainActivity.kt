package com.malka.androidappp.activities_main

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.activities_main.add_product.ListanItem
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.helper.HelpFunctions
import kotlinx.android.synthetic.main.activity_bottmmm.*


class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottmmm)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_notifications -> {
                    if (!HelpFunctions.IsUserLoggedIn()) {
                        val intentt = Intent(this, SignInActivity::class.java)
                        startActivity(intentt)
                        return@setOnNavigationItemSelectedListener false
                    } else {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_notifications)
                    }
                }
                R.id.navigation_account -> {

                    if (!HelpFunctions.IsUserLoggedIn()) {
                        val intentt = Intent(this, SignInActivity::class.java)
                        startActivity(intentt)


                        return@setOnNavigationItemSelectedListener false
                    } else {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_account)
                    }
                }
                R.id.navigation_watchlist -> {
                    if (!HelpFunctions.IsUserLoggedIn()) {
                        val intentt = Intent(this, SignInActivity::class.java)
                        startActivity(intentt)

                        return@setOnNavigationItemSelectedListener false
                    } else {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_watchlist)
                    }
                }
                R.id.navigation_home -> {
                   findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_home)
                }
            }
            return@setOnNavigationItemSelectedListener false
        }

        floatingActionButtonBottm.setOnClickListener() {
            if (HelpFunctions.IsUserLoggedIn()) {
                startActivity(Intent(this, ListanItem::class.java))
            } else {
                val intentt = Intent(this, SignInActivity::class.java)
                startActivity(intentt)
            }
        }
    }




}

