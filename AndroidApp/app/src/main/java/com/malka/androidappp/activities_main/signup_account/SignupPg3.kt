package com.malka.androidappp.activities_main.signup_account

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.activities_main.signup_account.signup_pg4.SignupPg4
import kotlinx.android.synthetic.main.activity_signup_pg3.*
import java.util.*


class SignupPg3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg3)
        supportActionBar?.hide()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        //Data Validation
        fullNamee = findViewById(R.id.editText10)
        date = findViewById(R.id.editText1033)

        ///////////Calender EditText///////////////
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        ///////////////////////////////Hightlight Expiry Date////////////////////////////////////////////////////////
        date!!.setOnClickListener() {
            hidekeyboard()
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    val monthplus:Int = mMonth+1
                    date!!.setText("" + mYear + "-" + monthplus + "-" + mDay)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
    }
    //Data Validation
    private var fullNamee:EditText? = null
    private var date:EditText? = null

    //Data Validation
    private fun validateSign3FullName(): Boolean {
        val Inputname = fullNamee!!.text.toString().trim { it <= ' ' }

        return if (Inputname.isEmpty()) {
            fullNamee!!.error = "Field can't be empty"
            false
        }
        else {
            fullNamee!!.error = null
            true
        }
    }

    //Data Validation
    private fun validateSign3Day(): Boolean {
        val Inputday = date!!.text.toString().trim { it <= ' ' }

        return if (Inputday.isEmpty()) {
            date!!.error = "Field can't be empty"
            false
        }
        else {
            date!!.error = null
            true
        }
    }





    fun SignuuPg3confirmInput(v: View) {
        if (!validateSign3FullName() or !validateSign3Day() ) {
            return

        }
        else
        {
            signup3next(v)
        }
        // signup3next(v)}
    }

    fun signup3next(view: View) {
        // get selected radio button from radioGroup
        val selectedId: Int = radioGroup.getCheckedRadioButtonId()
        val radioSexButton = findViewById<View>(selectedId) as RadioButton
        val str = radioSexButton.getText()


        //
        val fullnaam = editText10.text.toString().trim()
        val lastName = editTextlastname.text.toString().trim()
        val dateeparse = editText1033.text.toString().trim()
        val userupdateId = intent.getStringExtra("useridupdate")
        val intent = Intent(this@SignupPg3, SignupPg4::class.java)
        intent.putExtra("updateuserById", userupdateId)
        intent.putExtra("gender",str)
        intent.putExtra("fullnaam",fullnaam)
        intent.putExtra("dateee",dateeparse)
        intent.putExtra("lastName",lastName)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }

    // fun signupprev3(view: View) {
    //   onBackPressed()
    // finish()
    //}
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@SignupPg3, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun hidekeyboard(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)


    }


    fun addListenerOnButton() {

        // get selected radio button from radioGroup
        val selectedId: Int = radioGroup.getCheckedRadioButtonId()
        // find the radiobutton by returned id
        val radioSexButton = findViewById<View>(selectedId) as RadioButton
        Toast.makeText(
            this, radioSexButton.getText(), Toast.LENGTH_SHORT
        ).show()


    }

}
