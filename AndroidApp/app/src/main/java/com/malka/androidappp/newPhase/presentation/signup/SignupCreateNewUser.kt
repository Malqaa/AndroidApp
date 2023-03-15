package com.malka.androidappp.newPhase.presentation.signup

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.Extension.getDeviceId
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.widgets.DatePickerFragment
import com.malka.androidappp.newPhase.data.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malka.androidappp.newPhase.presentation.dialogsShared.countryDialog.CountryDialog
import com.malka.androidappp.newPhase.presentation.dialogsShared.neighborhoodDialog.NeighborhoodDialog
import com.malka.androidappp.newPhase.presentation.dialogsShared.regionDialog.RegionDialog
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.signup.signupViewModel.SignupViewModel
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.activity_signup_pg4.*

class SignupCreateNewUser : BaseActivity() {

    var selectedCountryId: Int = 0
    var selectedRegionId: Int = 0
    var selectedNeighborhoodId: Int = 0
    var gender_ = -1
    private var otpData: OtpData? = null
    private lateinit var signupViewModel: SignupViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg4)
        otpData = intent.getParcelableExtra(Constants.otpDataKey)
        setupRegisterViewModel()
        setupClickListeners()
//        ConstantObjects.countryList.filter {
//            it.id == ConstantObjects.defaltCountry }.let { if (it.size > 0) {
//                it.get(0).run{
//                    selectedCountryText = SearchListItem(id,name)
//                    select_country._setStartIconImage(flagimglink)
//                    select_country.text=it.get(0).name
//                }
//            }
//        }

    }

    private fun setupRegisterViewModel() {
        signupViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        signupViewModel.isLoading.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })
        signupViewModel.isNetworkFail.observe(this, Observer {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        })
        signupViewModel.errorResponseObserver.observe(this, Observer {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(
                    it.message,
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        })
        signupViewModel.registerRespObserver.observe(this) { registerResp ->
            if (registerResp.status_code == 200) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.Accounthasbeencreated),
                    this@SignupCreateNewUser
                )
                signInAfterSignUp()
            } else {
                showError(getString(R.string.serverError))
            }

        }
    }

    /***clickEvents*/
    private fun setupClickListeners() {
        date._setOnClickListener {
            DatePickerFragment(true, false) { selectdate_ ->
                date.text = "$selectdate_ "

            }.show(supportFragmentManager, "")

        }
        countryContainer._setOnClickListener {
            openCountryDialog()
        }
        regionContainer._setOnClickListener {
            if (selectedCountryId == 0) {
                showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else {
                openRegionDialog()
            }

        }
        neighborhoodContainer._setOnClickListener {
            if (selectedRegionId == 0) {
                showError(getString(R.string.Please_select, getString(R.string.Region)))
            } else {
                openNeighborhoodDialog()
            }
        }
        radiomale._setOnClickListener {
            radiomale._setCheck(!radiomale.getCheck())
            radiofemale._setCheck(false)
            gender_ = Constants.male
        }
        radiofemale._setOnClickListener {
            radiofemale._setCheck(!radiofemale.getCheck())
            radiomale._setCheck(false)
            gender_ = Constants.female

        }

        confirm_button.setOnClickListener {
            if (isValid()) {
                callCreateUser()
            }
        }
    }


    /**countries , region and Neighborhood Dialogs**/
    private fun openCountryDialog() {
        val countryDialog = CountryDialog(this, object : CountryDialog.GetSelectedCountry {
            override fun onSelectedCountry(id: Int, countryName: String, countryFlag: String?) {
                /**setCountryData*/
                selectedCountryId = id
                countryContainer.text = countryName.toString()
                countryContainer._setStartIconImage(countryFlag)
                /**resetRegion*/
                selectedRegionId = 0
                regionContainer.text = null
                /**resetRegion*/
                selectedNeighborhoodId = 0
                neighborhoodContainer.text = null
            }
        })
        countryDialog.show()
    }

    private fun openRegionDialog() {
        val regionDialog =
            RegionDialog(this, selectedCountryId, object : RegionDialog.GetSelectedRegion {
                override fun onSelectedRegion(id: Int, regionName: String) {
                    /**setRegionData*/
                    selectedRegionId = id
                    regionContainer.text = regionName.toString()
                    /**resetNeighborhood*/
                    selectedNeighborhoodId = 0
                    neighborhoodContainer.text = null
                }
            })
        regionDialog.show()
    }

    private fun openNeighborhoodDialog() {
        val neighborhoodDialog = NeighborhoodDialog(
            this,
            selectedRegionId,
            object : NeighborhoodDialog.GetSelectedNeighborhood {
                override fun onSelectedNeighborhood(id: Int, neighborhoodName: String) {
                    /**setNeighborhoodData*/
                    selectedNeighborhoodId = id
                    neighborhoodContainer.text = neighborhoodName.toString()
                }
            })
        neighborhoodDialog.show()
    }

    /**validation and createUseer*/


    private fun isValid(): Boolean {
        if (firstName!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.First_Name)))
            return false
        }

        if (editTextlastname!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.Last_Name)))
            return false
        }

        if (date!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.Date_of_Birth)))
            return false
        }


        if (gender_ == -1) {
            showError(getString(R.string.Please_select, getString(R.string.Gender)))
            return false
        }

        if (selectedCountryId == 0) {
            showError(getString(R.string.Please_select, getString(R.string.Country)))
            return false
        }

//        if (selectedRegionId == 0) {
//            showError(getString(R.string.Please_select, getString(R.string.Region)))
//            return false
//        }
//
//        if (selectedNeighborhoodId == 0) {
//            showError(getString(R.string.Please_select, getString(R.string.district)))
//            return false
//        }

//
//        if (area!!.text.toString().isEmpty()) {
//            showError(getString(R.string.Please_enter, getString(R.string.Area)))
//            return false
//        }
//        if (streetNUmber!!.text.toString().isEmpty()) {
//            showError(getString(R.string.Please_enter, getString(R.string.StreetNumber)))
//            return false
//        }
//        if (county_code!!.text.toString().isEmpty()) {
//            showError(getString(R.string.Please_enter, getString(R.string.ZipCode)))
//            return false
//        }
        return true
    }

    private fun callCreateUser() {
        var invitationCode = "";
        otpData?.let {
            invitationCode = it.invitationCode;
        }
        signupViewModel.createUser(
            otpData?.userName.toString(),
            otpData?.phoneNumber.toString(),
            otpData?.userEmail.toString(),
            otpData?.userPass.toString(),
            invitationCode,
            firstName.text.toString().trim(),
            editTextlastname.text.toString().trim(),
            date.text.toString().trim(),
            gender_.toString(),
            selectedCountryId.toString(),
            selectedRegionId.toString(),
            selectedNeighborhoodId.toString(),
            area.text.toString(),
            streetNUmber.text.toString(),
            county_code.text.toString(),
            otpData?.isBusinessAccount.toString(),
            Lingver.getInstance().getLanguage(),
            HelpFunctions.projectName,
            HelpFunctions.deviceType,
            getDeviceId()
        )
    }


    fun signInAfterSignUp() {
        val intentsignin = Intent(this, SignInActivity::class.java)
        intentsignin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentsignin)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@SignupCreateNewUser, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
//    fun apicallcreateuser() {
//        HelpFunctions.startProgressBar(this)
//        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        var invitationCode="";
//        otpData?.let {
//            invitationCode=it.invitationCode;
//        }
//        var selectedRegionId=0
//        selectedRegion?.let {
//            selectedRegionId=it.id
//        }
//        var selectedCountryId=0
//        selectedCountryText?.let {
//            selectedCountryId=it.id
//        }
//        var cityId=0
//        selectedCity?.let {
//            cityId=it.id
//        }
//
//        val call: Call<RegisterResp> = malqaa.createuser2(
//            otpData?.userName.toString().requestBody(),
//            otpData?.phoneNumber.toString().requestBody(),
//            otpData?.userEmail.toString().requestBody(),
//            otpData?.userPass.toString().requestBody(),
//            invitationCode.requestBody(),
//            firstName.text.toString().trim().requestBody(),
//            editTextlastname.text.toString().trim().requestBody(),
//            date.text.toString().trim().requestBody(),
//            gender_.toString().requestBody(),
//            selectedCountryId.toString().requestBody(),
//            selectedRegionId.toString().requestBody(),
//            cityId.toString().requestBody(),
//            area.text.toString().requestBody(),
//            streetNUmber.text.toString().requestBody(),
//            county_code.text.toString().requestBody(),
//            otpData?.isBusinessAccount.toString().requestBody(),
//            Lingver.getInstance().getLanguage().requestBody(),
//            HelpFunctions.projectName.requestBody(),
//            HelpFunctions.deviceType.requestBody(),
//            getDeviceId().requestBody(),
//        )
//
//        call.enqueue(object : Callback<RegisterResp> {
//
//            override fun onResponse(
//                call: Call<RegisterResp>, response: Response<RegisterResp>
//            ) {
//                if (response.isSuccessful) {
//                    val data=response.body()
//                    if (data!!.status_code == 200) {
//                        HelpFunctions.ShowLongToast(
//                            getString(R.string.Accounthasbeencreated),
//                            this@SignupPg3
//                        )
//                        signInAfterSignUp()
//                    } else {
//                        if(data.message!=null){
//                            showError(data.message)
//                        }else{
//                            showError(response.message())
//
//                        }
//                    }
//                } else {
//                    HelpFunctions.ShowLongToast(response.toString(), this@SignupPg3)
//                }
//                HelpFunctions.dismissProgressBar()
//
//            }
//
//            override fun onFailure(call: Call<RegisterResp>, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg3) }
//            }
//        })
//
//    }


//    fun updateapicall() {
//        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val userId = intent.getStringExtra("useridupdate")
//        val firstName = firstName.text.toString().trim()
//        val lastName = editTextlastname.text.toString().trim()
//        val country = select_country.text.toString()
//        val areaa = Camera.Area.text.toString().trim()
//
//
//
//        val call: Call<User> = malqaa.updateUserSiginup(
//            User(
//                id = userId,
//                gender = gender_.toString(),
//                firstName = firstName,
//                intDoBDay = HelpFunctions.getFormattedDate(date.text.toString(), HelpFunctions.datetimeformat_ddmyyyy, "dd").toInt(), intDoBMonth = HelpFunctions.getFormattedDate(date.text.toString(), HelpFunctions.datetimeformat_ddmyyyy, "MM").toInt(),
//                intDoBYear = HelpFunctions.getFormattedDate(date.text.toString(), HelpFunctions.datetimeformat_ddmyyyy, "yyyy").toInt(),
//                country = country,
//                region = select_region.text.toString(),
//                city = select_city.text.toString(),
//                distric = select_city.text.toString(),
//                area = areaa,
//                zipcode = county_code.text.toString(),
//                lastname = lastName,
//                fullName = "$firstName $lastName",
//                isApproved = 1
//            )
//        )
//
//        call.enqueue(object : Callback<User> {
//            override fun onResponse(
//                call: Call<User>, response: Response<User>
//            ) {
//                if (response.isSuccessful) {
//                    HelpFunctions.ShowLongToast(
//                        getString(R.string.Accounthasbeencreated),
//                        this@SignupPg3
//                    )
//                    signInAfterSignUp()
//                } else {
//                    HelpFunctions.ShowLongToast(getString(R.string.NoResponse), this@SignupPg3)
//
//                }
//            }
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                Toast.makeText(this@SignupPg3, t.message, Toast.LENGTH_LONG).show()
//            }
//        })
//    }


}
