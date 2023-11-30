package com.malka.androidappp.newPhase.presentation.addProduct.activity4

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.URLUtil

import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.getColorCompat
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.domain.models.ImageSelectModel
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.presentation.addProduct.activity5.DynamicTemplateActivtiy
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.FishBun.Companion.INTENT_PATH
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter

import kotlinx.android.synthetic.main.activity_add_photo.*
import kotlinx.android.synthetic.main.toolbar_main.*


class AddPhotoActivity : BaseActivity(), SelectedImagesAdapter.SetOnSelectedMainImage {

    var Title: String = ""
    var file_name: String = ""

    private val selectedImagesURI: ArrayList<ImageSelectModel> = ArrayList()
    private lateinit var selectedImagesAdapter: SelectedImagesAdapter
    private val IMAGE_PICk_CODE_2 = 2000
    private val PERMISSION_CODE = 1001

    private lateinit var videoAddLinkAdapter: VideoAddLinkAdapter
    private lateinit var videoLinks: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        Title = intent?.getStringExtra("Title").toString()
        file_name = intent?.getStringExtra("file_name") ?: ""
        setupVideoLinksAapter()
        HelpFunctions.ShowLongToast(
            getString(R.string.noSubCategoryFound),
            this@AddPhotoActivity
        )
        toolbar_title.text = getString(R.string.item_details)
        setViewClickListeners()
        setImagesAdapter()
    //    storePath()


    }

    private fun setupVideoLinksAapter() {
        videoLinks = ArrayList()
        videoAddLinkAdapter = VideoAddLinkAdapter(videoLinks,
            object : VideoAddLinkAdapter.SetOnTypingVideoLinkTypingVideoLinks {
                override fun onTypeTypingVideoLink(value: String, position: Int) {
                    videoLinks[position] = value
                }

                override fun onDeleteItem(position: Int) {
                    videoLinks.removeAt(position)
                    videoAddLinkAdapter.notifyDataSetChanged()
                }

            })
        rvVidoes.apply {
            adapter = videoAddLinkAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setImagesAdapter() {
        selectedImagesAdapter = SelectedImagesAdapter(selectedImagesURI, this)
        rvPakat.apply {
            adapter = selectedImagesAdapter
            layoutManager = GridLayoutManager(this@AddPhotoActivity, 2)
        }
    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }
        containerAddVideo.setOnClickListener {
            var dialogAddVideoLink =
                DialogAddVideoLink(this, object : DialogAddVideoLink.SetSaveLinkListeners {
                    override fun saveLinkListeners(value: String) {
                        videoLinks.add(value)
                        videoAddLinkAdapter.notifyDataSetChanged()
                    }

                })
            dialogAddVideoLink.show()
        }

        butt555.setOnClickListener {
            if (selectedImagesURI.size == 0) {
                showError(getString(R.string.Pleaseaddaphoto))
            } else if (videoLinks.isNotEmpty()) {
                var allLinkSet = true
                for (link in videoLinks) {
                    if (link.toString().trim() == "") {
                        allLinkSet = false
                        break
                    }
                }
                if (!allLinkSet) {
                    showError(
                        getString(
                            R.string.please_enter_valid,
                            getString(R.string.video_link)
                        )
                    )
                } else {
                    GO()
                }
            } else {
                GO()
            }
//            else {
//                val video = addvideo.text.toString()
//                if (video.isEmpty()) {
//                    GO()
//                } else {
//                    if (URLUtil.isValidUrl(video)) {
//                        GO()
//                    } else {
//                        showError(
//                            getString(
//                                R.string.please_enter_valid,
//                                getString(R.string.video_link)
//                            )
//                        )
//                    }
//                }
//            }

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
                    pickImageFromGallery2();
                }
            } else {
                pickImageFromGallery2();
            }

            addcamera.setOnClickListener {
                floatingActionButton.performClick()
            }
        }
    }

    private fun GO() {
        selectedImagesURI.filter {
            it.is_main == true
        }.let {
            if (it.size > 0) {
                AddProductObjectData.videoList = videoLinks
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
                    pickImageFromGallery2()
                } else {
                    HelpFunctions.ShowLongToast("Permission denied", this)
                }
            }
        }
    }


    fun pickImageFromGallery2() {
//        FishBun.with(this)
//            .setImageAdapter(GlideAdapter())
//            .setMaxCount(5)
//            .setMinCount(3)
//            .setPickerSpanCount(5)
//            .setActionBarColor(Color.parseColor("#795548"), Color.parseColor("#5D4037"), false)
//            .setActionBarTitleColor(Color.parseColor("#ffffff"))
//            //.setArrayPaths(path)
//           // .setAlbumSpanCount(2, 3)
//            .setButtonInAlbumActivity(false)
//           // .setCamera(true)
//            //.exceptGif(true)
//            .setReachLimitAutomaticClose(true)
//            .setHomeAsUpIndicatorDrawable(
//                ContextCompat.getDrawable(
//                    this,
//                    R.drawable.ic_search_black_24dp
//                )
//            )
//            .setDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_search_black_24dp))
//            .setAllDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_a_photo_gray_100dp))
//            .setIsUseAllDoneButton(true)
//            .setAllViewTitle("All")
//            .setMenuAllDoneText("All Done")
//            .setActionBarTitle("FishBun Dark")
//            .textOnNothingSelected("Please select three or more!")
//            .exceptMimeType(listOf(MimeType.GIF))
//            .setSpecifyFolderList(arrayListOf("Screenshots", "Camera"))
//            .startAlbumWithOnActivityResult(IMAGE_PICk_CODE_2)
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(10)
            .setMinCount(1)
            .setActionBarColor(getColorCompat(R.color.white), getColorCompat(R.color.white), false)
            .setActionBarTitleColor(getColorCompat(R.color.black))
            .setHomeAsUpIndicatorDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_baseline_keyboard_backspace_24
                )
            )
            .setDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done))
            .setAllDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done))
            .textOnNothingSelected(getString(R.string.Pleaseaddaphoto))
            .textOnImagesSelectionLimitReached(getString(R.string.selected_image_limit_reached))
            .isStartInAllView(false)
            .setAllViewTitle(getString(R.string.str_all_view))
            .startAlbumWithOnActivityResult(IMAGE_PICk_CODE_2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICk_CODE_2) {
                if (resultCode == Activity.RESULT_OK) {
                    val data = data?.getParcelableArrayListExtra<Uri>(INTENT_PATH)
                    var path: ArrayList<Uri> = ArrayList()
                    selectedImagesURI.clear()
                    data?.let {
                        path.addAll(it)
                        path.forEach {
                            try {
                                val uri: Uri = it
                                selectedImagesURI.add(ImageSelectModel(uri,""))
                              //  val path: String? = getRealPathFromURI(it)
                             //   val base64 = path?.let { it1 -> HelpFunctions.encodeImage(it1) }
//                                base64?.let {
//                                    it
//                                    selectedImagesURI.add(ImageSelectModel(uri, it))
//                                }

                            } catch (e: Exception) {
                            }
                        }
                        selectedImagesAdapter.notifyDataSetChanged()
                    }
                    // you can get an image path(ArrayList<Uri>) on 0.6.2 and later
                }
            }
        }

    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, contentUri, proj, null, null, null)
        val cursor: Cursor? = loader.loadInBackground()
        val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val result: String? = column_index?.let { cursor?.getString(it) }
        cursor?.close()
        return result
    }

    private fun categoryTemplate() {
        val Title = Title

        AddProductObjectData.images = selectedImagesURI

        startActivity(Intent(this, DynamicTemplateActivtiy::class.java).apply {
            putExtra("file_name", file_name)
            putExtra("Title", Title)
        })
    }

//    private fun storePath() {
//        Extension.clearPath()
//        for (i in 0 until AddProductObjectData.subCategoryPath.size) {
//            if (i == 0) {
//                AddProductObjectData.subcatone = AddProductObjectData.subCategoryPath[i]
//                AddProductObjectData.subcatonekey =
//                    AddProductObjectData.subCategoryPath[i] + "-${culture()}"
//            }
//            if (i == 1) {
//                AddProductObjectData.subcattwo = AddProductObjectData.subCategoryPath[i]
//                AddProductObjectData.subcattwokey =
//                    AddProductObjectData.subCategoryPath[i] + "-${culture()}"
//            }
//            if (i == 2) {
//                AddProductObjectData.subcatthree = AddProductObjectData.subCategoryPath[i]
//                AddProductObjectData.subcatthreekey =
//                    AddProductObjectData.subCategoryPath[i] + "-${culture()}"
//            }
//            if (i == 3) {
//                AddProductObjectData.subcatfour = AddProductObjectData.subCategoryPath[i]
//                AddProductObjectData.subcatfourkey =
//                    AddProductObjectData.subCategoryPath[i] + "-${culture()}"
//            }
//            if (i == 4) {
//                AddProductObjectData.subcatfive = AddProductObjectData.subCategoryPath[i]
//                AddProductObjectData.subcatfivekey =
//                    AddProductObjectData.subCategoryPath[i] + "-${culture()}"
//            }
//            if (i == 5) {
//                AddProductObjectData.subcatsix = AddProductObjectData.subCategoryPath[i]
//                AddProductObjectData.subcatsixkey =
//                    AddProductObjectData.subCategoryPath[i] + "-${culture()}"
//            }
//
//        }
//    }


    override fun onSelectedMainImage(position: Int) {
        for(item in selectedImagesURI){
            item.is_main = false
        }
        selectedImagesURI[position].is_main=true
        selectedImagesAdapter.notifyDataSetChanged()
//        if (isChecked) {
//            selectedImagesURI.forEach {
//                it.is_main = false
//            }
//            selectedImagesURI[position].is_main = isChecked
//
//            selectedImagesAdapter.notifyDataSetChanged()
//        } else {
//            selectedImagesURI.forEach {
//                it.is_main = false
//            }
//        }
    }


}