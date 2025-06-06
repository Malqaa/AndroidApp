package com.malka.androidappp.botmnav_fragments.cardetail_page

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.botmnav_fragments.AboutTheSeller
import com.malka.androidappp.botmnav_fragments.cardetail_page.bottomsheet_bidopt.BottomsheetDialogfragClass
import com.malka.androidappp.botmnav_fragments.cardetail_page.bottomsheet_bidopt.StoreDataForAdDetail
import com.malka.androidappp.botmnav_fragments.cardetail_page.bottomsheet_bidopt.getbidModel.ModelBidingResponse
import com.malka.androidappp.botmnav_fragments.create_ads.ModelDataCreateGeneralAd
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.botmnav_fragments.feedback_frag.all_feedback.ModelAllFeedback
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans.ModelQuesAnswr
import com.malka.androidappp.botmnav_fragments.sellerdetails.DetailOfUser
import com.malka.androidappp.botmnav_fragments.sellerdetails.SellerResponseBack
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.imageslider.ViewPagerAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.addtocart.InsertToCartRequestModel
import com.malka.androidappp.servicemodels.user.UserObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.carspce_card2.*
import kotlinx.android.synthetic.main.carspec_card1.*
import kotlinx.android.synthetic.main.carspec_card3.*
import kotlinx.android.synthetic.main.carspec_card5.*
import kotlinx.android.synthetic.main.carspec_card6.*
import kotlinx.android.synthetic.main.carspec_card8.*
import kotlinx.android.synthetic.main.carspec_card8.textView19
import kotlinx.android.synthetic.main.carspec_card9.*
import kotlinx.android.synthetic.main.carsspec_card1.*
import kotlinx.android.synthetic.main.carsspec_card10.*
import kotlinx.android.synthetic.main.carsspec_card4.*
import kotlinx.android.synthetic.main.fragment_car_specifics.*
import kotlinx.android.synthetic.main.my_product_cardview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class CarSpecificsFragment : Fragment(), BottomsheetDialogfragClass.BottomSheetListener {
    var AdvId: String = ""
    var template: String = ""
    var totalQuestions: Int = 0

    lateinit var watchlistButton: ImageView
    lateinit var giveFeedback: Button
    lateinit var addToFavorites: ImageButton

    lateinit var sellerImage: ImageView
    lateinit var sellerName: TextView
    lateinit var sellerLocation: TextView
    lateinit var sellerEmail: TextView
    lateinit var sellerMobile: TextView
    lateinit var itemView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.VISIBLE

        AdvId = arguments?.getString("AdvId").toString()
        template = arguments?.getString("Template").toString()

        StoreDataForAdDetail.saveAdvIdforAdDetailBiding = AdvId

        HelpFunctions.startProgressBar(this.requireActivity())

        return inflater.inflate(R.layout.fragment_car_specifics, container, false)
    }

    override fun onButtonClicked(text: String?) {
        textView19.setText(text)
    }

    var viewPager: ViewPager? = null
    var sliderDotspanel: LinearLayout? = null
    private var dotscount = 0
    private lateinit var dots: Array<ImageView?>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = requireActivity().findViewById<View>(R.id.viewPager) as ViewPager
        sliderDotspanel = requireActivity().findViewById<View>(R.id.SliderDots) as LinearLayout

        watchlistButton = requireActivity().findViewById(R.id.watchbutton)
        giveFeedback = requireActivity().findViewById(R.id.btn_give_feedback)
        addToFavorites = requireActivity().findViewById(R.id.add_seller_fav)

        sellerImage = requireActivity().findViewById(R.id.textView201)
        sellerEmail = requireActivity().findViewById(R.id.textView25)
        sellerName = requireActivity().findViewById(R.id.text555)
        sellerLocation = requireActivity().findViewById(R.id.tex6666)
        sellerMobile = requireActivity().findViewById(R.id.textView24)
        itemView = requireActivity().findViewById(R.id.itemViews)
        getadbyidapi(AdvId, template)
        getcurrentbidingprice()

        // Seller Details
        getSellerByID(SharedPreferencesStaticClass.ad_userid)


        val buttonOpenBottomSheet: Button = requireActivity().findViewById(R.id.placebid)
        buttonOpenBottomSheet.setOnClickListener {
            if (HelpFunctions.IsUserLoggedIn()) {
                val bottomSheet = BottomsheetDialogfragClass()
                bottomSheet.show(requireActivity().supportFragmentManager, "exampleBottomSheet")
            } else {
                val intentt = Intent(context, SignInActivity::class.java)
                startActivity(intentt)
            }

        }

        bckscarpces.setOnClickListener() {
            findNavController().navigate(R.id.carspec_home)
        }


        textView2011.setOnClickListener() {
            if (HelpFunctions.IsUserLoggedIn()) {
                findNavController().navigate(R.id.carspec_aboutseller)
            } else {
                val intentt = Intent(context, SignInActivity::class.java)
                startActivity(intentt)
            }
        }

        card5.setOnClickListener() {
            val args = Bundle()
            args.putString("AdvId", AdvId)
            NavHostFragment.findNavController(this@CarSpecificsFragment).navigate(
                R.id.carspec_quesans,
                args
            )
        }

//        buynow_advdetail.setOnClickListener() {
//            AddToCart();
//        }

        buynow_advdetail2.setOnClickListener() {
            AddToCart();
        }

        val alreadyAddedWatchlist: Boolean = HelpFunctions.AdAlreadyAddedToWatchList(AdvId)
        if (alreadyAddedWatchlist) {
            watchbutton.setImageResource(R.drawable.removewatchlist)
        }

        watchbutton.setOnClickListener(View.OnClickListener {
            if (HelpFunctions.IsUserLoggedIn()) {
                val alreadyadded: Boolean = HelpFunctions.AdAlreadyAddedToWatchList(AdvId)
                if (alreadyadded) {
                    HelpFunctions.DeleteAdFromWatchlist(
                        AdvId,
                        this@CarSpecificsFragment
                    )
                    watchbutton.setImageResource(R.drawable.watch_carspecs)

                } else {
                    watchListPopup(watchbutton)
                }
            } else {
                val intentt = Intent(context, SignInActivity::class.java)
                startActivity(intentt)

//                HelpFunctions.ShowAlert(
//                    this@CarSpecificsFragment.requireContext(),
//                    "Information",
//                    "Please Log In"
//                )
            }
        })

        sharebutton.setOnClickListener() {
            shared()
        }
        quesAnss(AdvId)

        if (ConstantObjects.logged_userid == SharedPreferencesStaticClass.ad_userid) {
            btn_give_feedback.visibility = View.GONE
            add_seller_fav.visibility = View.GONE
            textView2011.isEnabled = false
        }

        btn_give_feedback.setOnClickListener() {
            if (HelpFunctions.IsUserLoggedIn()) {
                val args = Bundle()
                args.putString("AdvId", AdvId)
                findNavController().navigate(R.id.detail_to_give_feedback, args)
            } else {
//                HelpFunctions.ShowAlert(
//                    this@CarSpecificsFragment.requireContext(),
//                    "Information",
//                    "Please Log In"
//                );

                val intentt = Intent(context, SignInActivity::class.java)
                startActivity(intentt)

            }
        }

        add_seller_fav.setOnClickListener() {
            if (HelpFunctions.IsUserLoggedIn()) {
                addSellerFav()
            } else {
                val intentt = Intent(context, SignInActivity::class.java)
                startActivity(intentt)
            }
        }

    }

    fun AddToCart(): Boolean {
        if (HelpFunctions.IsUserLoggedIn()) {
            //Zack
            //Date: 03/13/2021
            //findNavController().navigate(R.id.shipping_address_to_payment)
            //findNavController().navigate(R.id.shippingaddress_googlemap)
            //findNavController().navigate(R.id.buy_now_to_checkout)
            //return false;

            var resp: Boolean = false;
            var cartobj: InsertToCartRequestModel = InsertToCartRequestModel()
            cartobj.advertisementId = AdvId;
            cartobj.userid = ConstantObjects.logged_userid;
            resp = HelpFunctions.AddToUserCart(cartobj, this@CarSpecificsFragment);
            if (resp) {
                findNavController().navigate(R.id.buy_now_to_checkout)
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.Error),
                    this@CarSpecificsFragment.context
                )
            }
            return resp;
        } else {
//            HelpFunctions.ShowAlert(
//                this@CarSpecificsFragment.requireContext(),
//                "Information",
//                "Please Log In"
//            );
            val intentt = Intent(context, SignInActivity::class.java)
            startActivity(intentt)
            return false
        }
    }

    fun shared() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = getString(R.string.Hereisthesharecontentbody)
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.SubjectHere))
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.Sharevia)))
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

    fun getadbyidapi(advid: String, template: String) {
        var cardViewboth: CardView = requireActivity().findViewById(R.id.card8)
        var buynowButton: Button = requireActivity().findViewById(R.id.buynow_advdetail2)
        var textlabel: TextView = requireActivity().findViewById(R.id.textView14)

        val malqa: MalqaApiService = RetrofitBuilder.getAdDetailById(advid, template)
        val call: Call<AdDetailModel> = malqa.getAdDetailById(advid, template)

        call.enqueue(object : Callback<AdDetailModel> {
            @SuppressLint("UseRequireInsteadOfGet", "SetTextI18n")
            override fun onResponse(
                call: Call<AdDetailModel>,
                response: Response<AdDetailModel>
            ) {
                if (response.isSuccessful) {
                    val adUserId: String = response.body()!!.user.toString()
                    SharedPreferencesStaticClass.ad_userid = adUserId
                    val details: AdDetailModel = response.body()!!
                    if (details != null) {
                        if (SharedPreferencesStaticClass.ad_userid == ConstantObjects.logged_userid) {
                            watchlistButton.isClickable = false
                            giveFeedback.visibility = View.GONE
                            addToFavorites.visibility = View.GONE
                        }

                        val producttile: String = details.producttitle.toString()
                        textView7.text = producttile

                        val title: String = details.title.toString()
                        textView9.text = title


                        val location1: String = details.address.toString()
                        val location2: String = details.city.toString()
                        val location3: String = details.country.toString()
                        txt_car_location.text = location1 + " " + location2 + " " + location3

                        val pricee: String = response.body()!!.price.toString()

                        textView14.text = pricee
//                        buynowpriceText.setText(pricee)


                        //Setting view according to the Listing type
                        val listingType: String? = details.listingType
                        RunCountDownTimer(
                            listingType,
                            details.endTime,
                            details.timepicker,
                            details.duration,
                            details.createdOn
                        )

                        when (listingType) {
                            "1" -> {
                                placebid.isEnabled = false
                                textView15.text = ""
                                textView19.text = ""
                            }
                            "2" -> {
                                textView14.text = ""
                                buynow_advdetail2.isEnabled = false
                            }
                        }
                        val quantiy: String? = details.quantity
                        text1.text = getString(R.string.Quantity) + ":" + quantiy

                        val starting: String? = details.startingPrice
                        text2.text = getString(R.string.StartPrice) + ":" + starting

                        val brandNewItem: String? = details.brandNewItem.toString()
                        text3.text = getString(R.string.BrandNewItem) + ":" + brandNewItem

                        val listingT: String? = details.listingType
                        var listingText = ""
                        when (listingT) {
                            "1" -> {
                                listingText = "Buy now"
                            }
                            "2" -> {
                                listingText = "Auction"
                            }
                            "3" -> {
                                listingText = "Buynow & Auction"
                            }
                        }
                        text4.text = getString(R.string.ListingType) + ":" + listingText

                        val timepicker: String? = details.timepicker.toString()
                        text5.text = getString(R.string.Registrationexpires) + ":" + timepicker

                        val region: String? = details.region.toString()
                        text6.text = getString(R.string.Region) + ":" + region

                        val shippingOption: String? = details.shippingOption
                        text7.text = getString(R.string.Shipping) + ":" + shippingOption

                        val adid: String? = details.id
                        text8.text = getString(R.string.AdID) + adid

                        val reservePrice: String? = details.reservePrice
                        text9.text = getString(R.string.ReservePrice) + ":" + reservePrice

                        val description: String = details.description.toString()
                        txt_car_description.setText(description)

                        itemView.text =
                            getString(R.string.itemView, details.itemviews.toString().toInt())

                        if (details.startingPrice != null) {
                            StoreDataForAdDetail.saveAdvstartPriceForAdDetailBiding =
                                details.startingPrice.toString()
                        } else {
                            StoreDataForAdDetail.saveAdvstartPriceForAdDetailBiding = "0"
                        }

                        val viewPagerAdapter =
                            ViewPagerAdapter(this@CarSpecificsFragment.context!!, details.images!!)
                        viewPager!!.adapter = viewPagerAdapter
                        dotscount = viewPagerAdapter.count

                        dots = arrayOfNulls(dotscount)
                        for (i in 0 until dotscount) {
                            dots[i] = ImageView(this@CarSpecificsFragment.context)
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

                        if (dots != null && dots.size > 0) {
                            dots[0]!!.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.active_dot
                                )
                            )
                        }
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
                            this@CarSpecificsFragment.context,
                            getString(R.string.Information),
                            getString(R.string.NoRecordFound)
                        )
                    }
                } else {
                    HelpFunctions.ShowAlert(
                        this@CarSpecificsFragment.context,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
                }
            }

            override fun onFailure(call: Call<AdDetailModel>, t: Throwable) {
                HelpFunctions.ShowLongToast(t.message!!, this@CarSpecificsFragment.context)
            }
        })

    }


    //getBidPriceApiCall
    fun getcurrentbidingprice() {

        val malqaa: MalqaApiService = RetrofitBuilder.getBidingbyAdId(AdvId)
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
                        HelpFunctions.ShowLongToast(
                            getString(R.string.Error),
                            this@CarSpecificsFragment.context
                        )
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.ErrorOccur),
                        this@CarSpecificsFragment.context
                    )
                }

            }

            override fun onFailure(call: Call<ModelBidingResponse>, t: Throwable) {
                HelpFunctions.ShowLongToast(t.message!!, this@CarSpecificsFragment.context)
            }
        })
    }

    // To get total count of questions
    fun quesAnss(adsId: String) {
        val malqaa: MalqaApiService =
            RetrofitBuilder.getQuesAnsComnt(adsId, ConstantObjects.logged_userid)
        val call: Call<ModelQuesAnswr> = malqaa.quesAns(adsId, ConstantObjects.logged_userid)

        call.enqueue(object : Callback<ModelQuesAnswr> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ModelQuesAnswr>, response: Response<ModelQuesAnswr>
            ) {
                if (response.isSuccessful) {

                    totalQuestions = response.body()!!.questions.size
//                    totalQuestion.text = "$totalQuestions questions"
                    totalQuestion.text = getString(R.string.questions, totalQuestions)

                    HelpFunctions.dismissProgressBar()
                } else {
                    HelpFunctions.dismissProgressBar()
                    HelpFunctions.ShowLongToast(getString(R.string.FailedtogetQuestions), activity)
                }
            }

            override fun onFailure(call: Call<ModelQuesAnswr>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                t.message?.let { HelpFunctions.ShowLongToast(it, activity) }
            }
        })

    }


    // Add seller to favorites
    fun addSellerFav() {
        val malqaa: MalqaApiService = RetrofitBuilder.addSellerToFav()

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

                    HelpFunctions.ShowLongToast(
                        getString(R.string.SellerAddedtoFavorites),
                        activity
                    )

                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.Failedtoaddfavorite), activity)
                }
            }

            override fun onFailure(call: Call<ModelAddSellerFav>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, activity) }
            }
        })
    }

    private fun watchListPopup(view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_dont_email -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 0,
                        this@CarSpecificsFragment
                    )
                    watchbutton.setImageResource(R.drawable.removewatchlist)
                    true
                }
                R.id.menu_email_everyday -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 1,
                        this@CarSpecificsFragment
                    )
                    watchbutton.setImageResource(R.drawable.removewatchlist)
                    true
                }
                R.id.menu_email_3day -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 3,
                        this@CarSpecificsFragment
                    )
                    watchbutton.setImageResource(R.drawable.removewatchlist)
                    true
                }
                R.id.menu_email_once_a_week -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 7,
                        this@CarSpecificsFragment
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

    private fun getSellerByID(id: String) {

        val malqa: MalqaApiService = RetrofitBuilder.getAdSellerByID(id)
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

                        var sellerData: Data = response.body()!!.data

                        if (sellerData.username != null) {
                            sellerName.text = sellerData.username
                        } else {
                            sellerName.text = "Seller Name"
                        }
                        if (sellerData.phone != null) {
                            sellerMobile.text = sellerData.phone
                        } else {
                            sellerMobile.text = "Seller Number"
                        }
                        if (sellerData.email != null) {
                            sellerEmail.text = sellerData.email
                        } else {
                            sellerEmail.text = "Email the seller"
                        }
                        if (sellerData.country != null) {
                            sellerLocation.text = sellerData.country
                        } else {
                            sellerLocation.text = "Location"
                        }
                        if (sellerData.image != null) {
                            Picasso.get()
                                .load(ApiConstants.IMAGE_URL + sellerData.image)
                                .into(sellerImage)
                        } else {
                            sellerImage.setImageResource(R.drawable.profilepic)
                        }

                    } else {
                        HelpFunctions.ShowLongToast(getString(R.string.NoRecordFound), context)
                    }
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.NoRecordFound), context)
                }
            }

            override fun onFailure(call: Call<ModelSellerDetails>, t: Throwable) {
                HelpFunctions.ShowLongToast(getString(R.string.Somethingwentwrong), context)
            }
        })
    }
}
