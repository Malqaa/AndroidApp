package com.malqaa.androidappp.newPhase.presentation.activities.splashActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivitySplashBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    var productId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI(binding.mainContainer)

        lifecycleScope.launch(Dispatchers.IO) {
            /**get Product id from share product link*/
            val mainIntent = intent
            if (mainIntent != null && mainIntent.data != null) {
                if (mainIntent.data?.scheme == "http") {
                    val data = mainIntent.data
                    if (data?.query != null) {
                        productId = getLinkFromLocalPassedData(data)
                    }
                }
            }
            delay(5300)
            val intentt = Intent(this@SplashActivity, MainActivity::class.java).apply {
                putExtra("productId", productId)
            }
            startActivity(intentt)
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun getLinkFromLocalPassedData(data: Uri): Int {
        val linkQuery: String = data.query.toString()
        return try {
            productId =
                linkQuery.substring(linkQuery.indexOf('=') + 1, linkQuery.length)
                    .toInt()
            productId
        } catch (e: Exception) {
            productId
        }
    }

}

