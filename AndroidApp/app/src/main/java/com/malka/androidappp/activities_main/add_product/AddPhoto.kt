package com.malka.androidappp.activities_main.add_product

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.Filter
import androidx.core.content.PermissionChecker
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.servicemodels.ImageSelectModel
import com.malka.androidappp.fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.helper.Extension
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import kotlinx.android.synthetic.main.add_product_imgs.view.*
import kotlinx.android.synthetic.main.fragment_add_photo.*
import kotlinx.android.synthetic.main.toolbar_main.*


class AddPhoto : BaseActivity() {

    var Title: String = ""
    var file_name: String = ""

    private val selectedImagesURI: ArrayList<ImageSelectModel> = ArrayList()

    private val IMAGE_PICK_CODE = 1000

    private val PERMISSION_CODE = 1001
    private var mResults: ArrayList<String> = ArrayList()


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
                showError(getString(R.string.Pleaseaddaphoto))
            }else{
                val video=addvideo.text.toString()
                if(video.isEmpty()){
                    GO()
                }else{
                    if(URLUtil.isValidUrl(video)){
                        GO()
                    }else{
                        showError(getString(R.string.please_enter_valid,getString(R.string.video_link)))
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

        setCategoryAdaptor(selectedImagesURI)

    }

    private fun GO() {
        selectedImagesURI.filter {
            it.is_main==true
        }.let {
            if (it.size > 0) {
                StaticClassAdCreate.video = addvideo.text.toString()
                categoryTemplate()
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.mark_main_photo), this)
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
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 10)
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false)
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults)

        startActivityForResult(intent, IMAGE_PICK_CODE)
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
                        val base64 = HelpFunctions.encodeImage(it)!!
                        selectedImagesURI.add(ImageSelectModel(uri, base64))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                category_rcv.post { category_rcv.adapter!!.notifyDataSetChanged() }
            }
        }

    }

    private fun categoryTemplate() {
        val Title = Title



        StaticClassAdCreate.images = selectedImagesURI

        startActivity(Intent(this, DynamicTemplate::class.java).apply {
            putExtra("file_name", file_name)
            putExtra("Title", Title)
        })
    }

    private fun storePath() {
        Extension.clearPath()
        for (i in 0 until StaticClassAdCreate.subCategoryPath.size) {
            if (i == 0) {
                StaticClassAdCreate.subcatone = StaticClassAdCreate.subCategoryPath[i]
                StaticClassAdCreate.subcatonekey = StaticClassAdCreate.subCategoryPath[i] + "-${culture()}"
            }
            if (i == 1) {
                StaticClassAdCreate.subcattwo = StaticClassAdCreate.subCategoryPath[i]
                StaticClassAdCreate.subcattwokey = StaticClassAdCreate.subCategoryPath[i] + "-${culture()}"
            }
            if (i == 2) {
                StaticClassAdCreate.subcatthree = StaticClassAdCreate.subCategoryPath[i]
                StaticClassAdCreate.subcatthreekey =
                    StaticClassAdCreate.subCategoryPath[i] + "-${culture()}"
            }
            if (i == 3) {
                StaticClassAdCreate.subcatfour = StaticClassAdCreate.subCategoryPath[i]
                StaticClassAdCreate.subcatfourkey =
                    StaticClassAdCreate.subCategoryPath[i] + "-${culture()}"
            }
            if (i == 4) {
                StaticClassAdCreate.subcatfive = StaticClassAdCreate.subCategoryPath[i]
                StaticClassAdCreate.subcatfivekey =
                    StaticClassAdCreate.subCategoryPath[i] + "-${culture()}"
            }
            if (i == 5) {
                StaticClassAdCreate.subcatsix = StaticClassAdCreate.subCategoryPath[i]
                StaticClassAdCreate.subcatsixkey = StaticClassAdCreate.subCategoryPath[i] + "-${culture()}"
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
                                list.get(position).is_main=isChecked

                                category_rcv.post({ category_rcv.adapter!!.notifyDataSetChanged() })
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