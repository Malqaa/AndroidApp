package com.malka.androidappp.recycler_browsecat
import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.malka.androidappp.R
import com.malka.androidappp.network.constants.ApiConstants
import com.squareup.picasso.Picasso


class ImageAdapterImageArray(context: Context, imageArray: MutableList<String>) : PagerAdapter() {
    var arrayList: MutableList<String> = mutableListOf()
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
        if (arrayList[position] != null && arrayList[position].trim().length > 0)
            Picasso.get()
                .load(ApiConstants.IMAGE_URL + arrayList[position])
                .into(imageView) else imageView.setImageResource(R.drawable.cam)

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