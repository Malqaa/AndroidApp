package com.malka.androidappp.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings.Secure
import android.util.Base64
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.product_detail.ProductDetails
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ModelShipAddresses
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.*
import com.malka.androidappp.servicemodels.addtocart.AddToCartResponseModel
import com.malka.androidappp.servicemodels.checkout.CheckoutRequestModel
import com.malka.androidappp.servicemodels.favourites.FavouriteObject
import com.malka.androidappp.servicemodels.favourites.favouriteadd
import com.malka.androidappp.servicemodels.watchlist.watchlistadd
import com.malka.androidappp.servicemodels.watchlist.watchlistobject
import io.paperdb.Paper
import kotlinx.android.synthetic.main.add_address_fragment.*
import kotlinx.android.synthetic.main.alertpopup.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.Boolean as Boolean1


class HelpFunctions {

    companion object {
        const val datetimeformat_24hrs_milliseconds: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val datetimeformat_24hrs_milliseconds_timezone: String =
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"
        const val datetimeformat_24hrs_7milliseconds_timezone: String =
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX"
        const val datetimeformat_mmddyyyy: String = "MM/dd/yyyy"
        const val datetimeformat_ddmmmyyyy: String = "dd/MMM/yyyy"
        const val datetimeformat_ddmyyyy: String = "dd/MM/yyyy"
        const val datetimeformat_ddmmmyyyy_24hrs: String = "dd/MMM/yyyyHH:mm:ss"
        const val datetimeformat_mmddyyyy_24hrs: String = "MM/dd/yyyyHH:mm:ss"
        const val datetimeformat_24hrs: String = "yyyy-MM-dd'T'HH:mm:ss"
        const val datetimeformat_12hrs: String = "MM/dd/yyyy hh:mm:ss a"
        const val appName = "Malqaa"


        fun ShowLongToast(msg: String, context: Context?) {

//            ShowAlert(
//                context, "", msg
//            )

            if (context != null) {
                Toast.makeText(
                    context,
                    msg,
                    Toast.LENGTH_SHORT
                ).show();
            }
        }

        fun IsUserLoggedIn(): Boolean1 {
            val islogin = Paper.book().read(SharedPreferencesStaticClass.islogin, false) ?: false
            if(islogin){
                val logged_userid = Paper.book().read(SharedPreferencesStaticClass.logged_userid, "") ?:""
                ConstantObjects.logged_userid=logged_userid
            }
            return islogin
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

           // ShowError(error)
        }

        fun ShowAlert(context: Context?, alertTitle: String, alertMessage: String) {
            if (context != null) {
                val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context!!)
                val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogView: View = inflater.inflate(R.layout.alertpopup, null).also {
                    if (alertTitle.isNotEmpty()) {
                        it.Lbl_ALertTitle.setText(alertTitle);
                    }
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

        fun isEmailValid(email: String): Boolean1 {
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

        fun isPhoneNumberValid(number: String): Boolean1 {
            return Patterns.PHONE.matcher(number).matches()
        }

        fun ConvertStringToDate(aDate: String?, aFormat: String): Date? {
            if (aDate == null) return null
            val pos = ParsePosition(0)
            val simpledateformat = SimpleDateFormat(aFormat,Locale.ENGLISH)
            return simpledateformat.parse(aDate, pos)
        }

        fun FormatDateTime(
            value: String,
            sourceformat: String,
            requiredformat: String
        ): String {
            var RetVal = "";
            try {
                if (value.trim().length > 0) {
                    val defaultsourceformat =
                        if (sourceformat.trim().length == 0) datetimeformat_24hrs else sourceformat
                    val defaulttargetformat =
                        if (requiredformat.trim().length == 0) datetimeformat_mmddyyyy else requiredformat
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        val simpleFormat = DateTimeFormatter.ofPattern(defaultsourceformat)
                        val convertedDate = LocalDateTime.parse(
                            value,
                            simpleFormat
                        )
                        val formatter = DateTimeFormatter.ofPattern(requiredformat)
                        RetVal = convertedDate.format(formatter)
                    } else {
                        val parser = SimpleDateFormat(defaultsourceformat,Locale.ENGLISH)
                        val formatter = SimpleDateFormat(defaulttargetformat,Locale.ENGLISH)
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

        fun getFormattedDate(
            date: String?,
            givenFormat: String?,
            requiredFormat: String?
        ): String {
            var result = ""
            var dateFormat: DateFormat? = null
            var requestDateFormat: DateFormat? = null
            var requestDate: Date? = null
            try {
                dateFormat = SimpleDateFormat(requiredFormat,Locale.ENGLISH)
                //  dateFormat.setTimeZone(TimeZone.getDefault())
                requestDateFormat = SimpleDateFormat(givenFormat,Locale.ENGLISH)

                // requestDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
                requestDate = requestDateFormat.parse(date)
                result = dateFormat!!.format(requestDate)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        fun ViewAdvertismentDetail(
            AdvId: String,
            Category: String,
            context: Context,
            fragment: Fragment
        ) {
            val args = Bundle()
            args.putString("AdvId", AdvId);
            args.putString("Template", Category);

            context.startActivity(Intent(context, ProductDetails::class.java).apply {
                putExtra("AdvId", AdvId)
                putExtra("Template", Category)
            })

            // NavHostFragment.findNavController(fragment).navigate(R.id.carspicification, args)
        }

        fun AdAlreadyAddedToWatchList(adreferenceId: String?): Boolean1 {
            var RetVal = false;
            if (adreferenceId!!.trim().length > 0) {
                if (ConstantObjects.userwatchlist != null && ConstantObjects.userwatchlist!!.data.size > 0) {
                    for (IndWatch in ConstantObjects.userwatchlist!!.data) {
                        if (IndWatch.advertisement.referenceId!!.trim().lowercase()
                                .equals(adreferenceId!!.trim().lowercase())
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
            return RetVal
        }


        //Zack
        //Date: 11/13/2020
        fun GetUserFavourites(context: Fragment) {
            try {
                val malqa: MalqaApiService =
                    RetrofitBuilder.GetRetrofitBuilder()
                val call: Call<FavouriteObject> =
                    malqa.getuserfavourites(ConstantObjects.logged_userid)
                call.enqueue(object : Callback<FavouriteObject> {

                    override fun onResponse(
                        call: Call<FavouriteObject>,
                        response: Response<FavouriteObject>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val resp: FavouriteObject = response.body()!!
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
                    }

                    override fun onFailure(call: Call<FavouriteObject>, t: Throwable) {
                        ShowLongToast(
                            "No Record Found", context.requireContext()
                        )
                    }
                })
            } catch (ex: Exception) {
                throw ex
            }
        }

        fun InsertAdToWatchlist(
            AdsId: String,
            reminderType: Int,
            context: Context,
            onSuccess: (() -> Unit)? = null
        ) {
            startProgressBar(context as Activity)

            try {
                val ad = watchlistadd(
                    ConstantObjects.logged_userid,
                    AdsId,
                    reminderType
                )
                val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
                val call: Call<BasicResponse> = malqa.InsertAdtoUserWatchlist(ad)
                call.enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val resp: BasicResponse = response.body()!!;
                                if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                                    GetUserWatchlist()
                                    onSuccess?.invoke()
                                    ShowLongToast(
                                        resp.message,
                                        context
                                    )
                                } else {
                                    ShowLongToast(
                                        resp.message,
                                        context
                                    )
                                }
                            } else {
                                ShowLongToast(
                                    "Error",
                                    context
                                );
                            }
                        } else {
                            ShowLongToast(
                                "Error",
                                context
                            );
                        }
                        dismissProgressBar()

                    }

                    override fun onFailure(
                        call: Call<BasicResponse>,
                        t: Throwable
                    ) {
                        ShowLongToast(
                            "Error",
                            context
                        );
                        dismissProgressBar()

                    }
                })
            } catch (ex: Exception) {
                throw ex
            }
        }

        fun DeleteAdFromWatchlist(
            AdsId: String, context: Context, onSuccess: (() -> Unit)? = null
        ) {
            startProgressBar(context as Activity)

            try {
                val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
                val call: Call<BasicResponse> =
                    malqa.DeleteAdFromUserWatchlist(ConstantObjects.logged_userid, AdsId)
                call.enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val resp: BasicResponse = response.body()!!;
                                if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                                    GetUserWatchlist()
                                    onSuccess?.invoke()
                                    ShowLongToast(
                                        "Removed Successfully",
                                        context
                                    );
                                } else {
                                    ShowLongToast(
                                        resp.message,
                                        context
                                    )
                                }
                            } else {
                                ShowLongToast(
                                    "Error During Deletion",
                                    context
                                );
                            }
                        } else {
                            ShowLongToast(
                                "Error During Deletion",
                                context
                            );
                        }
                        dismissProgressBar()

                    }

                    override fun onFailure(
                        call: Call<BasicResponse>,
                        t: Throwable
                    ) {
                        ShowLongToast(
                            "Error During Deletion",
                            context
                        )
                        dismissProgressBar()

                    }
                })
            } catch (ex: Exception) {
                throw ex
            }
        }

        fun GetUserWatchlist() {
            val malqa: MalqaApiService =
                RetrofitBuilder.GetRetrofitBuilder()
            val call: Call<watchlistobject> =
                malqa.getUserWatchlist(ConstantObjects.logged_userid)
            call.enqueue(object : Callback<watchlistobject> {
                override fun onResponse(
                    call: Call<watchlistobject>,
                    response: Response<watchlistobject>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val watchlistinfo: watchlistobject = response.body()!!
                            ConstantObjects.userwatchlist = watchlistinfo
                            EventBus.getDefault().post(WatchList())

                        }
                    }
                }

                override fun onFailure(call: Call<watchlistobject>, t: Throwable) {

                }
            })
        }

        fun InsertToFavourite(
            sellerId: String,
            categoryName: String,
            searchQuery: String,
            context: Fragment
        ): Boolean1 {
            var RetVal: Boolean1 = false
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

                val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
                val call: Call<BasicResponse> = malqa.InsertToUserFavouritelist(ad)
                call.enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                var resp: BasicResponse = response.body()!!;
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
                        call: Call<BasicResponse>,
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
        ): Boolean1 {
            var RetVal: Boolean1 = false
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

                val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
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

        fun encodeImage(path: String): String? {
            val imagefile = File(path)
            var fis: FileInputStream? = null
            fis = FileInputStream(imagefile)

            val bm = BitmapFactory.decodeStream(fis)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            //Base64.de
            return Base64.encodeToString(b, Base64.DEFAULT)
        }




        fun DeleteUserCreditCard(CardId: String, context: Context): Boolean1 {
            var RetVal: Boolean1 = false;
            try {
                val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
                val call: Call<BasicResponse> = malqa.DeleteUserCreditCard(CardId)
                val response: Response<BasicResponse> = call.execute();

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var resp: BasicResponse = response.body()!!;
                        if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                            RetVal = true;
                            CommonAPI().GetUserCreditCards(context) {

                            }
                            ShowLongToast(
                                "Removed Successfully",
                                context
                            );
                        } else {
                            ShowLongToast(
                                resp.message,
                                context
                            );
                        }
                    } else {
                        ShowLongToast(
                            "Error During Deletion",
                            context
                        );
                    }
                } else {
                    ShowLongToast(
                        "Error During Deletion",
                        context
                    );
                }
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun GetUsersCartList(onSuccess: (() -> Unit)? = null) {
            val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
            val call: Call<AddToCartResponseModel> =
                malqa.GetUsersCartList(ConstantObjects.logged_userid)
            val response: Response<AddToCartResponseModel> = call.execute();
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val resp: AddToCartResponseModel = response.body()!!
                    ConstantObjects.usercart = resp.data
                    onSuccess?.invoke()
                }
            }
        }



        fun DeleteFromUserCart(CartId: String, context: Context): Boolean1 {
            var RetVal: Boolean1 = false;
            try {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
                val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
                val call: Call<BasicResponse> = malqa.DeleteFromUserCart(CartId)
                val response: Response<BasicResponse> = call.execute();
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var resp: BasicResponse = response.body()!!;
                        if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                            RetVal = true;
                            GetUsersCartList()
                            ShowLongToast(
                                "Removed Successfully",
                                context
                            );
                        } else {
                            ShowLongToast(
                                resp.message,
                                context
                            );
                        }
                    } else {
                        ShowLongToast(
                            "Error During Deletion",
                            context
                        );
                    }
                } else {
                    ShowLongToast(
                        "Error During Deletion",
                        context
                    );
                }
            } catch (ex: Exception) {
                throw ex
            }
            return RetVal
        }

        fun PostUserCheckOut(checkoutinfo: CheckoutRequestModel, context: Context): Boolean1 {
            var RetVal: Boolean1 = false
            try {
                val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
                val call: Call<BasicResponse> = malqa.PostUserCheckOut(checkoutinfo)
                val response: Response<BasicResponse> = call.execute();
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var resp: BasicResponse = response.body()!!;
                        if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                            RetVal = true
                            GetUsersCartList()
                            ShowLongToast(
                                "Added Successfully",
                                context
                            );
                        }
                    } else {
                        ShowLongToast(
                            "Error During Addition",
                            context
                        );
                    }
                } else {
                    ShowLongToast(
                        "Error During Addition",
                        context
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
        ): Boolean1 {
            var RetVal: Boolean1 = false
            try {
                val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
                val call: Call<BasicResponse> = malqa.AddNewShippingAddress(shippingaddress)
                val response: Response<BasicResponse> = call.execute();
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var resp: BasicResponse = response.body()!!;
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
            val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
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


        fun GetTemplatesJson(fileName: String,onSuccess: ((respone:String) -> Unit)? = null) {
            try {
                val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()

                val API_BASE_URL =
                    ApiConstants.HTTP_PROTOCOL + "://" + ApiConstants.SERVER_LOCATION + "/" + ApiConstants.IMAGE_FOLDER + "/jsonTemplates/"+fileName

                val call = malqa.jsonTemplates(API_BASE_URL)
                call.enqueue(object : Callback<JsonObject?> {
                    override fun onResponse(
                        call: Call<JsonObject?>,
                        response: Response<JsonObject?>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                onSuccess?.invoke(response.body()!!.toString())
                            }
                        }

                    }

                    override fun onFailure(call: Call<JsonObject?>, t: Throwable) {

                    }
                })



            } catch (ex: Exception) {
                throw ex
            }
        }

//        fun GetTemplatesJson(context: Context, fileName: String): String? {
//            var input: InputStream? = null
//            var jsonString = ""
//            try {
//                val folder_name: String = "json_templates";
//                input = context.assets.open(folder_name + "/" + fileName)
//                val size = input.available()
//                val buffer = ByteArray(size)
//                input.read(buffer)
//                jsonString = String(buffer)
//            } catch (ex: Exception) {
//                ReportError(ex)
//            } finally {
//                input?.close()
//            }
//            return jsonString
//        }
//

        lateinit var isdialog: AlertDialog

        fun startProgressBar(mActivity: Activity) {
            val infalter = mActivity.layoutInflater
            val dialogView = infalter.inflate(R.layout.progress_bar, null)
            Glide.with(mActivity).asGif().load(R.raw.loader).into(dialogView.splash_view)
            val bulider = AlertDialog.Builder(mActivity)
            bulider.setView(dialogView)
            bulider.setCancelable(false)
            isdialog = bulider.create()
            isdialog.show()
            isdialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        }

        fun dismissProgressBar() {
            if (isdialog != null) {
                isdialog.dismiss()
            }
        }


        val PASSWORD_PATTERN = Pattern.compile(
            "^" + "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +  //any letter
                    // "(?=.*[@#$%^&+=])" +  //at least 1 special character
                    "(?=\\S+$)" +  //no white spaces
                    ".{4,}" +  //at least 4 characters
                    "$"
        )

        @JvmStatic
        fun getLocationInfoFromLatLng(
            latitude: Double,
            longitude: Double,
            context: Context
        ): HashMap<String, String>? {
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(context, Locale.getDefault())
            val hashMapLocationInfo = HashMap<String, String>()
            try {
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses.size > 0) {
                    // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    hashMapLocationInfo["address"] = addresses[0].getAddressLine(0)
                    hashMapLocationInfo["country"] = addresses[0].countryName
                    hashMapLocationInfo["state"] = addresses[0].adminArea
                    hashMapLocationInfo["city"] = addresses[0].locality
                    hashMapLocationInfo["postalCode"] = addresses[0].postalCode ?: ""
                    // Only if available else return NULL
                    hashMapLocationInfo["knownName"] = addresses[0].featureName ?: ""
                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return hashMapLocationInfo
        }

        fun utcToLocal(time: String,format: String): String {

            val df = SimpleDateFormat(format, Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(time)
            df.timeZone = TimeZone.getDefault()
            return df.format(date)
        }


        fun  String.localToUTC(): String {

            val df = SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss", Locale.ENGLISH)
            df.timeZone = TimeZone.getDefault()
            val date = df.parse(this)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val formattedDate = df.format(date!!)
            return formattedDate

        }
        fun getQueryString(url: String?): HashMap<String, String> {
            val uri = Uri.parse(url)
            val map: HashMap<String, String> = HashMap()
            for (paramName in uri.getQueryParameterNames()) {
                if (paramName != null) {
                    val paramValue: String = uri.getQueryParameter(paramName)?:""
                    map[paramName] = paramValue
                }
            }
            return map
        }


        fun openExternalLInk(link: String, context: Context){
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(link)
                )
            )
        }
    }






}
