package com.malka.androidappp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.feedbacks.FeedbackObject
import com.malka.androidappp.newPhase.domain.models.servicemodels.feedbacks.FeedbackProperties
import com.malka.androidappp.newPhase.domain.models.servicemodels.user.UserObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.frag_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileNotFoundException
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProfileFrag : Fragment() {

    //Total Number of Images to pickup
    val imagCount = 1

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;

        private val PERMISSION_CODE = 1001;

        //        var profileImage: MutableList<String>? = null
        var profileImage: String? = null
    }

    lateinit var textName: TextView
    lateinit var textLastName: TextView
    lateinit var textFirstName: TextView
    lateinit var textDateOfBirth: TextView
    lateinit var textGender: TextView
    lateinit var textMobile: TextView
    lateinit var textEmail: TextView
    lateinit var textAlternateEmail: TextView
    lateinit var textBusinessName: TextView
    lateinit var textUserName: TextView
    lateinit var textBusinessNumber: TextView
    lateinit var textCountry: TextView
    lateinit var textRegistrationNumber: TextView
    lateinit var textExpiryDate: TextView
    lateinit var textSubUser: TextView
    lateinit var textGoogleText: TextView
    lateinit var textPrimaryContact: TextView
    lateinit var textLandLine: TextView
    lateinit var textRegion: TextView
    lateinit var textCity: TextView
    lateinit var textZipCode: TextView
    lateinit var textAddress: TextView
    lateinit var textFeedbackPercentage: TextView
    lateinit var textMemberNumber: TextView
    lateinit var textMemberSinceDate: TextView
    lateinit var imageUser: ImageView

    lateinit var positiveCounter: TextView
    lateinit var neutralCounter: TextView
    lateinit var negativeCounter: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        HelpFunctions.startProgressBar(requireActivity())
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_profile, container, false)
    }


    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        textName = requireActivity().findViewById(R.id.txt_name)
        textLastName = requireActivity().findViewById(R.id.txt_lname)
        textFirstName = requireActivity().findViewById(R.id.txt_Fname)
        textDateOfBirth = requireActivity().findViewById(R.id.txt_dob)
        textGender = requireActivity().findViewById(R.id.txt_gender)
        textMobile = requireActivity().findViewById(R.id.txt_mobile)
        textEmail = requireActivity().findViewById(R.id.email_text)
        textAlternateEmail = requireActivity().findViewById(R.id.alternate_email_text)
        textBusinessName = requireActivity().findViewById(R.id.business_name_text)
        textUserName = requireActivity().findViewById(R.id.username_text)
        textBusinessNumber = requireActivity().findViewById(R.id.business_number_text)
        textCountry = requireActivity().findViewById(R.id.country_text)
        textRegistrationNumber = requireActivity().findViewById(R.id.registration_number_text)
        textExpiryDate = requireActivity().findViewById(R.id.expiry_date_text)
        textSubUser = requireActivity().findViewById(R.id.sub_user_text)
        textGoogleText = requireActivity().findViewById(R.id.google_text)
        textPrimaryContact = requireActivity().findViewById(R.id.primary_contact_text)
        textLandLine = requireActivity().findViewById(R.id.txt_landline)
        textRegion = requireActivity().findViewById(R.id.region_text)
        textCity = requireActivity().findViewById(R.id.city_text)
        textZipCode = requireActivity().findViewById(R.id.zip_code_text)
        textAddress = requireActivity().findViewById(R.id.address_text)
        textFeedbackPercentage = requireActivity().findViewById(R.id.txt_feedback_percentage)
        textMemberNumber = requireActivity().findViewById(R.id.txt_membrnum)
        textMemberSinceDate = requireActivity().findViewById(R.id.txt_membrsince_date)
        imageUser = requireActivity().findViewById(R.id.img_user)


        positiveCounter = requireActivity().findViewById(R.id.lbl_positive_count)
        neutralCounter = requireActivity().findViewById(R.id.lbl_neutral_count)
        negativeCounter = requireActivity().findViewById(R.id.lbl_negative_count)


        ///////////////////Data parse from activity to fragment//////////Method1 Simple///////////////

       
        //Date: 11/04/2020
        GetUserInfo(ConstantObjects.logged_userid);

       
        //Date: 11/13/2020
        GetUserFeedBack(ConstantObjects.logged_userid)

        toolbar_profile.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_profile.title = getString(R.string.Profile)
        toolbar_profile.navigationIcon?.isAutoMirrored = true
        toolbar_profile.setTitleTextColor(Color.WHITE)
        toolbar_profile.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        imageView16.setOnClickListener() {
            findNavController().navigate(R.id.profile_editdetail)
        }
        imageButton2.setOnClickListener() {
            SharedPreferencesStaticClass.myFeedback = true
            findNavController().navigate(R.id.profile_feedback)
        }

        img_user.setOnClickListener() {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionChecker.checkSelfPermission(
                        this@ProfileFrag.requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, ProfileFrag.PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        userType()
    }

   
    //Date: 11/04/2020
    private fun GetUserInfo(userid: String) {
        try {
            val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
            val call: Call<UserObject> = malqa.getuser(userid)
            call.enqueue(object : Callback<UserObject> {
                @SuppressLint("NewApi")
                override fun onResponse(call: Call<UserObject>, response: Response<UserObject>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            var userinfo: UserObject = response.body()!!;
                            if (userinfo != null) {
                                ConstantObjects.userobj = userinfo.data;
                                textName.text = userinfo.data.fullName
                                textLastName.text = userinfo.data.lastname
                                textFirstName.text = userinfo.data.fullName
                                if (userinfo.data.image != null) {
                                    Picasso.get()
                                        .load(Constants.IMAGE_URL + userinfo.data.image)
                                        .into(imageUser)
                                } else {
                                    imageUser.setImageResource(R.drawable.profilepic)
                                }

                                var simpleFormat =
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                                var convertedDate =
                                    if (userinfo.data.dateOfBirth != null) LocalDate.parse(
                                        userinfo.data.dateOfBirth,
                                        simpleFormat
                                    ) else LocalDate.parse("1900-01-01T12:00:00", simpleFormat)
                                val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
                                textDateOfBirth.text = convertedDate.format(formatter)
                                textGender.text = userinfo.data.gender
                                textMobile.text = userinfo.data.phone
                                textEmail.text = userinfo.data.email
                                textAlternateEmail.text = "ssaeed@gmail.com"
                                textUserName.text = userinfo.data.userName
                                textBusinessName.text = "Auckland"
                                textBusinessNumber.text = "09 0000 00000"
                                textCountry.text = userinfo.data.country
                                textRegistrationNumber.text = "09 0000 00000"
                                textExpiryDate.text = "20/5/2020"
                                textSubUser.text = "Salman"
                                textGoogleText.text = "https://maps.googleapis.com/maps/"
                                textPrimaryContact.text = "09 0000 00000"
                                textLandLine.text = userinfo.data.phone
                                textRegion.text = userinfo.data.region
                                textCity.text = userinfo.data.city
                                textZipCode.text = userinfo.data.zipcode
                                textAddress.text = userinfo.data.address
//                                txt_location.setText(userinfo.data.country)
                                textFeedbackPercentage.text = "100% ${getString(R.string.positive)}"
                                textMemberNumber.text = "000-00-00"
                                textMemberSinceDate.text = "01/01/1900"
                            }
                        }
                    } else {
                        HelpFunctions.ShowLongToast(
                            getString(R.string.NoRecordFound),
                            this@ProfileFrag.context
                        );
                    }
                }

                override fun onFailure(call: Call<UserObject>, t: Throwable) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.NoRecordFound),
                        this@ProfileFrag.context
                    );
                }
            })
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

   
    //Date: 11/13/2020
    private fun GetUserFeedBack(userid: String) {
        try {
            val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
            val call: Call<FeedbackObject> = malqa.getuserfeedback(userid)
            call.enqueue(object : Callback<FeedbackObject> {
                override fun onFailure(call: Call<FeedbackObject>, t: Throwable) {
                    HelpFunctions.dismissProgressBar()
//                    Toast.makeText(context, "No Feedback Found", Toast.LENGTH_SHORT).show()
                    HelpFunctions.ShowLongToast(getString(R.string.NoRecordFound), context)
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<FeedbackObject>,
                    response: Response<FeedbackObject>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val resp: FeedbackObject = response.body()!!
                            if (resp != null) {
                                var lists: List<FeedbackProperties> = resp.data
                                try {
                                    if (lists != null && lists.count() > 0) {
                                        resp.Buying = mutableListOf()
                                        resp.Selling = mutableListOf()
                                        resp.negative_count = 0;
                                        resp.neutral_count = 0;
                                        resp.positive_count = 0;

                                        for (IndFeedback in lists) {
                                            if (IndFeedback.rating != null) {
                                                if (IndFeedback.rating == 0) {
                                                    resp.neutral_count += 1;
                                                } else if (IndFeedback.rating == 2) {
                                                    resp.positive_count += 1;
                                                } else if (IndFeedback.rating == 1) {
                                                    resp.negative_count += 1;
                                                }
                                            }
                                            if (IndFeedback.sellerId.trim().toUpperCase()
                                                    .equals(userid.toUpperCase().trim())
                                            ) {
                                                resp.Selling.add(IndFeedback)
                                            } else {
                                                resp.Buying.add(IndFeedback)
                                            }
                                        }

                                        ConstantObjects.userfeedback = resp
                                        if (ConstantObjects.userfeedback != null) {

//                                            neutralCounter.text =
//                                                ConstantObjects.userfeedback!!.neutral_count.toString() + " Neutral(s)"
//                                            negativeCounter.text =
//                                                ConstantObjects.userfeedback!!.negative_count.toString() + " Negative(s)"
//                                            positiveCounter.text =
//                                                ConstantObjects.userfeedback!!.positive_count.toString() + " Positive(s)"

                                            neutralCounter.text =
                                                ConstantObjects.userfeedback!!.neutral_count.toString() + " " + getString(
                                                    R.string.neutrals
                                                )
                                            negativeCounter.text =
                                                ConstantObjects.userfeedback!!.negative_count.toString() + " " + getString(
                                                    R.string.negatives
                                                )
                                            positiveCounter.text =
                                                ConstantObjects.userfeedback!!.positive_count.toString() + " " + getString(
                                                    R.string.positive
                                                )
                                        }
                                        HelpFunctions.dismissProgressBar()

                                    } else {
                                        HelpFunctions.dismissProgressBar()
                                        neutralCounter.text = "0 " + getString(R.string.neutrals)
                                        negativeCounter.text = "0 " + getString(R.string.negatives)
                                        positiveCounter.text = "0 " + getString(R.string.positive)

                                        if (context != null) {
//                                            Toast.makeText(
//                                                context,
//                                                "No Feedback Found",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
                                            HelpFunctions.ShowLongToast(
                                                getString(R.string.NoRecordFound),
                                                context
                                            )
                                        }
                                    }
                                } catch (ex: NullPointerException) {
                                    HelpFunctions.ReportError(ex)
                                }
                            } else {
                                HelpFunctions.dismissProgressBar()
//                                Toast.makeText(context, "No Feedback Found", Toast.LENGTH_SHORT)
//                                    .show()
                                HelpFunctions.ShowLongToast(
                                    getString(R.string.NoRecordFound),
                                    context
                                )
                            }
                        }
                    } else {
                        HelpFunctions.dismissProgressBar()
//                        Toast.makeText(context, "No Feedback Found", Toast.LENGTH_SHORT).show()
                        HelpFunctions.ShowLongToast(
                            getString(R.string.NoRecordFound),
                            context
                        )
                    }
                }
            })
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    private fun userType() {

        // Attributes of Single User
        val landLineLabel: TextView = requireActivity().findViewById(R.id.textView34)
        val landLineText: TextView = requireActivity().findViewById(R.id.txt_landline)

        val regionLabel: TextView = requireActivity().findViewById(R.id.region_label)
        val regionText: TextView = requireActivity().findViewById(R.id.region_text)

        val zipCodeLabel: TextView = requireActivity().findViewById(R.id.zip_code_label)
        val zipCodeText: TextView = requireActivity().findViewById(R.id.zip_code_text)

        val cityLabel: TextView = requireActivity().findViewById(R.id.city_label)
        val cityText: TextView = requireActivity().findViewById(R.id.city_text)

        val addressLabel: TextView = requireActivity().findViewById(R.id.address_label)
        val addressText: TextView = requireActivity().findViewById(R.id.address_text)

        // Attributes of Business User

        val emailLabel: TextView = requireActivity().findViewById(R.id.email_label)
        val emailText: TextView = requireActivity().findViewById(R.id.email_text)

        val usernameLabel: TextView = requireActivity().findViewById(R.id.username_label)
        val usernameText: TextView = requireActivity().findViewById(R.id.username_text)

        val businessNameLabel: TextView = requireActivity().findViewById(R.id.business_name_label)
        val businessNameText: TextView = requireActivity().findViewById(R.id.business_name_text)

        val businessNumberLabel: TextView =
            requireActivity().findViewById(R.id.business_number_label)
        val businessNumberText: TextView = requireActivity().findViewById(R.id.business_number_text)

        val registrationNumberLabel: TextView =
            requireActivity().findViewById(R.id.registration_number_label)
        val registrationNumberText: TextView =
            requireActivity().findViewById(R.id.registration_number_text)

        val subUserLabel: TextView = requireActivity().findViewById(R.id.sub_user_label)
        val subUserText: TextView = requireActivity().findViewById(R.id.sub_user_text)

        val googleLabel: TextView = requireActivity().findViewById(R.id.google_label)
        val googleText: TextView = requireActivity().findViewById(R.id.google_text)

        val primaryContactLabel: TextView =
            requireActivity().findViewById(R.id.primary_contact_label)
        val primaryContactText: TextView = requireActivity().findViewById(R.id.primary_contact_text)

        val expiryDateLabel: TextView = requireActivity().findViewById(R.id.expiry_date_label)
        val expiryDateText: TextView = requireActivity().findViewById(R.id.expiry_date_text)

        val alternateEmailLabel: TextView =
            requireActivity().findViewById(R.id.alternate_email_label)
        val alternateEmailText: TextView = requireActivity().findViewById(R.id.alternate_email_text)


        if (!ConstantObjects.isBusinessUser) {
            landLineLabel.visibility = View.VISIBLE
            landLineText.visibility = View.VISIBLE

            regionLabel.visibility = View.VISIBLE
            regionText.visibility = View.VISIBLE

            zipCodeLabel.visibility = View.VISIBLE
            zipCodeText.visibility = View.VISIBLE

            cityLabel.visibility = View.VISIBLE
            cityText.visibility = View.VISIBLE

            addressLabel.visibility = View.VISIBLE
            addressText.visibility = View.VISIBLE
        } else {
            emailLabel.visibility = View.VISIBLE
            emailText.visibility = View.VISIBLE

            usernameLabel.visibility = View.VISIBLE
            usernameText.visibility = View.VISIBLE

            businessNameLabel.visibility = View.VISIBLE
            businessNameText.visibility = View.VISIBLE

            businessNumberLabel.visibility = View.VISIBLE
            businessNumberText.visibility = View.VISIBLE

            registrationNumberLabel.visibility = View.VISIBLE
            registrationNumberText.visibility = View.VISIBLE

            subUserLabel.visibility = View.VISIBLE
            subUserText.visibility = View.VISIBLE

            googleLabel.visibility = View.VISIBLE
            googleText.visibility = View.VISIBLE

            primaryContactLabel.visibility = View.VISIBLE
            primaryContactText.visibility = View.VISIBLE

            expiryDateLabel.visibility = View.VISIBLE
            expiryDateText.visibility = View.VISIBLE

            alternateEmailLabel.visibility = View.VISIBLE
            alternateEmailText.visibility = View.VISIBLE
        }
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ProfileFrag.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    HelpFunctions.ShowLongToast(
                        getString(R.string.PermissionDenied),
                        this@ProfileFrag.context
                    )
                }
            }
        }
    }

    open fun pickImageFromGallery() {
        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, getString(R.string.UpdateProfilePicture)),
                ProfileFrag.IMAGE_PICK_CODE
            )
        } else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, ProfileFrag.IMAGE_PICK_CODE);
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ProfileFrag.IMAGE_PICK_CODE) {

                if (data != null && (data.clipData != null || data.data != null)) {
                    if (data.clipData != null) data.clipData!!.itemCount else 1

                    if (data.clipData != null) {
                        if (data.clipData!!.itemCount <= imagCount) {
                            val mClipData = data.clipData
                            for (i in 0 until data.clipData!!.itemCount) {
                                val item = mClipData!!.getItemAt(i)
                                val uri: Uri = item.uri

                                var base64 = ""
                                var imageStream: InputStream? = null
                                try {
                                    imageStream =
                                        this@ProfileFrag.requireContext().contentResolver
                                            .openInputStream(uri)
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                }
                                val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                                base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!
                                profileImage = base64
                            }
                        } else {
//                            Toast.makeText(
//                                this@ProfileFrag.context,
//                                "Maximum Images Allowed to Select are: " + imagCount,
//                                Toast.LENGTH_LONG
//                            )

                            HelpFunctions.ShowLongToast(
                                getString(
                                    R.string.MaximumImagesAllowedtoSelectare,
                                    imagCount
                                ), this@ProfileFrag.context
                            )
                        }
                    } else if (data != null && data.data != null) {
                        val uri: Uri = data.data!!
                        var base64 = ""
                        var imageStream: InputStream? = null
                        try {
                            imageStream =
                                this@ProfileFrag.requireContext().contentResolver
                                    .openInputStream(uri)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                        val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                        base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!

                        profileImage = base64

                    }
                } else {
//                    Toast.makeText(
//                        this@ProfileFrag.context,
//                        "No Image Selected",
//                        Toast.LENGTH_LONG
//                    )
                    HelpFunctions.ShowLongToast(
                        getString(R.string.NoImageSelected),
                        this@ProfileFrag.context
                    )
                }
                if (ProfileFrag.profileImage != null) {
                    updateProfilePicApiCall()
                }
            }
        }
    }

    private fun updateProfilePicApiCall() {
        try {
            val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
            val resp = profileImage

            val call: Call<UserImageResponseBack> =
                malqaa.userimageupload(ConstantObjects.logged_userid, resp!!)
            call.enqueue(object : Callback<UserImageResponseBack> {

                override fun onResponse(
                    call: Call<UserImageResponseBack>, response: Response<UserImageResponseBack>
                ) {
                    if (response.isSuccessful) {
//                        Toast.makeText(
//                            activity,
//                            "Image Uploaded successfully",
//                            Toast.LENGTH_LONG
//                        ).show()
                        HelpFunctions.ShowLongToast(
                            getString(R.string.ImageUploadedsuccessfully),
                            activity
                        )

                    } else {
//                        Toast.makeText(activity, "Failed in Image Upload", Toast.LENGTH_LONG).show()

                        HelpFunctions.ShowLongToast(
                            getString(R.string.FailedinImageUpload),
                            activity
                        )
                    }
                }

                override fun onFailure(call: Call<UserImageResponseBack>, t: Throwable) {
//                    Toast.makeText(
//                        activity,
//                        t.message + "Failed in Image Upload",
//                        Toast.LENGTH_LONG
//                    ).show()
                    HelpFunctions.ShowLongToast(getString(R.string.FailedinImageUpload), activity)
                }
            })
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }

    }


}

