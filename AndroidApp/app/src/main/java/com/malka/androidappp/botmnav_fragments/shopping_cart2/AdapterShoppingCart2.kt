package com.malka.androidappp.botmnav_fragments.shopping_cart2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.ShippingAddress
import com.malka.androidappp.botmnav_fragments.shoppingcart6_paymentmethod.CartPaymentMethod
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.servicemodels.ConstantObjects
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.shoppingcart2_card.view.*

class AdapterShoppingCart2(
    val shoppingcartProductposts: ArrayList<ModelShoppingcart2>,
    var context: Fragment,val mcontext: Context
) : RecyclerView.Adapter<AdapterShoppingCart2.AdapterShoppingCart2ViewHolder>() {

    class AdapterShoppingCart2ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val listingParty: TextView = itemview.listing_party
        val wishlistBtn: Button = itemview.btn_wishlist
        val closeproductCard: ImageButton = itemview.close_card_product
        val imgShopcartproduct: ImageView = itemview.img_shopcart
        val cartproductDescrip: TextView = itemview.tx_productdescription
        val pricebyQuantity: TextView = itemview.tx_after_descrip
        val totalPrice: TextView = itemview.total_price
        val spinner_quantity: Spinner = itemview.spinner_quantity
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterShoppingCart2ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.shoppingcart2_card, parent, false)
        return AdapterShoppingCart2ViewHolder(view)

    }

    override fun getItemCount() = shoppingcartProductposts.size

    override fun onBindViewHolder(holder: AdapterShoppingCart2ViewHolder, position: Int) {

        if (shoppingcartProductposts[position].productImgCaart != null && shoppingcartProductposts[position].productImgCaart.trim().length > 0)
            Picasso.get()
                .load(ApiConstants.IMAGE_URL + shoppingcartProductposts[position].productImgCaart.trim())
                .into(holder.imgShopcartproduct) else holder.imgShopcartproduct.setImageResource(R.drawable.cam)
        holder.listingParty.text = shoppingcartProductposts[position].listingPartyy
        holder.cartproductDescrip.text = shoppingcartProductposts[position].cartproductDescripp
        holder.pricebyQuantity.text = shoppingcartProductposts[position].pricebyQuantityy
        holder.totalPrice.text = shoppingcartProductposts[position].totalPrice
        holder.wishlistBtn.setOnClickListener(View.OnClickListener {
            shoppingcartProductposts[position].ItemInWatchlist =
                HelpFunctions.AdAlreadyAddedToWatchList(shoppingcartProductposts[position].advid)
            if (shoppingcartProductposts[position].ItemInWatchlist) {
                HelpFunctions.DeleteAdFromWatchlist(
                    shoppingcartProductposts[position].advid,
                    context = mcontext
                )
            } else {
                watchListPopup(holder.wishlistBtn, shoppingcartProductposts[position].advid)
//                HelpFunctions.InsertAdToWatchlist(
//                    shoppingcartProductposts[position].advid, 0,
//                    context = context
//                )
            }
        })
        holder.closeproductCard.setOnClickListener(View.OnClickListener {
            val resp: Boolean = HelpFunctions.DeleteFromUserCart(
                shoppingcartProductposts[position].CartId,
                mcontext
            );
            if (resp) {
                if (context is Shoppingcart2)
                    (context as Shoppingcart2).BindUserCartItems()
                else if (context is ShippingAddress)
                    (context as ShippingAddress).BindUserCartItems()
                else if (context is CartPaymentMethod)
                    (context as CartPaymentMethod).BindUserCartItems()
            }
        });
        holder.spinner_quantity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent!!.getItemAtPosition(position).toString()
                    if (ConstantObjects.usercart != null && ConstantObjects.usercart!!.size > 0) {
                        ConstantObjects.usercart!![position].advertisements.qty = selectedItem;
                        if (context is Shoppingcart2)
                            (context as Shoppingcart2).CalculateCost()
                        else if (context is ShippingAddress)
                            (context as ShippingAddress).CalculateCost()
                        else if (context is CartPaymentMethod)
                            (context as CartPaymentMethod).CalculateCost()
                    }
                }
            }
    }

    private fun watchListPopup(view: View, AdvId: String) {
        val popupMenu = PopupMenu(context.requireContext(), view)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_dont_email -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 0,
                        context = mcontext
                    )
                    true
                }
                R.id.menu_email_everyday -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 1,
                        context = mcontext
                    )
                    true
                }
                R.id.menu_email_3day -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 3,
                        context = mcontext
                    )
                    true
                }
                R.id.menu_email_once_a_week -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 7,
                        context = mcontext
                    )
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

}