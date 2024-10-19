package com.malqaa.androidappp.newPhase.core

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
import androidx.viewbinding.ViewBinding
import com.malqaa.androidappp.newPhase.presentation.activities.splashActivity.SplashActivity
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.yariksoffice.lingver.Lingver


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    // Abstract property to be initialized in subclasses
    protected lateinit var binding: VB

    // Method to get the current language based on a constant
    fun culture(): String {
        return if (ConstantObjects.currentLanguage == ConstantObjects.ENGLISH) {
            "en"
        } else {
            "ar"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure the layout adjusts correctly when the soft keyboard is shown
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        ConstantObjects.currentLanguage = Lingver.getInstance().getLocale().language
        HelpFunctions.isUserLoggedIn()
    }

    // Method to switch between languages and restart the activity
    fun setLocale() {
        ConstantObjects.categoryList = arrayListOf()
        ConstantObjects.categoryProductHomeList = arrayListOf()
        Lingver.getInstance().setLocale(
            this,
            if (Lingver.getInstance()
                    .getLanguage() == ConstantObjects.ARABIC
            ) ConstantObjects.ENGLISH else ConstantObjects.ARABIC
        )
        startActivity(Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    // Method to hide system UI
    fun hideSystemUI(mainContainer: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, mainContainer).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    // Method to show system UI
    fun showSystemUI(mainContainer: View) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            mainContainer
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    // Method to display an error alert
    fun showError(error: String) {
        HelpFunctions.ShowAlert(
            this, "", error.replaceFirstChar { it.uppercase() }
        )
    }

    // Method to retrieve a formatted category list
    fun getCategoryList(): String {
        return AddProductObjectData.subCategoryPath.joinToString(" - ")
    }

    // Method to hide the soft keyboard
    open fun hideSoftKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // Method to show the soft keyboard
    open fun openSoftKeyboard(context: Context, view: View?) {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.showSoftInput(view, 0)
    }

    // Method to trigger device vibration
    fun vibration() {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(500)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Method to navigate to the sign-in activity
    fun goToSignInActivity() {
        startActivity(Intent(this, SignInActivity::class.java))
    }
}
