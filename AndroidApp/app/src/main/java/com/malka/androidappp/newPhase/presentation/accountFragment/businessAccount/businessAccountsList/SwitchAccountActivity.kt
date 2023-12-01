package com.malka.androidappp.newPhase.presentation.accountFragment.businessAccount.businessAccountsList

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Browser
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.domain.models.bussinessAccountsListResp.BusinessAccountDetials
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.presentation.accountFragment.businessAccount.BusinessAccountViewModel
import com.malka.androidappp.newPhase.presentation.accountFragment.businessAccount.addBusinessAccount.BusinessAccountCreateActivity
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_switch_account.*
import kotlinx.android.synthetic.main.toolbar_main.*


class SwitchAccountActivity : BaseActivity(), BusinessAccountsAdapter.SetOnBusinessAccountSelected,
    SwipeRefreshLayout.OnRefreshListener {


    lateinit var businessAccountsAdapter: BusinessAccountsAdapter
    lateinit var accountList: ArrayList<BusinessAccountDetials>
    private var userData: LoginUser? = null
    private lateinit var businessAccountViewModel: BusinessAccountViewModel
    val creatAccountLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onRefresh()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_account)
        toolbar_title.text = getString(R.string.switch_accounts)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        userData = Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
        setViewClickListeners()
        setBusinessAccountsAdapter()
        setUserData(userData)
        setupViewModel()
        onRefresh()
//        getBusinessUserList()
    }

    private fun setupViewModel() {
        businessAccountViewModel = ViewModelProvider(this).get(BusinessAccountViewModel::class.java)
        businessAccountViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }


        businessAccountViewModel.isNetworkFail.observe(this) {
            if (it) {
                showErrorMessage(getString(R.string.noAccountFound))

                // showErrorMessage(getString(R.string.connectionError))
            } else {
                showErrorMessage(getString(R.string.serverError))
            }

        }
        businessAccountViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showErrorMessage(it.message!!)
            } else {
                showErrorMessage(getString(R.string.serverError))
            }
        }
        businessAccountViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showErrorMessage(it.message!!)
            } else {
                showErrorMessage(getString(R.string.serverError))
            }
        }
        businessAccountViewModel.changeBusinessAccountObserver.observe(this) {
            if (it.status_code == 200 && it.businessAccount != null) {
                if (it.businessAccount.chanegeAccountUrl != null && it.businessAccount.chanegeAccountUrl != "")
                    openExternalLInk(it.businessAccount.chanegeAccountUrl,it.businessAccount.token?:"")
            } else {
                if (it.message != null) {
                    showErrorMessage(it.message)
                } else {
                    showErrorMessage(getString(R.string.serverError))
                }
            }
        }
        businessAccountViewModel.businessAccountListObserver.observe(this) {
            if (it.status_code == 200) {
                if (it.businessAccountsList != null) {
                    //   var data: List<BusinessAccountDetails>
                    accountList.clear()
                    try {
//                        data = it.businessAccountsList as List<BusinessAccountDetails>
//                        println("hhhh "+Gson().toJson(it))
//                        println("hhhh "+Gson().toJson(data))
                        accountList.addAll(it.businessAccountsList)
                        businessAccountsAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {
                    }
                    if (accountList.isEmpty()) {
                        showErrorMessage(getString(R.string.noAccountFound))
                    }

                } else {
                    showErrorMessage(getString(R.string.noAccountFound))
                }
            } else {
                if (it.message != null) {
                    showErrorMessage(it.message)
                } else {
                    showErrorMessage(getString(R.string.serverError))
                }
            }
        }
    }

    private fun openExternalLInk(
        chanegeAccountUrl: String,
        token:String
    ) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(chanegeAccountUrl)
            val bundle = Bundle()
            bundle.putString("Authorization", "Bearer $token")
            i.putExtra(Browser.EXTRA_HEADERS, bundle)
            startActivity(i)
        } catch (e: java.lang.Exception) {
            HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
        }

    }

    private fun showErrorMessage(message: String) {
        tvError.show()
        tvError.text = message
    }

    private fun setUserData(userData: LoginUser?) {
        try {
            userData?.let {
                Picasso.get().load(userData.img)
                    .error(R.drawable.profileicon_bottomnav)
                    .placeholder(R.drawable.profileicon_bottomnav)
                    .into(review_profile_pic)
                review_name.text = userData.userName.toString()
            }
        } catch (e: Exception) {
        }
    }

    private fun setBusinessAccountsAdapter() {
        accountList = ArrayList()
        businessAccountsAdapter = BusinessAccountsAdapter(accountList, this)
        business_rcv.apply {
            adapter = businessAccountsAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }
        add_business_account_btn.setOnClickListener {
            val intent =
                Intent(this@SwitchAccountActivity, BusinessAccountCreateActivity::class.java)
            creatAccountLauncher.launch(intent)
        }
    }

    override fun setOnBusinessAccountSelected(position: Int) {

    }

    override fun setOnSwitchAccount(position: Int) {
        businessAccountViewModel.changeBusinessAccount(accountList[position].id)
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        accountList.clear()
        tvError.hide()
        businessAccountViewModel.getBusinessAccount()
    }

    /**
     **********************
     * ******************
     * *******************
     * ****/


//    fun getBusinessUserList() {
//
//        HelpFunctions.startProgressBar(this)
//
//        val malqa: MalqaApiService = getRetrofitBuilder()
//        val call = malqa.getBusinessUserList(ConstantObjects.logged_userid)
//
//
//
//        call.enqueue(object : retrofit2.Callback<BusinessUserRespone?> {
//            override fun onFailure(call: retrofit2.Call<BusinessUserRespone?>?, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//
//                Toast.makeText(this@SwitchAccountActivity, "${t.message}", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onResponse(
//                call: retrofit2.Call<BusinessUserRespone?>,
//                response: retrofit2.Response<BusinessUserRespone?>
//            ) {
//                if (response.isSuccessful) {
//
//                    if (response.body() != null) {
//                        val respone: BusinessUserRespone = response.body()!!
//                        if (respone.status_code == 200) {
//                            getBusinessUser(respone.data)
//                        } else {
//
//                            Toast.makeText(
//                                this@SwitchAccountActivity,
//                                respone.message,
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//
//                }
//                HelpFunctions.dismissProgressBar()
//            }
//        })
//
//    }
//
//
//    private fun getBusinessUser(list: List<BusinessUserRespone.BusinessUser>) {
//        accounts.text = "${list.size + 1} ${getString(R.string.accounts)}"
//        business_rcv.adapter = object : GenericListAdapter<BusinessUserRespone.BusinessUser>(
//            R.layout.item_switch_account_design,
//            bind = { element, holder, itemCount, position ->
//                holder.view.run {
//                    element.run {
//                        business_name.text = businessName
//                        val imageURL = Constants.IMAGE_URL + businessLogoPath
//                        if (!imageURL.isNullOrEmpty()) {
//                            Picasso.get().load(imageURL)
//                                .error(R.drawable.profileicon_bottomnav)
//                                .placeholder(R.drawable.profileicon_bottomnav)
//                                .into(review_profile_pic)
//                        } else {
//                            review_profile_pic.setImageResource(R.drawable.profileicon_bottomnav)
//                        }
//                    }
//                }
//            }
//        ) {
//            override fun getFilter(): Filter {
//                TODO("Not yet implemented")
//            }
//
//        }.apply {
//            submitList(
//                list
//            )
//        }
//    }


}

