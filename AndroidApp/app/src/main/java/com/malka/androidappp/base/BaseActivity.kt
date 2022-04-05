package com.malka.androidappp.base

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.*
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.helper.HelpFunctions
import java.util.*


abstract class BaseActivity : AppCompatActivity() {



    fun culture():String{
        return "en-US"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        HelpFunctions.IsUserLoggedIn()
        loadLocate()
    }

    fun getLanguage(): String {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", Locale.getDefault().language)
            ?: Locale.getDefault().language
        return language!!
    }

    //Methods For language
    fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    fun loadLocate() {
        setLocate(getLanguage())
    }

    fun hideSystemUI(mainContainer: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, mainContainer).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun showSystemUI(mainContainer: View) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            mainContainer
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    fun showError(error: String) {
        HelpFunctions.ShowAlert(
            this, "", error.replaceFirstChar{
                it.uppercase()
            }
        )
    }

    fun getCategortList(): String {

        var name = ""
        StaticClassAdCreate.subCategoryPath.forEach {
            name = name + it + " - "
        }
        if (name.length > 2) {
            name = name.dropLast(3)
        }
        return name
    }

    open fun hideSoftKeyboard(context: Context, et: EditText) {
        try {
            val inputMethodManager = context.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                et.windowToken, 0
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        windowImmersive((Activity)context);
    }

    open fun openSoftKeyboard(context: Context, view: View?) {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.showSoftInput(view, 0)
    }

    fun vibration() {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.getDefaultVibrator()
            } else {
                getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(500)
            }
        }catch (error:Exception){

        }


    }

}