package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity8

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemPakatDesginBinding
import com.malqaa.androidappp.newPhase.domain.models.pakatResp.ItemPacket
import com.malqaa.androidappp.newPhase.domain.models.pakatResp.PakatDetails
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class PakatAdapter(val pakatList: List<PakatDetails>, var setOnPakatSelected: SetOnPakatSelected) :
    RecyclerView.Adapter<PakatAdapter.PakatViewHolder>() {

    lateinit var context: Context
    var itemPackets: ArrayList<ItemPacket> = ArrayList<ItemPacket>()

    class PakatViewHolder(var viewBinding: ItemPakatDesginBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    lateinit var inflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PakatViewHolder {
        context = parent.context
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return PakatViewHolder(
            ItemPakatDesginBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = pakatList.size

    override fun onBindViewHolder(holder: PakatViewHolder, position: Int) {
        itemPackets = arrayListOf()
        holder.viewBinding.pkgName.text = pakatList[position].name
        holder.viewBinding.pkgPrice.text =
            "${pakatList[position].price} ${context.getString(R.string.Rayal)}"

        if (pakatList[position].popular) {
            holder.viewBinding.common.show()
            holder.viewBinding.isSelectimage.setImageResource(R.drawable.ic_check_black)
            holder.viewBinding.itemBg.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.bg
                )
            )
        } else {
            holder.viewBinding.common.hide()
            holder.viewBinding.isSelectimage.setImageResource(R.drawable.ic_check)
            holder.viewBinding.itemBg.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.textColor
                )
            )

        }
        if (pakatList[position].isSelected) {
            holder.viewBinding.bgline.setBackgroundResource(R.drawable.product_attribute_linebg)
            holder.viewBinding.isSelectimage.show()

        } else {
            holder.viewBinding.bgline.setBackgroundResource(R.drawable.product_attribute_bg4)
            holder.viewBinding.isSelectimage.hide()
        }

        itemPackets.add(
            ItemPacket(
                context.getString(R.string.numMonth),
                pakatList[position].numMonth
            )
        )
        itemPackets.add(
            ItemPacket(
                context.getString(R.string.countImage),
                pakatList[position].countImage
            )
        )
        itemPackets.add(
            ItemPacket(
                context.getString(R.string.countVideo),
                pakatList[position].countVideo
            )
        )
        itemPackets.add(
            ItemPacket(
                context.getString(R.string.productPosition),
                pakatList[position].productPosition
            )
        )
        itemPackets.add(
            ItemPacket(
                context.getString(R.string.showSupTitle),
                pakatList[position].showSupTitle
            )
        )
        itemPackets.add(
            ItemPacket(
                context.getString(R.string.showHighLight),
                pakatList[position].showHighLight
            )
        )

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
            pkg_service1.text = itemPakatCategory.Title
            holder.viewBinding.parentLayout.addView(_view)
        }
        holder.viewBinding.mainLayout.setOnClickListener {
            setOnPakatSelected.onSelectPakat(position)
        }

    }

    interface SetOnPakatSelected {
        fun onSelectPakat(position: Int)
    }
}