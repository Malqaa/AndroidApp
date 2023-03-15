package com.malka.androidappp.fragments.create_product.imageslider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.squareup.picasso.Picasso


class ViewPagerAdapter(private val context: Context, val imagelist: List<String>) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private var images: List<String> = imagelist

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater!!.inflate(R.layout.carspecs_imageviewpager, null)
        if (images != null && position < images.count()) {
            val imageView = view.findViewById<View>(R.id.imageView) as ImageView
            Picasso.get()
                .load(Constants.IMAGE_URL + images[position])
                .into(imageView);
            val vp = container as ViewPager
            vp.addView(view, 0)
        }
        return view
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

}
