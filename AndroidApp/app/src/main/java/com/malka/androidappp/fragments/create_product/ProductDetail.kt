package com.malka.androidappp.fragments.create_product

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.malka.androidappp.R
import com.malka.androidappp.fragments.cardetail_page.ModelAddSellerFav
import com.malka.androidappp.fragments.cardetail_page.ModelSellerDetails
import com.malka.androidappp.fragments.cardetail_page.bottomsheet_bidopt.BottomsheetDialogfragClass
import com.malka.androidappp.fragments.cardetail_page.bottomsheet_bidopt.StoreDataForAdDetail
import com.malka.androidappp.fragments.cardetail_page.bottomsheet_bidopt.getbidModel.ModelBidingResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.questionModel.ModelQuesAnswr
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.fragments.create_product.imageslider.ViewPagerAdapter
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import kotlinx.android.synthetic.main.carspce_card2.*
import kotlinx.android.synthetic.main.carspec_card1.*
import kotlinx.android.synthetic.main.carspec_card3.*
import kotlinx.android.synthetic.main.carspec_card5.*
import kotlinx.android.synthetic.main.carspec_card6.*
import kotlinx.android.synthetic.main.carspec_card8.*
import kotlinx.android.synthetic.main.carsspec_card4.*
import kotlinx.android.synthetic.main.fragment_car_specifics.bckscarpces
import kotlinx.android.synthetic.main.fragment_product_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class ProductDetail : Fragment(), BottomsheetDialogfragClass.BottomSheetListener {


    var AdvId: String = ""
    var sellerID: String = ""

    var totalQuestions: Int = 0

    lateinit var watchlistButton: ImageView
    lateinit var giveFeedback: Button
    lateinit var addToFavorites: ImageButton

    lateinit var sellerImage: ImageView
    lateinit var sellerName: TextView
    lateinit var sellerLocation: TextView
    lateinit var sellerEmail: TextView
    lateinit var sellerMobile: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        

        AdvId = arguments?.getString("AdvId").toString()
        sellerID = arguments?.getString("sellerID").toString()
        StoreDataForAdDetail.saveAdvIdforAdDetailBiding = AdvId
        HelpFunctions.startProgressBar(requireActivity())
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }


    override fun onButtonClicked(text: String?) {
        textView19.text = text
    }

    var viewPager: ViewPager? = null
    var sliderDotspanel: LinearLayout? = null
    private var dotscount = 0
    private lateinit var dots: Array<ImageView?>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = requireActivity().findViewById<View>(R.id.viewPagerProduct) as ViewPager
        sliderDotspanel =
            requireActivity().findViewById<View>(R.id.SliderDotsProduct) as LinearLayout

        watchlistButton = requireActivity().findViewById(R.id.watchbutton)
        giveFeedback = requireActivity().findViewById(R.id.btn_give_feedback)
        addToFavorites = requireActivity().findViewById(R.id.add_seller_fav)


        sellerImage = requireActivity().findViewById(R.id.textView201)
        sellerEmail = requireActivity().findViewById(R.id.textView25)
        sellerName = requireActivity().findViewById(R.id.text555)
        sellerLocation = requireActivity().findViewById(R.id.tex6666)
        sellerMobile = requireActivity().findViewById(R.id.textView24)


        ////////////////////////////Api//////////////////////////////////////////
        getProductbyidapi(AdvId, ConstantObjects.logged_userid)

        // Seller Details
        getSellerByID(sellerID)


//       here
        getcurrentbidingprice()

        val buttonOpenBottomSheet: Button = requireActivity().findViewById(R.id.placebid)
        buttonOpenBottomSheet.setOnClickListener {
            val bottomSheet = BottomsheetDialogfragClass()
            bottomSheet.show(requireActivity().supportFragmentManager, "exampleBottomSheet")
        }


        ///////////////////////SwtichingcCarspces to home////////////////////////////////////
        bckscarpces.setOnClickListener() {
            findNavController().navigate(R.id.carspec_home)
        }

//        textView2011.setOnClickListener() {
//            findNavController().navigate(R.id.carspec_aboutseller)
//        }

        card5Product.setOnClickListener() {
            val args = Bundle()
            args.putString("AdvId", AdvId);
            NavHostFragment.findNavController(this@ProductDetail).navigate(
                R.id.carspec_quesans,
                args
            )

        }


        buynow_advdetail2.setOnClickListener() {
            findNavController().navigate(R.id.advdetails_shipingaddress)
        }

        val alreadyAddedWatchlist: Boolean = HelpFunctions.AdAlreadyAddedToWatchList(AdvId)
        if (alreadyAddedWatchlist) {
            watchbutton.setImageResource(R.drawable.removewatchlist)
        }
        watchbutton.setOnClickListener(View.OnClickListener {
            val alreadyadded: Boolean = HelpFunctions.AdAlreadyAddedToWatchList(AdvId)
            if (alreadyadded) {
                HelpFunctions.DeleteAdFromWatchlist(
                    AdvId,
                    requireContext()
                )
                watchbutton.setImageResource(R.drawable.watch_carspecs)
            } else {
                watchListPopup(watchbutton)

            }
        })

        sharebutton.setOnClickListener {
            shared()
        }

//        btn_give_feedback.setOnClickListener() {
//
//            val args = Bundle()
//            args.putString("AdvId", AdvId)
//            //feedback screen
//            findNavController().navigate(R.id.detail_to_give_feedback, args)
//        }

        // To get total count of questions
        quesAnss(AdvId)

        // To check if user has created the ad
        if (ConstantObjects.logged_userid == sellerID) {
            btn_give_feedback.visibility = View.GONE
        }

//        add_seller_fav.setOnClickListener() {
//            addSellerFav()
//        }

    }

    private fun shared() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = "Here is the share content body"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    lateinit var countdown_timer: CountDownTimer
    var time_in_milli_seconds: Long = 0L

    @SuppressLint("NewApi")
    fun RunCountDownTimer(
        listingtype: String?,
        endTime: String?,
        timepicker: Any?,
        duration: String?,
        startdate: Any?
    ) {
        time_in_milli_seconds = 0L
        lbl_timer_countdown.text = ""
        if (listingtype != null) {
            //Hide Timer
            if (listingtype == "1") {
                lbl_timer_countdown.setVisibility(View.GONE)
            } else {
                if ((endTime != null && endTime != "" && endTime.trim().length > 0) &&
                    (timepicker != null && timepicker != "" && timepicker.toString()
                        .trim().length > 0)
                ) {
                    var _timepicker = timepicker.toString().replace(" ", "")
                    val currentdatetime: Date = Date();
                    var _endTime = endTime + _timepicker;
                    _endTime = HelpFunctions.FormatDateTime(
                        _endTime,
                        HelpFunctions.datetimeformat_ddmmmyyyy_24hrs,
                        HelpFunctions.datetimeformat_mmddyyyy_24hrs
                    )
                    val _dendTime = HelpFunctions.ConvertStringToDate(
                        _endTime,
                        HelpFunctions.datetimeformat_mmddyyyy_24hrs
                    )
                    if (_dendTime != null) {
                        val time_in_milli_seconds: Long =
                            _dendTime!!.time - currentdatetime.time
                        //time is remaining
                        if (time_in_milli_seconds > 0) {
                            startTimer(time_in_milli_seconds)
                        }
                    }
                } else if ((duration != null && duration != "" && duration.trim().length > 0) &&
                    (timepicker != null && timepicker != "" && timepicker.toString()
                        .trim().length > 0) &&
                    (startdate != null && startdate.toString() != "" && startdate.toString()
                        .trim().length > 0)
                ) {
                    var _timepicker = timepicker.toString().replace(" ", "")
                    var _iduration = duration.replace("week", "").replace("weeks", "")
                    val currentdatetime: LocalDateTime = LocalDateTime.now();

                    var _startdate = HelpFunctions.FormatDateTime(
                        startdate.toString(),
                        HelpFunctions.datetimeformat_24hrs_7milliseconds_timezone,
                        HelpFunctions.datetimeformat_mmddyyyy
                    )
                    var _startdatetime = _startdate + _timepicker;
                    _startdatetime = HelpFunctions.FormatDateTime(
                        _startdatetime,
                        HelpFunctions.datetimeformat_mmddyyyy_24hrs,
                        HelpFunctions.datetimeformat_mmddyyyy_24hrs
                    )
                    var simpleFormat =
                        DateTimeFormatter.ofPattern(HelpFunctions.datetimeformat_mmddyyyy_24hrs)
                    var _dendTime: LocalDateTime =
                        LocalDateTime.parse(_startdatetime, simpleFormat);

                    _dendTime = _dendTime.plusWeeks(_iduration.trim().toLong())

                    val time_in_milli_seconds: Long =
                        _dendTime.atOffset(ZoneOffset.UTC).toInstant()
                            .toEpochMilli() - currentdatetime.atOffset(ZoneOffset.UTC).toInstant()
                            .toEpochMilli();
                    //time is remaining
                    if (time_in_milli_seconds > 0) {
                        startTimer(time_in_milli_seconds)
                    }

                }
                lbl_timer_countdown.setVisibility(View.VISIBLE)
            }
        }
    }

    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            //When countdown finishes
            override fun onFinish() {

            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTextUI()
            }
        }
        countdown_timer.start()
    }

    private fun updateTextUI() {
        var timr = HelpFunctions.GetCountdownTimerText(time_in_milli_seconds)
        if (lbl_timer_countdown != null)
            lbl_timer_countdown.text = timr
    }

    private fun StopTimer() {
        if (this::countdown_timer != null && this::countdown_timer.isInitialized)
            countdown_timer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        StopTimer()
    }


    private fun getProductbyidapi(advid: String, loginUserId: String) {
        try {
            val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
            val call: Call<ProductResponseBack> = malqa.getProductDetailById(advid, loginUserId)

            call.enqueue(object : Callback<ProductResponseBack> {
                @SuppressLint("UseRequireInsteadOfGet")
                override fun onResponse(
                    call: Call<ProductResponseBack>,
                    response: Response<ProductResponseBack>
                ) {
                    ////////////////////get userid of this opened Ad//////////////////////////

                    if (response.isSuccessful) {

                        val adUserId: String = response.body()!!.data.userId.toString()
                        SharedPreferencesStaticClass.ad_userid = adUserId

                        val details: ProductDetailModel? = response.body()!!.data

                        if (details != null) {

                            if (SharedPreferencesStaticClass.ad_userid == ConstantObjects.logged_userid) {
                                watchlistButton.isClickable = false
                                giveFeedback.visibility = View.GONE
                                addToFavorites.visibility = View.GONE
                            }
                            val producttile: String = details.title.toString()
                            textView7.text = producttile

                            val subtitle: String = details.subTitle.toString()
                            textView9.text = subtitle

                            val location1: String = details.region.toString()
                            val location2: String = details.city.toString()
                            val location3: String = details.country.toString()
                            txt_car_location.text = location1 + " " + location2 + " " + location3

                            val buynowpricee: String = details.buyNowPrice.toString()

                            textView14.text = buynowpricee

                            //Setting view according to the Listing type
                            val listingType: String? = details.listingType
                            if (listingType == "1") {

                                textView15.text = ""
                                placebid.isEnabled = false
                                placebid.isClickable = false
                            } else if (listingType == "2") {
                                textView14.text = ""
                                buynow_advdetail2.isEnabled = false
                                buynow_advdetail2.isClickable = false
                            }

//                        Details
                            val colorText: String? = details.color
                            text1.text = "Color: " + colorText

                            val size: String? = details.size
                            text2.text = "Size: " + size

                            val width: String? = details.width
                            text3.text = "Width: " + width + " cm"

                            val listingT: String? = details.listingType
                            var listingText = ""
                            if (listingT == "1") {
                                listingText = "Buy now"
                            } else {
                                listingT == "2"
                                listingText = "Auction"
                            }
                            text4.text = "Listing Type: " + listingText

                            val pickUp: String? = details.pickUp
                            text5.text = "Pick Up: " + pickUp


                            val createdDate: String? = details.createdDate.toString()
                            text6.text = "Created Date: " + createdDate

                            val height: String? = details.height
                            text7.text = "Height: " + height + " cm"

                            val length: String? = details.length
                            text8.text = "Length: " + length + " cm"

                            val weight: String? = details.weight
                            text9.text = "Weight: " + weight + " kg"

                            val description: String = details.description.toString()
                            txt_car_description.text = description

                            if (details.startPrice != null) {
                                StoreDataForAdDetail.saveAdvstartPriceForAdDetailBiding =
                                    details.startPrice.toString()
                            } else {
                                StoreDataForAdDetail.saveAdvstartPriceForAdDetailBiding = "0"
                            }

                            val viewPagerAdapter =
                                ViewPagerAdapter(this@ProductDetail.context!!, details.images!!)
                            viewPager!!.adapter = viewPagerAdapter
                            dotscount = viewPagerAdapter.count

                            dots = arrayOfNulls(dotscount)
                            for (i in 0 until dotscount) {
                                dots[i] = ImageView(this@ProductDetail.context)
                                dots[i]!!.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.non_active_dot
                                    )
                                )
                                val params = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                params.setMargins(2, 0, 2, 0)
                                sliderDotspanel!!.addView(dots[i], params)
                            }

                            dots[0]!!.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.active_dot
                                )
                            )
                            viewPager!!.addOnPageChangeListener(object :
                                ViewPager.OnPageChangeListener {
                                override fun onPageScrolled(
                                    position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int
                                ) {
                                }

                                override fun onPageSelected(position: Int) {
                                    for (i in 0 until dotscount) {
                                        dots[i]!!.setImageDrawable(
                                            ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.non_active_dot
                                            )
                                        )
                                    }
                                    dots[position]!!.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            requireContext(),
                                            R.drawable.active_dot
                                        )
                                    )
                                }

                                override fun onPageScrollStateChanged(state: Int) {}
                            })


                        } else {
                            HelpFunctions.ShowAlert(
                                this@ProductDetail.context, "Information", "No Record Found"
                            )
                        }
                    } else {
                        HelpFunctions.ShowAlert(
                            this@ProductDetail.context, "Information", "No Record Found"
                        )
                    }
                }

                override fun onFailure(call: Call<ProductResponseBack>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (ex: Exception) {
            throw ex
        }
    }


    //getBidPriceApiCall
    private fun getcurrentbidingprice() {

        try {
            val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
            val call: Call<ModelBidingResponse> = malqaa.getbidgpricebyAdvId(AdvId)

            call.enqueue(object : Callback<ModelBidingResponse> {
                override fun onResponse(
                    call: Call<ModelBidingResponse>,
                    response: Response<ModelBidingResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null && response.body()!!.data.maxBid != null) {
                            val currentbidprice: Int = response.body()!!.data.maxBid
                            val strcurrentbidprice: String = Integer.toString(currentbidprice)
                            if (strcurrentbidprice == "0") {
                                textView15.setText(StoreDataForAdDetail.saveAdvstartPriceForAdDetailBiding)

                                StoreDataForAdDetail.maxlastbidPrice =
                                    StoreDataForAdDetail.saveAdvstartPriceForAdDetailBiding
                            } else {
                                textView15.setText(strcurrentbidprice)
                                StoreDataForAdDetail.maxlastbidPrice = strcurrentbidprice
                            }
                        } else {
                            HelpFunctions.dismissProgressBar()
                            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        HelpFunctions.dismissProgressBar()
                        Toast.makeText(activity, "Error in Successful", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ModelBidingResponse>, t: Throwable) {
                    HelpFunctions.dismissProgressBar()
                    Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        } catch (ex: Exception) {
            throw ex
        }
    }

    // To get total count of questions
    private fun quesAnss(adsId: String) {
        try {
            val malqaa: MalqaApiService =
                RetrofitBuilder.GetRetrofitBuilder()

            val call: Call<ModelQuesAnswr> = malqaa.quesAns(adsId, ConstantObjects.logged_userid)

            call.enqueue(object : Callback<ModelQuesAnswr> {
                override fun onResponse(
                    call: Call<ModelQuesAnswr>, response: Response<ModelQuesAnswr>
                ) {
                    if (response.isSuccessful) {

                        totalQuestions = response.body()!!.questions.size
                        totalQuestion.setText("${totalQuestions} questions")
                        HelpFunctions.dismissProgressBar()
                    } else {
                        HelpFunctions.dismissProgressBar()
                        Toast.makeText(activity, "Failed to get Questions", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ModelQuesAnswr>, t: Throwable) {
                    HelpFunctions.dismissProgressBar()
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (ex: Exception) {
            throw ex
        }

    }

    private fun watchListPopup(view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_dont_email -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 0,
                        requireContext()
                    )
                    watchbutton.setImageResource(R.drawable.removewatchlist)
                    true
                }
                R.id.menu_email_everyday -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 1,
                        requireContext()
                    )
                    watchbutton.setImageResource(R.drawable.removewatchlist)
                    true
                }
                R.id.menu_email_3day -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 3,
                        requireContext()
                    )
                    watchbutton.setImageResource(R.drawable.removewatchlist)
                    true
                }
                R.id.menu_email_once_a_week -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 7,
                        requireContext()
                    )
                    watchbutton.setImageResource(R.drawable.removewatchlist)
                    true
                }
                else -> false
            }
        }

        popupMenu.inflate(R.menu.menu_watchlist)

        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            Log.e("Main", "Error showing menu icons.", e)
        } finally {
            popupMenu.show()
        }
    }

    // Add seller to favorites
    fun addSellerFav() {
        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()

        val call: Call<ModelAddSellerFav> = malqaa.addSellerFav(
            ModelAddSellerFav(
                loggedInUserId = ConstantObjects.logged_userid,
                reminderType = 0,
                sellerId = SharedPreferencesStaticClass.ad_userid
            )
        )

        call.enqueue(object : Callback<ModelAddSellerFav> {
            override fun onResponse(
                call: Call<ModelAddSellerFav>, response: Response<ModelAddSellerFav>
            ) {
                if (response.isSuccessful) {

                    Toast.makeText(activity, "Seller Added to Favorites", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(activity, "Failed to add favorite", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ModelAddSellerFav>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getSellerByID(id: String) {

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ModelSellerDetails> = malqa.getAdSellerByID(id)

        call.enqueue(object : Callback<ModelSellerDetails> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ModelSellerDetails>,
                response: Response<ModelSellerDetails>
            ) {
                if (response.isSuccessful) {

                    val details: ModelSellerDetails = response.body()!!
                    if (details != null) {

//                        var sellerData = response.body()!!.data
//
//                        if (sellerData.username != null) {
//                            sellerName.text = sellerData.username
//                        } else {
//                            sellerName.text = "Seller Name"
//                        }
//                        if (sellerData.phone != null) {
//                            sellerMobile.text = sellerData.phone
//                        } else {
//                            sellerMobile.text = "Seller Number"
//                        }
//                        if (sellerData.email != null) {
//                            sellerEmail.text = sellerData.email
//                        } else {
//                            sellerEmail.text = "Email the seller"
//                        }
//                        if (sellerData.country != null) {
//                            sellerLocation.text = sellerData.country
//                        } else {
//                            sellerLocation.text = "Location"
//                        }
//                        if (sellerData.image != null) {
//                            Picasso.get()
//                                .load(ApiConstants.IMAGE_URL + sellerData.image)
//                                .into(sellerImage)
//                        } else {
//                            sellerImage.setImageResource(R.drawable.profilepic)
//                        }

                    } else {
                        Toast.makeText(context, "No record found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No record found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ModelSellerDetails>, t: Throwable) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }










}



