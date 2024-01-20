package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.malqaa.androidappp.R


class SpinnerExpireHoursAdapter(var context: Context, var types: List<Float>) :
    BaseAdapter() {
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return types.size
    }

    override fun getItem(i: Int): Float {
        return types[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    @Suppress("NAME_SHADOWING")
    override fun getView(i: Int, convertView: View?, parent: ViewGroup): View {
        val convertView = layoutInflater.inflate(R.layout.item_spinner, parent, false)
        val tvTypeName: TextView = convertView.findViewById(R.id.tvOrgName)
        tvTypeName.text = getItem(i).toString()
        return convertView
    }
}