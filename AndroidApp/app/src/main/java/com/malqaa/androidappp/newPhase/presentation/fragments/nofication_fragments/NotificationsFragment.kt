package com.malqaa.androidappp.newPhase.presentation.fragments.nofication_fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentNotificationsBinding
import com.malqaa.androidappp.newPhase.domain.models.NotifyOut
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import kotlin.math.min


class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    var itemList: ArrayList<NotifyOut>? = null
    private var notificationAdapter: NotificationAdapter? = null
    private var notifyListViewModel: NotificationViewModel? = null
    private var currentPage = 1 // Track current page
    private val pageSize = 20 // Define page size

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNotificationsBinding.bind(view) // Initialize View Binding

        setupViewModel()
        itemList = arrayListOf()
        binding.toolbarMain.toolbarTitle.text = getString(R.string.Notifications)

        binding.toolbarMain.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }


        notificationAdapter = NotificationAdapter(arrayListOf())
        binding.notificationRcv.adapter = notificationAdapter

        // Implement RecyclerView pagination
        binding.notificationRcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = binding.notificationRcv.layoutManager?.itemCount
                val lastVisibleItem = findLastVisibleItemPosition()

                if (lastVisibleItem + 1 == totalItemCount) {
                    // Load more data when user reaches the end
                    currentPage++
                    loadData(currentPage)
                }
            }
        })

        // Initial data load
        loadData(currentPage)
    }

    private fun findLastVisibleItemPosition(): Int {
        val layoutManager = binding.notificationRcv.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val lastVisibleChild = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount
            return min(
                lastVisibleChild + 1,
                totalItemCount
            ) // Add 1 to include partially visible items
        }
        return RecyclerView.NO_POSITION
    }

    private fun loadData(page: Int) {
        // Simulate fetching data from a data source (e.g., API)
        notifyListViewModel?.getAllNotificationList(page, pageSize)

    }

    private fun setupViewModel() {
        notifyListViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        notifyListViewModel!!.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else {
                HelpFunctions.dismissProgressBar()
            }
        }

        notifyListViewModel!!.errorResponseObserver.observe(this) {
            HelpFunctions.dismissProgressBar()
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), requireActivity())
            } else {
                if (it.message != null) {
                    Toast.makeText(requireContext(), it.message!!, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.serverError),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }
        notifyListViewModel!!.notifyListRespObserver.observe(this) {
            HelpFunctions.dismissProgressBar()
            if (it.data.isNotEmpty()) {
                itemList?.addAll(it.data)
                notificationAdapter?.updateAdapter(itemList ?: arrayListOf())
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemList = null
        notificationAdapter = null
        notifyListViewModel?.closeAllCall()
        notifyListViewModel = null
    }
}
