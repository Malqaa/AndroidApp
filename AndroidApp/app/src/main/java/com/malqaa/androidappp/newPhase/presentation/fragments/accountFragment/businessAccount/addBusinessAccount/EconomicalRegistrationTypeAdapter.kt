package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.businessAccount.addBusinessAccount

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.malqaa.androidappp.R

class EconomicalRegistrationTypeAdapter(
    var context: Context,
    var workSectorList: List<String>,
) : BaseAdapter() {
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return workSectorList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val convertView = layoutInflater.inflate(R.layout.item_type_spinner, parent, false)
        val tvTypeName: TextView = convertView.findViewById(R.id.tvTypeName)
        tvTypeName.text = getItem(position)

        return convertView
    }

    override fun getItem(i: Int): String {
        return workSectorList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }


}