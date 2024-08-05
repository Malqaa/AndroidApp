package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel

class ImageViewLargeActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var layoutCancel: ConstraintLayout
    private lateinit var imageCancel: ImageView
    private lateinit var mBtnPrevious: ImageButton
    private lateinit var mBtnNext: ImageButton
    private lateinit var mAdapter: ImagePagerAdapter
    private var mCurrentPosition = 0
    private var UrlImg: String = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slider_display)

        val receivedList: ArrayList<ImageSelectModel> =
            intent.getParcelableArrayListExtra("imgList") ?: ArrayList()

        UrlImg = (intent.getStringExtra("UrlImg") ?: "")
        receivedList.add(
            0, ImageSelectModel(
                null,
                "",
                false,
                UrlImg,
                false,
                0,
                1,
                false
            )
        )

        val distinctItems = receivedList.distinctBy { it.url }

        imageCancel = findViewById(R.id.imageCancel)
        mViewPager = findViewById(R.id.viewPager)
        mBtnPrevious = findViewById(R.id.btnPrevious)
        layoutCancel = findViewById(R.id.layoutImg)
        mBtnNext = findViewById(R.id.btnNext)

        mAdapter = ImagePagerAdapter(this, distinctItems)
        mViewPager.adapter = mAdapter
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // Not used
            }

            override fun onPageSelected(position: Int) {
                // Release the ExoPlayer when the page is changed
                mAdapter.releaseStop()
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not used
            }
        })

        imageCancel.setOnClickListener {
            setFinishOnTouchOutside(true)
            finish()
        }

        layoutCancel.setOnClickListener {
            setFinishOnTouchOutside(true)
            finish()
        }

        mBtnPrevious.setOnClickListener {
            if (mCurrentPosition > 0) {
                mCurrentPosition--
                mViewPager.currentItem = mCurrentPosition
            }
        }

        mBtnNext.setOnClickListener {
            if (mCurrentPosition < mAdapter.count - 1) {
                mCurrentPosition++
                mViewPager.currentItem = mCurrentPosition
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onPause() {
        super.onPause()
        mAdapter.releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.releasePlayer()
    }
}
