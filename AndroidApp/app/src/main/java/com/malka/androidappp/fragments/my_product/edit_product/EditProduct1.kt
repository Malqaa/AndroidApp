package com.malka.androidappp.fragments.my_product.edit_product

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.fragments.create_product.ProductDetailModel
import com.malka.androidappp.fragments.create_product.ProductResponseBack
import com.malka.androidappp.fragments.create_product.StaticClassProductCreate
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_create_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EditProduct1 : Fragment() {

    var AdvId: String = ""
    val allHours: Array<String> =
        arrayOf("1 Hour", "2 Hours", "4 Hours", "6 Hours", "8 Hours", "10 Hours")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        

        AdvId = arguments?.getString("AdvId").toString()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_product.title = "Edit Product"
        toolbar_product.setTitleTextColor(Color.WHITE)
        toolbar_product.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_product.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        ////////////////////////////Api//////////////////////////////////////////
        getProductbyidapi(AdvId, ConstantObjects.logged_userid)

        ///////////Calender EditText///////////////
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val minute = c.get(Calendar.MINUTE)
        val hour = c.get(Calendar.HOUR_OF_DAY)


        ///////////////////////////////////////CheckBox if/else Clicking//////////////////////////////////////////

        stock_unlimited.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                product_stock_limited.setEnabled(false)
                product_stock_limited.setText("")
                product_stock_limited.setInputType(InputType.TYPE_NULL)
                product_stock_limited.setFocusableInTouchMode(false)
            } else {
                product_stock_limited.setEnabled(true)
                product_stock_limited.setInputType(InputType.TYPE_CLASS_TEXT)
                product_stock_limited.setFocusableInTouchMode(true)
            }
        })

        specify_time.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                time_specifyy.setEnabled(true)
                time_specifyy.setFocusableInTouchMode(false)
                time_specifyy.setInputType(InputType.TYPE_CLASS_TEXT)
                time_specifyy.setOnClickListener() {
                    hidekeyboard()
                    val tpd = TimePickerDialog(
                        this.requireContext(),
                        TimePickerDialog.OnTimeSetListener { view, selectedHour, selectedMinute ->
                            time_specifyy.setText(
                                String.format(
                                    "%02d:%02d",
                                    selectedHour,
                                    selectedMinute
                                )
                            )
                        },
                        hour,
                        minute,
                        true
                    )
                    tpd.show()
                }
            } else {
                time_specifyy.setEnabled(false)
                time_specifyy.setText("")
                time_specifyy.setInputType(InputType.TYPE_NULL)
                time_specifyy.setFocusableInTouchMode(false)
            }
        })


        /////////////////For Listing Dropdown/Spinner/////////////////////
        val spinner: Spinner = requireActivity().findViewById(R.id.spinner2)
        spinner.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            allHours
        )

        btn_productinfo.setOnClickListener() {

            val productCodeText: String = product_codee.text.toString()
            StaticClassProductCreate.code = productCodeText

            val productSKUText: String = product_skuu.text.toString()
            StaticClassProductCreate.sKU = productSKUText

            val productTitleText: String = product_titlee.text.toString()
            StaticClassProductCreate.title = productTitleText

            val productSubtitleText: String = product_subtitlee.text.toString()
            StaticClassProductCreate.subTitle = productSubtitleText

            val productBrandText: String = product_brandd.text.toString()
            StaticClassProductCreate.brand = productBrandText

            val productBarcodeText: String = product_barcodee.text.toString()
            StaticClassProductCreate.barcodeGTIN = productBarcodeText

            val productManufaturerCodeText: String = product_manufaturer_codee.text.toString()
            StaticClassProductCreate.manufacturersCode = productManufaturerCodeText

            confirmListing(view)

            confirmStock(view)

            confirmSelectedTime(view)

            findNavController().navigate(R.id.edit_product1_to_2)
        }

    }

    fun hidekeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    fun confirmSelectedTime(v: View) {
        if (specify_time.isChecked) {
            val timetxt: String = time_specifyy.getText().toString()
            var hour = timetxt.substring(0, 2)
            var minute = timetxt.substring(3, 5)
            var time = "${hour}" + " : " + "${minute}" + " : 00"
            StaticClassProductCreate.specifyEndTime = time
        }
    }

    fun confirmListing(v: View) {
        val listing: String = spinner2.getSelectedItem().toString()
        StaticClassProductCreate.listingDuration = listing
    }

    fun confirmStock(v: View) {
        if (stock_unlimited.isChecked) {
            StaticClassProductCreate.UnlimitedStock = true
        } else {
            val productStockLimited: String = product_stock_limited.getText().toString()
            StaticClassProductCreate.stock = productStockLimited
        }
    }


    fun getProductbyidapi(advid: String, loginUserId: String) {

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ProductResponseBack> = malqa.getProductDetailById(advid, loginUserId)

        call.enqueue(object : Callback<ProductResponseBack> {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onResponse(
                call: Call<ProductResponseBack>,
                response: Response<ProductResponseBack>
            ) {

                if (response.isSuccessful) {
                    val details: ProductDetailModel? = response.body()!!.data
                    if (details != null) {

                        StaticClassProductCreate.id = details.id.toString()
                        product_codee.setText(details!!.code)
                        product_skuu.setText(details!!.sKU)
                        product_titlee.setText(details!!.title)
                        product_subtitlee.setText(details!!.subTitle)
                        product_brandd.setText(details!!.brand)
                        product_barcodee.setText(details!!.barcodeGTIN)
                        product_manufaturer_codee.setText(details!!.manufacturersCode)
                        spinner2.setSelection(allHours.indexOf(details!!.listingDuration))
                        product_stock_limited.setText(details!!.stock)
                        stock_unlimited.isChecked = details!!.unlimitedStock == true
                        specify_time.isChecked =
                            details!!.specifyEndTime != "" || details!!.specifyEndTime != null
                        time_specifyy.setText(details!!.specifyEndTime)


                    } else {
                        HelpFunctions.ShowAlert(
                            this@EditProduct1.context, "Information", "No Record Found"
                        )
                    }
                } else {
                    HelpFunctions.ShowAlert(
                        this@EditProduct1.context, "Information", "No Record Found"
                    )
                }
            }

            override fun onFailure(call: Call<ProductResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
