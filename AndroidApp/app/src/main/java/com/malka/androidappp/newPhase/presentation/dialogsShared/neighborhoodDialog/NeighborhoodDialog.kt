package com.malka.androidappp.newPhase.presentation.dialogsShared.neighborhoodDialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.regionsResp.Region
import com.malka.androidappp.newPhase.domain.models.regionsResp.RegionsResp
import com.malka.androidappp.newPhase.presentation.dialogsShared.regionDialog.RegionAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.etSearch
import kotlinx.android.synthetic.main.dialog_countries.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class NeighborhoodDialog(context: Context, var regionId:Int, var getSelectedNeighborhood: GetSelectedNeighborhood) :
    BaseDialog(context), RegionAdapter.OnRegionSelected {


    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var neighborhoodAdapter: RegionAdapter
    lateinit var neighborhoodsList: ArrayList<Region>
    lateinit var mainNeighborhoodList: ArrayList<Region>
    lateinit var neighborhoodsResp: RegionsResp


    var countriesCallback: Call<RegionsResp>? = null

    override fun getViewId(): Int {
        return R.layout.dialog_countries
    }


    override fun isLoadingDialog(): Boolean {
        return false
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun isCancelable(): Boolean {
        return true
    }


    override fun initialization() {
        tvTitleAr.text=context.getString(R.string.selectDistrict)
        ivClose.setOnClickListener {
            dismiss()
        }
        mainNeighborhoodList = ArrayList()
        neighborhoodsList = ArrayList()
        neighborhoodAdapter = RegionAdapter(context, neighborhoodsList, this)
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler.apply {
            layoutManager = linearLayoutManager
            adapter = neighborhoodAdapter
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etSearch.text.toString().trim() == "") {
                    updateList(mainNeighborhoodList)
                } else {
                    if (s != null)
                        filter(s)
                }
            }
        })
        getNeighborhoods()

    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: CharSequence) {
        var temp: ArrayList<Region> = ArrayList()
        for (country in mainNeighborhoodList) {
            if (country.name.lowercase().contains(text.toString().trim().lowercase())) {
                temp.add(country)
            }
        }
        updateList(temp)
    }

    private fun updateList(temp: List<Region>) {
        neighborhoodsList.clear()
        neighborhoodsList.addAll(temp)
        if (temp.isEmpty()) {
            tvNoCountries.visibility = View.VISIBLE
        } else {
            tvNoCountries.visibility = View.GONE
        }
        neighborhoodAdapter.notifyDataSetChanged()
    }


    interface GetSelectedNeighborhood {
        fun onSelectedNeighborhood(id: Int, neighborhoodName: String)
    }

    override fun onRegionSelected(id: Int, neighborhoodName: String) {
        getSelectedNeighborhood.onSelectedNeighborhood(id, neighborhoodName)
        dismiss()
    }

    fun getNeighborhoods() {
        progressBar.visibility = View.VISIBLE
        countriesCallback = getRetrofitBuilder().getNeighborhoodByRegionNew(regionId)
        countriesCallback?.enqueue(object : Callback<RegionsResp> {
            override fun onFailure(call: Call<RegionsResp>, t: Throwable) {
                // println("hhhh "+t.message)
                progressBar.visibility = View.GONE
                if (t is HttpException) {
                    HelpFunctions.ShowLongToast(context.getString(R.string.serverError), context)

                } else {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.connectionError),
                        context
                    )
                }
            }

            override fun onResponse(
                call: Call<RegionsResp>,
                response: Response<RegionsResp>
            ) {
                progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            neighborhoodsResp = it
                            neighborhoodsResp.regionsList?.let { regionsList ->
                                getNeighborhoodList(regionsList)
                            }
                        }

                    } else {
                        HelpFunctions.ShowLongToast(
                            context.getString(R.string.serverError),
                            context
                        )

                    }
                } catch (e: Exception) {
                }
            }

        })
    }

    private fun getNeighborhoodList(data: List<Region>) {
        mainNeighborhoodList.clear()
        neighborhoodsList.clear()
        mainNeighborhoodList.addAll(data)
        neighborhoodsList.addAll(data)
        neighborhoodAdapter.notifyDataSetChanged()

    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        if (countriesCallback != null) {
            countriesCallback?.cancel()
        }
    }
}