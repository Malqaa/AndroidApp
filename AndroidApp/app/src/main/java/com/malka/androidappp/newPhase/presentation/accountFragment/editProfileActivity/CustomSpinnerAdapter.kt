package com.malka.androidappp.newPhase.presentation.accountFragment.editProfileActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.domain.models.CountryCode
import com.malka.androidappp.newPhase.presentation.utils.ConstantsHelper.openRawResourceByName

class CustomSpinnerAdapter(private val context: Context, private val items: Array<CountryCode>) :
    BaseAdapter(){


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
        val convertView = LayoutInflater.from(context).inflate(R.layout.custom_spinner_item, parent, false)


        val icon = convertView.findViewById<ImageView>(R.id.imgCode)
        val text = convertView.findViewById<TextView>(R.id.textCode)

        context.openRawResourceByName(icon,items[position].code.toLowerCase())
        text.text = items[position].dialCode
        return convertView
    }


}