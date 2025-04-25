package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity4

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityAddPhotoBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData.Companion.selectedCategory
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity5.DynamicTemplateActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.getColorCompat
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.FishBun.Companion.INTENT_PATH
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter

class AddPhotoActivity : BaseActivity<ActivityAddPhotoBinding>(),
    SelectedImagesAdapter.SetOnSelectedMainImage {

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
        // Initialize view binding
        binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = intent?.getStringExtra("title").toString()
        file_name = intent?.getStringExtra("file_name") ?: ""
        idsImageRemoved = arrayListOf()


        val freeProductImagesCount = selectedCategory?.freeProductImagesCount ?: 0
        val freeProductVidoesCount = selectedCategory?.freeProductVidoesCount ?: 0
        val extraProductImageFee =
            getString(R.string.product_price_sar, selectedCategory?.extraProductImageFee ?: 0)
        val extraProductVidoeFee =
            getString(R.string.product_price_sar, selectedCategory?.extraProductVidoeFee ?: 0)

        binding.testImageLink.text =
            getString(
                R.string.you_can_upload_images_and_video_links_for_free_each_additional_image_costs_and_each_additional_link_costs,
                freeProductImagesCount.toString(),
                freeProductVidoesCount.toString(),
                extraProductImageFee,
                extraProductVidoeFee
            )

        setupVideoLinksAapter()

        binding.toolbarMain.toolbarTitle.text = getString(R.string.add_photos)
        setViewClickListeners()
        setImagesAdapter()

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

    private fun setupVideoLinksAapter() {
        videoLinks = ArrayList()
        videoAddLinkAdapter = VideoAddLinkAdapter(
            videoLinks!!,
            object : VideoAddLinkAdapter.SetOnTypingVideoLinkTypingVideoLinks {
                override fun onTypeTypingVideoLink(value: String, position: Int) {
                    videoLinks!![position] = value
                }

                override fun onDeleteItem(position: Int) {
                    if (position >= videoLinks!!.size) return

                    val linkToRemove = videoLinks!![position]

                    AddProductObjectData.images?.let { imageList ->
                        val iterator = imageList.iterator()
                        while (iterator.hasNext()) {
                            val item = iterator.next()
                            if (item.url == linkToRemove) {
                                item.id.takeIf { it != 0 }?.let { idsImageRemoved?.add(it) }
                                iterator.remove()
                                break
                            }
                        }
                    }

                    videoLinks!!.removeAt(position)

                    AddProductObjectData.imagesListRemoved = arrayListOf()
                    AddProductObjectData.imagesListRemoved?.addAll(idsImageRemoved ?: arrayListOf())

                    AddProductObjectData.videoList?.takeIf { it.size > position }?.removeAt(position)

                    videoAddLinkAdapter.notifyDataSetChanged()
                }


            })
        binding.rvVidoes.apply {
            adapter = videoAddLinkAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setImagesAdapter() {
        selectedImagesAdapter = SelectedImagesAdapter(selectedImagesURI, this)
        binding.rvPakat.apply {
            adapter = selectedImagesAdapter
            layoutManager = GridLayoutManager(this@AddPhotoActivity, 2)
        }
    }

    private fun setViewClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.containerAddVideo.setOnClickListener {
            val dialogAddVideoLink =
                DialogAddVideoLink(this, object : DialogAddVideoLink.SetSaveLinkListeners {
                    override fun saveLinkListeners(value: String) {
                        if (videoLinks?.contains(value) == true) {
                            Toast.makeText(
                                this@AddPhotoActivity,
                                getString(R.string.this_link_already_exists),
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        videoLinks?.add(value)
                        videoAddLinkAdapter.notifyDataSetChanged()
                    }
                })
            dialogAddVideoLink.show()
        }

        binding.butt555.setOnClickListener {
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
        }
        binding.floatingActionButton.setOnClickListener() {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (PermissionChecker.checkSelfPermission(
                        this,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PermissionChecker.PERMISSION_DENIED
                ) {
                    val permissions = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    pickImageFromGallery2()
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionChecker.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PermissionChecker.PERMISSION_DENIED
                ) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    pickImageFromGallery2()
                }
            } else {
                pickImageFromGallery2()
            }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
                        if (selectedImagesURI.isNotEmpty())
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

        startActivity(Intent(this, DynamicTemplateActivity::class.java).apply {
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