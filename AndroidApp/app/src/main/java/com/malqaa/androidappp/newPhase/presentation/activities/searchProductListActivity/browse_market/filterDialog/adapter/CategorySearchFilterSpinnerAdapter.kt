package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.productResp.CategoriesSearchItem

class CategorySearchFilterSpinnerAdapter(
    var context: Context,
    var categories: List<CategoriesSearchItem>,
) : BaseAdapter() {
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return categories.size
    }

    @Suppress("NAME_SHADOWING")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val convertView = layoutInflater.inflate(R.layout.item_type_spinner, parent, false)
        val tvTypeName: TextView = convertView.findViewById(R.id.tvTypeName)
        tvTypeName.text = categories[position].name ?: ""
        return convertView
    }

    override fun getItem(i: Int): CategoriesSearchItem {
        return categories[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun isEnabled(position: Int): Boolean {
        // Disable the first item from Spinner
        // First item will be use for hint
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view = super.getDropDownView(position, convertView, parent)
        val tvSelectedItem = view.findViewById<TextView>(R.id.tvTypeName)
        val spinnerContainer = view.findViewById<LinearLayout>(R.id.spinnerContainer)
        if (position == 0) {
            // Set the hint text color gray
///           tvSelectedItem.setBackgroundResource(R.drawable.gradient_background)
            //  spinnerContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.blueApp))
            tvSelectedItem.setTextColor(ContextCompat.getColor(context, R.color.white))
            tvSelectedItem.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
            tvSelectedItem.textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        return view
    }
}


