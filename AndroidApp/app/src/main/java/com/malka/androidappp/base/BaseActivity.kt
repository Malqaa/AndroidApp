package com.malka.androidappp.base

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.helper.HelpFunctions
import java.util.*


abstract class BaseActivity : AppCompatActivity() {


    fun culture():String{
        return "en-US"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        var alertDialog: AlertDialog? = null
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.error_layout, null)
        dialogBuilder.setView(dialogView)

        val msg_tv = dialogView.findViewById<TextView>(R.id.msg_tv)
        msg_tv.setText(error)

        dialogView.findViewById<TextView>(R.id.dismiss_tbn).setOnClickListener {
            alertDialog!!.dismiss()

        }
        alertDialog = dialogBuilder.create()
        dialogBuilder.setCancelable(false)
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        alertDialog.show()
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
}