package com.malqaa.androidappp.newPhase.utils.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object CameraHelper {
    private var mCurrentPhotoPath: String? = null
    private var bitmap: Bitmap? = null

    private fun createImageFile(context: Context): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun getImagePathFromInputStreamUri(context: Context, uri: Uri): String? {
        var inputStream: InputStream? = null
        var filePath: String? = null
        if (uri.authority != null) {
            try {
                inputStream = context.contentResolver.openInputStream(uri) // context needed
                val photoFile = createTemporalFileFrom(inputStream, context)
                filePath = photoFile!!.path
            } catch (e: IOException) {
                // log
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return filePath
    }

    private fun createTemporalFileFrom(inputStream: InputStream?, context: Context): File? {
        var targetFile: File? = null
        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)
            targetFile = createTemporalFile(context)
            val outputStream: OutputStream = FileOutputStream(targetFile)
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return targetFile
    }

    private fun createTemporalFile(context: Context): File? {
        return File(context.externalCacheDir, "tempFile.jpg") // context needed
    }

    private fun fixRotate(photoPath: String) {
        val ei = ExifInterface(photoPath)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> bitmap = rotateImage(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> bitmap = rotateImage(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> bitmap = rotateImage(270f)
            ExifInterface.ORIENTATION_NORMAL -> {}
            else -> {}
        }
    }

    private fun rotateImage(angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            bitmap!!,
            0,
            0,
            bitmap?.width!!,
            bitmap?.height!!,
            matrix,
            true
        )
    }

    fun setImage(type: ConstantsHelper.PermissionEnum, contentURI: Uri?, context: Context): Bitmap {
        try {
            if (type == ConstantsHelper.PermissionEnum.READ_GALLERY_STORAGE) {
                bitmap =
                    MediaStore.Images.Media.getBitmap(context.contentResolver, contentURI)
                mCurrentPhotoPath =
                    getImagePathFromInputStreamUri(context, contentURI!!)
            } else bitmap =
                BitmapFactory.decodeFile(mCurrentPhotoPath, BitmapFactory.Options())
            fixRotate(mCurrentPhotoPath!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap!!
    }

    fun getMultiPartFromBitmap(mBitmap: Bitmap, name: String, context: Context): MultipartBody.Part {
        val filesDir = context.applicationContext.filesDir
        val file = File(filesDir, name.replace("[]", "") + ".jpg")
        val os: OutputStream
        try {
            os = FileOutputStream(file)
            Log.i("sadsad", mBitmap.byteCount.toString() + "")
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 30, os)
            os.flush()
            os.close()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context.applicationContext, "select photo", Toast.LENGTH_SHORT).show()
            //Log.e("Error writing bitmap", e.getMessage());
        }
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

        return MultipartBody.Part.createFormData("imgProfile", file.name, requestFile)
    }

    fun getMultiPartFrom(mBitmap: Bitmap, name: String, context: Context): MultipartBody.Part {
        val filesDir = context.applicationContext.filesDir
        val file = File(filesDir, name.replace("[]", "") + ".jpg")
        val os: OutputStream
        try {
            os = FileOutputStream(file)
            Log.i("sadsad", mBitmap.byteCount.toString() + "")
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 30, os)
            os.flush()
            os.close()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context.applicationContext, "select photo", Toast.LENGTH_SHORT).show()
            //Log.e("Error writing bitmap", e.getMessage());
        }
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

        return MultipartBody.Part.createFormData(name, file.name, requestFile)
    }
//


    fun openGallery(startForResult: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startForResult.launch(intent)
    }

    fun handlePermission(requestPermission: ActivityResultLauncher<String>, context: Context) {

    }

    fun handleResult(contentURI: Uri?, context: Context): Bitmap {
        return setImage(ConstantsHelper.PermissionEnum.READ_GALLERY_STORAGE, contentURI, context)
    }

    /*val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        when {
            granted -> {
                openGallery()
                // access to the camera is allowed, open the camera
            }
            !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                // access to the camera is denied, the user has checked the Don't ask again.
            }
            else -> {
                // access to the camera is denied, the user has rejected the request

            }
        }
    }*/


    /*private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val bitmap = CameraHelper.setImage(ConstantsHelper.PermissionEnum.READ_GALLERY_STORAGE,
                it?.data?.data,this)
            val result = CameraHelper.imageCodeQR(bitmap)
            if (result.isEmpty())
                Toast.makeText(this, resources.getString(R.string.read_qr_failed), Toast.LENGTH_SHORT).show()
            else
                setResultQR(result)

        }*/


}