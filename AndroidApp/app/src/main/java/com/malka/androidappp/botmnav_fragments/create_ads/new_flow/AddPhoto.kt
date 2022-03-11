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

class AddPhoto : BaseActivity() {

    var Title: String = ""
    var file_name: String = ""

    val imagCount = 10



    private val selectedImagesURI: ArrayList<ImageSelectModel> = ArrayList()

    private val IMAGE_PICK_CODE = 1000

    private val PERMISSION_CODE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_photo)



        Title = intent?.getStringExtra("Title").toString()
        file_name = intent?.getStringExtra("file_name") ?:""

        toolbar_title.text = getString(R.string.item_details)
        back_btn.setOnClickListener {
            finish()
        }

        storePath()



        butt555.setOnClickListener {
            if(selectedImagesURI.size==0){
                HelpFunctions.ShowLongToast(getString(R.string.Pleaseaddaphoto), this)

            }else{
                selectedImagesURI.filter {
                    it.is_main==true
                }.let {
                    if (it.size > 0) {
                        StaticClassAdCreate.video = addvideo.text.toString().trim()
                        categoryTemplate()
                    } else {
                        HelpFunctions.ShowLongToast(getString(R.string.mark_main_photo), this)
                    }
                }
            }

        }
        floatingActionButton.setOnClickListener() {
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

            addcamera.setOnClickListener {
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
                    pickImageFromGallery()
                } else {
                    HelpFunctions.ShowLongToast("Permission denied", this)
                }
            }
        }
    }




    fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CODE) {
                if (data != null && (data!!.clipData != null || data.data != null)) {

                    if (data!!.clipData != null) {
                        if (data!!.clipData!!.itemCount <= imagCount) {

                            val mClipData = data!!.clipData

                            for (i in 0 until data!!.clipData!!.itemCount) {
                                val item = mClipData!!.getItemAt(i)
                                val uri: Uri = item.uri

                                var imageStream: InputStream? = null
                                try {
                                    imageStream = getContentResolver()
                                        .openInputStream(uri)
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                }
                                val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                                val base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!
                                selectedImagesURI.add(ImageSelectModel(uri, base64))

                            }


                            setCategoryAdaptor(selectedImagesURI)

                        } else {
                            HelpFunctions.ShowLongToast(
                                getString(R.string.MaximumImagesAllowedtoSelectare, imagCount),
                                this
                            )
                        }
                    } else if (data != null && data!!.data != null) {
                        val uri: Uri = data.data!!

                        var imageStream: InputStream? = null
                        try {
                            imageStream = getContentResolver()
                                .openInputStream(uri)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                        val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                        val base64 = HelpFunctions.EncodeToBase64(yourSelectedImage)!!
                        selectedImagesURI.add(ImageSelectModel(uri, base64))
                        setCategoryAdaptor(selectedImagesURI)
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.NoImageSelected),
                        this
                    )
                }
            }
        }

    }

    private fun categoryTemplate() {
        val file_name = "$file_name.js"
        val Title = Title



        StaticClassAdCreate.images = selectedImagesURI

        startActivity(Intent(this, DynamicTemplate::class.java).apply {
            putExtra("file_name", file_name)
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
                        is_select_cb.isChecked = is_main
                        is_select_cb.setOnCheckedChangeListener { buttonView, isChecked ->
                            if(isChecked){
                                list.forEach {
                                    it.is_main=false
                                }
                                list.get(position).is_main=true
                                category_rcv.adapter!!.notifyDataSetChanged()
                            }
                        }

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