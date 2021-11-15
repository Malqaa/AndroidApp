package com.malka.androidappp.botmnav_fragments.create_ads

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_adv_models.*
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_confirmation.*
import kotlinx.android.synthetic.main.fragment_listing_fee.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListingFee : Fragment() {

    lateinit var categoryDataOrderSummary: TextView
    lateinit var listingFeeDataOrderSummary: TextView
    lateinit var featureDataOrderSummary: TextView
    lateinit var totalDataOrderSummary: TextView
    lateinit var finishButton: Button
    lateinit var radioButtonMaster: RadioButton
    lateinit var radioButtonVisa: RadioButton
    lateinit var editTextCVV: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listing_fee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        categoryDataOrderSummary = requireActivity().findViewById(R.id.categoryDataOrderSummary)
        listingFeeDataOrderSummary = requireActivity().findViewById(R.id.listingFeeDataOrderSummary)
        featureDataOrderSummary = requireActivity().findViewById(R.id.featureDataOrderSummary)
        totalDataOrderSummary = requireActivity().findViewById(R.id.totalDataOrderSummary)
        finishButton = requireActivity().findViewById(R.id.btn_finish)
        radioButtonMaster = requireActivity().findViewById(R.id.radio_btn_master)
        radioButtonVisa = requireActivity().findViewById(R.id.radio_btn_visa)
        editTextCVV = requireActivity().findViewById(R.id.editTextMasterCvv)


        ////////////////////////////////////////////////////////
        toolbar_listing_fee.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_listing_fee.title = getString(R.string.Listingfee)
        toolbar_listing_fee.setTitleTextColor(Color.WHITE)
        toolbar_listing_fee.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_listing_fee.setNavigationOnClickListener() {
            requireActivity().onBackPressed()
        }

        setData()
        radioButtonValidation()

        finishButton.setOnClickListener() {
//            createListing()
//            createCarAdvApiCall()
            HelpFunctions.startProgressBar(requireActivity())
            mainModelToJSON()

        }
    }

    private fun radioButtonValidation() {
        radioButtonMaster.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
            if (b) {
                radioButtonVisa.isChecked = false
                editTextCVV.isEnabled = true
                editTextCVV.isFocusableInTouchMode = true
            }
        })
        radioButtonVisa.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
            if (b) {
                radioButtonMaster.isChecked = false
                editTextCVV.isEnabled = false
                editTextCVV.isFocusableInTouchMode = false
                editTextCVV.setText("")
            }
        })

    }

    private fun setData() {
        categoryDataOrderSummary.text = StaticClassAdCreate.mainCategory
        listingFeeDataOrderSummary.text = "SAR 100"
        featureDataOrderSummary.text = "iphone a1586 model"
        totalDataOrderSummary.text = "SAR NaN"

    }

    private fun createListing() {
        // Vehicle
        if (StaticClassAdCreate.subcat == "Car") {
            createCarAdvApiCall()
        } else if (StaticClassAdCreate.subcat == "Bus") {
            createBusAdvApiCall()
        } else if (StaticClassAdCreate.subcat == "Motorbike") {
            createBikeAdvApiCall()
        } else if (StaticClassAdCreate.subcat == "Trailers") {
            createTrailersAdvApiCall()
        } else if (StaticClassAdCreate.subcat == "Diggers") {
            createDiggerAdvApiCall()
        } else if (StaticClassAdCreate.subcat == "Forklifts & pallet movers") {
            createForkliftsAndPalletMoversAdvApiCall()
        } else if (StaticClassAdCreate.subcat == "Trucks") {
            createTrucksAdvApiCall()
        } else if (StaticClassAdCreate.subcat == "Wrecked car") {
            createWreckedCarAdvApiCall()
        }
        // Property
        else if (StaticClassAdCreate.propertySubCatB == "Lands") {
            createCommercialLandsAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Shops") {
            createCommercialShopsAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Hotels") {
            createCommercialHotelsAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Factories") {
            createCommercialFactoriesAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Offices") {
            createCommercialOfficesAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Workshops") {
            createCommercialWorkshopsAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Warehouses") {
            createCommercialWarehousesAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Agriculture For Sale") {
            createAgricultureForSaleAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Agriculture For Rent") {
            createAgricultureForRentAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Land") {
            createResidentialLandsAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Villas") {
            createResidentialVillasAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Buildings") {
            createResidentialBuildingsAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Apartments") {
            createResidentialApartmentsAdvApiCall()
        } else if (StaticClassAdCreate.propertySubCatB == "Holiday House") {
            createResidentialHolidayHousesAdvApiCall()
        }
        // General
        else if (StaticClassAdCreate.subcat == "General") {
            createGeneralAdvApiCall()
        }
    }

    fun createGeneralAdApiCall() {
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: ModelDataCreateGeneralAd = ModelDataCreateGeneralAd(
            id = "",
            images = StaticClassAdCreate.images,
            name = StaticClassAdCreate.name,
            slug = StaticClassAdCreate.slug,
            tag = StaticClassAdCreate.tag,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            producttitle = StaticClassAdCreate.producttitle,
            Template = StaticClassAdCreate.template,
            iscontactphone = StaticClassAdCreate.iscontactphone,
            iscontactemail = StaticClassAdCreate.iscontactemail,
            iscontactchat = StaticClassAdCreate.iscontactchat,
            title = StaticClassAdCreate.title,
            user = ConstantObjects.logged_userid,
            subtitle = StaticClassAdCreate.subtitle,
            quantity = StaticClassAdCreate.quantity,
            phone = StaticClassAdCreate.phone,
            country = StaticClassAdCreate.country,
            region = StaticClassAdCreate.region,
            city = StaticClassAdCreate.city,
            address = StaticClassAdCreate.address,
            description = StaticClassAdCreate.description,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            price = StaticClassAdCreate.price,
            cash_pm = StaticClassAdCreate.iscashpaid,
            sa_bank_pm = StaticClassAdCreate.isbankpaid,
            fixLength = StaticClassAdCreate.fixLength,
            timepicker = StaticClassAdCreate.timepicker,
            pickup_option = StaticClassAdCreate.pickup_option,
            shipping_option = StaticClassAdCreate.shipping_option,
            cost_amount = StaticClassAdCreate.cost_amount,
            cost_desc = StaticClassAdCreate.cost_desc,
            pack4 = StaticClassAdCreate.pack4,
            iswatching = StaticClassAdCreate.iswatching,
            platform = "Android"
        )

        val gson = Gson()
        val jsonstring: String = gson.toJson(resp);

        val call: Call<ModelDataCreateGeneralAd> = malqaa.createAdss(resp)
        call.enqueue(object : Callback<ModelDataCreateGeneralAd> {

            override fun onResponse(
                call: Call<ModelDataCreateGeneralAd>, response: Response<ModelDataCreateGeneralAd>
            ) {
                if (response.isSuccessful) {
                    //val importAdId = response.body().da
                    //StaticClassAdCreate.id = importAdId!!
                    Toast.makeText(activity, "Ads has been created", Toast.LENGTH_LONG).show()
                    val navBar: BottomNavigationView = activity!!.findViewById(R.id.nav_view)
                    navBar.setVisibility(View.VISIBLE)
                    findNavController().navigate(R.id.navigation_home)
                } else {
                    Toast.makeText(activity, response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ModelDataCreateGeneralAd>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun createPropertyAdvApiCall() {
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvPropertyModel = CreateAdvPropertyModel(
            bedrooms = StaticClassAdCreate.bedrooms,
            bathrooms = StaticClassAdCreate.bathrooms,
            floorArea = StaticClassAdCreate.floorarea,
            landrrea = StaticClassAdCreate.landarea,
            floorsNumber = StaticClassAdCreate.floorarea,
            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.template,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,

            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime

        )

        val call: Call<CreateAdvPropertyModel> = malqaa.createPropertyAd(resp)
        call.enqueue(object : Callback<CreateAdvPropertyModel> {

            override fun onResponse(
                call: Call<CreateAdvPropertyModel>, response: Response<CreateAdvPropertyModel>
            ) {
                if (response.isSuccessful) {
                    //val importAdId = response.body().da
                    //StaticClassAdCreate.id = importAdId!!
                    Toast.makeText(activity, "Property Ads has been created", Toast.LENGTH_LONG)
                        .show()
                    val navBar: BottomNavigationView = activity!!.findViewById(R.id.nav_view)
                    navBar.setVisibility(View.VISIBLE)
                    //findNavController().navigate(R.id.navigation_home)
                } else {
                    Toast.makeText(activity, "Failed in Property Adv", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvPropertyModel>, t: Throwable) {
                Toast.makeText(activity, t.message + "Failed in Property Adv", Toast.LENGTH_LONG)
                    .show()
            }
        })


    }

    fun createAutoMobileAdApiCall() {
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: ModelDataCreateAutoMobile = ModelDataCreateAutoMobile(
            id = "",
            images = listOf<String>(),
            platenumber = StaticClassAdCreate.platenumber,
            body = StaticClassAdCreate.body,
            model = StaticClassAdCreate.model,
            make = StaticClassAdCreate.make,
            year = StaticClassAdCreate.year,
            kilometers = StaticClassAdCreate.kilometers,
            fuel = StaticClassAdCreate.fuel,
            squencenumber = StaticClassAdCreate.squencenumber,
            cylinders = StaticClassAdCreate.cylinders,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            producttitle = StaticClassAdCreate.producttitle,
            Template = StaticClassAdCreate.template,
            selectasmain = StaticClassAdCreate.selectasmain,
            title = StaticClassAdCreate.title,
            user = ConstantObjects.logged_userid,
            subtitle = StaticClassAdCreate.subtitle,
            quantity = StaticClassAdCreate.quantity,
            phone = StaticClassAdCreate.phone,
            country = StaticClassAdCreate.country,
            region = StaticClassAdCreate.region,
            city = StaticClassAdCreate.city,
            address = StaticClassAdCreate.address,
            description = StaticClassAdCreate.description,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            price = StaticClassAdCreate.price,
            cash_pm = StaticClassAdCreate.iscashpaid,
            sa_bank_pm = StaticClassAdCreate.isbankpaid,
            fixLength = StaticClassAdCreate.fixLength,
            timepicker = StaticClassAdCreate.timepicker,
            pickup_option = StaticClassAdCreate.pickup_option,
            shipping_option = StaticClassAdCreate.shipping_option,
            cost_amount = StaticClassAdCreate.cost_amount,
            cost_desc = StaticClassAdCreate.cost_desc,
            pack4 = StaticClassAdCreate.pack4,
            sellertype = StaticClassAdCreate.sellertype,
            noofpreviousowners = StaticClassAdCreate.noofpreviousowners,
            motorvehiclesperiodicinspection = StaticClassAdCreate.motorvehiclesperiodicinspection,
            transmission = StaticClassAdCreate.transmission,
            isimported = StaticClassAdCreate.isimported,
            isnogotiable = StaticClassAdCreate.isnogotiable

        )
        val gson = Gson()
        val jsonstring: String = gson.toJson(resp);

        val call: Call<ModelDataCreateAutoMobile> = malqaa.createAutoMobileAd(resp)


        call.enqueue(object : Callback<ModelDataCreateAutoMobile> {

            override fun onResponse(
                call: Call<ModelDataCreateAutoMobile>, response: Response<ModelDataCreateAutoMobile>
            ) {
                if (response.isSuccessful) {
                    //val importAdId = response.body().da
                    //StaticClassAdCreate.id = importAdId!!
                    //Log.d("asdw",jsonstring)
                    Toast.makeText(activity, "AutoMobile Adv has been Created", Toast.LENGTH_LONG)
                        .show()
                    val navBar: BottomNavigationView = activity!!.findViewById(R.id.nav_view)
                    navBar.setVisibility(View.VISIBLE)
                    findNavController().navigate(R.id.navigation_home)
                } else {
                    Toast.makeText(activity, response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ModelDataCreateAutoMobile>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    // Vehicle Sub categories
    private fun createCarAdvApiCall() {

        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp = CreateAdvCarModel(
            platenumber = StaticClassAdCreate.platenumber,
            squencenumber = StaticClassAdCreate.squencenumber,
            make = StaticClassAdCreate.make,
            model = StaticClassAdCreate.model,
            body = StaticClassAdCreate.body,
            year = StaticClassAdCreate.year,
            kilometers = StaticClassAdCreate.kilometers,
            fuel = StaticClassAdCreate.fuel,
            cylinders = StaticClassAdCreate.cylinders,
            noofpreviousowner = StaticClassAdCreate.noofpreviousowners,
            transmission = StaticClassAdCreate.transmission,
            vehiclesperiodicinspection = StaticClassAdCreate.motorvehiclesperiodicinspection,
            isimported = StaticClassAdCreate.isimported,
            isnogotiable = StaticClassAdCreate.isnogotiable,
            sellertype = StaticClassAdCreate.sellertype,
            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = "Car",
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,

            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            Images = StaticClassAdCreate.images,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid
        )

        val call: Call<CreateAdvResponseBack> = malqaa.createCarAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {
                    val importAdId = response.body()!!.data

                    //val gson = Gson()
                    //val jsonstring: String = gson.toJson(resp);
                    //Log.d("myactivityjson",jsonstring)
                    Toast.makeText(
                        activity,
                        importAdId + "Car Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                } else {
                    Toast.makeText(activity, "Failed in Car Adv", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message + "Failed in Car Adv", Toast.LENGTH_LONG)
                    .show()
            }
        })


    }

    private fun createBusAdvApiCall() {
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvBusModel = CreateAdvBusModel(

            make = StaticClassAdCreate.make,
            model = StaticClassAdCreate.model,
            year = StaticClassAdCreate.year,
            kilometers = StaticClassAdCreate.kilometers,
            fueltype = StaticClassAdCreate.fuel,
            cylinders = StaticClassAdCreate.cylinders,
            transmission = StaticClassAdCreate.transmission,
            motorvehiclesperiodicinspection = StaticClassAdCreate.motorvehiclesperiodicinspection,
            numberofpassengers = StaticClassAdCreate.numberOfPassengers,
            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid
        )

        val call: Call<CreateAdvResponseBack> = malqaa.createbusAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data

                    Toast.makeText(
                        activity,
                        importAdId + "Bus Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                } else {
                    Toast.makeText(activity, "Failed in Bus Adv", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message + "Failed in Bus Adv", Toast.LENGTH_LONG)
                    .show()
            }
        })


    }

    private fun createBikeAdvApiCall() {
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp = CreateAdvMotorbikeModel(

            Style = StaticClassAdCreate.style,
            Make = StaticClassAdCreate.make,
            Model = StaticClassAdCreate.model,
            Year = StaticClassAdCreate.year,
            Kilometers = StaticClassAdCreate.kilometers,
            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createbikeAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    //StaticClassAdCreate.id = importAdId!!
                    val importAdId = response.body()!!.data

                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)
                    Toast.makeText(
                        activity,
                        importAdId + "MotorBike Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    Toast.makeText(activity, "Failed in Motorbike Adv", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message + "Failed in Motorbike Adv", Toast.LENGTH_LONG)
                    .show()
            }
        })


    }

    private fun createTrailersAdvApiCall() {
        StaticClassAdCreate.subcat = "WreckedCarTrucksDiggersForklifts"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp = CreateAdvWreckedCarTrucksDiggersForkliftsModel(

            make = StaticClassAdCreate.make,
            model = StaticClassAdCreate.model,
            year = StaticClassAdCreate.year,
            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createTrailersAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {


                    val importAdId = response.body()!!.data


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)
                    //val gson = Gson()
                    //val jsonstring: String = gson.toJson(resp);
                    //Log.d("myactivityjson",jsonstring)
                    Toast.makeText(
                        activity,
                        importAdId + "TrailerBike Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                } else {
                    Toast.makeText(activity, "Failed in Trailer Adv", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message + "Failed in Trailer Adv", Toast.LENGTH_LONG)
                    .show()
            }
        })


    }

    private fun createDiggerAdvApiCall() {
        StaticClassAdCreate.subcat = "WreckedCarTrucksDiggersForklifts"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp = CreateAdvWreckedCarTrucksDiggersForkliftsModel(

            make = StaticClassAdCreate.make,
            model = StaticClassAdCreate.model,
            year = StaticClassAdCreate.year,
            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.creatediggerAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data

                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)
                    Toast.makeText(
                        activity,
                        importAdId + "Digger Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()

                } else {
                    Toast.makeText(activity, "Failed in Digger Adv", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message + "Failed in Digger Adv", Toast.LENGTH_LONG)
                    .show()
            }
        })


    }

    private fun createForkliftsAndPalletMoversAdvApiCall() {
        StaticClassAdCreate.subcat = "WreckedCarTrucksDiggersForklifts"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp = CreateAdvWreckedCarTrucksDiggersForkliftsModel(

            make = StaticClassAdCreate.make,
            model = StaticClassAdCreate.model,
            year = StaticClassAdCreate.year,
            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createForkliftsAndPalletMoversAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data

                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)
                    Toast.makeText(
                        activity,
                        importAdId + "Forklift Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    Toast.makeText(activity, "Failed in Forklift Adv", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message + "Failed in Adv", Toast.LENGTH_LONG)
                    .show()
            }
        })


    }

    private fun createTrucksAdvApiCall() {
        StaticClassAdCreate.subcat = "WreckedCarTrucksDiggersForklifts"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp = CreateAdvWreckedCarTrucksDiggersForkliftsModel(

            make = StaticClassAdCreate.make,
            model = StaticClassAdCreate.model,
            year = StaticClassAdCreate.year,
            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createTrucksAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data

                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)
                    Toast.makeText(
                        activity,
                        importAdId + "Truck Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    Toast.makeText(activity, "Failed in Truck Adv", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message + "Failed in Truck Adv", Toast.LENGTH_LONG)
                    .show()
            }
        })


    }

    private fun createWreckedCarAdvApiCall() {
        StaticClassAdCreate.subcat = "WreckedCarTrucksDiggersForklifts"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp = CreateAdvWreckedCarTrucksDiggersForkliftsModel(

            make = StaticClassAdCreate.make,
            model = StaticClassAdCreate.model,
            year = StaticClassAdCreate.year,
            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createWreckedCarAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data

                    val gson = Gson()
                    val jsonstring: String = gson.toJson(resp);
                    Log.d("myactivityjson", jsonstring)
                    Toast.makeText(
                        activity,
                        importAdId + "WreckedCar Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()

                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                } else {
                    Toast.makeText(activity, "Failed in WreckedCar Adv", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message + "Failed in WreckedCar Adv", Toast.LENGTH_LONG)
                    .show()
            }
        })


    }

    // Property Sub Categories
    private fun createCommercialLandsAdvApiCall() {
        StaticClassAdCreate.subcat = "LSOWWFASAR"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvLSOWWFASARModel = CreateAdvLSOWWFASARModel(

            floorArea = StaticClassAdCreate.floorarea,
            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createLSOWWFASARAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>,
                response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Commercial Land Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                    //val navBar: BottomNavigationView = activity!!.findViewById(R.id.nav_view)
                    //navBar.setVisibility(View.VISIBLE)
                    //findNavController().navigate(R.id.navigation_home)
                } else {
                    Toast.makeText(activity, "Failed in Commercial Land Adv", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Commercial Land Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createCommercialShopsAdvApiCall() {
        StaticClassAdCreate.subcat = "LSOWWFASAR"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvLSOWWFASARModel = CreateAdvLSOWWFASARModel(

            floorArea = StaticClassAdCreate.floorarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createLSOWWFASARAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>,
                response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {


                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Commercial Shop Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

//                    //StaticClassAdCreate.id = importAdId!!
//                    Toast.makeText(activity, "Ad has been created", Toast.LENGTH_LONG)
//                        .show()
//                    val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
//                    navBar.setVisibility(View.VISIBLE)
//                    //findNavController().navigate(R.id.navigation_home)
                } else {
                    Toast.makeText(activity, "Failed in Commercial Shop Adv", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Commercial Shop  Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createCommercialHotelsAdvApiCall() {
        StaticClassAdCreate.subcat = "CommercialForSaleLeaseHotels"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvCommercialHotelModel = CreateAdvCommercialHotelModel(

            floorArea = StaticClassAdCreate.floorarea,
            landArea = StaticClassAdCreate.landarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createCommercialHotelAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>,
                response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data


                    Log.d("activity", importAdId)
                    Toast.makeText(
                        activity,
                        importAdId + "Hotel Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                } else {
                    Toast.makeText(activity, "Failed in Commercial Hotel Adv", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Commercial Hotel Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createCommercialFactoriesAdvApiCall() {
        StaticClassAdCreate.subcat = "LSOWWFASAR"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvLSOWWFASARModel = CreateAdvLSOWWFASARModel(

            floorArea = StaticClassAdCreate.floorarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createLSOWWFASARAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {


                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Factory Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)


                } else {
                    Toast.makeText(activity, "Failed in Commercial Factory Adv", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Commercial Factory Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createCommercialOfficesAdvApiCall() {
        StaticClassAdCreate.subcat = "LSOWWFASAR"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvLSOWWFASARModel = CreateAdvLSOWWFASARModel(

            floorArea = StaticClassAdCreate.floorarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createLSOWWFASARAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>,
                response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {
                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Office Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)
                } else {
                    Toast.makeText(activity, "Failed in Commercial Office Adv", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Commercial Office Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createCommercialWorkshopsAdvApiCall() {
        StaticClassAdCreate.subcat = "LSOWWFASAR"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvLSOWWFASARModel = CreateAdvLSOWWFASARModel(

            floorArea = StaticClassAdCreate.floorarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createLSOWWFASARAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>,
                response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Workshop Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                } else {
                    Toast.makeText(activity, "Failed in Commercial Workshop Adv", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Commercial Workshop Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createCommercialWarehousesAdvApiCall() {
        StaticClassAdCreate.subcat = "LSOWWFASAR"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvLSOWWFASARModel = CreateAdvLSOWWFASARModel(

            floorArea = StaticClassAdCreate.floorarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createLSOWWFASARAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>,
                response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {
                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Warehouse Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                } else {
                    Toast.makeText(
                        activity,
                        "Failed in Commercial Warehouse Adv",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Commercial Warehouse Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createAgricultureForSaleAdvApiCall() {
        StaticClassAdCreate.subcat = "LSOWWFASAR"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvLSOWWFASARModel = CreateAdvLSOWWFASARModel(

            floorArea = StaticClassAdCreate.floorarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createLSOWWFASARAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Agriculture for sale Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                } else {
                    Toast.makeText(activity, "Failed in Agriculture ForSale Adv", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Agriculture ForSale Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createAgricultureForRentAdvApiCall() {
        StaticClassAdCreate.subcat = "LSOWWFASAR"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvLSOWWFASARModel = CreateAdvLSOWWFASARModel(

            floorArea = StaticClassAdCreate.floorarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createLSOWWFASARAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Agriculture for rent Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                } else {
                    Toast.makeText(activity, "Failed in Agriculture ForRent Adv", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Agriculture ForRent Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })


    }

    private fun createResidentialLandsAdvApiCall() {
        StaticClassAdCreate.subcat = "LSOWWFASAR"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvLSOWWFASARModel = CreateAdvLSOWWFASARModel(
            floorArea = StaticClassAdCreate.floorarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createLSOWWFASARAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>,
                response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Residential land Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)


                } else {
                    Toast.makeText(activity, "Failed in Residential Land Adv", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Residential Land Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createResidentialVillasAdvApiCall() {
        StaticClassAdCreate.subcat = "ResidentialRentSaleVillasHHouse"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvRHHRVModel = CreateAdvRHHRVModel(
            bedrooms = StaticClassAdCreate.bedrooms,
            bathrooms = StaticClassAdCreate.bathrooms,
            floorArea = StaticClassAdCreate.floorarea,
            landArea = StaticClassAdCreate.landarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createRHHRVAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>,
                response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {
                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Residential villa Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                } else {
                    Toast.makeText(activity, "Failed in Residential Villa Adv", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Residential Villa Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createResidentialBuildingsAdvApiCall() {
        StaticClassAdCreate.subcat = "ResidentialRentSaleBuildings"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvRBuildingsModel = CreateAdvRBuildingsModel(
            bedrooms = StaticClassAdCreate.bedrooms,
            bathrooms = StaticClassAdCreate.bathrooms,
            floorArea = StaticClassAdCreate.floorarea,
            landArea = StaticClassAdCreate.landarea,
            floorNumber = StaticClassAdCreate.floorsnumber,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createResidentialBuildingsAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>,
                response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Residential building Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)
                } else {
                    Toast.makeText(
                        activity,
                        "Failed in Residential Building Adv",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Residential Building Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createResidentialApartmentsAdvApiCall() {
        StaticClassAdCreate.subcat = "PropertyResidentialRentSaleAppartments"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvRApartmentsModel = CreateAdvRApartmentsModel(
            bedrooms = StaticClassAdCreate.bedrooms,
            bathrooms = StaticClassAdCreate.bathrooms,
            floorArea = StaticClassAdCreate.floorarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createResidentialApartmentsAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>,
                response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Residential Apartment Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)


                } else {
                    Toast.makeText(
                        activity,
                        "Failed in Residential Apartment Adv",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Residential Apartment Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun createResidentialHolidayHousesAdvApiCall() {
        StaticClassAdCreate.subcat = "ResidentialRentSaleVillasHHouse"
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvRHHRVModel = CreateAdvRHHRVModel(
            bedrooms = StaticClassAdCreate.bedrooms,
            bathrooms = StaticClassAdCreate.bathrooms,
            floorArea = StaticClassAdCreate.floorarea,
            landArea = StaticClassAdCreate.landarea,

            //

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            Name = StaticClassAdCreate.name,
            Slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,

            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,

            DateTime = StaticClassAdCreate.duration,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid

        )

        val call: Call<CreateAdvResponseBack> = malqaa.createRHHRVAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>,
                response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {
                    val importAdId = response.body()!!.data


                    Toast.makeText(
                        activity,
                        importAdId + "Residential Holiday house Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                } else {
                    Toast.makeText(
                        activity,
                        "Failed in Residential Holiday House Adv",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed in Residential Holiday House Adv",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    // General
    private fun createGeneralAdvApiCall() {
        val malqaa: MalqaApiService = RetrofitBuilder.createAd()
        val resp: CreateAdvgeneralModel = CreateAdvgeneralModel(

            isnogotiable = StaticClassAdCreate.isnogotiable.toString(),
            isphonehidden = StaticClassAdCreate.isphonehidden.toString(),
            tag = StaticClassAdCreate.tag,
            isfeatured = StaticClassAdCreate.isfeatured.toString(),
            iscontactphone = StaticClassAdCreate.iscontactphone.toString(),
            iscontactemail = StaticClassAdCreate.iscontactemail.toString(),
            iscontactchat = StaticClassAdCreate.iscontactchat.toString(),
            Questions = "",

            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            name = StaticClassAdCreate.name,
            slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.subcat,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            Images = StaticClassAdCreate.images,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            isbankpaid = StaticClassAdCreate.isbankpaid,
            iscashpaid = StaticClassAdCreate.iscashpaid
        )

        val call: Call<CreateAdvResponseBack> = malqaa.creategeneralAd(resp)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {

                    val importAdId = response.body()!!.data

                    Toast.makeText(
                        activity,
                        importAdId + "General Ad has been created",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                } else {
                    Toast.makeText(activity, "Failed in General Adv", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message + "Failed in General Adv", Toast.LENGTH_LONG)
                    .show()
            }
        })


    }


    // Methods to handle dynamic ad creation
    private fun mainModelToJSON() {
        val mainModel = CreateAdvMainModel(
            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            name = StaticClassAdCreate.name,
            slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.template,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,
            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,
            isActive = false,
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            Images = StaticClassAdCreate.images,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid,
            subcatone = StaticClassAdCreate.subcatone,
            subcattwo = StaticClassAdCreate.subcattwo,
            subcatthree = StaticClassAdCreate.subcatthree,
            subcatfour = StaticClassAdCreate.subcatfour,
            subcatfive = StaticClassAdCreate.subcatfive,
            subcatsix = StaticClassAdCreate.subcatsix,
            subcatonekey = StaticClassAdCreate.subcatonekey,
            subcattwokey = StaticClassAdCreate.subcattwokey,
            subcatthreekey = StaticClassAdCreate.subcatthreekey,
            subcatfourkey = StaticClassAdCreate.subcatfourkey,
            subcatfivekey = StaticClassAdCreate.subcatfivekey,
            subcatsixkey = StaticClassAdCreate.subcatsixkey,
            category = StaticClassAdCreate.subCategoryPath[0],
            Video = StaticClassAdCreate.video
        )

        // Model Class to JSON String
        val jsonString = Gson().toJson(mainModel)

        // JSON String HashMap
        var map: Map<String, String> = HashMap()
        map = Gson().fromJson(jsonString, map.javaClass)

        // Merging both HashMaps
        ConstantObjects.dynamic_json_dictionary.putAll(map)

        createAllAds(ConstantObjects.dynamic_json_dictionary)

    }

    private fun createAllAds(data: HashMap<String, String>) {

        val malqaa: MalqaApiService = RetrofitBuilder.createAd()

        val call: Call<CreateAdvResponseBack> = malqaa.createAllAd(data)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {
                    val importAdId = response.body()!!.data

                    HelpFunctions.dismissProgressBar()
                    HelpFunctions.ShowLongToast(
                        importAdId + " " + getString(R.string.Youradhasbeencreatedsuccessfully),
                        context
                    )

//                    Toast.makeText(activity, importAdId + "Ad has been created", Toast.LENGTH_LONG).show()

                    val args = Bundle()
                    args.putString("AdvId", importAdId)
                    args.putString("Template", StaticClassAdCreate.template)
                    args.putString("sellerID", ConstantObjects.logged_userid)
                    NavHostFragment.findNavController(this@ListingFee)
                        .navigate(R.id.prom_continue, args)

                    AddPhotoFragment.clearPath()

                } else {
                    HelpFunctions.dismissProgressBar()
                    HelpFunctions.ShowLongToast(getString(R.string.something_went_wrong), context)
//                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                HelpFunctions.ShowLongToast(t.message + " " + getString(R.string.failed), context)
//                Toast.makeText(activity, t.message + "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }
}