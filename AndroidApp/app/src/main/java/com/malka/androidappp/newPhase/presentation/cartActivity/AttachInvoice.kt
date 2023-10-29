package com.malka.androidappp.newPhase.presentation.cartActivity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModelProvider
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.BetterActivityResult
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.ImagePicker
import com.malka.androidappp.newPhase.data.helper.SetOnImagePickedListeners
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.presentation.accountFragment.AccountViewModel
import com.malka.androidappp.newPhase.presentation.addProduct.AccountObject
import com.malka.androidappp.newPhase.presentation.dialogsShared.PickImageMethodsDialog
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_order_detail_confirm.attacted_picture
import kotlinx.android.synthetic.main.activity_order_detail_confirm.shipment
import kotlinx.android.synthetic.main.fragment_account.loader
import kotlinx.android.synthetic.main.fragment_account.rate3
import kotlinx.android.synthetic.main.fragment_account.tvFollowCategory
import kotlinx.android.synthetic.main.fragment_account.tvUserPointTotalBalance
import kotlinx.android.synthetic.main.fragment_account.tvWalletTotalBalance
import kotlinx.android.synthetic.main.fragment_account.user_rating
import java.io.File
import kotlin.math.roundToInt

class AttachInvoice : BaseActivity(), PickImageMethodsDialog.OnAttachedImageMethodSelected {

    private lateinit var invoiceViewModel: InvoiceViewModel
    private lateinit var imageMethodsPickerDialog: PickImageMethodsDialog
    private lateinit var imagePicker: ImagePicker
    var pickImage = ""
    var orderId = 0

    val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_confirm)
        orderId= intent.getIntExtra("orderId", 0)
        setUpViewModel()
        attacted_picture.setOnClickListener {
            openCameraChooser()
        }
        shipment.setOnClickListener {

            val file = File(pickImage)

            invoiceViewModel.confirmBankTransferPayment(orderId ,file)
        }

    }

    private fun openCameraChooser() {
        imageMethodsPickerDialog = PickImageMethodsDialog(this, true, this)
        imageMethodsPickerDialog.show()
    }


    private fun setUpViewModel() {
        invoiceViewModel = ViewModelProvider(this).get(InvoiceViewModel::class.java)
        invoiceViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        invoiceViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        invoiceViewModel.errorResponseObserver.observe(this) {
            if(it.status!=null && it.status=="409"){
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            }else{
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message!!, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }

        }

        invoiceViewModel.confirmImageObserver.observe(this) {
            if (it.status_code == 200) {
                imageMethodsPickerDialog.dismiss()
//                loader.show()
//                accountViewModel.getUserDataForAccountTap()
            }
        }


    }
    override fun setOnAttachedImageMethodSelected(attachedMethod: Int) {
        imagePicker = ImagePicker(this, null, object : SetOnImagePickedListeners {
            override fun onImagePicked(imageUri: Uri) {
                pickImage = imageUri.path.toString()
//                setImage(imageUri)
            }

            override fun launchImageActivityResult(
                imageIntent: Intent,
                requestCode: Int,
            ) {
                activityLauncher.launch(imageIntent) { activityResult ->
                    if (activityResult.resultCode == RESULT_OK) {
                        imagePicker.handleActivityResult(
                            activityResult.resultCode,
                            requestCode,
                            activityResult.data
                        )
                    }
                }
            }
        })
        if (attachedMethod == ConstantObjects.CAMERA) {
            imagePicker.choosePicture(ImagePicker.CAMERA)
        } else {
            imagePicker.choosePicture(ImagePicker.GALLERY)
        }
    }

    private fun setImage(imageUri: Uri) {
        try {
            val bitmap =
                BitmapFactory.decodeStream(
                    this.contentResolver.openInputStream(
                        imageUri
                    )
                )
            val scaleBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * 0.4f).roundToInt(),
                (bitmap.height * 0.4f).roundToInt(),
                true
            )
            println("hhhh loaded")
//
//                imageUri
//            val file = File(imageUri.path)
//
//            accountViewModel.editProfileImage(file)

        } catch (e: Exception) {
            // println("hhhh " + e.message)
            HelpFunctions.ShowLongToast(getString(R.string.pickRightImage), this)
        }
    }

    override fun onDeleteImage() {
        TODO("Not yet implemented")
    }
}