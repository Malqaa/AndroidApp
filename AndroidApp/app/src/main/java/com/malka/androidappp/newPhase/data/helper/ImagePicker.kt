package com.malka.androidappp.newPhase.data.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

class ImagePicker(
    var activity: Activity,
    var fragment: Fragment? = null,
    var setOnImagePickedListeners: SetOnImagePickedListeners,
) {
    private val CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE = 10000
    private val GALLERY_CAPTURE_PERMISSIONS_REQUEST_CODE = 20000
    private val BOTH_GALLERY_AND_CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE = 30000
    private var currentCameraFileName = ""
    private var imageFile: File? = null

    companion object {
        const val CAMERA = 1
        const val GALLERY = 2
        const val CAMERA_AND_GALlERY = 3
    }


    private var targetPicker: Int = 0


    fun choosePicture(targetPicker: Int) {
        when (targetPicker) {
            CAMERA -> {
                this.targetPicker = CAMERA
                checkPermissions(targetPicker)
            }
            GALLERY -> {
                this.targetPicker = GALLERY
                checkPermissions(targetPicker)
            }
            CAMERA_AND_GALlERY -> {
                this.targetPicker = CAMERA_AND_GALlERY
                checkPermissions(targetPicker)
            }
        }


    }

    @SuppressLint("NewApi")
    private fun checkPermissions(targetPicker: Int) {
        if (needToAskPermissions(targetPicker)) {
            val neededPermissions = getNeededPermissions(targetPicker)
            when (targetPicker) {
                CAMERA -> {
                    activity.requestPermissions(neededPermissions,
                        CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE)
                }
                GALLERY -> {
                    activity.requestPermissions(neededPermissions,
                        GALLERY_CAPTURE_PERMISSIONS_REQUEST_CODE)
                }
                else -> {
                    activity.requestPermissions(neededPermissions,
                        BOTH_GALLERY_AND_CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE)
                }
            }
        } else {
            startImagePickerActivity(targetPicker)
        }
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    private fun needToAskPermissions(targetPicker: Int): Boolean {
        return when (targetPicker) {
            CAMERA -> {
                ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED
            }
            GALLERY -> {
                ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED
            }
            else -> {
                ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED
            }
        }

    }

    private fun getNeededPermissions(targetPicker: Int): Array<String> {
        return when (targetPicker) {
            CAMERA -> {
                arrayOf(Manifest.permission.CAMERA)
            }
            GALLERY -> {
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            else -> {
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            }
        }

    }

    //====
    fun handelPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
    ) {
        if (requestCode == CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startImagePickerActivity(targetPicker)
                } else {
                    //Toast.makeText(activity, R.string.canceling, Toast.LENGTH_SHORT).show()
                }
            } else {
                // Toast.makeText(activity, R.string.canceling, Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == GALLERY_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startImagePickerActivity(targetPicker)
                else {
                    /// Toast.makeText(activity, R.string.canceling, Toast.LENGTH_SHORT).show()
                }

            } else {
                // Toast.makeText(activity, R.string.canceling, Toast.LENGTH_SHORT).show()

            }

        }
        if (requestCode == BOTH_GALLERY_AND_CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    startImagePickerActivity(targetPicker)
                else {
                    /// Toast.makeText(activity, R.string.canceling, Toast.LENGTH_SHORT).show()
                }

            } else {
                //Toast.makeText(activity, R.string.canceling, Toast.LENGTH_SHORT).show()

            }

        }

    }


    /*********/
    private fun startImagePickerActivity(targetPicker: Int) {
        when (targetPicker) {
            CAMERA -> {
                openCameraIntent()
            }
            GALLERY -> {
                openGalleryIntent(false)
            }
            else -> {
                openGalleryIntent(true)
            }
        }

    }

    fun handleActivityResult(resultCode: Int, requestCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE || requestCode == GALLERY_CAPTURE_PERMISSIONS_REQUEST_CODE) {
                handlePickedImageResult(data)
            }
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("NewApi")
    private fun handlePickedImageResult(data: Intent?) {
        var isCamera = true
        if (data != null && data.data != null) {
            val action = data.action
            isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
        }
        val imageUri =
            if (isCamera || data?.data == null) getCameraFileUri(activity) else data.data
        if (isCamera) {
            deletePreviousCameraFiles()
        }
//        if (withCrop) {
//            CropImage.activity(imageUri)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setAspectRatio(aspectRatioX, aspectRatioY)
//                .start(activity)
//        } else {
        imageFile = File(imageUri?.path)
        imageUri?.let { setOnImagePickedListeners.onImagePicked(it) }
        //  }
    }


    /*****Prepare ALl Intents****/
    @SuppressLint("NewApi", "QueryPermissionsNeeded")
    private fun openGalleryIntent(includeCamera: Boolean) {
        if (needToAskPermissions(targetPicker)) {
            activity.requestPermissions(getNeededPermissions(targetPicker),
                GALLERY_CAPTURE_PERMISSIONS_REQUEST_CODE)
        } else {
            val galleryIntent = getGalleryOrBothIntent(includeCamera)
            setOnImagePickedListeners.launchImageActivityResult(galleryIntent,
                GALLERY_CAPTURE_PERMISSIONS_REQUEST_CODE)

        }
    }

    @SuppressLint("NewApi", "QueryPermissionsNeeded")
    private fun openCameraIntent() {
        if (needToAskPermissions(targetPicker)) {
            activity.requestPermissions(getNeededPermissions(targetPicker),
                CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val cameraIntent = getCameraIntent()
//            if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            try {
                setOnImagePickedListeners.launchImageActivityResult(cameraIntent,
                    CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE)
            }catch (e:Exception){}
            //  }
        }
    }

    /****Create Camera Intent****/
    private fun getCameraIntent(): Intent {
        currentCameraFileName = "outputImage" + System.currentTimeMillis() + ".jpg"
        val imagesDir = File(activity.filesDir, "images")
        imagesDir.mkdirs()
        val file = File(imagesDir, currentCameraFileName)
        try {
            file.createNewFile()
        } catch (e: Exception) {
        }
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val authority = activity.packageName
        val outputUri: Uri = FileProvider.getUriForFile(
            activity.applicationContext,
            authority,
            file)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        activity.grantUriPermission(
            "com.google.android.GoogleCamera",
            outputUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        return cameraIntent
    }

    private fun getCameraFileUri(activity: Activity): Uri? {
        val imagePath = File(activity.filesDir, "images/$currentCameraFileName")
        return Uri.fromFile(imagePath)
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun deletePreviousCameraFiles() {
        val imagePath = File(activity.filesDir, "images")
        if (imagePath.exists() && imagePath.isDirectory) {
            if (imagePath.listFiles().isNotEmpty()) {
                for (file in imagePath.listFiles()) {
                    if (file.name != currentCameraFileName) {
                        file.delete()
                    }
                }
            }
        }
    }

    /****Create Gallery Intent****/
    private fun getGalleryOrBothIntent(includeCamera: Boolean): Intent {
        val allIntents: ArrayList<Intent> = ArrayList()
        val packageManager = activity.packageManager
        var galleryIntents =
            getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, false)
        if (galleryIntents.isEmpty()) {
            // if no intents found for get-content try pick intent action (Huawei P9).
            galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, false)
        }
        if (includeCamera) {
            allIntents.add(getCameraIntent())
        }
        allIntents.addAll(galleryIntents)
        val target: Intent
        if (allIntents.isEmpty()) {
            target = Intent()
        } else {
            target = allIntents[allIntents.size - 1]
            allIntents.removeAt(allIntents.size - 1)
        }

        // Create a chooser from the main  intent
        val chooserIntent = Intent.createChooser(target, "")

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray())
        return chooserIntent
    }

    private fun getGalleryIntents(
        packageManager: PackageManager, action: String, includeDocuments: Boolean,
    ): List<Intent> {
        val intents: MutableList<Intent> = ArrayList()
        val galleryIntent = if (action === Intent.ACTION_GET_CONTENT) Intent(action) else Intent(
            action,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
        for (res in listGallery) {
            val intent = Intent(galleryIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            intents.add(intent)
        }

        // remove documents intent
        if (!includeDocuments) {
            for (intent in intents) {
                if (intent
                        .component
                        ?.className
                    == "com.android.documentsui.DocumentsActivity"
                ) {
                    intents.remove(intent)
                    break
                }
            }
        }
        return intents
    }

}

interface SetOnImagePickedListeners {
    fun onImagePicked(imageUri: Uri)
    fun launchImageActivityResult(
        imageIntent: Intent,
        requestCode: Int,
    )
}
