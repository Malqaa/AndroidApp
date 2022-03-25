package com.malka.androidappp.botmnav_fragments.activities_main.business_signup

import android.Manifest
import android.content.Intent
import android.content.LocusId
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.FragmentManager
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate.Companion.video
import com.malka.androidappp.design.Models.BusinessUserModel
import com.malka.androidappp.design.Models.getBusinessRegisterFile
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.helper.widgets.DatePickerFragment
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.addBusinessRegisterFileResponse
import com.malka.androidappp.network.service.addBusinessUserlistReponse
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.ImageSelectModel
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import kotlinx.android.synthetic.main.activity_business_signup_pg2.*
import kotlinx.android.synthetic.main.fragment_add_photo.*


class BusinessAccountCreate : BaseActivity()  {
    var youtube=false
    var twitter=false
    var whatsApp=false
    var snapShot=false
    var businessDocument = ""
    var selectdate = ""
    var fm: FragmentManager? = null
    var isSelectShipping = false

    private val selectedImagesURI: ArrayList<ImageSelectModel> = ArrayList()

    private val IMAGE_PICK_CODE = 1000

    private val PERMISSION_CODE = 1001
    private var mResults: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_signup_pg2)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        busi_signup2_btn.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }


        business_signup2_button.setOnClickListener() {

            if (  validateUserName() && validateCompanyName()  && validateSignupEmail()  && validateCompanyURL() && validateEmployementType() && validateCommercialRegisterNo()
                && validateExpiryDate() && validateTaxNumber() && validateCommercialRegisterDoc() ) {

                if(selectedImagesURI.size==0){
                    showError(getString(R.string.download_the_commercial_registry_file))



                }else{
                    addBusinessUser()

                }
            }

        }

        employment_type.setOnClickListener {

            val list: ArrayList<SearchListItem> = ArrayList()

            list.add(SearchListItem(1, "Commercial Record"))
            list.add(SearchListItem(2, "Free work"))
            list.add(SearchListItem(3, "A favour"))

            employment_type.showSpinner(
                this,
                list,
                getString(R.string.Select, getString(R.string.type_of_employment))
            ) {
                employment_type.text = it.title

            }
        }

        download_the_commercial_registry_file.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionChecker.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PermissionChecker.PERMISSION_DENIED
                ) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    pickImageFromGallery();
                }
            } else {
                pickImageFromGallery();
            }

        }



        date.setOnClickListener {
            fm = supportFragmentManager
            val dateDialog = DatePickerFragment(false, true) { selectdate_ ->
                date.text = "$selectdate_ "
                selectdate = selectdate_

            }
            dateDialog.show(fm!!, "")
        }



        ic_youtube_select.setOnClickListener {
            youtube=!youtube
            if(youtube){
                ic_youtube_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_youtube.show()
            }else{
                ic_youtube_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_youtube.hide()
            }

        }
        ic_twitter_select.setOnClickListener {
            twitter=!twitter
            if(twitter){
                ic_twitter_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_twitter.show()
            }else{
                ic_twitter_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_twitter.hide()
            }

        }
        ic_whatsapp_select.setOnClickListener {
            whatsApp=!whatsApp
            if(whatsApp){
                ic_whatsapp_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_whatsapp.show()
            }else{
                ic_whatsapp_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_whatsapp.hide()
            }

        }
        ic_snapshot_select.setOnClickListener {
            snapShot=!snapShot
            if(snapShot){
                ic_snapshot_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_snapshot.show()
            }else{
                ic_snapshot_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_snapshot.hide()
            }

        }
    }

    fun BthirdPg() {
        val intent = Intent(this@BusinessAccountCreate, BusinessSignupPg3::class.java)
        startActivity(intent)
        if (ConstantObjects.currentLanguage == "en") {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } else {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }



    private fun validateBActualAddress(): Boolean {
        val BactualAdress = textEmaill.text.toString().trim { it <= ' ' }
        return if (BactualAdress.isEmpty()) {
            textEmaill.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textEmaill.error = null
            true
        }
    }






    fun addBusinessUser() {

        HelpFunctions.startProgressBar(this)

        val malqa = RetrofitBuilder.GetRetrofitBuilder()

        val UserNamee = userNamee.text.toString()
        val CompanyName = company_name.text.toString()
        val EmailAddress = textEmaill.text.toString()
        val WebsiteName = busi_signup3_edittext2.text.toString()
        val FacebookLink = Facebook.text.toString()
        val InstagramLink = Instagram.text.toString()
        val youtubeLink = ic_youtube.text.toString()
        val TwitterLink = ic_twitter.text.toString()
        val WhatsappNumber = ic_whatsapp.text.toString()
        val SnapchatLInk = ic_snapshot.text.toString()
        val SelectEmployemntType = employment_type.text.toString()
        val CommercialRegisterNo = commercial_registration_no.text.toString()
        val SelectDate = date.text.toString()
        val TaxNumber = TaxNumber.text.toString()


        val addBusinessUser = BusinessUserModel.getBusinessList(

            businessName = CompanyName,
            businessEmail = EmailAddress,
            businessURL = WebsiteName,
            businessFacebookURI = FacebookLink,
            businessInstagramURI = InstagramLink,
            businessYoutubeURI = youtubeLink,
            businessTwitterURI = TwitterLink,
            businessWatsappNumber = WhatsappNumber,
            businessSnapchatURI = SnapchatLInk,
            businessType = SelectEmployemntType,
            businessRegistrationNumber = CommercialRegisterNo,
            businessRegistrationExpiry = "",
            approvedBy = "",
            approvedOn = "",
            businessGoogleLatLng = "",
            businessLogoPath = "",
            businessPhone = "12345678",
            id = 0,
            isApproved = true,
            userId = ConstantObjects.logged_userid

        )

       val businessRegistrationExpiry = HelpFunctions.FormatDateTime(
            SelectDate,
            HelpFunctions.datetimeformat_ddmmmyyyy,
            HelpFunctions.datetimeformat_24hrs_milliseconds
        )

        addBusinessUser.businessRegistrationExpiry = businessRegistrationExpiry
        val call: retrofit2.Call<addBusinessUserlistReponse> = malqa.addBusinesUser(addBusinessUser)

        call?.enqueue(object : retrofit2.Callback<addBusinessUserlistReponse?> {
            override fun onFailure(
                call: retrofit2.Call<addBusinessUserlistReponse?>?,
                t: Throwable
            ) {
                HelpFunctions.dismissProgressBar()

                Toast.makeText(this@BusinessAccountCreate, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: retrofit2.Call<addBusinessUserlistReponse?>,
                response: retrofit2.Response<addBusinessUserlistReponse?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val respone: addBusinessUserlistReponse = response.body()!!

                        if(!respone.isError){
                            addBusinessRegisterFile(respone.id)

                        }else{

                            Toast.makeText(
                                this@BusinessAccountCreate,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })

    }









    //Email Validation
    private fun validateSignupEmail(): Boolean {
        val emailInput =
            textEmaill!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {

            showError(getString(R.string.Please_enter,getString(R.string.Email)))
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            showError(getString(R.string.Pleaseenteravalidemailaddress))
            false
        } else {

            true
        }
    }

    //CompanyName Validation
    private fun validateUserName(): Boolean {
        val emailInput =
            userNamee!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_enter,getString(R.string.Username)))
            false
        } else {
            userNamee!!.error = null
            true
        }
    }


    //CompanyName Validation
    private fun validateCompanyName(): Boolean {
        val emailInput =
            company_name!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_enter,getString(R.string.the_company_s_name)))
            false
        } else {
            true
        }
    }



    //WebsiteURL Validation
    private fun validateCompanyURL(): Boolean {
        val emailInput =
            busi_signup3_edittext2!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_enter,getString(R.string.website)))
            false
        } else {
           true
        }
    }


    //EmployementType Validation
    private fun validateEmployementType(): Boolean {
        val emailInput =
            employment_type!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_select,getString(R.string.type_of_employment)))
            false
        } else {
            true
        }
    }


    //CommercialRegisterNo Validation
    private fun validateCommercialRegisterNo(): Boolean {
        val emailInput =
            commercial_registration_no!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_enter,getString(R.string.commercial_registration_no)))
            false
        } else {
            true
        }
    }



    //ExpiryDate Validation
    private fun validateExpiryDate(): Boolean {
        val emailInput =
            date!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_select,getString(R.string.expiry_date)))
            false
        } else {
            true
        }
    }


    private fun GO() {
        selectedImagesURI.filter {
            it.is_main==true
        }.let {
            if (it.size > 0) {
                StaticClassAdCreate.video = addvideo.text.toString()
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.mark_main_photo), this)
            }
        }
    }

    //TaxNumber Validation
    private fun validateTaxNumber(): Boolean {
        val emailInput =
            TaxNumber!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_select,getString(R.string.expiry_date)))
            false
        } else {
            true
        }
    }

    //CommercialRegisterDoc Validation
    private fun validateCommercialRegisterDoc(): Boolean {
        val emailInput =
            download_the_commercial_registry_file!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please,getString(R.string.download_the_commercial_registry_file)))
            false
        } else {
            true
        }
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_CODE ) {
                mResults = data!!.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS)!!;
                selectedImagesURI.clear()
                mResults.forEach {
                    try {
                        val uri: Uri = Uri.parse(it)
                        businessDocument = HelpFunctions.encodeImage(it)!!
                        selectedImagesURI.add(ImageSelectModel(uri, businessDocument))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            }
        }

    }


    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    HelpFunctions.ShowLongToast("Permission denied", this)
                }
            }
        }
    }





    fun pickImageFromGallery() {
        val intent = Intent(this, ImagesSelectorActivity::class.java)
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1)
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false)
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults)

        startActivityForResult(intent, IMAGE_PICK_CODE)
    }






    fun addBusinessRegisterFile(businessId: Int) {

        HelpFunctions.startProgressBar(this)

        val malqa = RetrofitBuilder.GetRetrofitBuilder()

        val addBusinessDocumentFile = getBusinessRegisterFile.GetDocuments(

            isActive = true,
            documentName = businessDocument,
            uploadedOn = "2022-03-23T09:15:57.596Z",
            createdBy = "",
            businessId = businessId,

        )
        val call: retrofit2.Call<addBusinessRegisterFileResponse> = malqa.addBusinessRegisterFile(addBusinessDocumentFile)

        call?.enqueue(object : retrofit2.Callback<addBusinessRegisterFileResponse?> {
            override fun onFailure(
                call: retrofit2.Call<addBusinessRegisterFileResponse?>?,
                t: Throwable
            ) {
                HelpFunctions.dismissProgressBar()

                Toast.makeText(this@BusinessAccountCreate, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: retrofit2.Call<addBusinessRegisterFileResponse?>,
                response: retrofit2.Response<addBusinessRegisterFileResponse?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val respone: addBusinessRegisterFileResponse = response.body()!!

                        if(!respone.isError){

                            finish()

                            Toast.makeText(
                                this@BusinessAccountCreate,
                                respone.message,
                                Toast.LENGTH_LONG

                            ).show()

                        }else{

                            Toast.makeText(
                                this@BusinessAccountCreate,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })

    }





}