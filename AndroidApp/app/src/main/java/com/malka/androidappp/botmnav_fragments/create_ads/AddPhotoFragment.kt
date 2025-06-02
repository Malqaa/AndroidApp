package com.malka.androidappp.botmnav_fragments.create_ads

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.URIPathHelper
import kotlinx.android.synthetic.main.fragment_add_photo.*
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream


open class AddPhotoFragment : Fragment() {

    var Title: String = ""
    var file_name: String = ""

    //Total Number of Images to pickup
    val imagCount = 10

    var finalImageCount: Int = 0

    lateinit var videoLink: TextInputEditText

    lateinit var textImageCount: TextView
    lateinit var selectedImageView: ImageSwitcher
    lateinit var selectedImageText: TextView
    lateinit var nextImage: ImageView
    lateinit var previousImage: ImageView
    lateinit var imageInitial: ImageView

    //store uris of picked images
    private val selectedImagesURI: ArrayList<Uri?> = ArrayList()

    //current position/index of selected images
    private var imagePosition = 0

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000

        //Permission code
        private val PERMISSION_CODE = 1001

        fun clearPath() {
            StaticClassAdCreate.subCategoryPath.clear()

            StaticClassAdCreate.subcatone = ""
            StaticClassAdCreate.subcatonekey = ""

            StaticClassAdCreate.subcattwo = ""
            StaticClassAdCreate.subcattwokey = ""

            StaticClassAdCreate.subcatthree = ""
            StaticClassAdCreate.subcatthreekey = ""

            StaticClassAdCreate.subcatfour = ""
            StaticClassAdCreate.subcatfourkey = ""

            StaticClassAdCreate.subcatfive = ""
            StaticClassAdCreate.subcatfivekey = ""

            StaticClassAdCreate.subcatsix = ""
            StaticClassAdCreate.subcatsixkey = ""
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Title = arguments?.getString("Title").toString()
        file_name = arguments?.getString("file_name").toString()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_photo, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_addphoto.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_addphoto.title = getString(R.string.AddPhotos)
        toolbar_addphoto.setTitleTextColor(Color.WHITE)
        toolbar_addphoto.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_addphoto.setNavigationOnClickListener {
            clearPath()
            findNavController().navigate(R.id.back_to_cat)

        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                clearPath()
                findNavController().navigate(R.id.back_to_cat)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

//        selectedImagesURI = ArrayList()

        Toast.makeText(context, StaticClassAdCreate.subCategoryPath.toString(), Toast.LENGTH_LONG)
            .show()

        videoLink = requireActivity().findViewById(R.id.addvideo)

        textImageCount = requireActivity().findViewById(R.id.txv2)
//        textImageCount.text = "$finalImageCount out of 10 photos"
//        textImageCount.text = "${selectedImagesURI.size} out of 10 photos"
        textImageCount.text = getString(R.string.outof10photos, selectedImagesURI.size)

        selectedImageText = requireActivity().findViewById(R.id.selectedImages)
        selectedImageView = requireActivity().findViewById(R.id.imageView3)

        nextImage = requireActivity().findViewById(R.id.forwardSelectedImage)
        previousImage = requireActivity().findViewById(R.id.backSelectedImage)

        //setup image switcher
        selectedImageView.setFactory { ImageView(requireContext()) }
        imageInitial = requireActivity().findViewById(R.id.imageInitial)

        storePath()

        if (selectedImagesURI.size > 0) {
            //set first image from list to image switcher
            selectedImageView.setImageURI(selectedImagesURI[imagePosition])

            nextImage.visibility = View.VISIBLE
            previousImage.visibility = View.VISIBLE
            imageInitial.visibility = View.GONE
            selectedImageText.visibility = View.VISIBLE
        }


        toolbar_addphoto.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_close) {
                findNavController().navigate(R.id.close_addphoto)

                StaticClassAdCreate.subCategoryPath.clear()

                //closefragment()
            } else {
                // do something
            }
            false
        }

        ///////////////////////////////////////////
        butt555.setOnClickListener() {
            if (finalImageCount > 0) {
//                decisionbyCatandSubCat()
                StaticClassAdCreate.video = videoLink.text.toString().trim()

                categoryTemplate()

            } else {
                HelpFunctions.ShowLongToast(getString(R.string.Pleaseaddaphoto), context)
//                Toast.makeText(context, "Please add a photo", Toast.LENGTH_SHORT).show()
            }

        }
        floatingActionButton.setOnClickListener() {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(
                        this@AddPhotoFragment.requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }

        nextImage.setOnClickListener {
            if (imagePosition < selectedImagesURI!!.size - 1) {
                imagePosition++
                selectedImageView.setImageURI(selectedImagesURI!![imagePosition])
            } else {
                //no more images
                HelpFunctions.ShowLongToast(getString(R.string.Nomoreimages), context)
//                Toast.makeText(context, "No more images...", Toast.LENGTH_SHORT).show()
            }
        }
        previousImage.setOnClickListener {
            if (imagePosition > 0) {
                imagePosition--
                selectedImageView.setImageURI(selectedImagesURI!![imagePosition])
            } else {
                //no more images
                HelpFunctions.ShowLongToast(getString(R.string.Nomoreimages), context)
//                Toast.makeText(context, "No more images...", Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun decisionbyCatandSubCat() {
        if (StaticClassAdCreate.template == "Vehicles") {
            vehicleDecidenextbySubcategory()
        } else if (StaticClassAdCreate.template == "Property") {
            propertyDecision()
        } else if (StaticClassAdCreate.template == "General") {
            findNavController().navigate(R.id.addphoto_generalitemdetails)
        }
    }

    fun propertyDecision() {

        if (StaticClassAdCreate.propertySubCatB == "Lands") {
            findNavController().navigate(R.id.addphoto_commercialland)
        } else if (StaticClassAdCreate.propertySubCatB == "Shops") {
            findNavController().navigate(R.id.addphoto_commercialshops)
        } else if (StaticClassAdCreate.propertySubCatB == "Hotels") {
            findNavController().navigate(R.id.addphoto_commercialhotels)
        } else if (StaticClassAdCreate.propertySubCatB == "Factories") {
            findNavController().navigate(R.id.addphoto_commercialfactories)
        } else if (StaticClassAdCreate.propertySubCatB == "Offices") {
            findNavController().navigate(R.id.addphoto_commercialoffices)
        } else if (StaticClassAdCreate.propertySubCatB == "Workshops") {
            findNavController().navigate(R.id.addphoto_commercialworkshops)
        } else if (StaticClassAdCreate.propertySubCatB == "Warehouses") {
            findNavController().navigate(R.id.addphoto_commercialwarehouses)
        } else if (StaticClassAdCreate.propertySubCatB == "Agriculture For Sale") {
            findNavController().navigate(R.id.addphoto_agricultureforsale)
        } else if (StaticClassAdCreate.propertySubCatB == "Agriculture For Rent") {
            findNavController().navigate(R.id.addphoto_agricultureforrent)
        } else if (StaticClassAdCreate.propertySubCatB == "Land") {
            findNavController().navigate(R.id.addphoto_residentialland)
        } else if (StaticClassAdCreate.propertySubCatB == "Villas") {
            findNavController().navigate(R.id.addphoto_residentialvillas)
        } else if (StaticClassAdCreate.propertySubCatB == "Buildings") {
            findNavController().navigate(R.id.addphoto_residentialbuildings)
        } else if (StaticClassAdCreate.propertySubCatB == "Apartments") {
            findNavController().navigate(R.id.addphoto_residentialapartments)
        } else if (StaticClassAdCreate.propertySubCatB == "Holiday House") {
            findNavController().navigate(R.id.addphoto_residentialholidayhouse)
        }


    }

    fun decidenextScreenbyTemplate() {
        if (StaticClassAdCreate.template == "AutoMobile") {
            findNavController().navigate(R.id.addphoto_otherdetailautomobile)
        } else if (StaticClassAdCreate.template == "General") {
            findNavController().navigate(R.id.addphoto_otherlistdeta)
        } else if (StaticClassAdCreate.template == "Property") {
            findNavController().navigate(R.id.addphoto_otherlistproperty)
        }
    }


    fun vehicleDecidenextbySubcategory() {
        if (StaticClassAdCreate.subcat == "Car") {
            findNavController().navigate(R.id.addphoto_caritemdetails)
        } else if (StaticClassAdCreate.subcat == "Bus") {
            findNavController().navigate(R.id.addphoto_busitemdetails)
        } else if (StaticClassAdCreate.subcat == "Motorbike") {
            findNavController().navigate(R.id.addphoto_motorbikeitemdetail)
        } else if (StaticClassAdCreate.subcat == "Trailers") {
            findNavController().navigate(R.id.addphoto_trailers)
        } else if (StaticClassAdCreate.subcat == "Diggers") {
            findNavController().navigate(R.id.addphoto_listingdetail)
        } else if (StaticClassAdCreate.subcat == "Forklifts & pallet movers") {
            findNavController().navigate(R.id.add_photo_forlkliftpallet)
        } else if (StaticClassAdCreate.subcat == "Trucks") {
            findNavController().navigate(R.id.addphoto_truckitemdetail)
        } else if (StaticClassAdCreate.subcat == "Wrecked car") {
            findNavController().navigate(R.id.addphoto_wreckcar)
        }


    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    HelpFunctions.ShowLongToast("Permission denied", this@AddPhotoFragment.context)
                }
            }
        }
    }


    fun closefragment() {
    }


    open fun pickImageFromGallery() {
        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, getString(R.string.ChoosePictures)),
                IMAGE_PICK_CODE
            )
        } else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, IMAGE_PICK_CODE);
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CODE) {

//               var selectedImageUri: Uri? = data!!.data
//                if (null != selectedImageUri) {
//                    // update the preview image in the layout
//                    selectedImageView.setImageURI(selectedImageUri)
//                }

                //StaticClassAdCreate.images.clear()
                if (data != null && (data!!.clipData != null || data.data != null)) {
                    StaticClassAdCreate.images = mutableListOf()
                    val slectdimagecount: Int =
                        if (data!!.clipData != null) data!!.clipData!!.itemCount else 1
                    finalImageCount = slectdimagecount
//                    txv2.text =
//                        slectdimagecount.toString() + " out of " + imagCount + " photos selected"
                    if (data!!.clipData != null) {
                        if (data!!.clipData!!.itemCount <= imagCount) {

                            val mClipData = data!!.clipData

                            for (i in 0 until data!!.clipData!!.itemCount) {
                                val item = mClipData!!.getItemAt(i)
                                val uri: Uri = item.uri

                                // Storing Selected Images URI
                                selectedImagesURI!!.add(uri)

                                var base64 = ""
                                var imageStream: InputStream? = null
                                try {
                                    imageStream =
                                        this@AddPhotoFragment.requireContext().getContentResolver()
                                            .openInputStream(uri)
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                }
                                val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                                base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!
                                StaticClassAdCreate.images!!.add(base64)
                            }
                            //set first image from list to image switcher
                            selectedImageView.setImageURI(selectedImagesURI!![0])
                            imagePosition = 0
                            //
                        } else {
//                            Toast.makeText(
//                                this@AddPhotoFragment.context,
//                                "Maximum Images Allowed to Select are: " + imagCount,
//                                Toast.LENGTH_LONG
//                            )
                            HelpFunctions.ShowLongToast(
                                getString(R.string.MaximumImagesAllowedtoSelectare, imagCount),
                                this@AddPhotoFragment.context
                            )
                        }
                    } else if (data != null && data!!.data != null) {
                        val uri: Uri = data.data!!

                        //picked single image set image to image switcher
                        selectedImagesURI.add(uri)
                        selectedImageView.setImageURI(uri)
                        imagePosition = 0
                        //
                        var base64 = ""
                        var imageStream: InputStream? = null
                        try {
                            imageStream =
                                this@AddPhotoFragment.requireContext().getContentResolver()
                                    .openInputStream(uri)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                        val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                        base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!
                        StaticClassAdCreate.images!!.add(base64)
                    }
                } else {
//                    Toast.makeText(
//                        this@AddPhotoFragment.context,
//                        "No Image Selected",
//                        Toast.LENGTH_LONG
//                    )
                    HelpFunctions.ShowLongToast(
                        getString(R.string.NoImageSelected),
                        this@AddPhotoFragment.context
                    )
                }
                if (StaticClassAdCreate.images != null && StaticClassAdCreate.images!!.size > 0) {
//                    decisionbyCatandSubCat()
//                    textImageCount.text = "${selectedImagesURI.size} out of 10 photos"
                    textImageCount.text = getString(R.string.outof10photos, selectedImagesURI.size)


                    nextImage.visibility = View.VISIBLE
                    previousImage.visibility = View.VISIBLE
                    imageInitial.visibility = View.GONE
                    selectedImageText.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun categoryTemplate() {
        val args = Bundle()
        val file_name = "$file_name.js"
        val Title = Title
        args.putString("file_name", file_name)
        args.putString("Title", Title)
        findNavController().navigate(R.id.fragment_dynamic_template, args)
    }

    private fun storePath() {
        for (i in 0 until StaticClassAdCreate.subCategoryPath.size) {
            if (i == 0) {
                StaticClassAdCreate.subcatone = StaticClassAdCreate.subCategoryPath[0]
                StaticClassAdCreate.subcatonekey = StaticClassAdCreate.subCategoryPath[0] + "-en-US"
            }
            if (i == 1) {
                StaticClassAdCreate.subcattwo = StaticClassAdCreate.subCategoryPath[1]
                StaticClassAdCreate.subcattwokey = StaticClassAdCreate.subCategoryPath[1] + "-en-US"
            }
            if (i == 3) {
                StaticClassAdCreate.subcatthree = StaticClassAdCreate.subCategoryPath[2]
                StaticClassAdCreate.subcatthreekey =
                    StaticClassAdCreate.subCategoryPath[2] + "-en-US"
            }
            if (i == 4) {
                StaticClassAdCreate.subcatfour = StaticClassAdCreate.subCategoryPath[3]
                StaticClassAdCreate.subcatfourkey =
                    StaticClassAdCreate.subCategoryPath[3] + "-en-US"

            }
            if (i == 5) {
                StaticClassAdCreate.subcatfive = StaticClassAdCreate.subCategoryPath[4]
                StaticClassAdCreate.subcatfivekey =
                    StaticClassAdCreate.subCategoryPath[4] + "-en-US"
            }
            if (i == 6) {
                StaticClassAdCreate.subcatsix = StaticClassAdCreate.subCategoryPath[5]
                StaticClassAdCreate.subcatsixkey = StaticClassAdCreate.subCategoryPath[5] + "-en-US"
            }
        }
    }


}