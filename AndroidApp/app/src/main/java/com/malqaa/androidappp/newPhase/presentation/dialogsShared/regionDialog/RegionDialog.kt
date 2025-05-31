package com.malqaa.androidappp.newPhase.presentation.dialogsShared.regionDialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogCountriesBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.Region
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.RegionsResp
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class RegionDialog(
    context: Context,
    private var countryId: Int,
    private var getSelectedRegion: GetSelectedRegion
) : BaseDialog<DialogCountriesBinding>(context), RegionAdapter.OnRegionSelected {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var regionAdapter: RegionAdapter
    private lateinit var regionsList: ArrayList<Region>
    private lateinit var mainRegionsList: ArrayList<Region>
    private lateinit var regionsResp: RegionsResp

    var countriesCallback: Call<RegionsResp>? = null

    override fun isLoadingDialog(): Boolean {
        return false
    }

    override fun inflateViewBinding(): DialogCountriesBinding {
        return DialogCountriesBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun isCancelable(): Boolean {
        return true
    }


    override fun initialization() {
        binding.tvTitleAr.text = context.getString(R.string.selectRegionTitle)
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        //=========
        mainRegionsList = ArrayList()
        regionsList = ArrayList()
        regionAdapter = RegionAdapter(context, regionsList, this)
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recycler.apply {
            layoutManager = linearLayoutManager
            adapter = regionAdapter
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.etSearch.text.toString().trim() == "") {
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
        val temp: ArrayList<Region> = ArrayList()
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
            binding.tvNoCountries.visibility = View.VISIBLE
        } else {
            binding.tvNoCountries.visibility = View.GONE
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

    private fun getCountries() {
        binding.progressBar.visibility = View.VISIBLE
        countriesCallback = getRetrofitBuilder().getRegionNew(countryId)
        countriesCallback?.enqueue(object : Callback<RegionsResp> {
            override fun onFailure(call: Call<RegionsResp>, t: Throwable) {
                // println("hhhh "+t.message)
                binding.progressBar.visibility = View.GONE
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
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
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