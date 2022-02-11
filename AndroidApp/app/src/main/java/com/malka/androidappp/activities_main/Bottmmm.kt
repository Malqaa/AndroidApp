package com.malka.androidappp.activities_main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.helper.HelpFunctions
import kotlinx.android.synthetic.main.activity_bottmmm.*


class Bottmmm : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottmmm)
        //supportActionBar?.setTitle("Home")
        supportActionBar?.hide()

        ///////////////////////////////////////////////////////////////////////////////////
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        //////////////////////////////////////////////////////////////////////////////////
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_notifications,
                R.id.navigation_account,
                R.id.navigation_watchlist,
                R.id.list_an_item
            )
        )
        navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_notifications -> {
                    if (!HelpFunctions.IsUserLoggedIn()) {
                        val intentt = Intent(this, SignInActivity::class.java)
                        startActivity(intentt)
                        finish()

                        return@setOnNavigationItemSelectedListener false
                    } else {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_notifications)
                    }
                }
                R.id.navigation_account -> {
                    /*if (!HelpFunctions.IsUserLoggedIn()) {
                        HelpFunctions.ShowAlert(this@Bottmmm, "Information", "Please Log In");
                        return@setOnNavigationItemSelectedListener false
                    } else {*/
                    findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_account)
                    //}
                }
                R.id.navigation_watchlist -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_watchlist)
//                    if (!HelpFunctions.IsUserLoggedIn()) {
//                        val intentt = Intent(this, SignInActivity::class.java)
//                        startActivity(intentt)
//                        finish()
//
//                        return@setOnNavigationItemSelectedListener false
//                    } else {
//                        findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_watchlist)
//                    }
                }
                R.id.navigation_home -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_home)
                    //Zack
                    //Date: 04/08/2021
//                    val args = Bundle()
//                    val FileName : String = "Car-en-US.js";
//                    val Title : String = "Title Here";
//                    args.putString("FileName", FileName);
//                    args.putString("Title", Title);
//                    findNavController(R.id.nav_host_fragment).navigate(R.id.fragment_dynamic_template, args)
                }
            }
            return@setOnNavigationItemSelectedListener false
        }

        floatingActionButtonBottm.setOnClickListener() {
            if (HelpFunctions.IsUserLoggedIn()) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.list_an_item)
            } else {
//                HelpFunctions.ShowAlert(this@Bottmmm, "Information", "Please Log In");
                val intentt = Intent(this, SignInActivity::class.java)
                startActivity(intentt)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        navView.visibility = View.VISIBLE
        super.onBackPressed()
//        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.nav, menu)
        return true
    }
}

