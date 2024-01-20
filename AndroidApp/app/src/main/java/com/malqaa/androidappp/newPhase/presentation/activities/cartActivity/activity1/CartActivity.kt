package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity1

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.CartProductDetails
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.ProductCartItem
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass.Companion.removeItemCart
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity1.adapter.CartAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity2.AddressPaymentActivity
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.viewModel.CartViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CartActivity : BaseActivity(), CartAdapter.SetProductCartListeners {
    private var lastUpdatePosition: Int = 0
    lateinit var cartAdapter: CartAdapter
    private lateinit var cartViewModel: CartViewModel
    private lateinit var productsCartListResp: ArrayList<CartProductDetails>
    private lateinit var productCartItemRespList: ArrayList<ProductCartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        toolbar_title.text = getString(R.string.shopping_basket)
        setViewClickListeners()
        setCartAdapter()
        setupCartViewModel()

    }

    private fun setupCartViewModel() {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        cartViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
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
            if(it.status!=null && it.status=="409"){
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            }else {
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
                    it.cartProductQuantity = (it.cartProductQuantity?:0) + 1
                }
                cartAdapter.notifyItemChanged(lastUpdatePosition)
                setCartTotalPrice()
            }
        }
        cartViewModel.decreaseCartProductQuantityObserver.observe(this) { decreaseProductResp ->
            if (decreaseProductResp.status_code == 200) {
                productCartItemRespList[lastUpdatePosition].let {
                    it.cartProductQuantity = it.cartProductQuantity?:0 - 1
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
                        totalPrice += (product.price * (product.cartProductQuantity?:0))
                    } else {
                        totalPrice += (product.priceDiscount * (product.cartProductQuantity?:0))
                    }
                }
            }
            withContext(Dispatchers.Main) {
                ConstantObjects.newUserCart = productCartItemRespList
                cartAdapter.notifyDataSetChanged()
                price_total.text = totalPrice.toString()

            }
        }

    }

    private fun setCartAdapter() {
        productsCartListResp = ArrayList()
        productCartItemRespList = ArrayList()
        cartAdapter = CartAdapter(productCartItemRespList, this)
        rvCart.apply {
            adapter = cartAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }
        the_next.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                if (ConstantObjects.newUserCart.isNotEmpty()) {
                    //  if (SharedPreferencesStaticClass.getAssignCartToUser()) {
//                        gotToNextCartActivity()
//                    } else {
                    cartViewModel.assignCardToUser(SharedPreferencesStaticClass.getMasterCartId())
                    //  }
                } else {
                    showError(getString(R.string.empty_cart))
                }
            } else {
                goToSignInActivity()
            }

        }

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
        cartViewModel.getCartList(SharedPreferencesStaticClass.getMasterCartId())
    }

    override fun onDestroy() {
        super.onDestroy()
        cartViewModel.closeAllCall()
    }


}