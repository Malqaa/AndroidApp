package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityOrderDetailConfirmBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.PickImageMethodsDialog
import com.malqaa.androidappp.newPhase.utils.BetterActivityResult
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.ImagePicker
import com.malqaa.androidappp.newPhase.utils.SetOnImagePickedListeners
import java.io.File

class AttachInvoice : BaseActivity<ActivityOrderDetailConfirmBinding>(),
    PickImageMethodsDialog.OnAttachedImageMethodSelected {

    private lateinit var invoiceViewModel: InvoiceViewModel
    private lateinit var imageMethodsPickerDialog: PickImageMethodsDialog
    private lateinit var imagePicker: ImagePicker
    var pickImage = ""
    var orderId = 0

    val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityOrderDetailConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderId = intent.getIntExtra("orderId", 0)
        setUpViewModel()
        binding.attactedPicture.setOnClickListener {
            openCameraChooser()
        }
        binding.shipment.setOnClickListener {

            val file = File(pickImage)

            invoiceViewModel.confirmBankTransferPayment(orderId, file)
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
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
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

    override fun onDeleteImage() {
        TODO("Not yet implemented")
    }
}