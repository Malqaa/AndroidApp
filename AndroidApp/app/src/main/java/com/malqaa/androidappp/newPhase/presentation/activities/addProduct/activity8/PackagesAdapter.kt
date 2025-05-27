package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity8

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemPakatDesginBinding
import com.malqaa.androidappp.newPhase.domain.models.pakatResp.ItemPacket
import com.malqaa.androidappp.newPhase.domain.models.pakatResp.PakatDetails
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.utils.toStyledSpannable

class PackagesAdapter(
    val pakatList: List<PakatDetails>,
    var setOnPackageSelected: SetOnPackageSelected
) :
    RecyclerView.Adapter<PackagesAdapter.PackageViewHolder>() {

    lateinit var context: Context
    private var itemPackets: ArrayList<ItemPacket> = ArrayList()

    class PackageViewHolder(var viewBinding: ItemPakatDesginBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    lateinit var inflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        context = parent.context
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return PackageViewHolder(
            ItemPakatDesginBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = pakatList.size

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        itemPackets = arrayListOf()

        Glide.with(context)
            .load(pakatList[position].image)
            .into(holder.viewBinding.imagePackage)

        holder.viewBinding.pkgName.text = pakatList[position].name

        val totalFee = pakatList[position].price
        val currency = context.getString(R.string.Rayal)
        val feeText = "$totalFee $currency"

        holder.viewBinding.pkgPrice.text = feeText.toStyledSpannable(
            highlightPart = totalFee.toString(),
            sizeInSp = 20
        )

        if (pakatList[position].common) {
            holder.viewBinding.common.show()
            holder.viewBinding.isSelectimage.setImageResource(R.drawable.ic_check_black)
            holder.viewBinding.itemBg.setBackgroundResource(R.drawable.rounded_top_corners_bg_color)
        } else {
            holder.viewBinding.common.hide()
            holder.viewBinding.isSelectimage.setImageResource(R.drawable.ic_check)
            holder.viewBinding.itemBg.setBackgroundResource(R.drawable.rounded_top_corners_text_color)
        }
        if (pakatList[position].isSelected) {
            holder.viewBinding.bgline.setBackgroundResource(R.drawable.product_attribute_linebg)
            holder.viewBinding.isSelectimage.show()

        } else {
            holder.viewBinding.bgline.setBackgroundResource(R.drawable.product_attribute_bg4)
            holder.viewBinding.isSelectimage.hide()
        }

        val numMonth = pakatList[position].numMonth
        if (numMonth > 0) {
            itemPackets.add(ItemPacket(context.getString(R.string.numMonth), numMonth))
        }

        val countImage = pakatList[position].countImage
        if (countImage > 0) {
            itemPackets.add(ItemPacket(context.getString(R.string.countImage), countImage))
        }

        val countVideo = pakatList[position].countVideo
        if (countVideo > 0) {
            itemPackets.add(
                ItemPacket(
                    title = context.getString(R.string.countVideo),
                    value = countVideo
                )
            )
        }

        val productPosition = pakatList[position].productPosition
        when (productPosition) {
            "StarRuf" -> {
                itemPackets.add(
                    ItemPacket(
                        title = context.getString(R.string.ad_display_priority),
                        value = context.getString(R.string.star_ruf)
                    )
                )
                itemPackets.add(
                    ItemPacket(
                        title = context.getString(R.string.larger_ad_size),
                        value = "✔"
                    )
                )
            }

            "MoonRuf" -> {
                itemPackets.add(
                    ItemPacket(
                        title = context.getString(R.string.ad_display_priority),
                        value = context.getString(R.string.moon_ruf)
                    )
                )
            }
            "CloudRuf" -> {
                itemPackets.add(
                    ItemPacket(
                        title = context.getString(R.string.ad_display_priority),
                        value = context.getString(R.string.cloud_ruf)
                    )
                )
            }
            else -> {
                itemPackets.add(
                    ItemPacket(
                        title = context.getString(R.string.ad_display_priority),
                        value = context.getString(R.string.unavailable)
                    )
                )
            }
        }

        if (productPosition.isNullOrEmpty()) {
            itemPackets.add(
                ItemPacket(
                    title = context.getString(R.string.productPosition),
                    value = productPosition
                )
            )
        }

        if (pakatList[position].showSupTitle) {
            itemPackets.add(
                ItemPacket(title = context.getString(R.string.showSupTitle), value = "✔")
            )
        }

        if (pakatList[position].showHighLight) {
            itemPackets.add(
                ItemPacket(title = context.getString(R.string.featured), value = "✔")
            )
        }

        if (pakatList[position].enableFixedPrice) {
            itemPackets.add(
                ItemPacket(title = context.getString(R.string.package_fixed), value = "✔")
            )
        }

        if (pakatList[position].enableNegotiable) {
            itemPackets.add(
                ItemPacket(title = context.getString(R.string.package_negotiable), value = "✔")
            )
        }

        if (pakatList[position].enableAuction) {
            itemPackets.add(
                ItemPacket(title = context.getString(R.string.package_auction), value = "✔")
            )
        }

        if (pakatList[position].auctionClosingTimeOption) {
            itemPackets.add(
                ItemPacket(title = context.getString(R.string.closing_time_option), value = "✔")
            )
        }

        holder.viewBinding.parentLayout.removeAllViews()
        itemPackets.forEach { itemPakatCategory ->
            val _view = inflater.inflate(R.layout.promotion_item, null)
            val pkg_service1: TextView = _view.findViewById(R.id.pkg_service1)
            val value_service: TextView = _view.findViewById(R.id.value_service)
            val imgCheck: ImageView = _view.findViewById(R.id.imgCheck)
            if (itemPakatCategory.value.toString() != "true") {
                value_service.text = itemPakatCategory.value.toString()
            } else {
                imgCheck.setImageResource(R.drawable.ic_check)
            }
            pkg_service1.text = itemPakatCategory.title
            holder.viewBinding.parentLayout.addView(_view)
        }
        holder.viewBinding.mainLayout.setOnClickListener {
            setOnPackageSelected.onSelectPackage(position)
        }

    }

    interface SetOnPackageSelected {
        fun onSelectPackage(position: Int)
    }
}