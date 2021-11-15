package com.malka.androidappp.recycler_browsecat
import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.malka.androidappp.R


class ImageAdapter(context: Context, imageArray: ArrayList<Int>) : PagerAdapter() {
    var arrayList: ArrayList<Int> = ArrayList()
    private val inflater: LayoutInflater
    private val context: Context
    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout: View = inflater.inflate(R.layout.pager_layout, view, false)!!
        val imageView: ImageView = imageLayout
            .findViewById(R.id.image) as ImageView
        imageView.setImageResource(arrayList[position])
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun restoreState(
        state: Parcelable?,
        loader: ClassLoader?
    ) {
    }

    override fun saveState(): Parcelable? {
        return null
    }

    init {
        arrayList = imageArray
        this.context = context
        inflater = LayoutInflater.from(context)
    }
}