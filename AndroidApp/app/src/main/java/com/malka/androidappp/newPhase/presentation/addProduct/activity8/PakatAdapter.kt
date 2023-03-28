package com.malka.androidappp.newPhase.presentation.addProduct.activity8

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemPakatDesginBinding
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.pakatResp.PakatDetails
import kotlinx.android.synthetic.main.item_pakat_desgin.view.*

class PakatAdapter(val pakatList: List<PakatDetails>,var setOnPakatSelected:SetOnPakatSelected ) :RecyclerView.Adapter<PakatAdapter.PakatViewHolder>() {

    lateinit var context:Context
    class PakatViewHolder(var viewBinding:ItemPakatDesginBinding):RecyclerView.ViewHolder(viewBinding.root)
    lateinit var inflater :LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PakatViewHolder {
        context=parent.context
        inflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return PakatViewHolder(
            ItemPakatDesginBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int =pakatList.size

    override fun onBindViewHolder(holder: PakatViewHolder, position: Int) {
        holder.viewBinding.pkgName.text = pakatList[position].name
        holder.viewBinding.pkgPrice.text = "${pakatList[position].price} ${context.getString(R.string.Rayal)}"

        if (pakatList[position].popular) {
            holder.viewBinding.common.show()
            holder.viewBinding.isSelectimage.setImageResource(R.drawable.ic_check_black)
            holder.viewBinding.itemBg.setBackgroundColor(
                ContextCompat.getColor(context,
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

        holder.viewBinding.parentLayout.removeAllViews()
        pakatList[position].listCategories.forEach { itemPakatCategory->
            val _view = inflater.inflate(R.layout.promotion_item, null)
            val pkg_service1: TextView = _view.findViewById(R.id.pkg_service1)
            pkg_service1.text = itemPakatCategory.name
            holder.viewBinding.parentLayout.addView(_view)
        }
        holder.viewBinding.mainLayout.setOnClickListener {
            setOnPakatSelected.onSelectPakat(position)
        }

    }

    interface SetOnPakatSelected{
        fun onSelectPakat(position:Int)
    }
}