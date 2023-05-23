package com.malka.androidappp.newPhase.presentation.forgotPasswordActivity.activtiy1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.presentation.forgotPasswordActivity.activity2.ActivityForgotPassOtpcode
import com.malka.androidappp.newPhase.presentation.loginScreen.LoginViewModel
import kotlinx.android.synthetic.main.activity_forgot_password.*


class ForgotPasswordActivty : BaseActivity() {


    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        if (ConstantObjects.currentLanguage == ConstantObjects.ENGLISH) {
            language_toggle.checkedTogglePosition = 0
        } else {
            language_toggle.checkedTogglePosition = 1
        }
        setupLoginViewModel()
        setClickListeners()

    }

    private fun setClickListeners() {
        language_toggle.setOnToggleSwitchChangeListener { position, isChecked ->
            setLocate()
        }


        signin.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivty, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        ibBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupLoginViewModel() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.isLoading.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })
        loginViewModel.isNetworkFail.observe(this, Observer {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        })
        loginViewModel.errorResponseObserver.observe(this, Observer {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(
                    it.message!!,
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        })
        loginViewModel.forgetPasswordObserver.observe(this, Observer {
            if (it.status_code == 200) {
                startActivity(
                    Intent(
                        this@ForgotPasswordActivty,
                        ActivityForgotPassOtpcode::class.java
                    ).apply {
                        putExtra(ConstantObjects.emailKey,tvEmail.text.toString().trim())
                    }
                )
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(
                        it.message!!,
                        this
                    )
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.serverError),
                        this
                    )
                }
            }

        })
    }


    //Data Validation
    private fun validateforgot(): Boolean {
        val Inputemail = tvEmail.text.toString().trim()

        return if (Inputemail.isEmpty()) {
            tvEmail.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Inputemail).matches()) {
            tvEmail.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            tvEmail.error = null
            true
        }
    }

    fun forgotPasswordFrm(v: View) {
        if (!validateforgot()) {
            return
        } else {
            loginViewModel.forgetPassword(tvEmail.text.toString().trim())
            // forgotemail()
        }

    }


//    fun forgotemail() {
//        HelpFunctions.startProgressBar(this)
//
//        val emailfogotpass: String = editText4.text.toString().trim()
//        val modeldataitem = User(email  =emailfogotpass, password = "String")
//        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val call= malqaa.forgotpassemail(modeldataitem)
//
//
//        call.enqueue(object : Callback<GeneralRespone> {
//            override fun onFailure(call: Call<GeneralRespone>, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//                HelpFunctions.ShowLongToast(getString(R.string.Somethingwentwrong),applicationContext)
//            }
//
//            override fun onResponse(
//                call: Call<GeneralRespone>,
//                response: Response<GeneralRespone>
//            ) {
//
//                if (response.isSuccessful) {
//                    val query=HelpFunctions.getQueryString(response.body()!!.data)
//                    val userId=query.get("Id")?:""
//                    val code=query.get("code")?:""
//                    if(userId.isEmpty()||code.isEmpty()){
//                        showError( getString(R.string.ThisEmailAddressDoesnotExist))
//                    }else{
//                        val intentd = Intent(
//                            this@ForgotPasswordActivty,
//                            ActivityForgotPassOtpcode::class.java
//                        )
//                        intentd.putExtra("getid", userId)
//                        intentd.putExtra("getcode", code)
//                        startActivity(intentd)
//                        finish()
//                    }
//                } else {
//                    HelpFunctions.ShowLongToast(
//                        getString(R.string.Error),
//                        applicationContext
//                    )
//                }
//                HelpFunctions.dismissProgressBar()
//
//            }
//        })
//
//
//    }


}
