package com.malka.androidappp.fragments.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.domain.models.servicemodels.Slider


class SliderAdaptor(context: Context, val sliderList :ArrayList<Slider>) : PagerAdapter() {
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
        Extension.loadThumbnail(
            context,
            sliderList.get(position).img,
            slider_image, null
        )
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