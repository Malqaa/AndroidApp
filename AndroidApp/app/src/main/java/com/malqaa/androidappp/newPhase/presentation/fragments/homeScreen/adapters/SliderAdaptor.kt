package com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.domain.models.homeSilderResp.HomeSliderItem


class SliderAdaptor(context: Context, private val sliderList: List<HomeSliderItem>,val details :Boolean) :
    PagerAdapter() {
    private val context: Context
    private var layoutInflater: LayoutInflater? = null

    override fun getCount(): Int {
        return sliderList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        val view = layoutInflater!!.inflate(R.layout.slider_item, null)
        val slider_image: ImageView = view.findViewById(R.id.slider_image)
        val loader: ProgressBar = view.findViewById(R.id.loader)
        if(details){
            if (sliderList[position].type != 2) {
                Extension.loadImgGlide(
                    context,
                    sliderList[position].img,
                    slider_image, loader
                )
            }
        }else{
            slider_image.scaleType = ImageView.ScaleType.FIT_XY;

            Extension.loadImgGlide(
                context,
                sliderList[position].img,
                slider_image, loader
            )
        }


        val vp = container as ViewPager
        vp.addView(view, 0)
        return view!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view: View = `object` as View
        vp.removeView(view)
    }

    init {
        this.context = context
    }
}