package com.malka.androidappp.botmnav_fragments.create_ads

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.malka.androidappp.R
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_confirmation.view.*
import kotlinx.android.synthetic.main.fragment_list_details.*
import kotlinx.android.synthetic.main.fragment_listing_duration.*
import org.w3c.dom.Text


class ListingDetailsFragment : Fragment() {

    lateinit var itemTitle: TextInputEditText
    lateinit var quantityTextField: TextInputLayout
    lateinit var category : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ////////////////////////////////////////////////////////
//        toolbar_listdetails.setNavigationIcon(R.drawable.nav_icon_back)
//        toolbar_listdetails.title = getString(R.string.ListingDetails)
//        toolbar_listdetails.setTitleTextColor(Color.WHITE)
//        toolbar_listdetails.inflateMenu(R.menu.adcreation_close_btn)
//        toolbar_listdetails.setNavigationOnClickListener() {
//            requireActivity().onBackPressed()
//        }

        itemTitle = requireActivity().findViewById(R.id.title)
        itemTitle.setText(StaticClassAdCreate.producttitle)

        category = requireActivity().findViewById(R.id.textView49)

        quantityTextField = requireActivity().findViewById(R.id.othernameee)

//        toolbar_listdetails.setOnMenuItemClickListener { item ->
//            if (item.itemId == R.id.action_close) {
//                findNavController().navigate(R.id.close_listingdetails)
//                //closefrag()
//            } else {
//                // do something
//            }
//            false
//        }
        /////////////////CodingFlow For Country textDropdown/Spiner/////////////////////
        val spinner: CountryCodePicker = requireActivity().findViewById(R.id.country_code)


        /////////////////For Region Dropdown/Spinner/////////////////////

//        val spinner2: Spinner = requireActivity().findViewById(R.id.spinner2)
//        val adapter2 = ArrayAdapter.createFromResource(
//            this.requireActivity(),
//            R.array.regionlist,
//            R.layout.support_simple_spinner_dropdown_item
//        )
//        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
//        spinner2.adapter = adapter2


        /////////////////For City Dropdown/Spinner/////////////////////

//        val spinner3: Spinner = requireActivity().findViewById(R.id.spinner3)
//        val adapter3 = ArrayAdapter.createFromResource(
//            this.requireActivity(), R.array.citylist, R.layout.support_simple_spinner_dropdown_item
//        )
//        adapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
//        spinner3.adapter = adapter3


        //////////////////////////Image N Text DropdownSpinner for Phonenum country//////////////////////////
        val spinnerr: Spinner = requireActivity().findViewById(R.id.dropdown)
        val imageName = arrayOf("+973", "+971", "+92", "+973")
        val image =
            intArrayOf(R.drawable.flag6, R.drawable.flag7, R.drawable.flag2, R.drawable.flag3)
        val spinnerCustomAdapter = SpinnerCustomAdapter(requireContext(), image, imageName);
        spinnerr.adapter = spinnerCustomAdapter

        /////////////////////////////////Activity Switching to promtional///////////////////////////////
        btnotherr.setOnClickListener() { ListDetailsconfirmInput() }

//        userType()
//        setCategoryPath()

    }

    //Data Validation
    private fun validateTitle(): Boolean {
        var title = requireActivity().findViewById(R.id.title) as EditText
        val InputTitle = title.text.toString().trim { it <= ' ' }

        return if (InputTitle.isEmpty()) {
            title.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            title.error = null
            true
        }
    }

    //Data Validation
//    private fun validateSubTitle(): Boolean {
//        var subTitle = requireActivity().findViewById(R.id.subtitle) as EditText
//        val InputsubTitle = subTitle.text.toString().trim { it <= ' ' }
//
//        return if (InputsubTitle.isEmpty()) {
//            subTitle.error = "Field can't be empty"
//            false
//        } else {
//            subTitle.error = null
//            true
//        }
//    }

    //Data Validation
//    private fun validatequantityavail(): Boolean {
//        var quantity = requireActivity().findViewById<EditText>(R.id.quantityavail)
//        val InputQuantity = quantity!!.text.toString().trim { it <= ' ' }
//
//        return if (InputQuantity.isEmpty()) {
//            quantity.error = "Field can't be empty"
//            false
//        } else {
//            quantity.error = null
//            true
//        }
//    }


    private fun validatephoneNum(): Boolean {
        var phoneNum = requireActivity().findViewById<EditText>(R.id.phonetext)
        val InputPhoneNum = phoneNum!!.text.toString().trim { it <= ' ' }

        return if (InputPhoneNum.isEmpty()) {
            phoneNum.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            phoneNum.error = null
            true
        }
    }

    ///////////////////////////Spinners Validation////////////////////////////////////////////
//    private fun validateCountry(): Boolean {
//        var textcountry: Spinner = requireActivity().findViewById<Spinner>(R.id.country_code)
//        val errorcountryid = requireActivity().findViewById<TextView>(R.id.errorcountry)
//
//        return if (textcountry.selectedItem.toString().trim() == "- - Select Country - -") {
//            errorcountryid.visibility = View.VISIBLE
//            false
//        } else {
//            errorcountryid.visibility = View.GONE
//            true
//        }
//    }

    ///////////////////////////Spinners Validation////////////////////////////////////////////
//    private fun validateRegion(): Boolean {
//        var textregion: Spinner = requireActivity().findViewById<Spinner>(R.id.spinner2)
//        val errorRegionid = requireActivity().findViewById<TextView>(R.id.errorregion)
//
//        return if (textregion.selectedItem.toString().trim() == "- - Select Region - -") {
//            errorRegionid.visibility = View.VISIBLE
//            false
//        } else {
//            errorRegionid.visibility = View.GONE
//            true
//        }
//    }

    ///////////////////////////Spinners Validation////////////////////////////////////////////
//    private fun ValidateCity(): Boolean {
//        var textCity: Spinner = requireActivity().findViewById<Spinner>(R.id.spinner3)
//        val errorCity = requireActivity().findViewById<TextView>(R.id.errorcity)
//
//        return if (textCity.selectedItem.toString().trim() == "- - Select City - -") {
//            errorCity.visibility = View.VISIBLE
//            false
//        } else {
//            errorCity.visibility = View.GONE
//            true
//        }
//    }

    fun confirmBrandNewitem() {
        if (brandcheckbox.isChecked) {
            StaticClassAdCreate.brand_new_item = "on"
        } else {
            StaticClassAdCreate.brand_new_item = "Off"
        }
    }


    fun ListDetailsconfirmInput() {
//        if (!validateTitle() or !validatephoneNum()
////            or !validateCountry() or !validateRegion() or !ValidateCity()
//        ) {
//            return
//        } else {
//
//            val titlee: String = title.text.toString()
//            StaticClassAdCreate.title = titlee
//            val subtitlee: String = subtitle.text.toString()
//            StaticClassAdCreate.subtitle = subtitlee
//            val quantityavaill: String = quantityavail.text.toString()
//            StaticClassAdCreate.quantity = quantityavaill
//            val country: String = country_code.selectedImages.toString()
//            StaticClassAdCreate.country = country
//            val regionn: String = spinner2.selectedItem.toString()
//            StaticClassAdCreate.region = regionn
//            val cityy: String = spinner3.selectedItem.toString()
//            StaticClassAdCreate.city = cityy
//            val description: String = descriptionedit.text.toString()
//            StaticClassAdCreate.description = description
//            val address: String = itemAddress2.text.toString()
//            StaticClassAdCreate.address = address


            //zeeshanbhailine
            //val phonenum:String = dropdown.getItemAtPosition(dropdown.selectedItemPosition).toString()

            ///////Binding Phone Num (dropdoenspinner and edittext)////////////
            val phonenum: String =
                dropdown.selectedItem.toString() + phonetext.text.toString()
            StaticClassAdCreate.phone = phonenum

            //////////////////To get Checkbox value and save it on static class////////////////
//            val brandcheckbox: Boolean = brandcheckbox.isChecked
//            StaticClassAdCreate.brand_new_item = brandcheckbox


// T check brand new item
            confirmBrandNewitem()

            findNavController().navigate(R.id.listdetail_pricepayment)

        }

    }

    /////////////////////////////////Toast msg for textdropdown adapter/////////////////////////////////////////////////


    //////////////////////////////////////////ImageDropdown adapter////////////////////////////////////////////
    class SpinnerCustomAdapter(
        internal var context: Context,
        internal var flags: IntArray,
        internal var Network: Array<String>
    ) : BaseAdapter() {
        internal var inflter: LayoutInflater

        init {
            inflter = LayoutInflater.from(context)
        }

        override fun getCount(): Int {
            return Network.size
        }

        override fun getItem(i: Int): Any? {
            return Network[i]
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            var view = view
            view = inflter.inflate(R.layout.custom_spinner_row, null)
            val icon = view!!.findViewById(R.id.spinner_imageView) as ImageView
            val names = view!!.findViewById(R.id.spinner_textView) as TextView
            icon.setImageResource(flags[i])
            names.text = Network[i]
            return view
        }
    }

//    private fun userType() {
//
//        if (!ConstantObjects.isBusinessUser) {
//            quantityTextField.visibility = View.GONE
//        } else {
//            quantityTextField.visibility = View.VISIBLE
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun setCategoryPath(){
//        for (i in 0 until StaticClassAdCreate.subCategoryPath.size){
//            category.text = StaticClassAdCreate.subCategoryPath[i]
//        }
//    }


