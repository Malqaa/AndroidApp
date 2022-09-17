package com.malka.androidappp.fragments.create_product

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
import kotlinx.android.synthetic.main.fragment_create_product.*
import java.util.*

class CreateProduct : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_product.setTitle("My Products")
        toolbar_product.setTitleTextColor(Color.WHITE)
        toolbar_product.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_product.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        ///////////Calender EditText///////////////
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val minute = c.get(Calendar.MINUTE)
        val hour = c.get(Calendar.HOUR_OF_DAY)


        val allHours: Array<String> =
            arrayOf("1 Hour", "2 Hours", "4 Hours", "6 Hours", "8 Hours", "10 Hours")

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

            val productCodeText: String = product_codee.getText().toString()
            StaticClassProductCreate.code = productCodeText

            val productSKUText: String = product_skuu.getText().toString()
            StaticClassProductCreate.sKU = productSKUText

            val productTitleText: String = product_titlee.getText().toString()
            StaticClassProductCreate.title = productTitleText

            val productSubtitleText: String = product_subtitlee.getText().toString()
            StaticClassProductCreate.subTitle = productSubtitleText

            val productBrandText: String = product_brandd.getText().toString()
            StaticClassProductCreate.brand = productBrandText

            val productBarcodeText: String = product_barcodee.getText().toString()
            StaticClassProductCreate.barcodeGTIN = productBarcodeText

            val productManufaturerCodeText: String = product_manufaturer_codee.getText().toString()
            StaticClassProductCreate.manufacturersCode = productManufaturerCodeText

            confirmListing(view)

            confirmStock(view)

            confirmSelectedTime(view)

            findNavController().navigate(R.id.product_page2)
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
        val listing: String = spinner2.selectedItem.toString()
        var listing2: String = ""
        if (listing.matches("1 Hour".toRegex())){
            listing2 = "1"
        }else if (listing.matches("2 Hours".toRegex())){
            listing2 = "2"
        }else if (listing.matches("4 Hours".toRegex())){
            listing2 = "4"
        }else if (listing.matches("6 Hours".toRegex())){
            listing2 = "6"
        }else if (listing.matches("8 Hours".toRegex())){
            listing2 = "8"
        }else if(listing.matches("10 Hours".toRegex())){
            listing2 = "10"
        }
        StaticClassProductCreate.listingDuration = listing2
    }

    fun confirmStock(v: View) {
        if (stock_unlimited.isChecked) {
            StaticClassProductCreate.UnlimitedStock = true
        } else {
            val productStockLimited: String = product_stock_limited.getText().toString()
            StaticClassProductCreate.stock = productStockLimited
        }
    }
}

