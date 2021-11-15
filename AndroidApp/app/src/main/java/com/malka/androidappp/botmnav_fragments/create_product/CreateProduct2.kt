package com.malka.androidappp.botmnav_fragments.create_product

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.activities_main.business_signup.BusinessSignupPg3
import com.malka.androidappp.botmnav_fragments.activities_main.business_signup.StaticBusinessRegistration
import com.malka.androidappp.botmnav_fragments.create_ads.AddPhotoFragment
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.helper.HelpFunctions
import kotlinx.android.synthetic.main.activity_business_signup_pg3.*
import kotlinx.android.synthetic.main.fragment_add_photo.*
import kotlinx.android.synthetic.main.fragment_create_product.*
import kotlinx.android.synthetic.main.fragment_create_product.toolbar_product
import kotlinx.android.synthetic.main.fragment_create_product_pg2.*
import kotlinx.android.synthetic.main.fragment_listing_duration.*
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

open class CreateProduct2 : Fragment() {

    //Total Number of Images to pickup
    val imagCount = 10

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.setVisibility(View.GONE)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_product_pg2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_createpg2.setTitle("My Products")
        toolbar_createpg2.setTitleTextColor(Color.WHITE)
        toolbar_createpg2.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_createpg2.setNavigationOnClickListener({
            requireActivity().onBackPressed()
        })

        StaticClassProductCreate.images == null
        /// Upload photo
        btn_uploadphotos.setOnClickListener() {
            openGallery()
        }

        val unwantedGifts: Array<String> =
            arrayOf("gift 1", "gift 2", "gift 3")

        val colorSpinner: Array<String> =
            arrayOf("Green", "Black", "Red")

        val sizeSpinner: Array<String> =
            arrayOf("XL", "L", "S")

        val brandSpinner: Array<String> =
            arrayOf("Brand 1", "Brand 2", "Brand 3")


        /////////////////For Unwanted Gifts Dropdown/Spinner/////////////////////
        val spinner1: Spinner = requireActivity().findViewById(R.id.unwantgiftspinner)
        spinner1.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            unwantedGifts
        )
        /////////////////For Color Dropdown/Spinner/////////////////////
        val spinner2: Spinner = requireActivity().findViewById(R.id.color_spinner)
        spinner2.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            colorSpinner
        )
        /////////////////For Size Dropdown/Spinner/////////////////////
        val spinner3: Spinner = requireActivity().findViewById(R.id.size_spinner)
        spinner3.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            sizeSpinner
        )
        /////////////////For Brand Dropdown/Spinner/////////////////////
        val spinner4: Spinner = requireActivity().findViewById(R.id.brand_spinner)
        spinner4.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            brandSpinner
        )

        btn_createproduct_pg2.setOnClickListener()
        {
            if (checkImageIsEmpty()) {
                return@setOnClickListener
            } else {
                val descPhotoText: String = desc_photo.text.toString()
                StaticClassProductCreate.description = descPhotoText

                val unwantedGift: String = unwantgiftspinner.selectedItem.toString()
                StaticClassProductCreate.unwantedChristmasgift = unwantedGift

                val colorText: String = color_spinner.selectedItem.toString()
                StaticClassProductCreate.color = colorText

                val sizeText: String = size_spinner.selectedItem.toString()
                StaticClassProductCreate.size = sizeText

                val brandText: String = brand_spinner.selectedItem.toString()
                StaticClassProductCreate.ddlbrand = brandText


                findNavController().navigate(R.id.product_page3)
            }

        }

    }


    fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        val uri: Uri = Uri.parse(
            (Environment.getExternalStorageDirectory().getPath()
                    + File.separator).toString() + "myFolder" + File.separator
        )
        intent.setDataAndType(uri, "*/*")
        startActivityForResult(
            Intent.createChooser(intent, "Open folder"),
            CreateProduct2.IMAGE_PICK_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == CreateProduct2.IMAGE_PICK_CODE) {
                if (data != null && (data!!.clipData != null || data.data != null)) {
                    StaticClassProductCreate.images = mutableListOf()
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
                                        this@CreateProduct2.requireContext().getContentResolver()
                                            .openInputStream(uri)
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                }
                                val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                                base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!
                                StaticClassProductCreate.images!!.add(base64)
                            }
                        } else {
                            Toast.makeText(
                                this@CreateProduct2.context,
                                "Maximum Images Allowed to Select are: " + imagCount,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else if (data != null && data!!.data != null) {
                        val uri: Uri = data.data!!
                        var base64 = ""
                        var imageStream: InputStream? = null
                        try {
                            imageStream =
                                this@CreateProduct2.requireContext().getContentResolver()
                                    .openInputStream(uri)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                        val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                        base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!
                        StaticClassProductCreate.images!!.add(base64)
                    }
                } else {
                    Toast.makeText(
                        this@CreateProduct2.context,
                        "No Image Selected",
                        Toast.LENGTH_LONG
                    ).show()
                }
                if (StaticClassProductCreate.images != null && StaticClassProductCreate.images!!.size > 0) {
                    Toast.makeText(
                        this@CreateProduct2.context,
                        "Image has been uploaded",
                        Toast.LENGTH_LONG
                    ).show()
                    btn_uploadphotos.error = null
                }
            }
        }
    }


    private fun checkImageIsEmpty(): Boolean {
        val uploadButton: Button = requireActivity().findViewById(R.id.btn_uploadphotos)
        return if (StaticClassProductCreate.images == null) {
            uploadButton.error = "Choose an image"
            Toast.makeText(
                activity,
                "Choose an image",
                Toast.LENGTH_LONG
            ).show()

            true
        } else {

            false
        }
    }
}


