package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.businessAccount.businessAccountsList

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Browser
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivitySwitchAccountBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.bussinessAccountsListResp.BusinessAccountDetials
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.businessAccount.BusinessAccountViewModel
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.businessAccount.addBusinessAccount.BusinessAccountCreateActivity
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import io.paperdb.Paper


class SwitchAccountActivity : BaseActivity<ActivitySwitchAccountBinding>(),
    BusinessAccountsAdapter.SetOnBusinessAccountSelected,
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var businessAccountsAdapter: BusinessAccountsAdapter
    lateinit var accountList: ArrayList<BusinessAccountDetials>
    private var userData: LoginUser? = null
    private var businessAccountViewModel: BusinessAccountViewModel? = null
    private val createAccountLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onRefresh()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySwitchAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.switch_accounts)
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
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
        businessAccountViewModel!!.isLoading.observe(this) {
            if (it)
                binding.progressBar.show()
            else
                binding.progressBar.hide()
        }


        businessAccountViewModel!!.isNetworkFail.observe(this) {
            if (it) {
                showErrorMessage(getString(R.string.noAccountFound))
            } else {
                showErrorMessage(getString(R.string.serverError))
            }

        }
        businessAccountViewModel!!.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showErrorMessage(it.message!!)
            } else {
                showErrorMessage(getString(R.string.serverError))
            }
        }
        businessAccountViewModel!!.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showErrorMessage(it.message!!)
            } else {
                showErrorMessage(getString(R.string.serverError))
            }
        }
        businessAccountViewModel!!.changeBusinessAccountObserver.observe(this) {
            if (it.status_code == 200 && it.businessAccount != null) {
                if (it.businessAccount.chanegeAccountUrl != null && it.businessAccount.chanegeAccountUrl != "")
                    openExternalLInk(
                        it.businessAccount.chanegeAccountUrl,
                        it.businessAccount.token ?: ""
                    )
            } else {
                if (it.message != null) {
                    showErrorMessage(it.message)
                } else {
                    showErrorMessage(getString(R.string.serverError))
                }
            }
        }
        businessAccountViewModel!!.businessAccountListObserver.observe(this) {
            if (it.status_code == 200) {
                if (it.businessAccountsList != null) {
                    accountList.clear()
                    accountList.addAll(it.businessAccountsList)
                    businessAccountsAdapter.notifyDataSetChanged()

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
        token: String
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
        binding.tvError.show()
        binding.tvError.text = message
    }

    private fun setUserData(userData: LoginUser?) {
        try {
            userData?.let {
                getPicassoInstance().load(userData.img)
                    .error(R.drawable.profileicon_bottomnav)
                    .placeholder(R.drawable.profileicon_bottomnav)
                    .into(binding.reviewProfilePic)
                binding.reviewName.text = "${userData.firstName} ${userData.lastName}"
            }
        } catch (_: Exception) {
        }
    }

    private fun setBusinessAccountsAdapter() {
        accountList = ArrayList()
        businessAccountsAdapter = BusinessAccountsAdapter(accountList, this)
        binding.businessRcv.apply {
            adapter = businessAccountsAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setViewClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.addBusinessAccountBtn.setOnClickListener {
            val intent =
                Intent(this@SwitchAccountActivity, BusinessAccountCreateActivity::class.java)
            createAccountLauncher.launch(intent)
        }
    }

    override fun setOnBusinessAccountSelected(position: Int) {

    }

    override fun setOnSwitchAccount(position: Int) {
        businessAccountViewModel!!.changeBusinessAccount(accountList[position].id)
        SharedPreferencesStaticClass.saveAccountId(accountList[position].id)
    }

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        accountList.clear()
        binding.tvError.hide()
        businessAccountViewModel!!.getBusinessAccount()
    }

    override fun onDestroy() {
        super.onDestroy()
        businessAccountViewModel!!.closeAllCall()
        businessAccountViewModel = null
    }

}

