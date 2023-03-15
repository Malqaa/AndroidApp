package com.malka.androidappp.newPhase.presentation.dialogsShared.regionDialog

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
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.regionsResp.Region
import com.malka.androidappp.newPhase.domain.models.regionsResp.RegionsResp
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.etSearch
import kotlinx.android.synthetic.main.dialog_countries.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class RegionDialog(context: Context,var countryId:Int, var getSelectedRegion: GetSelectedRegion) :
    BaseDialog(context), RegionAdapter.OnRegionSelected {


    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var regionAdapter: RegionAdapter
    lateinit var regionsList: ArrayList<Region>
    lateinit var mainRegionsList: ArrayList<Region>
    lateinit var regionsResp: RegionsResp


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
        tvTitle.text=context.getString(R.string.selectRegionTitle)
        ivClose.setOnClickListener {
            dismiss()
        }
        //=========
        mainRegionsList = ArrayList()
        regionsList = ArrayList()
        regionAdapter = RegionAdapter(context, regionsList, this)
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler.apply {
            layoutManager = linearLayoutManager
            adapter = regionAdapter
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etSearch.text.toString().trim() == "") {
                    updateList(mainRegionsList)
                } else {
                    if (s != null)
                        filter(s)
                }
            }
        })
        getCountries()

    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: CharSequence) {
        var temp: ArrayList<Region> = ArrayList()
        for (country in mainRegionsList) {
            if (country.name.lowercase().contains(text.toString().trim().lowercase())) {
                temp.add(country)
            }
        }
        //update recyclerview
        updateList(temp)
    }

    private fun updateList(temp: List<Region>) {
        regionsList.clear()
        regionsList.addAll(temp)
        if (temp.isEmpty()) {
            tvNoCountries.visibility = View.VISIBLE
        } else {
            tvNoCountries.visibility = View.GONE
        }
        regionAdapter.notifyDataSetChanged()
    }


    interface GetSelectedRegion {
        fun onSelectedRegion(id: Int, countryName: String)
    }

    override fun onRegionSelected(id: Int, countryName: String) {
        getSelectedRegion.onSelectedRegion(id, countryName)
        dismiss()
    }

    fun getCountries() {
        progressBar.visibility = View.VISIBLE
        countriesCallback = RetrofitBuilder.GetRetrofitBuilder().getRegionNew(countryId)
        countriesCallback?.enqueue(object : Callback<RegionsResp> {
            override fun onFailure(call: Call<RegionsResp>, t: Throwable) {
                // println("hhhh "+t.message)
                progressBar.visibility = View.GONE
                if (call.isCanceled) {

                } else if (t is HttpException) {
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
                            it
                            regionsResp = it
                            regionsResp.regionsList?.let { regionsList ->
                                //ConstantObjects.countryList = countryList
                                getRegionsList(regionsList)
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

    private fun getRegionsList(data: List<Region>) {
        mainRegionsList.clear()
        regionsList.clear()
        mainRegionsList.addAll(data)
        regionsList.addAll(data)
        regionAdapter.notifyDataSetChanged()

    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        if (countriesCallback != null) {
            countriesCallback?.cancel()
        }
    }
}