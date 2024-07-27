package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity4

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.getColorCompat
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity5.DynamicTemplateActivtiy
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.FishBun.Companion.INTENT_PATH
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import kotlinx.android.synthetic.main.activity_add_photo.*
import kotlinx.android.synthetic.main.toolbar_main.*


class AddPhotoActivity : BaseActivity(), SelectedImagesAdapter.SetOnSelectedMainImage {

    private var title: String = ""
    private var file_name: String = ""

    private var selectedImagesURI: ArrayList<ImageSelectModel> = ArrayList()
    private lateinit var selectedImagesAdapter: SelectedImagesAdapter
    private lateinit var videoAddLinkAdapter: VideoAddLinkAdapter
    private val IMAGE_PICk_CODE_2 = 2000
    private val PERMISSION_CODE = 1001

    private var videoLinks: ArrayList<String>? = null
    private var idsImageRemoved: ArrayList<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        title = intent?.getStringExtra("title").toString()
        file_name = intent?.getStringExtra("file_name") ?: ""
        idsImageRemoved = arrayListOf()

        setupVideoLinksAapter()
        if (!ConstantObjects.isModify) {
//            HelpFunctions.ShowLongToast(
//                getString(R.string.noSubCategoryFound),
//                this@AddPhotoActivity
//            )
        }

        toolbar_title.text = getString(R.string.item_details)
        setViewClickListeners()
        setImagesAdapter()

        //    storePath()
        if (intent.getBooleanExtra(ConstantObjects.isEditKey, false)) {
            AddProductObjectData.images?.forEach { list ->
                list.isEdit = true
            }
            selectedImagesURI =
                (AddProductObjectData.images?.filter { it.type == 1 } as ArrayList<ImageSelectModel>)
            selectedImagesAdapter.updateData(selectedImagesURI)

            if (!AddProductObjectData.videoList.isNullOrEmpty()) {
                videoLinks?.addAll(AddProductObjectData.videoList ?: arrayListOf())
                videoAddLinkAdapter.updateAdapter(videoLinks!!)
            }

        }
    }

//    fun setData() {
//        selectedImagesAdapter.updateData(AddProductObjectData.listMedia ?: arrayListOf())
//    }

    private fun setupVideoLinksAapter() {
        videoLinks = ArrayList()
        videoAddLinkAdapter = VideoAddLinkAdapter(videoLinks!!,
            object : VideoAddLinkAdapter.SetOnTypingVideoLinkTypingVideoLinks {
                override fun onTypeTypingVideoLink(value: String, position: Int) {
                    videoLinks!![position] = value
                }

                override fun onDeleteItem(position: Int) {
                    for (i in AddProductObjectData.images?.indices!!) {
                        if (AddProductObjectData.images!![i].url == videoLinks!![position]) {
                            idsImageRemoved?.add(AddProductObjectData.images!![i].id)
                            AddProductObjectData.images?.removeAt(i)
                            break
                        }
                    }
                    videoLinks?.removeAt(position)
                    AddProductObjectData.imagesListRemoved = arrayListOf()
                    AddProductObjectData.imagesListRemoved?.addAll(idsImageRemoved!!)

                    if (AddProductObjectData.videoList?.isNotEmpty() == true)
                        AddProductObjectData.videoList?.removeAt(position)
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
            val dialogAddVideoLink =
                DialogAddVideoLink(this, object : DialogAddVideoLink.SetSaveLinkListeners {
                    override fun saveLinkListeners(value: String) {
                        videoLinks?.add(value)
                        videoAddLinkAdapter.notifyDataSetChanged()
                    }

                })
            dialogAddVideoLink.show()
        }

        butt555.setOnClickListener {
            if (selectedImagesURI.size == 0) {
                showError(getString(R.string.Pleaseaddaphoto))
            } else if (videoLinks!!.isNotEmpty()) {
                var allLinkSet = true
                for (link in videoLinks!!) {
                    if (link.trim() == "") {
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PermissionChecker.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    pickImageFromGallery2()
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    pickImageFromGallery2()
                }
            } else {
                pickImageFromGallery2()
            }


//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (PermissionChecker.checkSelfPermission(
//                        this,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    ) == PermissionChecker.PERMISSION_DENIED
//                ) {
//                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    requestPermissions(permissions, PERMISSION_CODE)
//                } else {
//                    pickImageFromGallery2()
//                }
//            } else {
//                pickImageFromGallery2()
//            }
        }
    }

    private fun GO() {
        selectedImagesURI.filter {
            it.is_main
        }.let {
            if (it.isNotEmpty()) {
                AddProductObjectData.videoList = videoLinks
                categoryTemplate()
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.mark_main_photo), this)
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery2()
            } else {
                // Permission denied, show a message to the user
                HelpFunctions.ShowLongToast("Permission denied", this)
            }
        }
    }


    fun pickImageFromGallery2() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(100)
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
                    val path: ArrayList<Uri> = ArrayList()
                    data?.let {
                        path.addAll(it)
                        path.forEach {
                            try {
                                val uri: Uri = it
                                selectedImagesURI.add(ImageSelectModel(uri, ""))

                            } catch (e: Exception) {
                            }
                        }
                        if(selectedImagesURI.isNotEmpty())
                        selectedImagesURI[0].is_main = true

                        selectedImagesAdapter.updateData(selectedImagesURI)
                        selectedImagesAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }

    private fun categoryTemplate() {
        val title = title

        selectedImagesURI = ArrayList(selectedImagesURI.sortedByDescending { it.is_main })
        AddProductObjectData.images = selectedImagesURI

        startActivity(Intent(this, DynamicTemplateActivtiy::class.java).apply {
            putExtra("file_name", file_name)
            putExtra("title", title)
            putExtra(
                ConstantObjects.isEditKey,
                intent.getBooleanExtra(ConstantObjects.isEditKey, false)
            )
            finish()
        }
        )
    }

    override fun onRemoveImage(data: ArrayList<ImageSelectModel>, position: Int) {
        if (data[position].id != 0)
            idsImageRemoved?.add(data[position].id)
        AddProductObjectData.imagesListRemoved = arrayListOf()
        AddProductObjectData.imagesListRemoved?.addAll(idsImageRemoved!!)
        if (selectedImagesURI.isNotEmpty())
            selectedImagesURI.removeAt(position)
        selectedImagesAdapter.updateData(data)
        selectedImagesAdapter.notifyDataSetChanged()
    }

    override fun onSelectedMainImage(data: ArrayList<ImageSelectModel>, position: Int) {
        selectedImagesURI = arrayListOf()
        selectedImagesURI.addAll(data)
        selectedImagesURI[position].is_main = true
        selectedImagesAdapter.updateData(selectedImagesURI)
        selectedImagesAdapter.notifyDataSetChanged()
    }

}