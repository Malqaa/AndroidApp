package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.editProfileActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.CountryCode
import com.malqaa.androidappp.newPhase.utils.helper.ConstantsHelper.openRawResourceByName

class CustomSpinnerAdapter(private val context: Context, private val items: Array<CountryCode>) :
    BaseAdapter() {


    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val convertView =
            LayoutInflater.from(context).inflate(R.layout.custom_spinner_item, parent, false)


        val icon = convertView.findViewById<ImageView>(R.id.imgCode)
        val text = convertView.findViewById<TextView>(R.id.textCode)

        context.openRawResourceByName(icon, items[position].code.toLowerCase())
        text.text = items[position].dialCode
        return convertView
    }

}