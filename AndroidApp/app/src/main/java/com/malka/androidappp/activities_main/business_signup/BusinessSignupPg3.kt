package com.malka.androidappp.botmnav_fragments.activities_main.business_signup

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.activity_business_signup_pg3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

class BusinessSignupPg3 : AppCompatActivity() {

    //Total Number of Images to pickup
    val imagCount = 10

    lateinit var fileText: TextView

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_signup_pg3)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        fileText = findViewById(R.id.textViewFile)

        if (StaticBusinessRegistration.fileUpload != null) {
            fileText.text = getString(R.string.Fileuploaded)
        }

        ///////////Expiry date Calender EditText////////////////////////////////////////////////////////////////////
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        /////////////////////////////// Expiry Date////////////////////////////////////////////////////////
        busi_signup3_edittext2.setOnClickListener() {
            hidekeyboard()
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    val monthplus: Int = mMonth + 1
                    busi_signup3_edittext2.setText("" + mYear + "-" + monthplus + "-" + mDay)
                },
                year,
                month,
                day
            )
            dpd.show()

        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        busi_signup3_btn.setOnClickListener() {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        business_signup3_button.setOnClickListener() {
            if (StaticBusinessRegistration.fileUpload != null) {
                confirmInputtbpg3()
            } else {
//                Toast.makeText(applicationContext, "Please upload a file", Toast.LENGTH_SHORT)
//                    .show()
                HelpFunctions.ShowLongToast(
                    getString(R.string.Pleaseuploadafile),
                    applicationContext
                )
            }
        }

        upload_btn.setOnClickListener() {
            openGallery()
        }
    }


    fun switchFinish() {
        val intent = Intent(this@BusinessSignupPg3, SignInActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finishAffinity()
    }

    fun confirmInputtbpg3() {
        if (!validateBRegistrationnNum() or !validateBExpirydate() or !validateBGooglemapInte() or !validatePrimaryContactInfo()) {
            return
        } else {
            busiUserRegistrationApi()
            //switchFinish()
        }
    }


    private fun validateBRegistrationnNum(): Boolean {
        val BRegNum = busi_signup3_edittext1.text.toString().trim { it <= ' ' }
        return if (BRegNum.isEmpty()) {
            busi_signup3_edittext1.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            busi_signup3_edittext1.error = null
            true
        }
    }

    private fun validateBExpirydate(): Boolean {
        val BExpDate = busi_signup3_edittext2.text.toString().trim { it <= ' ' }
        return if (BExpDate.isEmpty()) {
            busi_signup3_edittext2.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            busi_signup3_edittext2.error = null
            true
        }
    }

    private fun validateBGooglemapInte(): Boolean {
        val BmapInteg = busi_signup3_edittext4.text.toString().trim { it <= ' ' }
        return if (BmapInteg.isEmpty()) {
            busi_signup3_edittext4.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            busi_signup3_edittext4.error = null
            true
        }
    }

    private fun validatePrimaryContactInfo(): Boolean {
        val pContactInfo = busi_signup3_edittext5.text.toString().trim { it <= ' ' }
        return if (pContactInfo.isEmpty()) {
            busi_signup3_edittext5.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            busi_signup3_edittext5.error = null
            true
        }
    }

    fun hidekeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

    }

    /////////////////////////////////////////After Opening File Explorer - Uploading File//////////////////////////////////////////////////
    fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        val uri: Uri = Uri.parse(
            (Environment.getExternalStorageDirectory().path
                    + File.separator).toString() + "myFolder" + File.separator
        )
        intent.setDataAndType(uri, "*/*")
        startActivityForResult(Intent.createChooser(intent, "Open folder"), IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CODE) {
                if (data != null && (data!!.clipData != null || data.data != null)) {
                    StaticBusinessRegistration.fileUpload = mutableListOf()
                    if (data!!.clipData != null) {
                        if (data!!.clipData!!.itemCount <= imagCount) {
                            val mClipData = data!!.clipData
                            for (i in 0 until data!!.clipData!!.itemCount) {
                                val item = mClipData!!.getItemAt(i)
                                val uri: Uri = item.uri
                                var base64 = ""
                                var imageStream: InputStream? = null
                                try {
                                    imageStream =
                                        this@BusinessSignupPg3.contentResolver
                                            .openInputStream(uri)
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                }
                                val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                                base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!
                                StaticBusinessRegistration.fileUpload!!.add(base64)
                            }
                        } else {
//                            Toast.makeText(
//                                applicationContext,
//                                "Maximum Images Allowed to Select are: $imagCount",
//                                Toast.LENGTH_LONG
//                            ).show()
                            HelpFunctions.ShowLongToast(
                                getString(
                                    R.string.MaximumImagesAllowedtoSelectare,
                                    imagCount
                                ), applicationContext
                            )
                        }
                    } else if (data != null && data!!.data != null) {
                        val uri: Uri = data.data!!
                        var base64 = ""
                        var imageStream: InputStream? = null
                        try {
                            imageStream =
                                this@BusinessSignupPg3.contentResolver
                                    .openInputStream(uri)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                        val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                        base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!
                        StaticBusinessRegistration.fileUpload!!.add(base64)
                    }
                } else {
//                    Toast.makeText(
//                        applicationContext,
//                        "No Image Selected",
//                        Toast.LENGTH_LONG
//                    ).show()
                    HelpFunctions.ShowLongToast(
                        getString(R.string.NoImageSelected),
                        applicationContext
                    )
                }
                if (StaticBusinessRegistration.fileUpload != null && StaticBusinessRegistration.fileUpload!!.size > 0) {
                    fileText.text = getString(R.string.Fileuploaded)
//                    Toast.makeText(
//                        applicationContext,
//                        "Image has been uploaded",
//                        Toast.LENGTH_LONG
//                    ).show()
                    HelpFunctions.ShowLongToast(
                        getString(R.string.Imagehasbeenuploaded),
                        applicationContext
                    )
                }
            }
        }
    }

    fun busiUserRegistrationApi() {
        val getRegNum = busi_signup3_edittext1.text.toString().trim()
        StaticBusinessRegistration.RegNum = getRegNum
        val getExpDate = busi_signup3_edittext2.text.toString().trim()
        StaticBusinessRegistration.ExpDate = getExpDate
        val getGmapInte = busi_signup3_edittext4.text.toString().trim()
        StaticBusinessRegistration.GmapInte = getGmapInte
        //
        val getPrimContactInfo = busi_signup3_edittext5.text.toString().trim()
        val busiprimaryContact = cppfield3.selectedCountryCode
        val busiContactnum = "+" + busiprimaryContact + getPrimContactInfo
        StaticBusinessRegistration.PrimContactInfo = busiContactnum
        //
        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val respBusiReg: ModelBusinessRegistration = ModelBusinessRegistration(
            alternateEmail = StaticBusinessRegistration.BEmail2,
            bUsername = StaticBusinessRegistration.Busername,
            BillingAddress = StaticBusinessRegistration.Bbillingadress,
            BusinessName = StaticBusinessRegistration.businessName,
            bussinesPhoneNum = StaticBusinessRegistration.BphoneNo,
            bussinessAddress = StaticBusinessRegistration.bActualadress,
            BussinessDescription = "This is my business from android",
            bussinessExpiryDate = StaticBusinessRegistration.ExpDate,
            country = StaticBusinessRegistration.Bcountryy,
            email = StaticBusinessRegistration.BEmail1,
            fullName = StaticBusinessRegistration.BfirstName,
            googleMapPin = StaticBusinessRegistration.GmapInte,
            lastname = StaticBusinessRegistration.BlastName,
            password = StaticBusinessRegistration.Bpassw,
            phone = StaticBusinessRegistration.BphoneNo,
            regNumber = StaticBusinessRegistration.RegNum,
            username = StaticBusinessRegistration.Busername,
            bussinessWebsite = "www.android.com",
            isApproved = false,
            filepond = StaticBusinessRegistration.fileUpload
        )

        val call: Call<ModelBusinessRegistration> = malqaa.busiRegis(respBusiReg)

        call.enqueue(object : Callback<ModelBusinessRegistration> {
            override fun onResponse(
                call: Call<ModelBusinessRegistration>,
                response: Response<ModelBusinessRegistration>
            ) {
                if (response.isSuccessful) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.YourBusinessAccounthasbeenCreated),
                        this@BusinessSignupPg3
                    )
                    signInAfterBusinessRegistration()
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.ErrorOccur),
                        this@BusinessSignupPg3
                    )
                }
            }

            override fun onFailure(call: Call<ModelBusinessRegistration>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, this@BusinessSignupPg3) }
            }
        })

    }

    fun signInAfterBusinessRegistration() {
        StaticBusinessRegistration.fileUpload = null
        val intentsignin = Intent(this, SignInActivity::class.java)
        intentsignin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentsignin)
    }
}