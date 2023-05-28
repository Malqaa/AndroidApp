package com.malka.androidappp.newPhase.presentation.accountFragment.technicalSupportActivity.listtechincalSupportMessage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.contauctUsMessage.TechnicalSupportMessageDetails
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.presentation.accountFragment.AccountViewModel
import com.malka.androidappp.newPhase.presentation.accountFragment.technicalSupportActivity.addTechencalSupport.AddTechnicalSupportMessageActivity
import kotlinx.android.synthetic.main.activity_technical_support_list.*

import kotlinx.android.synthetic.main.toolbar_main.*

class TechnicalSupportListActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var technicalSupportListAdapter:TechnicalSupportListAdapter
    lateinit var  technicalSupportMessageList:ArrayList<TechnicalSupportMessageDetails>
    private lateinit var accountViewModel: AccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_technical_support_list)
        toolbar_title.text = getString(R.string.technical_support)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setUpRecyclerView()
        setClickListeners()
        setUpViewModel()
        onRefresh()
    }
    val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onRefresh()
            }
        }
    private fun setClickListeners() {
        back_btn.setOnClickListener {
            onBackPressed()
        }
        btnAddTechnicalSupport.setOnClickListener {
            activityLauncher.launch(
                Intent(
                    this,
                    AddTechnicalSupportMessageActivity::class.java
                )
            )
        }
    }

    private fun setUpViewModel() {
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        accountViewModel.isNetworkFail.observe(this) {
            if (it) {
                showMessageError(getString(R.string.connectionError))
            } else {
                showMessageError(getString(R.string.serverError))
            }

        }
        accountViewModel.errorResponseObserver.observe(this) {
            showMessageError(it.message)
        }
        accountViewModel.technicalSupportMessageListObserver.observe(this) { technicalSupportMessageListResp ->
            if(technicalSupportMessageListResp.status_code==200){
                if(technicalSupportMessageListResp.technicalSupportMessageList!=null){
                    technicalSupportMessageList.addAll(technicalSupportMessageListResp.technicalSupportMessageList)
                    technicalSupportListAdapter.notifyDataSetChanged()
                }else{
                    showMessageError(technicalSupportMessageListResp.message)
                }
            }else{
                showMessageError(technicalSupportMessageListResp.message)
            }
        }

    }
   private fun showMessageError(error: String?) {
        tvError.text=error?:getString(R.string.serverError)
        tvError.show()
    }
    private fun setUpRecyclerView() {
        technicalSupportMessageList= ArrayList()
        technicalSupportListAdapter=TechnicalSupportListAdapter(technicalSupportMessageList,object:TechnicalSupportListAdapter.SetonClickListeners{
            override fun onEditClickListener(position: Int) {
                activityLauncher.launch(
                    Intent(
                        this@TechnicalSupportListActivity,
                        AddTechnicalSupportMessageActivity::class.java
                    ).apply {
                        putExtra(ConstantObjects.isEditKey,true)
                        putExtra(ConstantObjects.idKey,technicalSupportMessageList[position].id)
                        putExtra(ConstantObjects.objectKey,technicalSupportMessageList[position])
                    }
                )
            }

        })
        rvMessages.apply {
            adapter=technicalSupportListAdapter
            layoutManager=linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing=false
        tvError.hide()
        technicalSupportMessageList.clear()
        technicalSupportListAdapter.notifyDataSetChanged()
        accountViewModel.getListContactUs()
    }
}