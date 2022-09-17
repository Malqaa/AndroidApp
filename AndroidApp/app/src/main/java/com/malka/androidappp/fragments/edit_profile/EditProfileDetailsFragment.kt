package com.malka.androidappp.fragments.edit_profile

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.malka.androidappp.R
import com.malka.androidappp.servicemodels.User
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.user.UserObject
import kotlinx.android.synthetic.main.fragment_edit_profile_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EditProfileDetailsFragment : Fragment() {

    lateinit var editFullName: TextInputEditText
    lateinit var editLastName: TextInputEditText
    lateinit var zipcode: TextInputEditText
    lateinit var dateOfBirth: TextInputEditText
    lateinit var addressUser: TextInputEditText
    lateinit var district: TextInputEditText
    lateinit var regionSpinner: Spinner
    lateinit var citySpinner: Spinner
    var areaa = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        HelpFunctions.startProgressBar(requireActivity())
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbareditprof.setNavigationIcon(R.drawable.nav_icon_back)
        toolbareditprof.navigationIcon?.isAutoMirrored = true
        toolbareditprof.title = getString(R.string.EditUserprofile)
        toolbareditprof.setTitleTextColor(Color.WHITE)
        toolbareditprof.setNavigationOnClickListener(View.OnClickListener {
            requireActivity().onBackPressed()
        })
        toolbareditprof.inflateMenu(R.menu.edit_profile)
        toolbareditprof.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.save_img) {
                // do something
                confirmInputttt(view)

            } else {
                // do something
            }
            false
        }

        editFullName = requireActivity().findViewById(R.id.fn_id)
        editLastName = requireActivity().findViewById(R.id.ln_id)
        zipcode = requireActivity().findViewById(R.id.zipCode)
        dateOfBirth = requireActivity().findViewById(R.id.d_o_b)
        addressUser = requireActivity().findViewById(R.id.address2_id)
        district = requireActivity().findViewById(R.id.districname)
        regionSpinner = requireActivity().findViewById(R.id.region_id)
        citySpinner = requireActivity().findViewById(R.id.city_id)


        /////////////////For Region Dropdown/Spinner/////////////////////
        val spinner2: Spinner = requireActivity().findViewById(R.id.region_id)
        val adapter2 = ArrayAdapter.createFromResource(
            requireContext(), R.array.regionlist, R.layout.support_simple_spinner_dropdown_item
        )
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner2.adapter = adapter2


        /////////////////For City Dropdown/Spinner/////////////////////
        val spinner3: Spinner = requireActivity().findViewById(R.id.city_id)
        val adapter3 = ArrayAdapter.createFromResource(
            requireContext(), R.array.citylist, R.layout.support_simple_spinner_dropdown_item
        )
        adapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner3.adapter = adapter3


        /////////////Show Api already Saved Profile Data////////////////////////////
        getuserprofiledataapi()
        ///////////////////////////////////////////////////////////////////////////

        ///////////Calender EditText///////////////
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dateofbirth: TextInputEditText = requireActivity().findViewById(R.id.d_o_b)

        ///////////////////////////////Date of Birth ////////////////////////////////////////////////////////
        dateofbirth.setOnClickListener() {
            hidekeyboard()
            val inputMethodManager =
                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    val monthplus: Int = mMonth + 1
                    dateofbirth.setText("" + mYear + "-" + monthplus + "-" + mDay)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
    }


    //Edit profile Validation

    ////////////////////////////////Data Validation///////////////////////////////////


    private fun validateFName(): Boolean {
        val textfName = requireActivity().findViewById(R.id.fn_id) as TextInputEditText
        val Inputfname = textfName.text.toString().trim { it <= ' ' }
        return if (Inputfname.isEmpty()) {
            textfName.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textfName.error = null
            true
        }
    }

    private fun validateLName(): Boolean {
        val textlName = requireActivity().findViewById(R.id.ln_id) as TextInputEditText
        val Inputlname = textlName.text.toString().trim { it <= ' ' }
        return if (Inputlname.isEmpty()) {
            textlName.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textlName.error = null
            true
        }
    }

    private fun validateDateBirth(): Boolean {
        val dobb = requireActivity().findViewById(R.id.d_o_b) as TextInputEditText
        val Inputmob = dobb.text.toString().trim { it <= ' ' }
        return if (Inputmob.isEmpty()) {
            dobb.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            dobb.error = null
            true
        }
    }

    private fun validateDistrict(): Boolean {
        val textdistrict = requireActivity().findViewById(R.id.districname) as TextInputEditText
        val Inputlline = textdistrict.text.toString().trim { it <= ' ' }
        return if (Inputlline.isEmpty()) {
            textdistrict.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textdistrict.error = null
            true
        }
    }

    private fun validateZipCode(): Boolean {
        val textzipcode = requireActivity().findViewById(R.id.zipCode) as TextInputEditText
        val Inputlline = textzipcode.text.toString().trim { it <= ' ' }
        return if (Inputlline.isEmpty()) {
            textzipcode.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textzipcode.error = null
            true
        }
    }

//    private fun validateCountry(): Boolean {
//        val textcountry = requireActivity().findViewById(R.id.country_id) as TextInputEditText
//        val Inputcoun = textcountry.text.toString().trim { it <= ' ' }
//        return if (Inputcoun.isEmpty()) {
//            textcountry.error = "Field can't be empty"
//            false
//        } else {
//            textcountry.error = null
//            true
//        }
//    }

//    private fun validateRegion(): Boolean {
//        val textregion = requireActivity().findViewById(R.id.region_id) as TextInputEditText
//        val Inputregn = textregion.text.toString().trim { it <= ' ' }
//        return if (Inputregn.isEmpty()) {
//            textregion.error = "Field can't be empty"
//            false
//        } else {
//            textregion.error = null
//            true
//        }
//    }

//    private fun validateCity(): Boolean {
//        val textcity = requireActivity().findViewById(R.id.city_id) as TextInputEditText
//        val Inputcity = textcity.text.toString().trim { it <= ' ' }
//        return if (Inputcity.isEmpty()) {
//            textcity.error = "Field can't be empty"
//            false
//        } else {
//            textcity.error = null
//            true
//        }
//    }


    private fun validateAddress(): Boolean {
        val textaddress2 = requireActivity().findViewById(R.id.address2_id) as TextInputEditText
        val Inputadd2 = textaddress2.text.toString().trim { it <= ' ' }
        return if (Inputadd2.isEmpty()) {
            textaddress2.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textaddress2.error = null
            true
        }
    }


    ////////////////////Call api saved data to view//////////////////////////////////
    fun checkedgender(gender: String) {

        val genderradiobtn: String = gender
        if (genderradiobtn == "Male") {
            radioButton.isChecked = true
        } else if (genderradiobtn == "Female") {
            radioButton2.isChecked = true
        }

    }

    fun whocanseeProfile(profileprivacy: String) {

        val profileprivacyradiobtn: String = profileprivacy
        if (profileprivacyradiobtn == "Everyone") {
            radioButton11.isChecked = true
        } else if (profileprivacyradiobtn == "Member Only") {
            radioButton22.isChecked = true
        } else if (profileprivacyradiobtn == "No One") {
            radioButton33.isChecked = true
        }

    }


    /////////////////////Validation and updateProfile///////////////////////////
    fun confirmInputttt(v: View) {
        if (!validateFName() or !validateLName() or !validateDateBirth() or !validateDistrict() or
            !validateAddress()
            or !validateZipCode()
        ) {
            return
        } else {
            updateapicall()
        }
    }


    fun updateapicall() {
        var userprop: User? = ConstantObjects.userobj;
        if (userprop != null) {
            val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
            val userId4: String = userprop.id!!
            val firstname: String = fn_id.text.toString().trim()
            val lastnaam: String = ln_id.text.toString().trim()


            val userRegion: Spinner = requireActivity().findViewById(R.id.region_id)
            val region: String = userRegion.selectedItem.toString().trim()

            val userCity: Spinner = requireActivity().findViewById(R.id.city_id)
            val city: String = userCity.selectedItem.toString().trim()

            val address: String = address2_id.text.toString().trim()
            //
            val dateofbirth: String = d_o_b.text.toString().trim()
            val districnamee: String = districname.text.toString().trim()
            val zipCodee: String = zipCode.text.toString().trim()
            //
            //////getting gender radiobutton////////
            var genderRadiobtnnn: String = ""
            val selectedIdgender: Int = gender_group.checkedRadioButtonId
            if (selectedIdgender !== null && selectedIdgender > -1) {
                val genderradioBtn: RadioButton = requireActivity().findViewById(selectedIdgender)
                if (genderradioBtn != null) {
                    genderRadiobtnnn = genderradioBtn.getText().toString()
                }
            }

            val call: Call<User> = malqaa.updateUserSiginup(
                User(
                    id = userId4,
                    firstName = firstname,
                    lastname = lastnaam,
                    gender = genderRadiobtnnn,
                    phone = null,
                    region = region,
                    city = city,
                    address = address,
                    dateOfBirth = dateofbirth,
                    area = areaa,
                    distric = districnamee,
                    zipcode = zipCodee
                )
            )
            call.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>, response: Response<User>
                ) {
                    if (response.isSuccessful) {
//                        Toast.makeText(
//                            activity,
//                            getString(R.string.Profileinformationhasbeenupdated),
//                            Toast.LENGTH_LONG
//                        ).show()
                        HelpFunctions.ShowLongToast(
                            getString(R.string.Profileinformationhasbeenupdated),
                            activity
                        )
                        findNavController().navigate(R.id.editto_profile)

                    } else {
                        HelpFunctions.ShowLongToast(response.message(), activity)
//                        Toast.makeText(activity, response.message(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    t.message?.let { HelpFunctions.ShowLongToast(it, activity) }

//                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    fun getuserprofiledataapi() {
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<UserObject> = malqa.getuser(ConstantObjects.logged_userid)
        call.enqueue(object : Callback<UserObject> {
            override fun onResponse(call: Call<UserObject>, response: Response<UserObject>) {

                if (response.isSuccessful) {
                    ConstantObjects.userobj = response.body()!!.data
                    var userprop: User? = ConstantObjects.userobj
                    if (userprop != null) {

                        if (userprop.fullName != null) {
                            editFullName.setText(userprop.fullName)
                        }

                        if (userprop.lastname != null) {
                            editLastName.setText(userprop.lastname)
                        }



                        if (userprop.region != null) {
                            regionSpinner.setSelection(
                                resources.getStringArray(R.array.regionlist)
                                    .indexOf(userprop.region)
                            )

                        }

                        if (userprop.city != null) {

                            citySpinner.setSelection(
                                resources.getStringArray(R.array.citylist).indexOf(userprop.city)
                            )
                        }

                        if (userprop.address != null) {

                            addressUser.setText(userprop.address)
                        }

                        if (userprop.dateOfBirth != null) dateOfBirth.setText(
                            userprop.dateOfBirth!!.take(
                                10
                            )
                        )
                        else ""


                        if (userprop.distric != null) district.setText(userprop.distric)
                        else ""


                        if (userprop.zipcode != null) zipcode.setText(userprop.zipcode)
                        else ""

                        if (userprop.gender != null) checkedgender(userprop.gender!!)
                        else checkedgender("Male")

                        whocanseeProfile("Everyone")
                    }


                    val returnarea: String =
                        if (response.body()!!.data.area != null) response.body()!!.data.area.toString() else ""
                    areaa = returnarea
                    HelpFunctions.dismissProgressBar()

                }
            }

            override fun onFailure(call: Call<UserObject>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun hidekeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}