package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Filter
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.AccountMainItemBinding
import com.malqaa.androidappp.databinding.AccountSubItemBinding
import com.malqaa.androidappp.databinding.FragmentAccountBinding
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AccountObject
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.AccountItem
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.AccountSubItem
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addressUser.addressListActivity.ListAddressesActivity
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.PickImageMethodsDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.businessAccount.businessAccountsList.SwitchAccountActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.editProfileActivity.EditProfileActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myBids.MyBidsActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase.negotiationOfferPurchase.NegotiationOffersPurchaseActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase.negotiationOfferSale.NegotiationOffersSaleActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.technicalSupportActivity.listtechincalSupportMessage.TechnicalSupportListActivity
import com.malqaa.androidappp.newPhase.utils.BetterActivityResult
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.ImagePicker
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance
import com.malqaa.androidappp.newPhase.utils.SetOnImagePickedListeners
import com.malqaa.androidappp.newPhase.utils.helper.CameraHelper
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import io.paperdb.Paper
import kotlin.math.roundToInt

class AccountFragment : Fragment(R.layout.fragment_account),
    PickImageMethodsDialog.OnAttachedImageMethodSelected {
    val list: ArrayList<AccountItem> = ArrayList()
    private lateinit var accountViewModel: AccountViewModel

    private var isDeleteImage = false
    private lateinit var imageMethodsPickerDialog: PickImageMethodsDialog
    private lateinit var imagePicker: ImagePicker
    private var userImageUri: Uri? = null
    val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)

    private lateinit var layoutManager: LinearLayoutManager

    // View Binding variable
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list.apply {
            add(
                AccountItem(
                    getString(R.string.Sale),
                    arrayListOf(
                        AccountSubItem(getString(R.string.MyProducts), R.drawable.newslogo),
                        AccountSubItem(getString(R.string.MyProductsOffers), R.drawable.path),
                    )
                )
            )

            add(
                AccountItem(
                    getString(R.string.purchase), arrayListOf(
                        AccountSubItem(getString(R.string.my_orders), R.drawable.shopingbag),
                        AccountSubItem(getString(R.string.my_bids), R.drawable.bidlogo),
                        AccountSubItem(getString(R.string.Loser), R.drawable.lost),
                        AccountSubItem(
                            getString(R.string.shopping_basket),
                            R.drawable.cart
                        ),
                        AccountSubItem(
                            getString(R.string.negotiation_offers),
                            R.drawable.path
                        ),

                        )
                )
            )

            add(
                AccountItem(
                    getString(R.string.Settings), arrayListOf(
                        AccountSubItem(getString(R.string.edit_profile), R.drawable.setting),
                        AccountSubItem(getString(R.string.payment_cards), R.drawable.paymentcard),
                        AccountSubItem(getString(R.string.save_addresses), R.drawable.maps),
                        AccountSubItem(
                            getString(R.string.application_settings),
                            R.drawable.settinglogo
                        ),
                        AccountSubItem(getString(R.string.help), R.drawable.discord),
                        AccountSubItem(getString(R.string.technical_support), R.drawable.call),
                        AccountSubItem(
                            getString(R.string.switch_accounts_and_business_account),
                            R.drawable.user
                        ),
                        AccountSubItem(getString(R.string.logout), R.drawable.logout),

                        )
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        setUserData(ConstantObjects.userobj)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize View Binding
        _binding = FragmentAccountBinding.bind(view)

        if (MainActivity.myOrderTrigger) {
            MainActivity.myOrderTrigger = false
            findNavController().navigate(R.id.myRequest)
        }
        binding.tvUserName.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    EditProfileActivity::class.java
                )
            )
        }
        setViewClickListeners()
        setUpViewModel()
        setListener()
        accountViewModel.getAccountInfo()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpViewModel() {
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }
        accountViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), requireActivity())
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }

        }
        accountViewModel.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), requireActivity())
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message!!, requireActivity())
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
                }
            }

        }
        accountViewModel.walletDetailsObserver.observe(this) { walletDetailsResp ->
            if (walletDetailsResp.status_code == 200) {
                AccountObject.walletDetails = walletDetailsResp.walletDetails
                walletDetailsResp.walletDetails?.let {
//                    tvWalletTotalBalance.text = "${it.walletBalance} ${getString(R.string.Rayal)}"
                }

            }
        }
        accountViewModel.userPointsDetailsObserver.observe(this) { userPointsResp ->
            if (userPointsResp.status_code == 200) {
                AccountObject.userPointData = userPointsResp.userPointData
                userPointsResp.userPointData?.let {
                    binding.tvUserPointTotalBalance.text = "${it.pointsBalance} "
                }
            }
        }
        accountViewModel.editProfileImageObserver.observe(this) {
            if (it.status_code == 200) {
                binding.loader.show()
                accountViewModel.getUserDataForAccountTap()
            }
        }
        accountViewModel.getUserDataObserver.observe(this) {
            if (it.status == "Success") {
                if (it.userObject != null) {
                    val userData =
                        Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
                    val tempUserData = it.userObject
                    tempUserData.token = userData?.token ?: ""
                    Paper.book()
                        .write<LoginUser>(SharedPreferencesStaticClass.user_object, tempUserData)
                    tempUserData.let { it ->
                        ConstantObjects.userobj = it
                    }
                    setUserData(tempUserData)
                }
            }
        }
        accountViewModel.accountInfoObserver.observe(this) {
            binding.tvUserPointTotalBalance.text = it.data.pointsBalance.toString()
            binding.tvWalletTotalBalance.text = it.data.walletBalance.toString()
            binding.tvFollowCategory.text = it.data.followCatergoriesCount.toString()
            binding.userRating.text = it.data.rate.toString()
            when (it.data.rate) {
                3 -> {
                    binding.rate3.setImageResource(R.drawable.happyface_color)
                }

                2 -> {
                    binding.rate3.setImageResource(R.drawable.smileface_color)
                }

                1 -> {
                    binding.rate3.setImageResource(R.drawable.sadcolor_gray)
                }

                else -> {
                    binding.rate3.setImageResource(R.drawable.smile)
                }
            }
        }

        accountViewModel.logoutObserver.observe(this) {
            ConstantObjects.logged_userid = ""
            Paper.book().write(
                SharedPreferencesStaticClass.islogin,
                false
            )
            Paper.book()
                .delete(SharedPreferencesStaticClass.user_object)
            HelpFunctions.ShowLongToast(
                getString(R.string.loggedoutsuccessfully),
                context
            )
            SharedPreferencesStaticClass.saveMasterCartId("0")

            SharedPreferencesStaticClass.clearCartCount()
            findNavController().navigate(R.id.logout_to_home)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setUserData(userData: LoginUser?) {
        try {
            userData?.let {
                HelpFunctions.loadProfileImage(
                    requireContext(),
                    userData.img,
                    binding.ivUserImage,
                    binding.loader,
                )
                binding.tvUserName.text =
                    "${userData.firstName.toString()} ${userData.lastName.toString()}"
                userData.createdAt?.let {
                    binding.tvMemberSince.text = "${getString(R.string.member_since)} ${
                        HelpFunctions.getViewFormatForDate(it)
                    }"
                }
                binding.tvMembershipNumber.text =
                    "${getString(R.string.membership_number)} ${userData.membershipNumber}"
                setAdaptor()

            }
        } catch (e: Exception) {
        }
    }

    private fun setViewClickListeners() {
        binding.myWallet.setOnClickListener {
            findNavController().navigate(R.id.myWallet)
        }

        binding.myPoints.setOnClickListener {
            findNavController().navigate(R.id.myPoints)
        }

        binding.ivUserImageContainer.setOnClickListener {
            openCameraChooser()
        }
    }

    @SuppressLint("ResourceType")
    private fun setAdaptor() {
        layoutManager = LinearLayoutManager(requireContext())
        binding.mainItemRcv.adapter =
            object : GenericListAdapter<AccountItem>(
                R.layout.account_main_item,
                bind = { element, holder, itemCount, position ->

                    // AccountMainItemBinding is the generated binding class for account_main_item.xml
                    val mainItemBinding = AccountMainItemBinding.bind(holder.view)

                    holder.view.run {
                        element.run {
                            mainItemBinding.mainItemTv.text = name

                            mainItemBinding.subItemRcv.adapter =
                                object : GenericListAdapter<AccountSubItem>(
                                    R.layout.account_sub_item,
                                    bind = { element, holder, itemCount, position ->

                                        // AccountMainItemBinding is the generated binding class for account_main_item.xml
                                        val accountSubItemBinding =
                                            AccountSubItemBinding.bind(holder.view)

                                        holder.view.run {
                                            element.run {
                                                accountSubItemBinding.subItemTv.text = name
                                                accountSubItemBinding.itemIcon.setImageResource(
                                                    image
                                                )
                                                if (MainActivity.myOrderTrigger) {
                                                    MainActivity.myOrderTrigger = false
                                                    findNavController().navigate(R.id.myRequest)
                                                } else if (MainActivity.myBidTrigger) {
                                                    MainActivity.myBidTrigger = false
                                                    findNavController().navigate(R.id.mybids)
                                                }

                                                if (name == getString(R.string.logout)) {
                                                    accountSubItemBinding.subItemTv.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.bg
                                                        )
                                                    )
                                                    accountSubItemBinding.itemRightIcon.setColorFilter(
                                                        ContextCompat.getColor(
                                                            context,
                                                            R.color.bg
                                                        ), android.graphics.PorterDuff.Mode.SRC_IN
                                                    )
                                                    accountSubItemBinding.line.hide()
                                                } else {
                                                    accountSubItemBinding.subItemTv.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.black
                                                        )
                                                    )
                                                    accountSubItemBinding.itemRightIcon.setColorFilter(
                                                        ContextCompat.getColor(
                                                            context,
                                                            R.color.black
                                                        ), android.graphics.PorterDuff.Mode.SRC_IN
                                                    )
                                                    accountSubItemBinding.line.show()

                                                }
                                                setOnClickListener {
                                                    when (name) {
                                                        getString(R.string.MyProducts) -> {
                                                            findNavController().navigate(R.id.myProduct)
                                                        }

                                                        getString(R.string.my_orders) -> {
                                                            findNavController().navigate(R.id.myRequest)

                                                        }

                                                        getString(R.string.Loser) -> {
                                                            findNavController().navigate(R.id.lost_frag)

                                                        }

                                                        getString(R.string.my_bids) -> {
                                                            startActivity(
                                                                Intent(
                                                                    requireActivity(),
                                                                    MyBidsActivity::class.java
                                                                )
                                                            )
                                                        }

                                                        getString(R.string.negotiation_offers) -> {
                                                            startActivity(
                                                                Intent(
                                                                    requireActivity(),
                                                                    NegotiationOffersPurchaseActivity::class.java
                                                                ).apply {
                                                                    putExtra(
                                                                        "ComeFrom",
                                                                        "AccountFragment"
                                                                    )
                                                                }
                                                            )
                                                        }

                                                        getString(R.string.MyProductsOffers) -> {
                                                            startActivity(
                                                                Intent(
                                                                    requireActivity(),
                                                                    NegotiationOffersSaleActivity::class.java
                                                                )
                                                            )
                                                        }

                                                        getString(R.string.edit_profile) -> {
                                                            startActivity(
                                                                Intent(
                                                                    requireActivity(),
                                                                    EditProfileActivity::class.java
                                                                )
                                                            )
                                                        }

                                                        getString(R.string.save_addresses) -> {
                                                            startActivity(
                                                                Intent(
                                                                    requireActivity(),
                                                                    ListAddressesActivity::class.java
                                                                )
                                                            )
                                                        }

                                                        getString(R.string.application_settings) -> {
                                                            findNavController().navigate(R.id.applicationSetting)

                                                        }

                                                        getString(R.string.technical_support) -> {
                                                            startActivity(
                                                                Intent(
                                                                    requireActivity(),
                                                                    TechnicalSupportListActivity::class.java
                                                                )
                                                            )

                                                        }

                                                        getString(R.string.switch_accounts_and_business_account) -> {
                                                            startActivity(
                                                                Intent(
                                                                    requireActivity(),
                                                                    SwitchAccountActivity::class.java
                                                                )
                                                            )
                                                        }

                                                        getString(R.string.logout) -> {
                                                            accountViewModel.logoutUser(
                                                                SharedPreferencesStaticClass.getFcmToken()
                                                            )

                                                        }
                                                    }
                                                }


                                            }
                                        }
                                    }
                                ) {
                                    override fun getFilter(): Filter {
                                        // Your filter implementation
                                        return object : Filter() {
                                            override fun performFiltering(constraint: CharSequence?): FilterResults {
                                                return FilterResults()
                                            }

                                            override fun publishResults(
                                                constraint: CharSequence?,
                                                results: FilterResults?
                                            ) {
                                                // Handle filter results
                                            }
                                        }
                                    }
                                }.apply {
                                    submitList(list)
                                }
                        }
                    }
                }
            ) {
                override fun getFilter(): Filter {
                    // Your filter implementation
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            return FilterResults()
                        }

                        override fun publishResults(
                            constraint: CharSequence?,
                            results: FilterResults?
                        ) {
                            // Handle filter results
                        }
                    }
                }
            }.apply {
                submitList(list)
            }
        binding.mainItemRcv.isNestedScrollingEnabled = false
        binding.mainItemRcv.layoutManager = layoutManager
        binding.mainItemRcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = layoutManager.findFirstVisibleItemPosition()
                val currentItem = list[position]
                if (position != 0) binding.subTitle.visibility = View.VISIBLE
                else binding.subTitle.visibility = View.GONE
                binding.subTitle.text = currentItem.name
            }
        })
    }

    /*****/
    private fun openCameraChooser() {
        imageMethodsPickerDialog = PickImageMethodsDialog(requireActivity(), true, this)
        imageMethodsPickerDialog.show()
    }

    //====camera chooser
    override fun setOnAttachedImageMethodSelected(attachedMethod: Int) {
        imagePicker = ImagePicker(requireActivity(), null, object : SetOnImagePickedListeners {
            override fun onImagePicked(imageUri: Uri) {
                setImage(imageUri)
            }

            override fun launchImageActivityResult(
                imageIntent: Intent,
                requestCode: Int,
            ) {
                activityLauncher.launch(imageIntent) { activityResult ->
                    if (activityResult.resultCode == AppCompatActivity.RESULT_OK) {
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
            openGallery(startForResult)
        }
    }

    private fun setImage(imageUri: Uri) {
        try {
            val bitmap =
                BitmapFactory.decodeStream(
                    requireActivity().contentResolver.openInputStream(
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
            getPicassoInstance()
                .load(imageUri)
                .into(binding.ivUserImage)
            userImageUri = imageUri

            val file = CameraHelper.getMultiPartFromBitmap(bitmap, "attachment", requireContext())

            accountViewModel.editProfileImage(file)
        } catch (e: Exception) {
            HelpFunctions.ShowLongToast(getString(R.string.pickRightImage), requireActivity())
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val bitmap = CameraHelper.handleResult(it?.data?.data!!, requireContext())
                val file =
                    CameraHelper.getMultiPartFromBitmap(bitmap, "imgProfile", requireContext())
                accountViewModel.editProfileImage(file)
                binding.ivUserImage.setImageBitmap(bitmap)
            }
        }

    private fun openGallery(startForResult: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startForResult.launch(intent)
    }


    override fun onDeleteImage() {
        isDeleteImage = true
        accountViewModel.editProfileImage(null)
    }

    //=======Permissions and data handling
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker.handelPermissionsResult(requestCode, grantResults)
    }

    private fun setListener() {
        binding.followUp.setOnClickListener {
            findNavController().navigate(R.id.followUp)
        }

        binding.ratingBtn.setOnClickListener {
            findNavController().navigate(R.id.sellerRating)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (::accountViewModel.isInitialized)
            accountViewModel.closeAllCall()
    }

}
