package com.malka.androidappp.newPhase.data.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.StrictMode
import android.provider.MediaStore
import android.provider.Settings.Secure
import android.util.Base64
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import com.malka.androidappp.R
import com.malka.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ModelShipAddresses
import com.malka.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.network.CommonAPI
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.domain.models.servicemodels.*
import com.malka.androidappp.newPhase.domain.models.servicemodels.addtocart.AddToCartResponseModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.favourites.FavouriteObject
import com.malka.androidappp.newPhase.domain.models.servicemodels.favourites.favouriteadd
import com.malka.androidappp.newPhase.domain.models.servicemodels.watchlist.watchlistadd
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import kotlinx.android.synthetic.main.alertpopup.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.DateFormat
import java.text.ParseException
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
        const val datetimeformat_12hrs2: String = "dd/MM/yyyy hh:mm a"
        const val datetimeformat_hrs: String = "yyyy-MM-dd HH:mm"
        const val appName = "Malqaa"
        const val projectName = "OnRuff"
        const val deviceType = "Android"


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

        fun isUserLoggedIn(): Boolean1 {
            val isLogin = Paper.book().read(SharedPreferencesStaticClass.islogin, false) ?: false
            if (isLogin) {
                val userObject: LoginUser? =
                    Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
                ConstantObjects.logged_userid = userObject?.id.toString()
                // ConstantObjects.businessAccountUser=userObject?.businessAccounts
                ConstantObjects.logged_authtoken = userObject?.token.toString()
                ConstantObjects.userobj=userObject
                // ConstantObjects.loggin_businessAccountId=user_object?.
            }
            return isLogin
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
            val simpledateformat = SimpleDateFormat(aFormat, Locale.ENGLISH)
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
                        val parser = SimpleDateFormat(defaultsourceformat, Locale.ENGLISH)
                        val formatter = SimpleDateFormat(defaulttargetformat, Locale.ENGLISH)
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
                dateFormat = SimpleDateFormat(requiredFormat, Locale.ENGLISH)
                //  dateFormat.setTimeZone(TimeZone.getDefault())
                requestDateFormat = SimpleDateFormat(givenFormat, Locale.ENGLISH)

                // requestDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
                requestDate = requestDateFormat.parse(date)
                result = dateFormat!!.format(requestDate)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        fun getFormattedDate2(
            date: String?,
            givenFormat: String?,
            requiredFormat: String?
        ): String {
            var result = ""
            var dateFormat: DateFormat? = null
            var requestDateFormat: DateFormat? = null
            var requestDate: Date? = null
            try {
                dateFormat = SimpleDateFormat(requiredFormat, Locale.ENGLISH)
                //  dateFormat.setTimeZone(TimeZone.getDefault())
                requestDateFormat = SimpleDateFormat(givenFormat, Locale.ENGLISH)

                requestDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
                requestDate = requestDateFormat.parse(date)
                result = dateFormat!!.format(requestDate)
            } catch (e: Exception) {
                println("hhhh " + e.message)
                e.printStackTrace()
            }
            return result
        }

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        fun getViewFormatForDateTrack(dateStr: String?): String? {
            try {
//        String outputPattern = "EEEE MMMM d, yyyy";
                val outputPattern = "dd/MM/yyyy"
                val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
                val tz = TimeZone.getTimeZone("UTC")
//            val tz = TimeZone.getTimeZone("Africa/Cairo")
                //  SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
                val df: SimpleDateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                //  val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
                df.timeZone = tz
                val date: Date
                val str: String
                try {
                    date = df.parse(dateStr)
                    str = outputFormat.format(date)
                } catch (e: ParseException) {
                    return ""
                }
                return str
            } catch (e: Exception) {
                return ""
            }
        }

        fun getAuctionClosingTime(dataStr:String):String{
            // 23/06/20236:55 PM
            try {
//        String outputPattern = "EEEE MMMM d, yyyy";
                val outputPattern = "yyyy-MM-dd'T'HH:mm:ss"
                val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
                val tz = TimeZone.getTimeZone("UTC")
                outputFormat.timeZone=tz
               //
//            val tz = TimeZone.getTimeZone("Africa/Cairo")
                //  SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
                val df: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm aa", Locale.ENGLISH);
                //  val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
               // df.timeZone = tz
                val date: Date
                val str: String
                try {
                    date = df.parse(dataStr)
                    str = outputFormat.format(date)
                } catch (e: ParseException) {
                    println("hhhh " + e.message)
                    return ""
                }
                return str
            } catch (e: Exception) {
                println("hhhh " + e.message)
                return ""
            }
        }

        fun getAuctionClosingTimeByDate(dataStr: String): Date? {
            // 23/06/20236:55 PM
            try {
                println("hhhh "+dataStr)
                //  =====================;
                val inputPattern = "yyyy-MM-dd'T'HH:mm:ss"
                val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val tz = TimeZone.getTimeZone("UTC")
               //=======
                val outputFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm aa", Locale.ENGLISH);
                //  val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
                outputFormat.timeZone = TimeZone.getDefault()
                val date: Date
                val str: String
                try {
                    date = inputFormat.parse(dataStr)
                    println("hhhh " +date.toString())
                    str = outputFormat.format(date)
                } catch (e: ParseException) {
                    println("hhhh " + e.message)
                    return null
                }
                return date
            } catch (e: Exception) {
                println("hhhh " + e.message)
                return null
            }
        }

        fun getAuctionClosingTime2(dataStr: String): String {
            // 23/06/20236:55 PM
            try {
//        String outputPattern = "EEEE MMMM d, yyyy";
                val outputPattern = "yyyy-MM-dd'T'HH:mm:ss"
                val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
                val tz = TimeZone.getTimeZone("UTC")
                outputFormat.timeZone = tz
                //
//            val tz = TimeZone.getTimeZone("Africa/Cairo")
                //  SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
                val df: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                //  val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
                // df.timeZone = tz
                val date: Date
                val str: String
                try {
                    date = df.parse(dataStr)
                    str = outputFormat.format(date)
                } catch (e: ParseException) {
                    println("hhhh "+e.message)
                    return ""
                }
                return str
            } catch (e: Exception) {
                println("hhhh "+e.message)
                return ""
            }
        }

        fun AdAlreadyAddedToWatchList(adreferenceId: String?): Boolean1 {
            var RetVal = false
            adreferenceId?.let {
                if (it.trim().length > 0) {

                    for (IndWatch in ConstantObjects.userwatchlist) {
                        val id = IndWatch.id
                        if (id.toString().lowercase()
                                .equals(it.trim().lowercase())
                        ) {
                            RetVal = true
                            break
                        } else {
                            RetVal = false
                            continue
                        }
                    }

                }
            } ?: kotlin.run {
                RetVal = false
            }

            return RetVal
        }


        //Date: 11/13/2020
        fun GetUserFavourites(context: Fragment) {
            try {
                val malqa: MalqaApiService =
                    getRetrofitBuilder()
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
                val malqa: MalqaApiService = getRetrofitBuilder()
                val call: Call<AddFavResponse> = malqa.insertAddToUserWatchlist(15)
                call.enqueue(object : Callback<AddFavResponse> {
                    override fun onResponse(
                        call: Call<AddFavResponse>,
                        response: Response<AddFavResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val resp: AddFavResponse = response.body()!!;
                                if (resp.status_code == 200) {
                                    //  GetUserWatchlist()
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
                        call: Call<AddFavResponse>,
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
                val malqa: MalqaApiService = getRetrofitBuilder()
                val call: Call<BasicResponse> =
                    malqa.deleteAdFromUserWatchlist(ConstantObjects.logged_userid, AdsId)
                call.enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val resp: BasicResponse = response.body()!!;
                                if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                                    // GetUserWatchlist()
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

//        fun GetUserWatchlist() {
//            val malqa: MalqaApiService =
//                getRetrofitBuilder()
//            val call: Call<BasicResponse> =
//                malqa.getUserWatchlist()
//            call.enqueue(object : Callback<BasicResponse> {
//                override fun onResponse(
//                    call: Call<BasicResponse>,
//                    response: Response<BasicResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        if (response.body() != null) {
//                            val watchlistinfo: BasicResponse = response.body()!!
//                            if (watchlistinfo.status_code == 200)
//                            {
//                                val productList: ArrayList<Product> = Gson().fromJson(
//                                    Gson().toJson(watchlistinfo.data),
//                                    object : TypeToken<ArrayList<Product>>() {}.type
//                                )
//                                ConstantObjects.userwatchlist = productList
//                                EventBus.getDefault().post(WatchList())
//
//                            }
//
//
//
//
//
//
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
//
//                }
//            })
//        }


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
                    apiurl = Constants.REMOVE_FAVOURTIE_SELLER_URL;
                } else if (category != null && category.trim().length > 0) {
                    apiurl = Constants.REMOVE_FAVOURTIE_CATEGORY_URL;
                } else if (query != null && query.trim().length > 0) {
                    apiurl = Constants.REMOVE_FAVOURTIE_SEARCH_URL;
                }

                val malqa: MalqaApiService = getRetrofitBuilder()
                val call: Call<BasicResponseInt> =
                    malqa.deleteFromUserFavoriteList(
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

        fun encodeImage2(path: String): ByteArray {
            val imagefile = File(path)
            var fis: FileInputStream? = null
            fis = FileInputStream(imagefile)

            val bm = BitmapFactory.decodeStream(fis)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            //Base64.de
            return b
        }

        fun getBytesImage(uri: Uri, context: Context): ByteArray? {
            val path: String? = getRealPathFromURI(uri, context)
            return path?.let { encodeImage2(it) }

        }

        fun getFileImage(uri: Uri, context: Context): File {
            val path: String? = getRealPathFromURI(uri, context)
            val imagefile = File(path)
            return imagefile
        }

        private fun getRealPathFromURI(contentUri: Uri, context: Context): String? {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val loader = CursorLoader(context, contentUri, proj, null, null, null)
            val cursor: Cursor? = loader.loadInBackground()
            val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            val result: String? = column_index?.let { cursor?.getString(it) }
            cursor?.close()
            return result
        }


        fun DeleteUserCreditCard(CardId: String, context: Context): Boolean1 {
            var RetVal: Boolean1 = false;
            try {
                val malqa: MalqaApiService = getRetrofitBuilder()
                val call: Call<BasicResponse> = malqa.deleteUserCreditCard(CardId)
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
            val malqa: MalqaApiService = getRetrofitBuilder()
            val call: Call<AddToCartResponseModel> =
                malqa.getUsersCartList(ConstantObjects.logged_userid)
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
                val malqa: MalqaApiService = getRetrofitBuilder()
                val call: Call<BasicResponse> = malqa.deleteFromUserCart(CartId)
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


        fun AddNewShippingAddress(
            shippingaddress: ShippingAddressessData,
            context: Fragment
        ): Boolean1 {
            var RetVal: Boolean1 = false
            try {
                val malqa: MalqaApiService = getRetrofitBuilder()
                val call: Call<BasicResponse> = malqa.addNewShippingAddress(shippingaddress)
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
            val malqaa: MalqaApiService = getRetrofitBuilder()
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


        fun GetTemplatesJson(fileName: String, onSuccess: ((respone: String) -> Unit)? = null) {
            try {
                val malqa: MalqaApiService = getRetrofitBuilder()

                val API_BASE_URL =
                    Constants.HTTP_PROTOCOL + "://" + Constants.SERVER_LOCATION + "/" + Constants.IMAGE_FOLDER + "/jsonTemplates/" + fileName

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

        var isdialog: AlertDialog? = null

        fun startProgressBar(mActivity: Activity) {
            val infalter = mActivity.layoutInflater
            val dialogView = infalter.inflate(R.layout.progress_bar, null)
            Glide.with(mActivity).asGif().load(R.raw.loader).into(dialogView.splash_view)
            val bulider = AlertDialog.Builder(mActivity)
            bulider.setView(dialogView)
            bulider.setCancelable(false)
            isdialog = bulider.create()
            isdialog?.show()
            isdialog?.window!!.setBackgroundDrawableResource(R.color.transparent)
        }

        fun dismissProgressBar() {
            if (isdialog != null) {
                isdialog?.dismiss()
            }
        }


        val PASSWORD_PATTERN = Pattern.compile(
            "^" + "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +  //any letter
                     "(?=.*[@#$%^&+=])" +  //at least 1 special character
                    "(?=\\S+$)" +  //no white spaces
                    ".{6,}" +  //at least 4 characters
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

        fun utcToLocal(time: String, format: String): String {

            val df = SimpleDateFormat(format, Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(time)
            df.timeZone = TimeZone.getDefault()
            return df.format(date)
        }


        fun String.localToUTC(): String {

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
                    val paramValue: String = uri.getQueryParameter(paramName) ?: ""
                    map[paramName] = paramValue
                }
            }
            return map
        }


        fun openExternalLInk(link: String, context: Context) {
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(link)
                    )
                )
            } catch (e: java.lang.Exception) {
                HelpFunctions.ShowLongToast(context.getString(R.string.serverError), context)
            }

        }

        fun loadCompanyImage(context: Context, iv: ImageView, url: String?) {
//            Glide.with(context)
//                .load(url?.replace("http","https"))
//                .apply(
//                    RequestOptions()
//                        .placeholder(R.mipmap.malqa_iconn)
//                        .error(R.mipmap.malqa_iconn)
//                )
//                .into(iv)
//
            Picasso.get()
                .load(url?.replace("http","https"))
                .placeholder(R.drawable.splash_logo)
                .error(R.drawable.splash_logo)
                .into(iv)
        }

        fun loadImage(context: Context, iv: ImageView, url: String?) {


            Glide.with(context)
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(R.mipmap.malqa_iconn)
                        .error(R.mipmap.malqa_iconn)
                )
                .into(iv)
        }

        fun loadProfileImage(
            context: Context,
            path: String?,
            imageView: ImageView,
            pb_loading: View? = null,
            onComplete: (() -> Unit)? = null
        ) {

            pb_loading?.show()
            var imagePath = if (path == "" || path == null) "emptyPath" else path
            Picasso.get()
                .load(imagePath.replace("http","https"))
                .error(R.drawable.profileicon_bottomnav)
                .into(imageView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        pb_loading?.hide()
                        //  onComplete?.invoke()
                    }

                    override fun onError(e: java.lang.Exception?)
                    {
                        println("hhhh "+e?.message)
                        pb_loading?.hide()
                        // onComplete?.invoke()
                    }

                })
        }

    }


}