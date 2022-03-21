package com.malka.androidappp.botmnav_fragments.cardetail_page.bottomsheet_bidopt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.cardetail_page.bottomsheet_bidopt.post_bidprice.ModelPostBidPrice
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_bottmmm.*
import kotlinx.android.synthetic.main.botm_sheet_layout.view.*
import kotlinx.android.synthetic.main.carspec_card8.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomsheetDialogfragClass : BottomSheetDialogFragment() {

    private var mListener: BottomSheetListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.botm_sheet_layout, container, false)

        getcurrentbidingprice(v)
        val button1 = v.findViewById<Button>(R.id.buttoncancel)
        val button2 = v.findViewById<Button>(R.id.placebidd)
        button1.setOnClickListener {
            //mListener!!.onButtonClicked("Button 1 clicked")
            dismiss()

        }
        button2.setOnClickListener {
            //mListener!!.onButtonClicked("Button 2 clicked")
            placebidconfirmInput(v)
            //dismiss()
        }
        return v
    }
/////////////////////////////////////BottomSheet View Enddd/////////////////////////////////////////

    //////////////////////////////////Placebid Confirm Buttom//////////////////////////////////////////
    fun placebidconfirmInput(v: View) {
        if (!validateBidPrice(v)) {
            return
        } else {
            postmaxbidprice(v)
        }

    }

    //PLaceBid Validation
    private fun validateBidPrice(v: View): Boolean {
        val bidInputText: TextInputEditText = v.findViewById(R.id.bidpriceinput)
        val Input = v.bidpriceinput.text.toString().trim { it <= ' ' }
        val lastmaxbid: Int = StoreDataForAdDetail.maxlastbidPrice.toInt()
//        val getCurrentInputBidprice:Int = bidInputText.getText().toString().trim().toInt()


        return if (Input.isEmpty()) {
            bidInputText.error = "Empty Box"
            false
        } else if (bidInputText.getText().toString().trim().toInt() <= lastmaxbid) {
            bidInputText.error = "BidPrice Should be Greater than Previous"
            false
        } else if (ConstantObjects.logged_userid == SharedPreferencesStaticClass.ad_userid) {
            bidInputText.error = "You can't bid on this ad"
            false
        } else {
            bidInputText.error = null
            true
        }


    }


    fun postmaxbidprice(v: View) {

        val bidInputText: TextInputEditText = v.findViewById(R.id.bidpriceinput)
        val getbidPrice = bidInputText.text.toString().trim()


        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val resp: ModelPostBidPrice = ModelPostBidPrice(
            StoreDataForAdDetail.saveAdvIdforAdDetailBiding,
            "0", getbidPrice, ConstantObjects.logged_userid, "1", 0
        )

        val call: Call<ModelPostBidPrice> = malqaa.postBidPrice(resp)
        call.enqueue(object : Callback<ModelPostBidPrice> {
            override fun onResponse(
                call: Call<ModelPostBidPrice>,
                response: Response<ModelPostBidPrice>
            ) {
                Toast.makeText(activity, "Max Bid Updated", Toast.LENGTH_SHORT).show()
                dismiss()
            }

            override fun onFailure(call: Call<ModelPostBidPrice>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun getcurrentbidingprice(v: View) {
        val textchange = v.findViewById<TextView>(R.id.bidprice)
        textchange.setText(StoreDataForAdDetail.maxlastbidPrice)
    }

    interface BottomSheetListener {
        fun onButtonClicked(text: String?)
    }

}