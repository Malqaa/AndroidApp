package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Filter
import androidx.core.content.PermissionChecker
import androidx.navigation.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.ImageSelectModel
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.add_product_imgs.view.*
import kotlinx.android.synthetic.main.fragment_add_photo.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.io.FileNotFoundException
import java.io.InputStream

class AddPhoto:  BaseActivity()  {

    var Title: String = ""
    var file_name: String = ""

    //Total Number of Images to pickup
    val imagCount = 10

    var finalImageCount: Int = 0



    //store uris of picked images
    private val selectedImagesURI: ArrayList<ImageSelectModel> = ArrayList()

    //current position/index of selected images
    private var imagePosition = 0
    private val IMAGE_PICK_CODE = 1000

    //Permission code
    private val PERMISSION_CODE = 1001




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_photo)



        Title = intent?.getStringExtra("Title").toString()
        file_name = intent?.getStringExtra("file_name")?:"CarCare-en-US".toString()
        
        toolbar_title.text = getString(R.string.item_details)
        back_btn.setOnClickListener {
            finish()
        }

        storePath()


        if (selectedImagesURI.size > 0) {
            //set first image from list to image switcher


            
        }

        butt555.setOnClickListener() {
            if (finalImageCount > 0) {
//                decisionbyCatandSubCat()
                StaticClassAdCreate.video = addvideo.text.toString().trim()

                categoryTemplate()

            } else {
                HelpFunctions.ShowLongToast(getString(R.string.Pleaseaddaphoto), this)
//                Toast.makeText(context, "Please add a photo", Toast.LENGTH_SHORT).show()
            }

        }
        floatingActionButton.setOnClickListener() {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionChecker.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PermissionChecker.PERMISSION_DENIED
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

            addcamera.setOnClickListener(){
                floatingActionButton.performClick()
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
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    HelpFunctions.ShowLongToast("Permission denied", this)
                }
            }
        }
    }


    fun closefragment() {
    }


    fun pickImageFromGallery() {
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
                                selectedImagesURI!!.add(ImageSelectModel(uri))

                                var base64 = ""
                                var imageStream: InputStream? = null
                                try {
                                    imageStream = getContentResolver()
                                            .openInputStream(uri)
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                }
                                val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                                base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!
                                StaticClassAdCreate.images!!.add(base64)
                            }


                            setCategoryAdaptor(selectedImagesURI)
                            //set first image from list to image switcher
                            imagePosition = 0
                            //
                        } else {
//                            Toast.makeText(
//                                this@context,
//                                "Maximum Images Allowed to Select are: " + imagCount,
//                                Toast.LENGTH_LONG
//                            )
                            HelpFunctions.ShowLongToast(
                                getString(R.string.MaximumImagesAllowedtoSelectare, imagCount),
                                this
                            )
                        }
                    } else if (data != null && data!!.data != null) {
                        val uri: Uri = data.data!!

                        //picked single image set image to image switcher
                        selectedImagesURI.add(ImageSelectModel(uri))
                        imagePosition = 0
                        //
                        var base64 = ""
                        var imageStream: InputStream? = null
                        try {
                            imageStream = getContentResolver()
                                    .openInputStream(uri)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                        val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                        base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!
                        StaticClassAdCreate.images!!.add(base64)

                        setCategoryAdaptor(selectedImagesURI)
                    }
                } else {
//                    Toast.makeText(
//                        this@context,
//                        "No Image Selected",
//                        Toast.LENGTH_LONG
//                    )
                    HelpFunctions.ShowLongToast(
                        getString(R.string.NoImageSelected),
                        this
                    )
                }
                if (StaticClassAdCreate.images != null && StaticClassAdCreate.images!!.size > 0) {
                    //decisionbyCatandSubCat()
//                    textImageCount.text = "${selectedImagesURI.size} out of 10 photos"
//                    textImageCount.image = getImage(R.string.outof10photos, selectedImagesURI.size)


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

        startActivity(Intent(this, DynamicTemplate::class.java).apply {
            putExtra("file_name",file_name)
            putExtra("Title", Title)
        })
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


    private fun setCategoryAdaptor(list: List<ImageSelectModel>) {
        category_rcv.adapter = object : GenericListAdapter<ImageSelectModel>(
            R.layout.add_product_imgs,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        add_img_ic.setImageURI(uri)
                        is_select_cb.isChecked=is_select
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }

}