package com.malqaa.androidappp.newPhase.presentation.activities.signup.activity3
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.domain.models.countryResp.Country


class SpinnerCountryAdapter(var context: Context, var types: List<Country>) :
    BaseAdapter() {
    var layoutInflater: LayoutInflater

    init {

        layoutInflater  = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return types.size
    }

    override fun getItem(i: Int): Country {
        return types[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    @Suppress("NAME_SHADOWING")
    override fun getView(i: Int, convertView: View?, parent: ViewGroup): View {
        val convertView = layoutInflater.inflate(R.layout.item_spinner, parent, false)
        val tvTypeName: TextView = convertView.findViewById(R.id.tvOrgName)
        if(ConstantObjects.currentLanguage== ConstantObjects.ARABIC){
            tvTypeName.text = getItem(i).name.toString()
        }else{
            tvTypeName.text = getItem(i).name.toString()
        }
        return convertView
    }

}