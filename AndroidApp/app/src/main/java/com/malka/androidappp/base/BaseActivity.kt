package com.malka.androidappp.base

import android.content.Context
import android.content.Intent
import android.os.*
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.malka.androidappp.activities_main.SplashActivity
import com.malka.androidappp.fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import com.yariksoffice.lingver.Lingver


abstract class BaseActivity : AppCompatActivity() {



    fun culture():String{
        if (ConstantObjects.currentLanguage == ConstantObjects.ENGLISH) {
            return "en"
        } else {
            return "ar"
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        ConstantObjects.currentLanguage = Lingver.getInstance().getLocale().language
        HelpFunctions.IsUserLoggedIn()
    }



    //Methods For language
    fun setLocate() {
        ConstantObjects.categoryList= ArrayList()
        ConstantObjects.list= ArrayList()
        Lingver.getInstance().setLocale(this, if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) ConstantObjects.ENGLISH else ConstantObjects.ARABIC)
        startActivity(Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
       finish()
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

    open fun hideSoftKeyboard( et: View) {
        try {
            val inputMethodManager = getSystemService(
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