package com.malqaa.androidappp.newPhase.utils

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
import android.util.Base64
import android.util.Log
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
import com.malqaa.androidappp.R
import com.malqaa.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ModelShipAddresses
import com.malqaa.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressesData
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.data.network.CommonAPI
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.data.network.service.MalqaApiService
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.*
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.addtocart.AddToCartResponseModel
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.favourites.FavouriteObject
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.favourites.favouriteadd
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.watchlist.watchlistadd
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance

import io.paperdb.Paper
import kotlinx.android.synthetic.main.alertpopup.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.*
import java.text.DateFormat
import java.text.ParseException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.Boolean as Boolean1


class HelpFunctions {

    companion object {
        const val datetimeformat_mmddyyyy: String = "MM/dd/yyyy"
        const val datetimeformat_24hrs: String = "yyyy-MM-dd'T'HH:mm:ss"
        const val datetimeformat_12hrs: String = "MM/dd/yyyy hh:mm:ss a"
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
                ).show()
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
                ConstantObjects.userobj = userObject
                // ConstantObjects.loggin_businessAccountId=user_object?.
            }
            return isLogin
        }

        private fun getCurrentDateTime(format: String): String {
            val sdf = SimpleDateFormat(format)

            return sdf.format(Date())
        }


        private fun ReportError(exception: java.lang.Exception) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            exception.printStackTrace(pw)
            val sStackTrace: String = sw.toString()

            var error = "App-Name: " + appName + "\r\n"
            error += "DateTime: " + getCurrentDateTime(datetimeformat_12hrs) + "\r\n"
            error += "Error: " + exception.message + "\r\n"
            error += "Stacktrace: " + sStackTrace + "\r\n"

            // ShowError(error)
        }

        fun ShowAlert(context: Context?, alertTitle: String, alertMessage: String) {
            if (context != null) {
                val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogView: View = inflater.inflate(R.layout.alertpopup, null).also {
                    if (alertTitle.isNotEmpty()) {
                        it.Lbl_ALertTitle.setText(alertTitle)
                    }
                    it.Lbl_AlertMessage.setText(alertMessage)
                }
                dialogBuilder.setView(dialogView)
                val alertDialog: AlertDialog = dialogBuilder.create()
                alertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()
                dialogView.btn_alertclose.setOnClickListener() {
                    alertDialog.dismiss()
                }
            }
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
            var RetVal = ""
            try {
                if (value.trim().isNotEmpty()) {
                    val defaultsourceformat =
                        if (sourceformat.trim().isEmpty()) datetimeformat_24hrs else sourceformat
                    val defaulttargetformat =
                        if (requiredformat.trim()
                                .isEmpty()
                        ) datetimeformat_mmddyyyy else requiredformat
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
                        RetVal = formatter.format(parser.parse(value)!!)
                    }
                } else {
                    RetVal = "01/01/1900"
                }
            } catch (ex: Exception) {
                ReportError(ex)
            }
            return RetVal
        }


        fun getFormattedDate2(
            date: String?,
            givenFormat: String?,
            requiredFormat: String?
        ): String {
            var result = ""
            val dateFormat: DateFormat?
            val requestDateFormat: DateFormat?
            var requestDate: Date? = null
            try {
                dateFormat = SimpleDateFormat(requiredFormat, Locale.ENGLISH)
                //  dateFormat.setTimeZone(TimeZone.getDefault())
                requestDateFormat = SimpleDateFormat(givenFormat, Locale.ENGLISH)

                requestDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
                requestDate = requestDateFormat.parse(date.toString())
                result = dateFormat.format(requestDate.toString())
            } catch (e: Exception) {
                println("hhhh " + e.message)
                e.printStackTrace()
            }
            return result
        }
        fun getViewFormatForDate(dateStr: String?): String? {
            try {
//        String outputPattern = "EEEE MMMM d, yyyy"
                val outputPattern = "dd/MM/yyyy"
                val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
                val tz = TimeZone.getTimeZone("UTC")
//            val tz = TimeZone.getTimeZone("Africa/Cairo")
                //  SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
                val df: SimpleDateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
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
//        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        fun getViewFormatForDateTrack(dateStr: String?,outputPattern :String): String? {
            try {
//        String outputPattern = "EEEE MMMM d, yyyy"
//                val outputPattern = "dd/MM/yyyy HH:mm:ss"
                val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
                val tz = TimeZone.getTimeZone("UTC")
//            val tz = TimeZone.getTimeZone("Africa/Cairo")
                //  SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
                val df: SimpleDateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
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
        fun getViewFormatForDateTrackWithoutUTC(dateStr: String?,outputPattern :String): String? {
            try {
                val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
                val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
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

        fun getAuctionClosingTime(dataStr: String): String {
            // 23/06/20236:55 PM
            try {
//        String outputPattern = "EEEE MMMM d, yyyy"
                val outputPattern = "yyyy-MM-dd HH:mm:ss"
                val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
                var typeI = SimpleDateFormat()

                if (dataStr.contains("/")) {
                    typeI = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH)
                } else {
                    typeI = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                }
                val tz = TimeZone.getTimeZone("UTC")
                outputFormat.timeZone = tz
                //
//            val tz = TimeZone.getTimeZone("Africa/Cairo")
                //  SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
//                val df: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                //  val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
                typeI.timeZone = tz
                val date: Date
                val str: String
                try {
                    date = typeI.parse(dataStr)
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
                println("hhhh " + dataStr)
                //  =====================
                val inputPattern = "yyyy-MM-dd'T'HH:mm:ss"
                val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                //=======
                val outputFormat: SimpleDateFormat =
                    SimpleDateFormat("dd/MM/yyyy HH:mm aa", Locale.ENGLISH)
                //  val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
                outputFormat.timeZone = TimeZone.getDefault()
                val date: Date
                val str: String
                try {
                    date = inputFormat.parse(dataStr)
                    println("hhhh " + date.toString())
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

        fun getAuctionClosingTimeForApi(dataStr: String): String {
            try {
                val outputPattern = "yyyy-MM-dd'T'HH:mm:ss"
                val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
                val typeI = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

                val date: Date
                val str: String
                try {
                    date = typeI.parse(dataStr)
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

        fun getAuctionClosingTime2(dataStr: String): String {
            // 23/06/20236:55 PM
            try {
//        String outputPattern = "EEEE MMMM d, yyyy"
                val outputPattern = "yyyy-MM-dd HH:mm:ss"
                val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)
                val tz = TimeZone.getTimeZone("UTC")
                outputFormat.timeZone = tz
                //
//            val tz = TimeZone.getTimeZone("Africa/Cairo")
                //  SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
                var typeI = SimpleDateFormat()

                if (dataStr.contains("T")) {
                    typeI = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
                } else {
                    typeI = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
                }
                outputFormat.timeZone = tz

                //  val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
                typeI.timeZone = tz
                val date: Date
                val str: String
                try {
                    date = typeI.parse(dataStr)
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


        fun InsertAdToWatchlist(
            AdsId: String,
            reminderType: Int,
            context: Context,
            onSuccess: (() -> Unit)? = null
        ) {
            startProgressBar(context as Activity)
            try {
                val callWatchList = getRetrofitBuilder().insertAddToUserWatchlist(15)
                callApi(callWatchList,
                    onSuccess = {
                        if (it.status_code == 200) {
                            //  GetUserWatchlist()
                            onSuccess?.invoke()
                            ShowLongToast(
                                it.message,
                                context
                            )
                        } else {
                            ShowLongToast(
                                it.message,
                                context
                            )
                        }
                        dismissProgressBar()
                    },
                    onFailure = { throwable, statusCode, errorBody ->
                        ShowLongToast(
                            "Error",
                            context
                        )
                        dismissProgressBar()
                    },
                    goLogin = {
                        dismissProgressBar()
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
                val callWatchList = getRetrofitBuilder().deleteAdFromUserWatchlist(ConstantObjects.logged_userid, AdsId)
                callApi(callWatchList,
                    onSuccess = {
                        if (it.status_code == 200 && (it.data == true || it.data == 1 || it.data == 1.0)) {
                            // GetUserWatchlist()
                            onSuccess?.invoke()
                            ShowLongToast(
                                "Removed Successfully",
                                context
                            )
                        } else {
                            ShowLongToast(
                                it.message,
                                context
                            )
                        }
                        dismissProgressBar()
                    },
                    onFailure = { throwable, statusCode, errorBody ->
                        ShowLongToast(
                            "Error",
                            context
                        )
                        dismissProgressBar()
                    },
                    goLogin = {
                        dismissProgressBar()
                    })

            } catch (ex: Exception) {
                throw ex
            }
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

        fun getFileImage(uri: Uri, context: Context): File {
            val path: String? = getRealPathFromURI(uri, context)
            return File(path.toString())
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

        fun getIndexOfValueInSpinner(spinner: Spinner, myString: String): Int {
            for (i in 0 until spinner.count) {
                if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                    return i
                }
            }
            return 0
        }


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
            Log.i("notifyListRespObserver","true "+isdialog)
            if (isdialog != null) {
                isdialog?.dismiss()
//                isdialog = null
            }
        }


        val PASSWORD_PATTERN = Pattern.compile(
            "^" + "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
//                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +  //any letter
//                    "(?=.*[@#$%^&+=])" +  //at least 1 special character
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
            val addresses: List<Address>
            val geocoder = Geocoder(context, Locale.getDefault())
            val hashMapLocationInfo = HashMap<String, String>()
            try {
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses.isNotEmpty()) {
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
            return df.format(date!!)

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
                ShowLongToast(context.getString(R.string.serverError), context)
            }

        }

        fun loadProfileImage(
            context: Context,
            path: String?,
            imageView: ImageView,
            pb_loading: View? = null,
            onComplete: (() -> Unit)? = null
        ) {

            pb_loading?.show()
            val imagePath = if (path == "" || path == null) "emptyPath" else path
            getPicassoInstance()
                .load(imagePath.replace("http", "https"))
                .error(R.drawable.profileicon_bottomnav)
                .into(imageView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        pb_loading?.hide()
                        //  onComplete?.invoke()
                    }

                    override fun onError(e: java.lang.Exception?) {
                        println("hhhh " + e?.message)
                        pb_loading?.hide()
                        // onComplete?.invoke()
                    }

                })
        }

    }


}