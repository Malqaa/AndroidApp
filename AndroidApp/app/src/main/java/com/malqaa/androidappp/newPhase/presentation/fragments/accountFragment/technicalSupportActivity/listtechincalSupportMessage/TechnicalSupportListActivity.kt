package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.technicalSupportActivity.listtechincalSupportMessage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityTechnicalSupportListBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.contauctUsMessage.TechnicalSupportMessageDetails
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.technicalSupportActivity.addTechencalSupport.AddTechnicalSupportMessageActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show

class TechnicalSupportListActivity : BaseActivity<ActivityTechnicalSupportListBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var technicalSupportListAdapter: TechnicalSupportListAdapter
    private lateinit var technicalSupportMessageList: ArrayList<TechnicalSupportMessageDetails>
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityTechnicalSupportListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.technical_support)
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
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
        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.btnAddTechnicalSupport.setOnClickListener {
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
                binding.progressBar.show()
            else
                binding.progressBar.hide()
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
            if (technicalSupportMessageListResp.status_code == 200) {
                if (technicalSupportMessageListResp.technicalSupportMessageList != null) {
                    technicalSupportMessageList.addAll(technicalSupportMessageListResp.technicalSupportMessageList)
                    technicalSupportListAdapter.notifyDataSetChanged()
                } else {
                    showMessageError(technicalSupportMessageListResp.message)
                }
            } else {
                showMessageError(technicalSupportMessageListResp.message)
            }
        }

    }

    private fun showMessageError(error: String?) {
        binding.tvError.text = error ?: getString(R.string.serverError)
        binding.tvError.show()
    }

    private fun setUpRecyclerView() {
        technicalSupportMessageList = ArrayList()
        technicalSupportListAdapter = TechnicalSupportListAdapter(technicalSupportMessageList,
            object : TechnicalSupportListAdapter.SetonClickListeners {
                override fun onEditClickListener(position: Int) {
                    activityLauncher.launch(
                        Intent(
                            this@TechnicalSupportListActivity,
                            AddTechnicalSupportMessageActivity::class.java
                        ).apply {
                            putExtra(ConstantObjects.isEditKey, true)
                            putExtra(
                                ConstantObjects.idKey,
                                technicalSupportMessageList[position].id
                            )
                            putExtra(
                                ConstantObjects.objectKey,
                                technicalSupportMessageList[position]
                            )
                        }
                    )
                }

            })
        binding.rvMessages.apply {
            adapter = technicalSupportListAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        binding.tvError.hide()
        technicalSupportMessageList.clear()
        technicalSupportListAdapter.notifyDataSetChanged()
        accountViewModel.getListContactUs()
    }

    override fun onDestroy() {
        super.onDestroy()
        accountViewModel.closeAllCall()
    }

}