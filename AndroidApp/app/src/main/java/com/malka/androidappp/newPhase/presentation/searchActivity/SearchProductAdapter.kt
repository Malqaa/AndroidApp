package com.malka.androidappp.newPhase.presentation.searchActivity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.helper.Extension.decimalNumberFormat
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.product_item.view.*

class SearchProductAdapter(
    var allProducts: List<Product>,
    var context: Context,
    var listener: SearchProductAdapter.OnItemClickListener
) : RecyclerView.Adapter<BaseViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.product_item, parent, false)
        return BaseViewHolder(view)

    }

    override fun getItemCount() = allProducts.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.view.run {
            allProducts[position].run {
                ivFav.setImageResource(R.drawable.star)
                ivFav.setOnClickListener {
                        listener.addToWishList(position)
                }
//                if(Paper.book().read<Boolean>(SharedPreferencesStaticClass.islogin) == true){
//                    if (HelpFunctions.AdAlreadyAddedToWatchList(id.toString())) {
//                        is_watch_iv.setImageResource(R.drawable.starcolor)
//                    } else {
//                        is_watch_iv.setImageResource(R.drawable.star)
//                    }
//                }else{
//                    is_watch_iv.setImageResource(R.drawable.star)
//                }

                if(name!=null)
                   titlenamee.text = name
                else
                    titlenamee.text = ""
                if(regionName!=null)
                   city_tv.text = regionName
                else
                    city_tv.text = ""

                setOnClickListener {
                    SharedPreferencesStaticClass.ad_userid = ""
                    ConstantObjects.is_watch_iv = ivFav
                    context.startActivity(Intent(context, ProductDetailsActivity::class.java).apply {
                        putExtra("AdvId", id)
                        putExtra("Template", "")
                    })
                }
                Extension.loadThumbnail(
                    context,
                    productImage,
                    productimg,
                    loader
                )
                LowestPrice_layout.invisible()
                LowestPrice_layout_2.hide()
                lisView.hide()
                gridview.show()

                tvProductPrice.text = "${price!!.toDouble().decimalNumberFormat()} ${
                    context.getString(
                        R.string.Rayal
                    )
                }"
                purchasing_price_tv_2.text =
                    "${
                        price.toDouble().decimalNumberFormat()
                    } ${context.getString(R.string.Rayal)}"


                if(Lingver.getInstance().getLanguage()== ConstantObjects.ARABIC){
                    containerTimeBar.background= ContextCompat.getDrawable(context, R.drawable.product_attribute_bg1_ar)
                }else{
                    containerTimeBar.background= ContextCompat.getDrawable(context, R.drawable.product_attribute_bg1_en)
                }

                date_tv.text = HelpFunctions.getViewFormatForDateTrack(createdAt)
                setOnClickListener {
                    listener.onSelectItem(position)
                }
            }
        }

    }

    interface OnItemClickListener {
        fun onSelectItem(position: Int)
        fun addToWishList(position: Int)
    }

}