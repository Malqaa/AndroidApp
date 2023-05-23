package com.malka.androidappp.newPhase.data.network.service

import com.google.gson.JsonObject
import com.malka.androidappp.fragments.UserImageResponseBack
import com.malka.androidappp.fragments.cardetail_page.ModelAddSellerFav
import com.malka.androidappp.fragments.cardetail_page.ModelSellerDetails
import com.malka.androidappp.fragments.cardetail_page.bottomsheet_bidopt.getbidModel.ModelBidingResponse
import com.malka.androidappp.fragments.cardetail_page.bottomsheet_bidopt.post_bidprice.ModelPostBidPrice
import com.malka.androidappp.fragments.create_product.CreateProductResponseBack
import com.malka.androidappp.fragments.create_product.ModelCreateProduct
import com.malka.androidappp.fragments.create_product.ProductResponseBack
import com.malka.androidappp.fragments.feedback_frag.insert_feedback.GiveFeedbackResponseBack
import com.malka.androidappp.fragments.feedback_frag.insert_feedback.ModelGiveFeedBack
import com.malka.androidappp.fragments.myProductFragment.AllProductsResponseBack
import com.malka.androidappp.fragments.myProductFragment.edit_product.EditProductResponseBack
import com.malka.androidappp.fragments.myProductFragment.edit_product.ModelEditProduct
import com.malka.androidappp.fragments.sellerdetails.SellerResponseBack
import com.malka.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ModelShipAddresses
import com.malka.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.data.network.constants.Constants.GET_CATEGORY_TAGS_ENDPOINT
import com.malka.androidappp.newPhase.domain.models.addOrderResp.AddOrderResp
import com.malka.androidappp.newPhase.domain.models.addProductToCartResp.AddProductToCartResp
import com.malka.androidappp.newPhase.domain.models.addRateResp.AddRateResp
import com.malka.androidappp.newPhase.domain.models.addWaletTransactionResp.AddWalletTranactionResp
import com.malka.androidappp.newPhase.domain.models.bussinessAccountsListResp.BusinessAccountsListResp
import com.malka.androidappp.newPhase.domain.models.cartListResp.CartListResp
import com.malka.androidappp.newPhase.domain.models.categoryFollowResp.CategoryFollowResp
import com.malka.androidappp.newPhase.domain.models.categoryResp.CategoriesResp
import com.malka.androidappp.newPhase.domain.models.configrationResp.ConfigurationResp
import com.malka.androidappp.newPhase.domain.models.contauctUsMessage.ContactUsMessageResp
import com.malka.androidappp.newPhase.domain.models.contauctUsMessage.TechnicalSupportMessageListResp
import com.malka.androidappp.newPhase.domain.models.countryResp.CountriesResp
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malka.androidappp.newPhase.domain.models.homeCategoryProductResp.HomeCategoryProductResp
import com.malka.androidappp.newPhase.domain.models.homeSilderResp.HomeSliderResp
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginResp
import com.malka.androidappp.newPhase.domain.models.orderDetails.OrderDetailsResp
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderDetailsByMasterIDResp
import com.malka.androidappp.newPhase.domain.models.orderListResp.OrderListResp
import com.malka.androidappp.newPhase.domain.models.orderRateResp.RateObject
import com.malka.androidappp.newPhase.domain.models.orderRateResp.ShipmentRateResp
import com.malka.androidappp.newPhase.domain.models.pakatResp.PakatResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductResp
import com.malka.androidappp.newPhase.domain.models.productTags.CategoryTagsResp
import com.malka.androidappp.newPhase.domain.models.questionResp.AddQuestionResp
import com.malka.androidappp.newPhase.domain.models.questionsResp.QuestionsResp
import com.malka.androidappp.newPhase.domain.models.ratingResp.CurrentUserRateResp
import com.malka.androidappp.newPhase.domain.models.ratingResp.RateResponse
import com.malka.androidappp.newPhase.domain.models.regionsResp.RegionsResp
import com.malka.androidappp.newPhase.domain.models.resgisterResp.RegisterResp
import com.malka.androidappp.newPhase.domain.models.sellerInfoResp.SellerInfoResp
import com.malka.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.*
import com.malka.androidappp.newPhase.domain.models.servicemodels.addtocart.AddToCartResponseModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.addtocart.InsertToCartRequestModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.categorylistings.CategoryResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.checkout.CheckoutRequestModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.creditcard.CreditCardModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.creditcard.CreditCardResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.favourites.FavouriteObject
import com.malka.androidappp.newPhase.domain.models.servicemodels.favourites.favouriteadd
import com.malka.androidappp.newPhase.domain.models.servicemodels.feedbacks.FeedbackObject
import com.malka.androidappp.newPhase.domain.models.servicemodels.home.visitcount.visit_count_object
import com.malka.androidappp.newPhase.domain.models.servicemodels.questionModel.ModelAskQues
import com.malka.androidappp.newPhase.domain.models.servicemodels.questionModel.ModelPostAns
import com.malka.androidappp.newPhase.domain.models.servicemodels.questionModel.ModelQuesAnswr
import com.malka.androidappp.newPhase.domain.models.servicemodels.total_members.ModelGetTotalMembers
import com.malka.androidappp.newPhase.domain.models.servicemodels.total_online_users.ModelGetTotalOnlineUsers
import com.malka.androidappp.newPhase.domain.models.servicemodels.user.UserObject
import com.malka.androidappp.newPhase.domain.models.userAddressesResp.UserAddressesResp
import com.malka.androidappp.newPhase.domain.models.userPointsDataResp.ConvertMoneyToPointResp
import com.malka.androidappp.newPhase.domain.models.userPointsDataResp.UserPointDataResp
import com.malka.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.UserVerifiedResp
import com.malka.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.ValidateAndGenerateOTPResp
import com.malka.androidappp.newPhase.domain.models.walletDetailsResp.WalletDetailsResp
import com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market.popup_subcategories_list.ModelAddSearchFav
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface MalqaApiService {
    //    @Multipart
//    @POST("RegisterProviderWebsite")
//    fun createuser(
//        @Part("email") email: RequestBody,
//        @Part("password") password: RequestBody,
//        @Part("phone") phone: RequestBody,
//        @Part("cPassword") cPassword: RequestBody,
//        @Part("userName") userName: RequestBody,
//        @Part("info") info: RequestBody,
//        @Part("lang") lang: RequestBody,
//        @Part("projectName") projectName: RequestBody,
//        @Part("deviceType") deviceType: RequestBody,
//        @Part("deviceId") deviceId: RequestBody,
//        @Part file: MultipartBody.Part
//    ): Call<GeneralRespone>
    @Multipart
    @POST("RegisterWebsite")
    fun createuser2(
        @Part("userName") userName: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("invitationCode") invitationCode: RequestBody,
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("dateOfBirth") dateOfBirth: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("countryId") countryId: RequestBody,
        @Part("regionId") regionId: RequestBody,
        @Part("neighborhoodId") neighborhoodId: RequestBody,
        @Part("districtName") districtName: RequestBody,
        @Part("streetNumber") streetNumber: RequestBody,
        @Part("zipCode") zipCode: RequestBody,
        @Part("isBusinessAccount") info: RequestBody,
        @Part("lang") lang: RequestBody,
        @Part("projectName") projectName: RequestBody,
        @Part("deviceType") deviceType: RequestBody,
        @Part("deviceId") deviceId: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<RegisterResp>

    @GET("ValidateUserAndGenerateOtp")
    fun validateUserAndGenerateOtp(
        @Query("userName") userName: String,
        @Query("email") userEmail: String,
        @Query("phoneNumber") userPhone: String,
      //  @Query("User-Language") lang: String,
    ): Call<ValidateAndGenerateOTPResp>

    @GET("ResendOtp")
    fun resendOtp(
        @Query("phoneNumber") userPhone: String,
        @Query("User-Language") lang: String,
    ): Call<ValidateAndGenerateOTPResp>

    @POST("VerifyOtp")
    fun verifyOtp(
        @Query("phoneNumber") userPhone: String,
        @Query("otpCode") otpCode: String,
    ): Call<UserVerifiedResp>

    @POST("Accounts/verify")
    fun verifycode(@Body verifyusercode: PostReqVerifyCode): Call<BasicResponse>

    @GET("ListAllCategory?isShowInHome=true")
    fun getAllCategories(): Call<GeneralResponse>

    @GET("ListHomeCategoryProduct?currentPage=1&productNumber=10")
    fun ListHomeCategoryProduct(
        @Query("lang")
        language: String = ConstantObjects.currentLanguage
    ): Call<HomeCategoryProductResp>


//    @GET("GetSliderImages")
//    fun getHomeSlidersImages(
//        @Query("lang")
//        language: String = ConstantObjects.currentLanguage
//    ): Call<HomeSliderResp>

    @GET("ListAdvertisments")
    fun getHomeSlidersImages(
        @Query("type") type: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<HomeSliderResp>

    @GET("ListCountryDDL")
    fun getCountryNew(): Call<CountriesResp>

    @GET("ListRegionsByCountryIdDDL")
    fun getRegionNew(
        @Query("id") Id: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<RegionsResp>


    @GET("ListNeighborhoodByRegionIdDDL")
    fun getNeighborhoodByRegionNew(
        @Query("id") Id: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<RegionsResp>


    @GET("GetProductById")
    fun getProductDetailById2(
        @Query("id") id: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ProductResp>

    @GET("GetSellerInformation")
    fun getSellerInformation(
        @Query("productId") productId: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<SellerInfoResp>

    @GET("GetProductById")
    fun getAdDetailById(
        @Query("id") id: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralRespone>

    @Multipart
    @POST("AddQuestion")
    fun addQuestion(
        @Part("Question") question: RequestBody,
        @Part("ProductId") productId: RequestBody,
        @Part("lang") lang: RequestBody,
    ): Call<AddQuestionResp>

    @Multipart
    @POST("ReplyQuestion")
    fun replayQuestion(
        @Part("Answer") question: RequestBody,
        @Part("Id") id: RequestBody,
        @Query("lang") language: String = ConstantObjects.currentLanguage,
    ): Call<AddQuestionResp>


    @GET("ListSimilarProducts")
    fun getSimilarProductForOtherProduct(
        @Query("currentPage") page: Int,
        @Query("productId") productId: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ProductListResp>


    @GET("ListLastView?pageIndex=1&PageRowsCount=10")
    fun getListLastView(
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ProductListResp>


    @POST("AddToLastView")
    fun addLastViewProduct(
        @Query("productId") productId: Int
    ): Call<GeneralRespone>

    @GET("ListQuestions")
    fun getQuestionList(
        @Query("productId") productId: Int
    ): Call<QuestionsResp>

    @POST("AddFavoriteProduct")
    fun addProductToFav(
        @Query("productId") productId: Int
    ): Call<GeneralResponse>

    @GET("ListFavoriteProduct")
    fun getUserWatchlist(@Query("lang") language: String = ConstantObjects.currentLanguage): Call<ProductListResp>;


    @GET("GetListCategoriesByProductName")
    fun getListCategoriesByProductName(
        @Query("productName") productName: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<CategoryTagsResp>

    @GET("ListAllSpecificationAndSubSpecificationByCatId")
    fun getDynamicSpecificationForCategory(
        @Query("id") id: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage,
        @Query("currentPage") page: String = "1"
    ): Call<DynamicSpecificationResp>

    @GET("ListAllSpecificationAndSubSpecificationByCatId")
    fun getSpecification(
        @Query("id") id: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralResponse>

    @GET("GetAllPakatsList")
    fun getAllPakatList(
        @Query("categoryId") id: String,
        @Query("PakatType") PakatType: Int = 1,
        @Query("isAdmin") isAdmin: Boolean = false,
        @Query("lang") language: String = ConstantObjects.currentLanguage,
    ): Call<PakatResp>

    @Multipart
    @POST("AddProduct")
    fun addProduct(
        @Part("nameAr") nameAr: RequestBody,
        @Part("nameEn") nameEn: RequestBody,
        @Part("subTitleAr") subTitleAr: RequestBody,
        @Part("subTitleEn") subTitleEn: RequestBody,
        @Part("descriptionAr") descriptionAr: RequestBody,
        @Part("descriptionEn") descriptionEn: RequestBody,
        @Part("qty") qty: RequestBody,
        @Part("price") price: RequestBody,
        @Part("priceDisc") priceDisc: RequestBody,
        @Part("acceptQuestion") acceptQuestion: RequestBody,
        @Part("isNegotiationOffers") isNegotiationOffers: RequestBody,
        @Part("withFixedPrice") withFixedPrice: RequestBody,
        @Part("isMazad") isMazad: RequestBody,
        @Part("isSendOfferForMazad") isSendOfferForMazad: RequestBody,
        @Part("startPriceMazad") startPriceMazad: RequestBody,
        @Part("lessPriceMazad") lessPriceMazad: RequestBody,
        @Part("mazadNegotiatePrice") mazadNegotiatePrice: RequestBody,
        @Part("mazadNegotiateForWhom") mazadNegotiateForWhom: RequestBody,
        @Part("appointment") appointment: RequestBody,
        @Part("status") productCondition: RequestBody,
        @Part("categoryId") categoryId: RequestBody,
        @Part("countryId") countryId: RequestBody,
        @Part("regionId") regionId: RequestBody,
        @Part("neighborhoodId") neighborhoodId: RequestBody,
        @Part("Street") Street: RequestBody,
        @Part("GovernmentCode") GovernmentCode: RequestBody,
        @Part("pakatId") pakatId: RequestBody,
        @Part("productSep") productSep: RequestBody,
        @Part listImageFile: List<MultipartBody.Part>,//listImageFile
        @Part("MainImageIndex") MainImageIndex: RequestBody,
        @Part("videoUrl") videoUrl: RequestBody,
        @Part("PickUpDelivery") PickUpDelivery: RequestBody,
        @Part("DeliveryOption") DeliveryOption: RequestBody,
    ): Call<GeneralResponse>

    @Multipart
    @POST("AddProduct")
    fun addProduct2(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        // @Part files: List<MultipartBody.Part>
    ): Call<AddProductResponse>

    @Multipart
    @POST("AddRateProduct")
    fun AddRateProduct(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<AddRateResp>

    @GET("ListProductByBusinessAccountId")
    fun getMyProduct(
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ProductListResp>

    @GET("ListDidntSellProducts")
    fun getListDidntSellProducts(
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ProductListResp>

    //    @GET("AdvancedFilter")
//    fun searchForProductInCategory(@QueryName(encoded = true) queryString: HashMap<String,String>):Call<ProductListResp>
//    @GET("AdvancedFilter")
//    fun searchForProductInCategory(@Query("mainCatId")mainCatId:Int,@QueryName queryString: HashMap<String,String>):Call<ProductListResp>
    @GET
    fun searchForProductInCategory(
        @Url url: String
    ): Call<ProductListResp>

    @GET("ListRateProduct")
    fun getRates(
        @Query("productId") productID: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<RateResponse>

    @GET("CurrenUserRateForProdust")
    fun getCurrenUserRateForProdust(
        @Query("productId") productID: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<CurrentUserRateResp>

    @Multipart
    @PUT("EditRateProduct")
    fun editRateProduct(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<AddRateResp>

    @GET("ListRateProvider")
    fun getSellerRates(
        @Query("providerId") providerId: String,
        @Query("BusinessAccountId") BusinessAccountId: String?,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<SellerRateListResp>

    @GET("ListRateSeller?PageRowsCount=10")
    fun getSellerRates2AsAsseller(
        @Query("providerId") providerId: String,
        @Query("BusinessAccountId") BusinessAccountId: String?,
        @Query("pageIndex") page: Int,
        @Query("rate") rate: Int?,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<SellerRateListResp>

    @GET("ListRateBuyer?PageRowsCount=10")
    fun getSellerRates2AsAsABuyer(
        @Query("buyerId") providerId: String,
        @Query("BusinessAccountId") BusinessAccountId: String?,
        @Query("pageIndex") page: Int,
        @Query("rate") rate: Int?,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<SellerRateListResp>

    @POST("AddRateProvider")
    fun addRateSeller2(
        @Body partMap: HashMap<String, Any>,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<AddRateResp>

    @GET("GetBusinessAccountOrders?PageRowsCount=10")
    fun getBusinessAccountOrders(
        @Query("pageIndex") page: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<OrderListResp>

    @GET("GetClientAddedOrders?PageRowsCount=10&OrderStatus!=5")
    fun getCurrentOrders(
        @Query("pageIndex") page: Int,
        @Query("userId") userId: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<OrderListResp>

    @GET("GetClientAddedOrders?PageRowsCount=10&OrderStatus=5")
    fun getFinishedOrders(
        @Query("pageIndex") page: Int,
        @Query("userId") userId: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<OrderListResp>

    @POST("ProductDiscount")
    fun addDiscount(
        @Query("productId") productID: Int,
        @Query("PriceDiscount") priceDiscount: Float,
        @Query("discountEndDate") finaldate: String,
    ): Call<GeneralResponse>

    @GET("ListAddressesForUser")
    fun getListAddressesForUser(
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<UserAddressesResp>

    @Multipart
    @POST("AddAddressForUser")
    fun addAddressForUser(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<GeneralResponse>

    @Multipart
    @PUT("EditAddressForUser")
    fun editAddressForUser(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<GeneralResponse>

    @Multipart
    @POST("AddProductToCartProducts")
    fun addProductToCartProducts(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<AddProductToCartResp>


    @GET("ListCartProductsForClient")
    fun getListCartProductsForClient(@Query("cartMasterId") cartMasterId: String): Call<CartListResp>

    @GET("AssignCartMastetToUser")
    fun assignCartMastetToUser(
        @Query("cartMasterId") cartMasterId: String
    ): Call<GeneralResponse>

    @POST("IncreaseCartProductQuantity")
    fun increaseCartProductQuantity(
        @Query("cartproductId") cartproductId: String,
        @Query("quantity") quantity: String
    ): Call<GeneralResponse>

    @POST("DecreaseCartProductQuantity")
    fun decreaseCartProductQuantity(
        @Query("cartproductId") cartproductId: String,
        @Query("quantity") quantity: String
    ): Call<GeneralResponse>

    @DELETE("RemoveProductFromCartProducts")
    fun removeProductFromCartProducts(@Query("cartproductId") cartproductId: String): Call<GeneralResponse>

    @Multipart
    @POST("AddOrder")
    fun addOrder(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<AddOrderResp>

    @POST("ApplyCouponOnCart")
    fun applyCouponOnCart(
        @Query("cartMasterId") cartMasterId: String,
        @Query("couponCode") couponCode: String,
        @Query("buyWithFixedRpriceOrNegotiation") buyWithFixedRpriceOrNegotiation: String
    ): Call<GeneralResponse>

    @POST("ApplyCouponOnCart")
    fun applyCouponOnCart(
        @Query("cartMasterId") cartMasterId: String,
        @Query("couponCode") couponCode: String,
        @Query("buyWithFixedRpriceOrNegotiation") buyWithFixedRpriceOrNegotiation: String,
        @Query("couponForbusinessAccountId") couponForbusinessAccountId: String
    ): Call<GeneralResponse>

    @GET("ListSellerProducts?PageRowsCount=10")
    fun getListSellerProducts(
        @Query("pageIndex") pageIndex: Int,
        @Query("sellerId") sellerProviderId: String,
        @Query("sellerBusinssAccountId") sellerBusinssAccountId: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ProductListResp>;

    @GET("GetOrderDetailsByOrderId")
    fun getOrderDetailsByOrderID(
        @Query("orderId") orderId: Int,
    ): Call<OrderDetailsResp>

    @GET("GetOrderMasterDetailsByMasterOrderId")
    fun getOrderMasterDetailsByMasterOrderId(
        @Query("masterOrderId") masterOrderId: Int,
    ): Call<OrderDetailsByMasterIDResp>

    @POST("OrderRate")
    fun addShipmentRate(
        @Body shimpentRateObject: RateObject
    ): Call<GeneralResponse>

    @GET("CurrenUserRateForEdit")
    fun getShipmentRate(@Query("orderId") orderId: Int): Call<ShipmentRateResp>


    @POST("ChangeOrderStatus")
    fun changeOrderStatus(
        @Query("orderId") orderId: Int,
        @Query("status") orderStatus: Int
    ): Call<GeneralResponse>

    @GET("GetUserWalletTransactions")
    fun getWalletDetails(
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<WalletDetailsResp>

    @Multipart
    @POST("AddWalletTransaction")
    fun addWalletTransaction(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<AddWalletTranactionResp>

    @GET("GetUserPointsTransactions")
    fun getUserPointsTransactions(
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<UserPointDataResp>

    @POST("TransferPointsToMoney")
    fun transferPointsToMoney(
        @Query("transactionPointsAmount") transactionPointsAmount: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ConvertMoneyToPointResp>


    @GET("ListLostProducts")
    fun getLostProducts(): Call<ProductListResp>


    @POST("AddEditContactUs")
    fun addEditContactUs(@Body data: HashMap<String, Any>): Call<ContactUsMessageResp>

    @GET("ListContactUs?isAdmin=false")
    fun getListContactUs(): Call<TechnicalSupportMessageListResp>

    @DELETE("RemoveAddressForUser")
    fun deleteUserAddress(@Query("id") addressID: Int): Call<GeneralResponse>


    @POST("ForgetPassword")
    fun forgetPassword(@Query("email") email: String): Call<GeneralResponse>

    @POST("ChangePassword")
    fun changePasswordAfterForget(@Body data: HashMap<String, Any>): Call<GeneralResponse>

    @GET("GatAllBusinessAccounts")
    fun gatAllBusinessAccounts(): Call<BusinessAccountsListResp>


    @Multipart
    @POST("EditProfileImage")
    fun editProfileImage(@Part part: MultipartBody.Part): Call<GeneralResponse>

    /***
     * ***********************************
     * ***********************************
     * ***********************************
     * ***********************************
     * **/
    // ?currentPage=1
    //+++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++
    @GET("ListAdvertisments")
    fun SliderAPI(): Call<GeneralResponse>


    @GET("BussinessProduct/detailsofproduct")
    fun getProductDetailById(
        @Query("id") id: String,
        @Query("loginUserId") loginUserId: String
    ): Call<ProductResponseBack>


    @POST("Accounts/updatePersonalInfo")
    fun updateUserSiginup(@Body updateusersignupp: User): Call<User>

    @POST("Accounts/ResendCode")
    fun resendcode(@Body resendotpcode: User): Call<BasicResponse>


    @POST("CarTemplate/Create")
    fun createAllAd(@Body data: HashMap<String, String>): Call<CreateAdvResponseBack>


    @POST("CarTemplate/update")
    fun updateCarTemplate(@Body data: HashMap<String, String>): Call<CreateAdvResponseBack>


    @POST("Accounts/insertaddress")
    fun insertAddress(@Body info: GetAddressResponse.AddressModel): Call<GeneralRespone>

    @POST("Accounts/updateAdress")
    fun updateAddress(@Body info: GetAddressResponse.AddressModel): Call<GeneralRespone>

    @POST("Accounts/CreateBusinessAccount")
    fun addBusinesUser(@Body info: BusinessUserRespone.BusinessUser): Call<GeneralRespone>

    @POST("Accounts/UploadBusinessDocuments")
    fun addBusinessRegisterFile(@Body info: getBusinessRegisterFile.GetDocuments): Call<GeneralRespone>

    @POST("UserBankAccount/AddUserBankAccount")
    fun addbankaccount(@Body info: BankListRespone.BankDetail): Call<GeneralRespone>

    @GET("Accounts/getaddresses")
    fun getAddress(@Query("loginId") loginId: String): Call<GetAddressResponse>

    @GET("UserBankAccount/UserBankAccountsById")
    fun getBankDetail(@Query("UserID") loginId: String): Call<BankListRespone>

    @GET("Accounts/GetBusinessListByUserId")
    fun getBusinessUserList(@Query("UserID") loginId: String): Call<BusinessUserRespone>


    @GET("Accounts/GetUser")
    fun getuser(@Query("id") userid: String): Call<UserObject>;


    @GET("Auction/getall")
    fun getuserfeedback(@Query("loggedin") userid: String): Call<FeedbackObject>;

    @GET("all")
    fun getuserfavourites(@Query("loggedIn") userid: String): Call<FavouriteObject>;


    @GET("Accounts/GetUser")
    fun getAdSeller(
        @Query("id") id: String,
        @Query("loggedin") loggedin: String
    ): Call<SellerResponseBack>

    @GET("Accounts/GetUser")
    fun getAdSellerByID(
        @Query("id") id: String
    ): Call<ModelSellerDetails>


    @POST("Accounts/ForgetEmailRequest")
    fun forgotpassemail(@Body forgottpass: User): Call<GeneralRespone>

    @POST("Accounts/ChangePassword")
    fun changepass(@Body changepasspost: User): Call<User>

    @POST("AddFavoriteProduct")
    fun InsertAdtoUserWatchlist(@Query("productId") productId: Int): Call<AddFavResponse>

    @POST("Watchlist/delete")
    fun DeleteAdFromUserWatchlist(
        @Query("userid") userid: String,
        @Query("adid") adsId: String
    ): Call<BasicResponse>

    @POST("Accounts/userimageupload")
    fun userimageupload(
        @Query("userId") userid: String,
        @Body imagebase64string: String
    ): Call<UserImageResponseBack>

    @POST("?")
    fun InsertToUserFavouritelist(@Body insertfav: favouriteadd): Call<BasicResponse>

    @POST("?")
    fun DeleteFromUserFavouritelist(
        @Query("sellerid") sellerid: String,
        @Query("category") category: String,
        @Query("query") query: String,
        @Query("userid") userid: String,
    ): Call<BasicResponseInt>

    @POST("AdvQueAndAns/create")
    fun askQues(@Body askques: ModelAskQues): Call<ModelAskQues>

    @GET("CarTemplate/GetQuestionByAd")
    fun quesAns(
        @Query("adid") qaadid: String,
        @Query("loggedin") qalogid: String
    ): Call<ModelQuesAnswr>

    @GET("$GET_CATEGORY_TAGS_ENDPOINT?")
    fun getCategoryTags(
        @Query("name") name: String
    ): Call<CategoryTagsResp>

    @GET("BussinessProduct/getall")
    fun getAllProducts(): Call<AllProductsResponseBack>


    @POST("Favourite/insertseller")
    fun addSellerFav(@Body addfav: ModelAddSellerFav): Call<ModelAddSellerFav>

    @POST("Favourite/insertcategory")
    fun addCatFav(@Body addfav: ModelAddCatFav): Call<ModelAddCatFav>

    @POST("Favourite/insertsearch")
    fun addSearchFav(@Body addfav: ModelAddSearchFav): Call<ModelAddSearchFav>

    @POST("AdvQueAndAns/answer")
    fun postAnsByQid(
        @Query("qId") ansqid: String,
        @Query("answer") ansans: String,
        @Query("Loggedin") ansLoginId: String
    ): Call<ModelPostAns>


    @GET(Constants.HOME_TOTAL_VISIT_COUNT)
    fun GetTotalVisitCount(): Call<visit_count_object>;

    @GET("Accounts/soldunsolditems")
    fun getunsolditemsbyId(@Query("userid") userid: String): Call<ModelSoldUnsold>

    @GET(
        "Accounts/soldunsolditems"
    )
    fun getsolditemsbyId(@Query("userid") userid: String): Call<ModelSoldUnsold>

    @POST("BusinessUser/Insertbusinessuser")
    fun busiRegis(@Body busiReg: ModelBusinessRegistration): Call<ModelBusinessRegistration>


    @GET("GetAllOnlineUsers")
    fun GetTotalOnlineUers(): Call<ModelGetTotalOnlineUsers>

    @GET("Accounts/usercount")
    fun getTotalMembers(): Call<ModelGetTotalMembers>

    @GET(Constants.GET_AD_DETAIL_BIDING_PRICE_ENDPOINT)
    fun getbidgpricebyAdvId(@Query("advId") getbidByadvId: String): Call<ModelBidingResponse>

    @POST(Constants.POST_MAX_BIDING_PRICE)
    fun postBidPrice(@Body postBidpricee: ModelPostBidPrice): Call<ModelPostBidPrice>

    @GET(Constants.GET_BUYNOW_SHIPPINGADDRESS_ENDPOINT)
    fun getshipaddress(@Query("loginId") getaddressbyLoginId: String): Call<ModelShipAddresses>

    @POST(Constants.INSERT_BUYNOW_SHIPPINGADDRESS_ENDPOINT)
    fun AddNewShippingAddress(@Body shippingaddress: ShippingAddressessData): Call<BasicResponse>

    @GET("CarTemplate/Search")
    fun searchcategorylist(@Query("query") category: String): Call<CategoryResponse>;

    @POST("BussinessProduct/createproduct")
    fun createBusinessProduct(@Body createproduct: ModelCreateProduct): Call<CreateProductResponseBack>

    @POST("Auction/InsertFeedback")
    fun giveFeedback(@Body giveFeedback: ModelGiveFeedBack): Call<GiveFeedbackResponseBack>

    @POST("BussinessProduct/editproduct")
    fun editBusinessProduct(@Body editproduct: ModelEditProduct): Call<EditProductResponseBack>


    //  GeneralResponse
    @GET("CardDetail/getbyuserid")
    fun GetUserCreditCards(@Query("usid") userid: String): Call<CreditCardResponse>;

    @POST("CardDetail/insertcard")
    fun InsertUserCreditCard(@Body insertads: CreditCardModel): Call<BasicResponse>

    @POST("CardDetail/updatecard")
    fun UpdateUserCreditCard(@Body insertads: CreditCardModel): Call<BasicResponse>

    @DELETE("CardDetail/deletecard")
    fun DeleteUserCreditCard(@Query("id") userid: String): Call<BasicResponse>

    @GET("AddToCart/getbyloginuserid")
    fun GetUsersCartList(@Query("loggedinUserId") userid: String): Call<AddToCartResponseModel>;

    @POST("AddToCart/create")
    fun AddToUserCart(@Body addtocart: InsertToCartRequestModel): Call<BasicResponse>

    @GET("AddToCart/getall")
    fun getMyRequest(@Query("loginId") loginId: String): Call<getCartModel>

    @DELETE("AddToCart/delete")
    fun DeleteFromUserCart(@Query("id") userid: String): Call<BasicResponse>

    @POST("Checkout/checkoutiteminsert")
    fun PostUserCheckOut(@Body checkout: CheckoutRequestModel): Call<GeneralRespone>

    @GET()
    fun jsonTemplates(@Url url: String): Call<JsonObject>


    // NEw API START HERE

    @POST("AddFollow")
    fun AddFollow(@Body body: List<Int>): Call<GeneralResponse>


    @DELETE("RemoveFollow")
    fun RemoveFollow(@Query("catId") catId: Int): Call<GeneralResponse>

    @GET("ListCategoryFollow")
    fun getListCategoryFollow(@Query("lang") language: String = ConstantObjects.currentLanguage): Call<CategoryFollowResp>


    @GET("GetCategoryById")
    fun GetCategoryById(
        @Query("id") id: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralResponse>

    @GET("AdvanceFiltter")
    fun AdvanceFiltter(
        @QueryMap filter: Map<String, String>,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralResponse>


    @GET("Serach")
    fun Serach(
        @QueryMap filter: Map<String, String>,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralResponse>

    @GET("GetSubCategoryByMainCategory")
    fun GetSubCategoryByMainCategory(
        @Query("id") id: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralResponse>

    @GET("GetSubCategoryByMainCategory")
    fun getSubCategoryByMainCategory2(
        @Query("id") id: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<CategoriesResp>


    @GET("GetConfigurationByName")
    fun getConfigurationData(
        @Query("configKey") configKey: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ConfigurationResp>


    @GET("ListCountries")
    fun getCountry(@Query("lang") language: String = ConstantObjects.currentLanguage): Call<GeneralResponse>

    @GET("ListRegoinsByCountryId")
    fun getRegion(
        @Query("id") Id: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralResponse>


    @GET("ListNeighborhoodByRegionId")
    fun getCity(
        @Query("id") Id: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralResponse>


    @FormUrlEncoded
    @POST("loginWebsite")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("lang") language: String,
    ): Call<LoginResp>


    @GET("ListSimilarProducts?currentPage=1&lang=ar")
    fun getsimilar(): Call<BasicResponse>


}