package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity1

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityCartBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.CartProductDetails
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.ProductCartItem
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity1.adapter.CartAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity2.AddressPaymentActivity
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.viewModel.CartViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass.Companion.removeItemCart
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : BaseActivity<ActivityCartBinding>(), CartAdapter.SetProductCartListeners {
    private var lastUpdatePosition: Int = 0
    lateinit var cartAdapter: CartAdapter
    private lateinit var cartViewModel: CartViewModel
    private lateinit var productsCartListResp: ArrayList<CartProductDetails>
    private lateinit var productCartItemRespList: ArrayList<ProductCartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.shopping_basket)
        setViewClickListeners()
        setCartAdapter()
        setupCartViewModel()

    }

    private fun setupCartViewModel() {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        cartViewModel.isLoading.observe(this) {
            if (it)
                binding.progressBar.show()
            else
                binding.progressBar.hide()
        }
        cartViewModel.isLoadingAssignCartToUser.observe(this) {
            if (it) {
                HelpFunctions.startProgressBar(this)
            } else {
                HelpFunctions.dismissProgressBar()
            }
        }
        cartViewModel.isLoadingQuntity.observe(this) {
            if (it) {
                HelpFunctions.startProgressBar(this)
            } else {
                HelpFunctions.dismissProgressBar()
            }
        }
        cartViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        cartViewModel.errorResponseObserver.observe(this) {
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
        cartViewModel.cartListRespObserver.observe(this) { cartListResp ->
            if (cartListResp.status_code == 200) {
                if (cartListResp.cartDataObject?.listCartProducts != null) {
                    productsCartListResp.clear()
                    productCartItemRespList.clear()
                    productsCartListResp.addAll(cartListResp.cartDataObject.listCartProducts)
                    for (item in productsCartListResp) {
                        item.listProduct?.let {
                            productCartItemRespList.addAll(it)
                        }
                    }
                    removeItemCart(productCartItemRespList.size)
                    setCartTotalPrice()
                }
            } else {
                if (cartListResp.message != null) {
                    HelpFunctions.ShowLongToast(cartListResp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)

                }
            }
        }
        cartViewModel.increaseCartProductQuantityObserver.observe(this) { increaseProductResp ->
            if (increaseProductResp.status_code == 200) {
                productCartItemRespList[lastUpdatePosition].let {
                    it.cartProductQuantity = (it.cartProductQuantity ?: 0) + 1
                }
                cartAdapter.notifyItemChanged(lastUpdatePosition)
                setCartTotalPrice()
            }
        }
        cartViewModel.decreaseCartProductQuantityObserver.observe(this) { decreaseProductResp ->
            if (decreaseProductResp.status_code == 200) {
                productCartItemRespList[lastUpdatePosition].let {
                    it.cartProductQuantity = it.cartProductQuantity ?: 0 - 1
                }
                cartAdapter.notifyItemChanged(lastUpdatePosition)
                setCartTotalPrice()
            }
        }
        cartViewModel.removeProductFromCartProductsObserver.observe(this) { decreaseProductResp ->
            if (decreaseProductResp.status_code == 200) {
                productCartItemRespList.removeAt(
                    lastUpdatePosition
                )
                removeItemCart(productCartItemRespList.size)
                cartAdapter.notifyDataSetChanged()
                setCartTotalPrice()
            }
        }
        cartViewModel.assignCartToUserObserver.observe(this) { it ->
            if (it.status_code == 200) {
                gotToNextCartActivity()
            }

        }
    }

    private fun setCartTotalPrice() {
        lifecycleScope.launch(Dispatchers.IO) {
            var totalPrice = 0F
            for (item in productCartItemRespList) {
                item.let { product ->
                    if (product.priceDiscount == product.price) {
                        totalPrice += (product.price * (product.cartProductQuantity ?: 0))
                    } else {
                        totalPrice += (product.priceDiscount * (product.cartProductQuantity ?: 0))
                    }
                }
            }
            withContext(Dispatchers.Main) {
                ConstantObjects.newUserCart = productCartItemRespList
                cartAdapter.notifyDataSetChanged()
                binding.priceTotal.text = totalPrice.toString()

            }
        }

    }

    private fun setCartAdapter() {
        productsCartListResp = ArrayList()
        productCartItemRespList = ArrayList()
        cartAdapter = CartAdapter(productCartItemRespList, this)
        binding.rvCart.apply {
            adapter = cartAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setViewClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.theNext.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                if (ConstantObjects.newUserCart.isNotEmpty()) {
                    cartViewModel.assignCardToUser(SharedPreferencesStaticClass.getMasterCartId())
                } else {
                    showError(getString(R.string.empty_cart))
                }
            } else {
                goToSignInActivity()
            }
        }
    }

    private fun mergeTuples(
        tuple1: Triple<Int, Int, Int>,
        tuple2: Triple<Int, Int, Int>
    ): Triple<Int, Int, Int> {
        val mergedFirst = tuple1.first // Assuming the first value is always the same
        val mergedSecond = tuple1.second + tuple2.second
        val mergedThird = tuple1.third + tuple2.third
        return Triple(mergedFirst, mergedSecond, mergedThird)
    }

    fun mergeTuplesInList(tupleList: ArrayList<Triple<Int, Int, Int>>): ArrayList<Triple<Int, Int, Int>> {
        val mergedList = ArrayList<Triple<Int, Int, Int>>()

        // Iterate through the list two elements at a time and merge them
        var i = 0
        while (i < tupleList.size) {
            if (i + 1 < tupleList.size) {
                val mergedTuple = mergeTuples(tupleList[i], tupleList[i + 1])
                mergedList.add(mergedTuple)
                i += 2 // Skip to the next pair
            } else {
                // If there's only one tuple left, add it as is
                mergedList.add(tupleList[i])
                i++
            }
        }

        return mergedList
    }


    private fun gotToNextCartActivity() {
        startActivity(Intent(this, AddressPaymentActivity::class.java))
    }

    override fun onIncreaseQuantityProduct(position: Int) {
        lastUpdatePosition = position
        val productCartId = productCartItemRespList[position].cartproductId
        cartViewModel.increaseCartProductQuantity(productCartId.toString())
    }

    override fun onDecreaseQuantityProduct(position: Int) {
        lastUpdatePosition = position
        val productCartId = productCartItemRespList[position].cartproductId
        cartViewModel.decreaseCartProductQuantity(productCartId.toString())
    }

    override fun onDeleteProduct(position: Int) {
        val productCartId = productCartItemRespList[position].cartproductId
        cartViewModel.removeProductFromCartProducts(productCartId.toString())
    }


    override fun onResume() {
        super.onResume()
        if (SharedPreferencesStaticClass.getMasterCartId().toInt() != 0)
            cartViewModel.getCartList(SharedPreferencesStaticClass.getMasterCartId())
    }

    override fun onDestroy() {
        super.onDestroy()
        cartViewModel.closeAllCall()
    }

}