package com.malka.androidappp.newPhase.presentation.dialogsShared.countryDialog

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
import com.malka.androidappp.newPhase.domain.models.countryResp.CountriesResp
import com.malka.androidappp.newPhase.domain.models.countryResp.Country
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.etSearch
import kotlinx.android.synthetic.main.dialog_countries.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class CountryDialog(context: Context, var getSelectedCountry: GetSelectedCountry) :
    BaseDialog(context), CountriesAdapter.OnCountrySelected {


    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var countriesAdapter: CountriesAdapter
    lateinit var countriesList: ArrayList<Country>
    lateinit var mainCountriesList: ArrayList<Country>
    lateinit var countryResp: CountriesResp


    var countriesCallback: Call<CountriesResp>? = null

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
        tvTitleAr.text=context.getString(R.string.selectCountry)
        ivClose.setOnClickListener {
            dismiss()
        }
        //=========
        mainCountriesList = ArrayList()
        countriesList = ArrayList()
        countriesAdapter = CountriesAdapter(context, countriesList, this)
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler.apply {
            layoutManager = linearLayoutManager
            adapter = countriesAdapter
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etSearch.text.toString().trim() == "") {
                    updateList(mainCountriesList)
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
        var temp: ArrayList<Country> = ArrayList()
        for (country in mainCountriesList) {
            if (country.name.lowercase().contains(text.toString().trim().lowercase())) {
                temp.add(country)
            }
        }
        //update recyclerview
        updateList(temp)
    }

    private fun updateList(temp: List<Country>) {
        countriesList.clear()
        countriesList.addAll(temp)
        if (temp.isEmpty()) {
            tvNoCountries.visibility = View.VISIBLE
        } else {
            tvNoCountries.visibility = View.GONE
        }
        countriesAdapter.notifyDataSetChanged()
    }


    interface GetSelectedCountry {
        fun onSelectedCountry(id: Int, countryName: String, countryFlag: String?,countryCode:String?)
    }

    override fun onCountrySelected(id: Int, countryName: String, countryFlag: String?,countryCode:String?) {
        getSelectedCountry.onSelectedCountry(id, countryName, countryFlag,countryCode)
        dismiss()
    }

    fun getCountries() {
        progressBar.visibility = View.VISIBLE
        countriesCallback = RetrofitBuilder.GetRetrofitBuilder().getCountryNew()
        countriesCallback?.enqueue(object : Callback<CountriesResp> {
            override fun onFailure(call: Call<CountriesResp>, t: Throwable) {
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
                call: Call<CountriesResp>,
                response: Response<CountriesResp>
            ) {
                progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            it
                            countryResp = it
                            countryResp.countriesList?.let { countryList ->
                                //ConstantObjects.countryList = countryList
                                getCountriesList(countryList)
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

    private fun getCountriesList(data: List<Country>) {
        mainCountriesList.clear()
        countriesList.clear()
        mainCountriesList.addAll(data)
        countriesList.addAll(data)
        countriesAdapter.notifyDataSetChanged()

    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        if (countriesCallback != null) {
            countriesCallback?.cancel()
        }
    }
}