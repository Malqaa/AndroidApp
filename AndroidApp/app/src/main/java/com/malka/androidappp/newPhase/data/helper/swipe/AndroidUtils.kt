package com.malka.androidappp.newPhase.data.helper.swipe

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread

object AndroidUtils {

    private var density = 1f

    fun dp(value: Float, context: Context): Int {
        if (density == 1f) {
            checkDisplaySize(context)
        }
        return if (value == 0f) {
            0
        } else Math.ceil((density * value).toDouble()).toInt()
    }


    private fun checkDisplaySize(context: Context) {
        try {
            density = context.resources.displayMetrics.density
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    const val MAX_FILE_SIZE = 100000

    @JvmStatic
    fun pickFile(context: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            context.startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"),
                requestCode)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()

        }

    }

    @JvmStatic
    fun getFileNameByUri(uri: Uri, context: Context): String {
        var fileName = System.currentTimeMillis().toString()
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
            cursor.close()
        }
        return fileName
    }


    interface FileSave {
        fun fileSaved(path: String)
    }


    @JvmStatic
    fun copyUriToExternalFilesDir(uri: Uri, fileName: String, context: Activity, fileSave: FileSave) {
        thread {
            val inputStream = context.contentResolver.openInputStream(uri)


            val tempDir = context.getExternalFilesDir("temp")
            if (inputStream != null && tempDir != null) {
                val file = File("$tempDir/$fileName")
                val fos = FileOutputStream(file)
                val bis = BufferedInputStream(inputStream)
                val bos = BufferedOutputStream(fos)
                val byteArray = ByteArray(1024)
                var bytes = bis.read(byteArray)
                while (bytes > 0) {
                    bos.write(byteArray, 0, bytes)
                    bos.flush()
                    bytes = bis.read(byteArray)
                }
                bos.close()
                fos.close()
                context.runOnUiThread {
                    fileSave.fileSaved(file.path)
                }
            }
        }
    }


}
