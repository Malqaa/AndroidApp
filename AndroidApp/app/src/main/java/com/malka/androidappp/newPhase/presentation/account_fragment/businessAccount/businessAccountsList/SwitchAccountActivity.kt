package com.malka.androidappp.newPhase.presentation.account_fragment.businessAccount.businessAccountsList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.account_fragment.businessAccount.addBusinessAccount.BusinessAccountCreateActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.domain.models.bussinessAccountsListResp.businessAccountDetails
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.presentation.account_fragment.businessAccount.BusinessAccountViewModel
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_switch_account.*
import kotlinx.android.synthetic.main.toolbar_main.*

class SwitchAccountActivity : BaseActivity(), BusinessAccountsAdapter.SetOnBusinessAccountSelected,
    SwipeRefreshLayout.OnRefreshListener {


    lateinit var businessAccountsAdapter: BusinessAccountsAdapter
    lateinit var accountList: ArrayList<businessAccountDetails>
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
                showErrorMessage(getString(R.string.connectionError))
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
        businessAccountViewModel.businessAccountListObserver.observe(this) {
            if (it.status_code == 200) {
                if (it.businessAccountsList != null && it.businessAccountsList.isNotEmpty()) {
                    accountList.clear()
                    accountList.addAll(it.businessAccountsList)
                    businessAccountsAdapter.notifyDataSetChanged()
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
            val intent = Intent(this@SwitchAccountActivity, BusinessAccountCreateActivity::class.java)
            creatAccountLauncher.launch(intent)
        }
    }

    override fun setOnBusinessAccountSelected(position: Int) {

    }

    override fun setOnSwitchAccount(position: Int) {

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
//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
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

