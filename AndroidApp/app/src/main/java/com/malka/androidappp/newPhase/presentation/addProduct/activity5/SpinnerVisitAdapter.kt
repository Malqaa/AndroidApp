package com.malka.androidappp.newPhase.presentation.addProduct.activity5
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.SubSpecificationItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects


class SpinnerVisitAdapter(var context: Context, var types: List<SubSpecificationItem>) :
    BaseAdapter() {
    var layoutInflater: LayoutInflater

    init {

        layoutInflater  = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return types.size
    }

    override fun getItem(i: Int): SubSpecificationItem {
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
            tvTypeName.text = getItem(i).nameAr.toString()
        }else{
            tvTypeName.text = getItem(i).nameEn.toString()
        }
        return convertView
    }

//    override fun isEnabled(position: Int): Boolean {
//        // Disable the first item from Spinner
//        // First item will be use for hint
//        return position != 0
//    }
//
//    override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
//        val view = super.getDropDownView(position, convertView, parent)
//        val tvTypeName = view.findViewById<View>(R.id.tvOrgName) as TextView
//        //  LinearLayout spinnerContainer = (LinearLayout) view.findViewById(R.id.spinnerContainer);
//        if (position == 0) {
//            // Set the hint text color gray
/////           tvSelectedItem.setBackgroundResource(R.drawable.gradient_background)
//            //  spinnerContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.blueApp))
//            tvTypeName.setTextColor(ContextCompat.getColor(context, R.color.white))
//            tvTypeName.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
//            tvTypeName.textAlignment = View.TEXT_ALIGNMENT_CENTER
//        }
//        return view
//    }
}