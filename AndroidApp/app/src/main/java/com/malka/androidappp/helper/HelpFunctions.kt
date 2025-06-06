package com.malka.androidappp.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings.Secure
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ModelShipAddresses
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.botmnav_fragments.watchlist_fragment.WatchlistFragment
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_watchlist.WatchlistModel
import com.malka.androidappp.servicemodels.BasicResponseInt
import com.malka.androidappp.servicemodels.Basicresponse
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.addtocart.AddToCartResponseModel
import com.malka.androidappp.servicemodels.addtocart.InsertToCartRequestModel
import com.malka.androidappp.servicemodels.checkout.CheckoutRequestModel
import com.malka.androidappp.servicemodels.creditcard.CreditCardRequestModel
import com.malka.androidappp.servicemodels.creditcard.CreditCardResponse
import com.malka.androidappp.servicemodels.favourites.FavouriteObject
import com.malka.androidappp.servicemodels.favourites.favouriteadd
import com.malka.androidappp.servicemodels.watchlist.watchlistadd
import com.malka.androidappp.servicemodels.watchlist.watchlistobject
import kotlinx.android.synthetic.main.alertpopup.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


interface ListRefreshed {
    fun onWatchlistRefresed()
    fun onFavouritelistRefresed()
}

class HelpFunctions {

    companion object {
        const val datetimeformat_24hrs_milliseconds: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val datetimeformat_24hrs_milliseconds_timezone: String =
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"
        const val datetimeformat_24hrs_7milliseconds_timezone: String =
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX"
        const val datetimeformat_mmddyyyy: String = "MM/dd/yyyy"
        const val datetimeformat_ddmmmyyyy: String = "dd/MMM/yyyy"
        const val datetimeformat_ddmmmyyyy_24hrs: String = "dd/MMM/yyyyHH:mm:ss"
        const val datetimeformat_mmddyyyy_24hrs: String = "MM/dd/yyyyHH:mm:ss"
        const val datetimeformat_24hrs: String = "yyyy-MM-dd'T'HH:mm:ss"
        const val datetimeformat_12hrs: String = "MM/dd/yyyy hh:mm:ss a"
        const val appName = "Malqaa";


        fun ShowLongToast(msg: String, context: Context?) {
            if (context != null) {
                Toast.makeText(
                    context,
                    msg,
                    Toast.LENGTH_SHORT
                ).show();
            }
        }

        fun IsUserLoggedIn(): Boolean {
            var RetVal: Boolean = false
            if (ConstantObjects.logged_userid != null && ConstantObjects.logged_userid.trim().length > 0) {
                RetVal = true
            }
            return RetVal;
        }

        fun ShowShortToast(msg: String, context: Context?) {
            if (context != null) {
                Toast.makeText(
                    context,
                    msg,
                    Toast.LENGTH_SHORT
                ).show();
            }
        }

        fun ShowVerbose(Text: String) {
            Log.v(appName, Text)
        }

        fun ShowWarning(Text: String) {
            Log.w(appName, Text)
        }

        fun ShowInformation(Text: String) {
            Log.i(appName, Text)
        }

        fun ShowError(Text: String) {
            Log.e(appName, Text)
        }

        fun showLog(Text: String) {
            Log.d(appName, Text)
        }

        fun GetCurrentDateTime(format: String): String {
            var timestamp: String = "";
            val sdf = SimpleDateFormat(format)
            val currentDate = sdf.format(Date())
            timestamp = currentDate
            return timestamp
        }

        fun GetCountdownTimerText(time_in_milli_seconds: Long): String {
            var RetVal = "";
            if (time_in_milli_seconds > 0) {
                val days =
                    TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(time_in_milli_seconds));
                val hours =
                    (TimeUnit.MILLISECONDS.toHours(time_in_milli_seconds) - TimeUnit.DAYS.toHours(
                        TimeUnit.MILLISECONDS.toDays(time_in_milli_seconds)
                    ));
                val mins =
                    (TimeUnit.MILLISECONDS.toMinutes(time_in_milli_seconds) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(time_in_milli_seconds)
                    ));
                val seconds =
                    (TimeUnit.MILLISECONDS.toSeconds(time_in_milli_seconds) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(time_in_milli_seconds)
                    ));
                if (days > 0)
                    RetVal += String.format("%02dd ", days);
                if (hours > 0)
                    RetVal += String.format("%02dh ", hours);
                if (mins > 0)
                    RetVal += String.format("%02dm ", mins);
                if (seconds > 0)
                    RetVal += String.format("%02ds ", seconds);
            }
            return RetVal
        }

        fun ReportError(exception: java.lang.Exception) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            exception.printStackTrace(pw)
            val sStackTrace: String = sw.toString()

            var error: String = "";
            error = "App-Name: " + appName + "\r\n";
            error += "DateTime: " + GetCurrentDateTime(datetimeformat_12hrs) + "\r\n";
            error += "Error: " + exception.message + "\r\n";
            error += "Stacktrace: " + sStackTrace + "\r\n";

            ShowError(error)
        }

        fun ShowAlert(context: Context?, alertTitle: String, alertMessage: String) {
            if (context != null) {
                val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context!!)
                val inflater =
                    context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogView: View = inflater.inflate(R.layout.alertpopup, null).also {
                    it.Lbl_ALertTitle.setText(alertTitle);
                    it.Lbl_AlertMessage.setText(alertMessage)
                };
                dialogBuilder.setView(dialogView)
                val alertDialog: AlertDialog = dialogBuilder.create()
                alertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()
                dialogView.btn_alertclose.setOnClickListener() {
                    alertDialog.dismiss()
                }
            }
        }

        @SuppressLint("HardwareIds")
        fun getDeviceId(context: Context): String? {
            return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        }

        fun isEmailValid(email: String): Boolean {
            var isValid = false
            val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val inputStr: CharSequence = email
            val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(inputStr)
            if (matcher.matches()) {
                isValid = true
            }
            return isValid
        }

        fun isPhoneNumberValid(number: String): Boolean {
            return Patterns.PHONE.matcher(number).matches()
        }

        fun ConvertStringToDate(aDate: String?, aFormat: String): Date? {
            if (aDate == null) return null
            val pos = ParsePosition(0)
            val simpledateformat = SimpleDateFormat(aFormat)
            return simpledateformat.parse(aDate, pos)
        }

        fun FormatDateTime(
            value: String,
            sourceformat: String,
            requiredformat: String
        ): String {
            var RetVal: String = "";
            try {
                if (value != null && value.trim().length > 0) {
                    val defaultsourceformat =
                        if (sourceformat == null || sourceformat.trim().length == 0) datetimeformat_24hrs else sourceformat
                    val defaulttargetformat =
                        if (requiredformat == null || requiredformat.trim().length == 0) datetimeformat_mmddyyyy else requiredformat
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        var simpleFormat = DateTimeFormatter.ofPattern(defaultsourceformat)
                        var convertedDate = if (value != null) LocalDateTime.parse(
                            value,
                            simpleFormat
                        ) else LocalDateTime.parse("1900-01-01T12:00:00", simpleFormat)
                        val formatter = DateTimeFormatter.ofPattern(requiredformat)
                        RetVal = convertedDate.format(formatter)
                    } else {
                        val parser = SimpleDateFormat(defaultsourceformat)
                        val formatter = SimpleDateFormat(defaulttargetformat)
                        RetVal = formatter.format(parser.parse(value))
                    }
                } else {
                    RetVal = "01/01/1900"
                }
            } catch (ex: Exception) {
                ReportError(ex)
            }
            return RetVal
        }

        fun ViewAdvertismentDetail(AdvId: String, Category: String, currentfragment: Fragment) {
            val args = Bundle()
            args.putString("AdvId", AdvId);
            args.putString("Template", Category);
            NavHostFragment.findNavController(currentfragment).navigate(R.id.carspicification, args)
        }

        fun AdAlreadyAddedToWatchList(adreferenceId: String): Boolean {
            var RetVal: Boolean = false;
            try {
                if (adreferenceId != null && adreferenceId.trim().length > 0) {
                    if (ConstantObjects.userwatchlist != null && ConstantObjects.userwatchlist!!.data != null && ConstantObjects.userwatchlist!!.data.size > 0) {
                        for (IndWatch in ConstantObjects.userwatchlist!!.data) {
                            if (IndWatch.advertisement != null) {
                                if (IndWatch.advertisement.referenceId.trim().toUpperCase()
                                        .equals(adreferenceId.trim().toUpperCase())
                                ) {
                                    RetVal = true
                                    break
                                } else {
                                    RetVal = false
                                    continue
                                }
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
                throw  ex
            }
            return RetVal
        }

        fun GetUserWatchlist(context: Fragment) {
            try {
                val malqa: MalqaApiService =
                    RetrofitBuilder.getUserWatchlist(ConstantObjects.logged_userid)
                val call: Call<watchlistobject> =
                    malqa.getUserWatchlist(ConstantObjects.logged_userid)
                call.enqueue(object : Callback<watchlistobject> {
                    override fun onResponse(
                        call: Call<watchlistobject>,
                        response: Response<watchlistobject>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                var watchlistinfo: watchlistobject = response.body()!!;
                                if (watchlistinfo != null && watchlistinfo.data != null && watchlistinfo.data.size > 0) {
                                    val watchlistpost: ArrayList<WatchlistModel> = ArrayList()
                                    for (IndWatch in watchlistinfo.data) {
                                        if (IndWatch.advertisement != null) {
                                            watchlistpost.add(
                                                WatchlistModel(
                                                    IndWatch.advertisement.title,
                                                    IndWatch.advertisement.enddate,
                                                    IndWatch.advertisement.price,
                                                    "Buy now",
                                                    IndWatch.advertisement.homepageImage,
                                                    IndWatch.advertisement.referenceId,
                                                    IndWatch.advertisement.template
                                                )
                                            )
                                        }
                                    }
                                    ConstantObjects.userwatchlist = watchlistinfo
                                } else {
                                    ConstantObjects.userwatchlist = watchlistinfo
                                }
                                if (context is WatchlistFragment) {
                                    (context as WatchlistFragment).BindUserWatchlist()
                                }
                            } else {
                                ShowLongToast(
                                    "No Record Found",
                                    context.requireContext()
                                );
                            }
                        } else {
                            ShowLongToast(
                                "No Record Found",
                                context.requireContext()
                            );
                        }
                    }

                    override fun onFailure(call: Call<watchlistobject>, t: Throwable) {
                        ShowLongToast(
                            "No Record Found",
                            context.requireContext()
                        );
                    }
                })
            } catch (ex: Exception) {
                throw ex
            }
        }

        //Zack
        //Date: 11/13/2020
        fun GetUserFavourites(context: Fragment) {
            try {
                val malqa: MalqaApiService =
                    RetrofitBuilder.getuserfavourites(ConstantObjects.logged_userid)
                val call: Call<FavouriteObject> =
                    malqa.getuserfavourites(ConstantObjects.logged_userid)
                call.enqueue(object : Callback<FavouriteObject> {
                    override fun onFailure(call: Call<FavouriteObject>, t: Throwable) {
                        ShowLongToast(
                            "No Record Found", context.requireContext()
                        )
                    }

                    override fun onResponse(
                        call: Call<FavouriteObject>,
                        response: Response<FavouriteObject>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val resp: FavouriteObject = response.body()!!
                                if (resp != null) {
                                    ConstantObjects.userfavourite = resp
                                } else {
                                    ShowLongToast(
                                        "No Record Found", context.requireContext()
                                    )
                                }
                            } else {
                                ShowLongToast(
                                    "No Record Found", context.requireContext()
                                )
                            }
                        } else {
                            ShowLongToast(
                                "No Record Found", context.requireContext()
                            )
                        }
                    }
                })
            } catch (ex: Exception) {
                throw ex
            }
        }

        fun InsertAdToWatchlist(AdsId: String, reminderType: Int, context: Fragment): Boolean {
            var RetVal: Boolean = false
            try {
                var ad: watchlistadd = watchlistadd(
                    ConstantObjects.logged_userid,
                    AdsId,
                    reminderType
                )
                val malqa: MalqaApiService = RetrofitBuilder.InsertAdtoUserWatchlist()
                val call: Call<Basicresponse> = malqa.InsertAdtoUserWatchlist(ad)
                call.enqueue(object : Callback<Basicresponse> {
                    override fun onResponse(
                        call: Call<Basicresponse>,
                        response: Response<Basicresponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                var resp: Basicresponse = response.body()!!;
                                if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                                    RetVal = true
                                    GetUserWatchlist(context)
                                    ShowLongToast(
                                        "Added Successfully",
                                        context.requireContext()
                                    );
                                }
                            } else {
                                ShowLongToast(
                                    "Error During Addition",
                                    context.requireContext()
                                );
                            }
                        } else {
                            ShowLongToast(
                                "Error During Addition",
                                context.requireContext()
                            );
                        }
                    }

                    override fun onFailure(
                        call: Call<Basicresponse>,
                        t: Throwable
                    ) {
                        ShowLongToast(
                            "Error During Addition",
                            context.requireContext()
                        );
                    }
                })
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun DeleteAdFromWatchlist(AdsId: String, context: Fragment): Boolean {
            var RetVal: Boolean = false;
            try {
                val malqa: MalqaApiService = RetrofitBuilder.DeleteAdFromUserWatchlist()
                val call: Call<Basicresponse> =
                    malqa.DeleteAdFromUserWatchlist(ConstantObjects.logged_userid, AdsId)
                call.enqueue(object : Callback<Basicresponse> {
                    override fun onResponse(
                        call: Call<Basicresponse>,
                        response: Response<Basicresponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                var resp: Basicresponse = response.body()!!;
                                if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                                    RetVal = true;
                                    GetUserWatchlist(context)
                                    ShowLongToast(
                                        "Removed Successfully",
                                        context.requireContext()
                                    );
                                } else {
                                    ShowLongToast(
                                        resp.message,
                                        context.requireContext()
                                    );
                                }
                            } else {
                                ShowLongToast(
                                    "Error During Deletion",
                                    context.requireContext()
                                );
                            }
                        } else {
                            ShowLongToast(
                                "Error During Deletion",
                                context.requireContext()
                            );
                        }
                    }

                    override fun onFailure(
                        call: Call<Basicresponse>,
                        t: Throwable
                    ) {
                        ShowLongToast(
                            "Error During Deletion",
                            context.requireContext()
                        );
                    }
                })
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun InsertToFavourite(
            sellerId: String,
            categoryName: String,
            searchQuery: String,
            context: Fragment
        ): Boolean {
            var RetVal: Boolean = false
            try {
                var ad: favouriteadd = favouriteadd(
                    sellerId = sellerId,
                    categoryName = categoryName,
                    loggedInUserId = ConstantObjects.logged_userid,
                    remindertype = 0,
                    searchQuery = searchQuery,
                    category = "",
                    query = "",
                    sellerid = "",
                    userid = ""
                )
                var apiurl: String = ""
                if (sellerId != null && sellerId.trim().length > 0) {
                    apiurl = ApiConstants.INSERT_FAVOURTIE_SELLER_URL;
                } else if (categoryName != null && categoryName.trim().length > 0) {
                    apiurl = ApiConstants.INSERT_FAVOURTIE_CATEGORY_URL;
                } else if (searchQuery != null && searchQuery.trim().length > 0) {
                    apiurl = ApiConstants.INSERT_FAVOURTIE_SEARCH_URL;
                }

                val malqa: MalqaApiService = RetrofitBuilder.InsertToUserFavouritelist(apiurl)
                val call: Call<Basicresponse> = malqa.InsertToUserFavouritelist(ad)
                call.enqueue(object : Callback<Basicresponse> {
                    override fun onResponse(
                        call: Call<Basicresponse>,
                        response: Response<Basicresponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                var resp: Basicresponse = response.body()!!;
                                if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                                    RetVal = true
                                    GetUserFavourites(context)
                                    ShowLongToast(
                                        "Added Successfully",
                                        context.requireContext()
                                    );
                                } else {
                                    ShowLongToast(
                                        resp.message,
                                        context.requireContext()
                                    );
                                }
                            } else {
                                ShowLongToast(
                                    "Error During Addition",
                                    context.requireContext()
                                );
                            }
                        } else {
                            ShowLongToast(
                                "Error During Addition",
                                context.requireContext()
                            );
                        }
                    }

                    override fun onFailure(
                        call: Call<Basicresponse>,
                        t: Throwable
                    ) {
                        ShowLongToast(
                            "Error During Addition",
                            context.requireContext()
                        );
                    }
                })
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun DeleteFromFavourite(
            sellerid: String,
            category: String,
            query: String, context: Fragment
        ): Boolean {
            var RetVal: Boolean = false
            try {
                var ad: favouriteadd = favouriteadd(
                    sellerId = "",
                    categoryName = "",
                    loggedInUserId = "",
                    remindertype = 0,
                    searchQuery = "",
                    category = category,
                    query = query,
                    sellerid = sellerid,
                    userid = ConstantObjects.logged_userid
                )
                var apiurl: String = ""
                if (sellerid != null && sellerid.trim().length > 0) {
                    apiurl = ApiConstants.REMOVE_FAVOURTIE_SELLER_URL;
                } else if (category != null && category.trim().length > 0) {
                    apiurl = ApiConstants.REMOVE_FAVOURTIE_CATEGORY_URL;
                } else if (query != null && query.trim().length > 0) {
                    apiurl = ApiConstants.REMOVE_FAVOURTIE_SEARCH_URL;
                }

                val malqa: MalqaApiService = RetrofitBuilder.DeleteFromUserFavouritelist(apiurl)
                val call: Call<BasicResponseInt> =
                    malqa.DeleteFromUserFavouritelist(
                        sellerid,
                        category,
                        query,
                        ConstantObjects.logged_userid
                    )
                call.enqueue(object : Callback<BasicResponseInt> {
                    override fun onResponse(
                        call: Call<BasicResponseInt>,
                        response: Response<BasicResponseInt>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                var resp: BasicResponseInt = response.body()!!;
                                if (resp.status_code == 200 && resp.data == 1) {
                                    RetVal = true
                                    GetUserFavourites(context)
                                    ShowLongToast(
                                        "Removed Successfully",
                                        context.requireContext()
                                    );
                                } else {
                                    ShowLongToast(
                                        resp.message,
                                        context.requireContext()
                                    );
                                }
                            } else {
                                ShowLongToast(
                                    "Error During Deletion",
                                    context.requireContext()
                                );
                            }
                        } else {
                            ShowLongToast(
                                "Error During Deletion",
                                context.requireContext()
                            );
                        }
                    }

                    override fun onFailure(
                        call: Call<BasicResponseInt>,
                        t: Throwable
                    ) {
                        ShowLongToast(
                            "Error During Deletion",
                            context.requireContext()
                        );
                    }
                })
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun EncodeToBase64(image: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)
            return imageEncoded
        }

        fun GetUserCreditCards(context: Fragment) {
            try {
                val malqa: MalqaApiService = RetrofitBuilder.GetUserCreditCards()
                val call: Call<CreditCardResponse> =
                    malqa.GetUserCreditCards(ConstantObjects.logged_userid)
                val response: Response<CreditCardResponse> = call.execute();
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val resp: CreditCardResponse = response.body()!!
                        if (resp != null && resp.data != null) {
                            ConstantObjects.usercreditcard = resp.data
                        } else {
                            ConstantObjects.usercreditcard = null
                            ShowLongToast(
                                "No Record Found", context.requireContext()
                            )
                        }
                    } else {
                        ShowLongToast(
                            "No Record Found", context.requireContext()
                        )
                    }
                } else {
                    ShowLongToast(
                        "No Record Found", context.requireContext()
                    )
                }
            } catch (ex: Exception) {
                throw ex
            }
        }

        fun InsertUserCreditCard(cardinfo: CreditCardRequestModel, context: Fragment): Boolean {
            var RetVal: Boolean = false
            try {
                val malqa: MalqaApiService = RetrofitBuilder.InsertUserCreditCard()
                val call: Call<Basicresponse> = malqa.InsertUserCreditCard(cardinfo)
                val response: Response<Basicresponse> = call.execute();
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var resp: Basicresponse = response.body()!!;
                        if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                            RetVal = true
                            GetUserCreditCards(context)
                            ShowLongToast(
                                "Added Successfully",
                                context.requireContext()
                            );
                        }
                    } else {
                        ShowLongToast(
                            "Error During Addition",
                            context.requireContext()
                        );
                    }
                } else {
                    ShowLongToast(
                        "Error During Addition",
                        context.requireContext()
                    );
                }
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun DeleteUserCreditCard(CardId: String, context: Fragment): Boolean {
            var RetVal: Boolean = false;
            try {
                val malqa: MalqaApiService = RetrofitBuilder.DeleteUserCreditCard()
                val call: Call<Basicresponse> = malqa.DeleteUserCreditCard(CardId)
                val response: Response<Basicresponse> = call.execute();

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var resp: Basicresponse = response.body()!!;
                        if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                            RetVal = true;
                            GetUserCreditCards(context)
                            ShowLongToast(
                                "Removed Successfully",
                                context.requireContext()
                            );
                        } else {
                            ShowLongToast(
                                resp.message,
                                context.requireContext()
                            );
                        }
                    } else {
                        ShowLongToast(
                            "Error During Deletion",
                            context.requireContext()
                        );
                    }
                } else {
                    ShowLongToast(
                        "Error During Deletion",
                        context.requireContext()
                    );
                }
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun GetUsersCartList(context: Fragment) {
            try {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
                val malqa: MalqaApiService = RetrofitBuilder.GetUsersCartList()
                val call: Call<AddToCartResponseModel> =
                    malqa.GetUsersCartList(ConstantObjects.logged_userid)
                val response: Response<AddToCartResponseModel> = call.execute();
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val resp: AddToCartResponseModel = response.body()!!
                        if (resp != null && resp.data != null) {
                            ConstantObjects.usercart = resp.data
                        } else {
                            ConstantObjects.usercart = null
                            ShowLongToast(
                                "No Record Found", context.requireContext()
                            )
                        }
                    } else {
                        ShowLongToast(
                            "No Record Found", context.requireContext()
                        )
                    }
                } else {
                    ShowLongToast(
                        "No Record Found", context.requireContext()
                    )
                }
            } catch (ex: Exception) {
                throw ex
            }
        }

        fun AddToUserCart(cartiteminfo: InsertToCartRequestModel, context: Fragment): Boolean {
            var RetVal: Boolean = false
            try {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
                val malqa: MalqaApiService = RetrofitBuilder.AddToUserCart()
                val call: Call<Basicresponse> = malqa.AddToUserCart(cartiteminfo)
                val response: Response<Basicresponse> = call.execute();
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var resp: Basicresponse = response.body()!!;
                        if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                            RetVal = true
                            GetUsersCartList(context)
                            ShowLongToast(
                                "Added Successfully",
                                context.requireContext()
                            );
                        }
                    } else {
                        ShowLongToast(
                            "Error During Addition",
                            context.requireContext()
                        );
                    }
                } else {
                    ShowLongToast(
                        "Error During Addition",
                        context.requireContext()
                    );
                }
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun DeleteFromUserCart(CartId: String, context: Fragment): Boolean {
            var RetVal: Boolean = false;
            try {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
                val malqa: MalqaApiService = RetrofitBuilder.DeleteFromUserCart()
                val call: Call<Basicresponse> = malqa.DeleteFromUserCart(CartId)
                val response: Response<Basicresponse> = call.execute();
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var resp: Basicresponse = response.body()!!;
                        if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                            RetVal = true;
                            GetUsersCartList(context)
                            ShowLongToast(
                                "Removed Successfully",
                                context.requireContext()
                            );
                        } else {
                            ShowLongToast(
                                resp.message,
                                context.requireContext()
                            );
                        }
                    } else {
                        ShowLongToast(
                            "Error During Deletion",
                            context.requireContext()
                        );
                    }
                } else {
                    ShowLongToast(
                        "Error During Deletion",
                        context.requireContext()
                    );
                }
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun PostUserCheckOut(checkoutinfo: CheckoutRequestModel, context: Fragment): Boolean {
            var RetVal: Boolean = false
            try {
                val malqa: MalqaApiService = RetrofitBuilder.PostUserCheckOut()
                val call: Call<Basicresponse> = malqa.PostUserCheckOut(checkoutinfo)
                val response: Response<Basicresponse> = call.execute();
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var resp: Basicresponse = response.body()!!;
                        if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                            RetVal = true
                            GetUsersCartList(context)
                            ShowLongToast(
                                "Added Successfully",
                                context.requireContext()
                            );
                        }
                    } else {
                        ShowLongToast(
                            "Error During Addition",
                            context.requireContext()
                        );
                    }
                } else {
                    ShowLongToast(
                        "Error During Addition",
                        context.requireContext()
                    );
                }
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun AddNewShippingAddress(
            shippingaddress: ShippingAddressessData,
            context: Fragment
        ): Boolean {
            var RetVal: Boolean = false
            try {
                val malqa: MalqaApiService = RetrofitBuilder.AddNewShippingAddress()
                val call: Call<Basicresponse> = malqa.AddNewShippingAddress(shippingaddress)
                val response: Response<Basicresponse> = call.execute();
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var resp: Basicresponse = response.body()!!;
                        if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                            RetVal = true
                            GetUserShippingAddress(context)
                            ShowLongToast(
                                "Added Successfully",
                                context.requireContext()
                            );
                        }
                    }
                }
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun GetUserShippingAddress(context: Fragment) {
            val malqaa: MalqaApiService = RetrofitBuilder.getaddress(ConstantObjects.logged_userid)
            val call: Call<ModelShipAddresses> =
                malqaa.getshipaddress(ConstantObjects.logged_userid)
            val response: Response<ModelShipAddresses> = call.execute();
            if (response.isSuccessful) {
                if (response.body() != null && response.body()!!.data != null) {
                    ConstantObjects.useraddresses = response.body()!!.data
                } else {
                    ConstantObjects.useraddresses = null
                }
            }
        }

        fun getIndexOfValueInSpinner(spinner: Spinner, myString: String): Int {
            for (i in 0 until spinner.count) {
                if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                    return i
                }
            }
            return 0
        }

        fun GetTemplatesJson(context: Context, fileName: String): String? {
            var input: InputStream? = null
            var jsonString: String = ""
            try {
                val folder_name: String = "json_templates";
                input = context.assets.open(folder_name + "/" + fileName)
                val size = input.available()
                val buffer = ByteArray(size)
                input.read(buffer)
                jsonString = String(buffer)
            } catch (ex: Exception) {
                ReportError(ex)
            } finally {
                input?.close()
            }
            return jsonString
        }


        lateinit var isdialog: AlertDialog

        fun startProgressBar(mActivity: Activity) {
            val infalter = mActivity.layoutInflater

            val dialogView = infalter.inflate(R.layout.progress_bar, null)


            val bulider = AlertDialog.Builder(mActivity)

            bulider.setView(dialogView)
            bulider.setCancelable(false)

            isdialog = bulider.create()
            isdialog.show()
            isdialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        }

        fun dismissProgressBar() {
            isdialog.dismiss()
        }
    }
}
