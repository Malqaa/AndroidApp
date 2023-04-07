package com.malka.androidappp.newPhase.presentation.addSellerReviewActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import kotlinx.android.synthetic.main.toolbar_main.*

class AddRateSellerActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rate_seller)
        toolbar_title.text = getString(R.string.add_Review)
    }
}