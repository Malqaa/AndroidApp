package com.malqaa.androidappp.newPhase.data.network.service

import com.google.gson.JsonObject
import com.malqaa.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ModelShipAddresses
import com.malqaa.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressesData
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.domain.models.GeneralResponses
import com.malqaa.androidappp.newPhase.domain.models.NotificationResp
import com.malqaa.androidappp.newPhase.domain.models.NotificationUnReadResp
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountBankListResp
import com.malqaa.androidappp.newPhase.domain.models.accountProfile.AccountInfo
import com.malqaa.androidappp.newPhase.domain.models.addBidResp.AddBidResp
import com.malqaa.androidappp.newPhase.domain.models.addOrderResp.AddOrderResp
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductToCartResp
import com.malqaa.androidappp.newPhase.domain.models.addRateResp.AddRateResp
import com.malqaa.androidappp.newPhase.domain.models.addWaletTransactionResp.AddWalletTranactionResp
import com.malqaa.androidappp.newPhase.domain.models.bidPersonsResp.BidPersonsResp
import com.malqaa.androidappp.newPhase.domain.models.bussinessAccountsListResp.BusinessAccountsListResp
import com.malqaa.androidappp.newPhase.domain.models.bussinessAccountsListResp.ChangeBusinessAccountResp
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.CartListResp
import com.malqaa.androidappp.newPhase.domain.models.cartPriceSummery.CartPriceSummeryResp
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.AddFollowObj
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.CategoryFollowResp
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.FavoriteSeller
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.SavedSearch
import com.malqaa.androidappp.newPhase.domain.models.categoryResp.CategoriesResp
import com.malqaa.androidappp.newPhase.domain.models.configrationResp.ConfigurationResp
import com.malqaa.androidappp.newPhase.domain.models.contauctUsMessage.ContactUsMessageResp
import com.malqaa.androidappp.newPhase.domain.models.contauctUsMessage.TechnicalSupportMessageListResp
import com.malqaa.androidappp.newPhase.domain.models.countryResp.CountriesResp
import com.malqaa.androidappp.newPhase.domain.models.discopuntResp.DiscountCouponResp
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malqaa.androidappp.newPhase.domain.models.editProfileResp.EditProfileResp
import com.malqaa.androidappp.newPhase.domain.models.homeCategoryProductResp.HomeCategoryProductResp
import com.malqaa.androidappp.newPhase.domain.models.homeSilderResp.HomeSliderResp
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginResp
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LogoutResp
import com.malqaa.androidappp.newPhase.domain.models.negotiationOfferResp.NegotiationOfferResp
import com.malqaa.androidappp.newPhase.domain.models.orderDetails.OrderDetailsResp
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderDetailsByMasterIDResp
import com.malqaa.androidappp.newPhase.domain.models.orderListResp.OrderListResp
import com.malqaa.androidappp.newPhase.domain.models.orderRateResp.BuyerRateResp
import com.malqaa.androidappp.newPhase.domain.models.orderRateResp.RateObject
import com.malqaa.androidappp.newPhase.domain.models.orderRateResp.ShipmentRateResp
import com.malqaa.androidappp.newPhase.domain.models.pakatResp.PakatResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductListSearchResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.RequestBidOffers
import com.malqaa.androidappp.newPhase.domain.models.productTags.CategoryTagsResp
import com.malqaa.androidappp.newPhase.domain.models.questionResp.AddQuestionResp
import com.malqaa.androidappp.newPhase.domain.models.questionsResp.QuestionsResp
import com.malqaa.androidappp.newPhase.domain.models.ratingResp.CurrentUserRateResp
import com.malqaa.androidappp.newPhase.domain.models.ratingResp.RateProductResponse
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.RegionsResp
import com.malqaa.androidappp.newPhase.domain.models.resgisterResp.RegisterResp
import com.malqaa.androidappp.newPhase.domain.models.sellerInfoResp.SellerInfoResp
import com.malqaa.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateListResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.*
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.addtocart.AddToCartResponseModel
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.creditcard.CreditCardModel
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.creditcard.CreditCardResponse
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.favourites.FavouriteObject
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.questionModel.ModelAskQues
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.questionModel.ModelPostAns
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.questionModel.ModelQuesAnswr
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.total_members.ModelGetTotalMembers
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.total_online_users.ModelGetTotalOnlineUsers
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.user.UserObject
import com.malqaa.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionResp
import com.malqaa.androidappp.newPhase.domain.models.userAddressesResp.UserAddressesResp
import com.malqaa.androidappp.newPhase.domain.models.userPointsDataResp.ConvertMoneyToPointResp
import com.malqaa.androidappp.newPhase.domain.models.userPointsDataResp.UserPointDataResp
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.UserVerifiedResp
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.ValidateAndGenerateOTPResp
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletDetailsResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface MalqaApiService {
    @Multipart
    @POST("RegisterWebsite")
    fun createUser2(
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

    // @GET("ResendOtp")
    @GET("SendOtp")
    fun resendOtp(
        @Query("phoneNumber") userPhone: String,
        @Query("otpType") otpType: String,
        @Query("User-Language") lang: String,
    ): Call<ValidateAndGenerateOTPResp>

    @POST("VerifyOtp")
    fun verifyOtp(
        @Query("phoneNumber") userPhone: String,
        @Query("otpCode") otpCode: String,
    ): Call<UserVerifiedResp>

    @POST("ListNotifications")
    fun getListNotifications(
        @Query("pageIndex") pageIndex: Int,
        @Query("PageRowsCount") pageRowsCount: Int,
    ): Call<NotificationResp>


    @POST("UnreadNotificationsCount")
    fun unreadNotificationsCount(
        @Query("pageIndex") pageIndex: Int,
        @Query("PageRowsCount") pageRowsCount: Int,
    ): Call<NotificationUnReadResp>


    @GET("ListAllCategory?isShowInHome=true")
    fun getAllCategories(): Call<GeneralResponse>

    @GET("ListHomeCategoryProduct?currentPage=1&productNumber=10")
    fun listHomeCategoryProduct(
        @Query("lang")
        language: String = ConstantObjects.currentLanguage
    ): Call<HomeCategoryProductResp>

    @GET("ListAdvertisments")
    fun getHomeSlidersImages(
        @Query("type") type: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<HomeSliderResp>

    @GET("ListCountryDDL")
    fun getCountryNew(): Call<CountriesResp>

    @GET("ListRegionsByCountryIdDDL")
    fun getRegionNew(
        @Query("countriesIds") Id: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<RegionsResp>


    @GET("ListNeighborhoodByRegionIdDDL")
    fun getNeighborhoodByRegionNew(
        @Query("regionsIds") Id: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<RegionsResp>


    @GET("GetProductById")
    fun getProductDetailById2(
        @Query("id") id: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ProductResp>


    @POST("BuyNow")
    fun getBuyNow(
        @Query("productId") productId: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralRespone>


    @GET("GetSellerInformation")
    fun getSellerInformation(
        @Query("productId") productId: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<SellerInfoResp>

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

    @GET("ListQuestions?pageIndex=1&PageRowsCount=30")
    fun getQuestionList(
        @Query("productId") productId: Int
    ): Call<QuestionsResp>

    @POST("AddFavoriteProduct")
    fun addProductToFav(
        @Query("productId") productId: Int
    ): Call<GeneralResponse>

    @GET("ListFavoriteProduct")
    fun getUserWatchlist(@Query("lang") language: String = ConstantObjects.currentLanguage): Call<ProductListResp>


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


    @GET("GetAllPakatsList")
    fun getAllPakatList(
        @Query("categoryId") id: String,
        @Query("PakatType") PakatType: Int = 1,
        @Query("isAdmin") isAdmin: Boolean = false,
        @Query("lang") language: String = ConstantObjects.currentLanguage,
    ): Call<PakatResp>

    @Multipart
    @POST("AddProduct")
    suspend fun uploadData(
        @Part("nameAr") nameAr: RequestBody,
        @Part("nameEn") nameEn: RequestBody,
        @Part("subTitleAr") subTitleAr: RequestBody,
        @Part("subTitleEn") subTitleEn: RequestBody,
        @Part("descriptionAr") descriptionAr: RequestBody,
        @Part("descriptionEn") descriptionEn: RequestBody,
        @Part("qty") qty: RequestBody,
        @Part("status") status: RequestBody,
        @Part("categoryId") categoryId: RequestBody,
        @Part("countryId") countryId: RequestBody,
        @Part("regionId") regionId: RequestBody,
        @Part("neighborhoodId") neighborhoodId: RequestBody,
        @Part("District") district: RequestBody,
        @Part("Street") street: RequestBody,
        @Part("GovernmentCode") governmentCode: RequestBody,
        @Part("productSep") productSep: RequestBody,
        @Part listImageFile: List<MultipartBody.Part>,
        @Part("MainImageIndex") mainImageIndex: RequestBody,
//        @Part("videoUrl") videoUrl: List<RequestBody>,
        @Part videoUrlList: ArrayList<MultipartBody.Part>,

        @Part shippingOptions:  ArrayList<MultipartBody.Part>,
        @Part("Lat") lat: RequestBody,
        @Part("Lon") lon: RequestBody,
        @Part("AcceptQuestion") acceptQuestion: RequestBody,
        @Part("IsFixedPriceEnabled") isFixedPriceEnabled: RequestBody,
        @Part("IsAuctionEnabled") isAuctionEnabled: RequestBody,
        @Part("IsNegotiationEnabled") isNegotiationEnabled: RequestBody,
        @Part("Price") price: RequestBody,
        @Part("PriceDisc") priceDisc: RequestBody,
        @Part  paymentOptions: ArrayList<MultipartBody.Part>,
        @Part productBankAccounts: ArrayList<MultipartBody.Part>,
        @Part("IsCashEnabled") isCashEnabled: RequestBody,
        @Part("AuctionStartPrice") auctionStartPrice: RequestBody,
        @Part("DisccountEndDate") discountEndDate: RequestBody,
        @Part("SendOfferForAuction") sendOfferForAuction: RequestBody,
        @Part("AuctionMinimumPrice") auctionMinimumPrice: RequestBody,
        @Part("AuctionNegotiateForWhom") auctionNegotiateForWhom: RequestBody,
        @Part("AuctionNegotiatePrice") auctionNegotiatePrice: RequestBody,
        @Part("AuctionClosingTime") auctionClosingTime: RequestBody,
        @Part("SendYourAccountInfoToAuctionWinner") sendYourAccountInfoToAuctionWinner: RequestBody,
        @Part("AlmostSoldOutQuantity") almostSoldOutQuantity: RequestBody,
        @Part("ProductPaymentDetailsDto.PakatId") pakatId: RequestBody,
        @Part("ProductPaymentDetailsDto.AdditionalPakatId") additionalPakatId: RequestBody,
        @Part("ProductPaymentDetailsDto.ProductPublishPrice") productPublishPrice: RequestBody,
        @Part("ProductPaymentDetailsDto.EnableFixedPriceSaleFee") enableFixedPriceSaleFee: RequestBody,
        @Part("ProductPaymentDetailsDto.EnableAuctionFee") enableAuctionFee: RequestBody,
        @Part("ProductPaymentDetailsDto.EnableNegotiationFee") enableNegotiationFee: RequestBody,
        @Part("ProductPaymentDetailsDto.ExtraProductImageFee") extraProductImageFee: RequestBody,
        @Part("ProductPaymentDetailsDto.ExtraProductVidoeFee") extraProductVideoFee: RequestBody,
        @Part("ProductPaymentDetailsDto.SubTitleFee") subTitleFee: RequestBody,
        @Part("ProductPaymentDetailsDto.FixedPriceSaleFee") fixedPriceSaleFee: RequestBody,
        @Part("ProductPaymentDetailsDto.AuctionFee") auctionFee: RequestBody,
        @Part("ProductPaymentDetailsDto.NegotiationFee") negotiationFee: RequestBody,
        @Part("ProductPaymentDetailsDto.CategoryId") productPaymentDetailsCategoryId: RequestBody,
        @Part("ProductPaymentDetailsDto.CouponId") productPaymentDetailsCouponId: RequestBody,
        @Part("ProductPaymentDetailsDto.CouponDiscountValue") productPaymentDetailsCouponDiscountValue: RequestBody,
        @Part("ProductPaymentDetailsDto.TotalAmountBeforeCoupon") productPaymentDetailsTotalAmountBeforeCoupon: RequestBody,
        @Part("ProductPaymentDetailsDto.TotalAmountAfterCoupon") productPaymentDetailsTotalAmountAfterCoupon: RequestBody,
        @Part("ProductPaymentDetailsDto.PaymentType") productPaymentDetailsPaymentType: RequestBody,
        @Header("Content-Type") contentType:String,

    ): Call<AddProductResponse>
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
    @POST("AddProduct")
    @Headers("Content-Type:multipart/form-data; boundary=----WebKitFormBoundaryyEmKNDsBKjB7QEqu")
    fun addProduct3(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part part: List<MultipartBody.Part>?,
        @Part shippingOptionsList: ArrayList<MultipartBody.Part>,
        @Part videoUrlList: ArrayList<MultipartBody.Part>,
        @Part sendPaymentOptionList: ArrayList<MultipartBody.Part>,
        @Part productBankAccounts: ArrayList<MultipartBody.Part>,
    ): Call<AddProductResponse>

    @Multipart
    @POST("AddProduct")
    @Headers("Content-Type:multipart/form-data; boundary=----WebKitFormBoundaryyEmKNDsBKjB7QEqu")
    fun addProductTest(
        @Part data: MultipartBody.Part,
        @Part part: List<MultipartBody.Part>?,
        @Part shippingOptionsList: List<MultipartBody.Part>,
        @Part videoUrlList: List<MultipartBody.Part>,
        @Part sendPaymentOptionList: List<MultipartBody.Part>,
        @Part productBankAccounts: List<MultipartBody.Part>,
    ): Call<AddProductResponse>

    @Multipart
    @POST("EditProduct")
    fun editProduct(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part part: List<MultipartBody.Part>?,
        @Part shippingOptionsList: ArrayList<MultipartBody.Part>,
        @Part deletedMediasList: List<MultipartBody.Part>,
        @Part videoUrlList: ArrayList<MultipartBody.Part>,
        @Part sendPaymentOptionList: ArrayList<MultipartBody.Part>,
        @Part productBankAccounts: ArrayList<MultipartBody.Part>,
    ): Call<AddProductResponse>

    @Multipart
    @POST("AddRateProduct")
    fun addRateProduct(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<AddRateResp>


    @POST("AddEditBuyerRate")
    fun addRateBuyer(
        @Body partMap: HashMap<String, Any>,
    ): Call<GeneralRespone>

    @GET("BuyerRateForEdit")
    fun getRateBuyer(
        @Query("orderId") orderId: Int
    ): Call<BuyerRateResp>


    @GET("ListProductByBusinessAccountId")
    fun getMyProduct(
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ProductListResp>

    @GET("ListDidntSellProducts")
    fun getListDidntSellProducts(
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ProductListResp>

    @GET
    fun searchForProductInCategory(
        @Url url: String
    ): Call<ProductListSearchResp>

    @GET("ListRateProduct?pageIndex=1&PageRowsCount=1000")
    fun getRates(
        @Query("productId") productID: Int,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<RateProductResponse>

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

    @GET("GetCartTotalPrice")
    fun getCartTotalPrice(
        @Query("cartMasterId") cartMasterId: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralResponse>


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


    @GET("GetAllShippingOptions")
    fun getAllShippingOptions(
//        @Query("providerId") providerId: String,
//        @Query("BusinessAccountId") BusinessAccountId: String?,
//        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ShippingOptionResp>

    @GET("ListRateSeller?PageRowsCount=10")
    fun getSellerRates(
        @Query("pageIndex") page: Int,
        @Query("rate") rate: Int?,
        @Query("providerId") providerId: String = ConstantObjects.logged_userid,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<SellerRateListResp>


    @GET("ListRateBuyer?PageRowsCount=10")
    fun getBuyerRates(
        @Query("pageIndex") page: Int,
        @Query("rate") rate: Int?,
        @Query("buyerId") providerId: String = ConstantObjects.logged_userid,
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

    @GET("GetClientAddedOrders?PageRowsCount=10&OrderStatus!=6")
    fun getCurrentOrders(
        @Query("pageIndex") page: Int,
        @Query("userId") userId: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<OrderListResp>

    @GET("GetClientAddedOrders?PageRowsCount=10&OrderStatus=6")
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
    @POST("ConfirmBankTransferPayment")
    fun confirmBankTransferPayment(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part part: MultipartBody.Part?
    ): Call<GeneralResponse>

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


    @GET("ListCartProductsForClient?currentPage=1&maxRows=10")
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


    @DELETE("RemoveProduct")
    fun removeProduct(@Query("id") productId: Int): Call<GeneralResponse>

    @POST("RepostProduct")
    fun repostProduct(@Query("productId") productId: Int): Call<GeneralResponse>


    @Multipart
    @POST("AddOrder")
    fun addOrder(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part productOrderPaymentDetailsDto: List<MultipartBody.Part>?

    ): Call<AddOrderResp>

    @POST("addPaymentTransaction")
    fun addPaymentTransaction(
        @Body partMap: HashMap<String, Any>,
    ): Call<GeneralResponse>

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
        @Query("couponForProviderId") providerId: String,
        @Query("buyWithFixedRpriceOrNegotiation") buyWithFixedRpriceOrNegotiation: String,
        @Query("couponForbusinessAccountId") couponForbusinessAccountId: String?
    ): Call<GeneralResponse>

    @POST("UnApplyCouponOnCart")
    fun unApplyCouponOnCart(
        @Query("cartMasterId") cartMasterId: String,
        @Query("couponCode") couponCode: String,
        @Query("couponForProviderId") providerId: String,
        @Query("buyWithFixedRpriceOrNegotiation") buyWithFixedRpriceOrNegotiation: String,
        @Query("couponForbusinessAccountId") couponForbusinessAccountId: String?
    ): Call<GeneralResponse>

    @GET("ListSellerProducts?PageRowsCount=10")
    fun getListSellerProducts(
        @Query("pageIndex") pageIndex: Int,
        @Query("sellerId") sellerProviderId: String,
        @Query("sellerBusinssAccountId") sellerBusinssAccountId: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<ProductListResp>

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

    @DELETE("RemoveShippmentProductsFromCart")
    fun removeShipmentProductsFromCart(
        @Query("businessAccountId") businessAccountId: String?,
        @Query("cartMasterId") cartMasterId: String,
        @Query("providerId") providerId: String,
    ): Call<GeneralResponse>

    @GET("GetUserWalletTransactions")
    fun getWalletDetails(
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<WalletDetailsResp>


    @GET("GetMyAccountMainPageData")
    fun getMyAccountInfo(
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<AccountInfo>


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

    @GET("ChangeAccount")
    fun changeBusinessAccount(@Query("businessAccountId") businessAccountId: Int): Call<ChangeBusinessAccountResp>

    @Multipart
    @POST("AddEditBusinessAccount")
    fun addEditBusinessAccount(
        @Part("id") id: RequestBody,
        @Part("BusinessAccountUserName") businessAccountUserName: RequestBody,
        @Part("ProviderId") providerId: RequestBody,
        @Part("BusinessAccountNameAr") businessAccountNameAr: RequestBody,
        @Part("BusinessAccountNameEn") BusinessAccountNameEn: RequestBody,
        @Part("BusinessAccountEmail") BusinessAccountEmail: RequestBody,
        @Part("BusinessAccountPhoneNumber") BusinessAccountPhoneNumber: RequestBody,
        @Part BusinessAccountImage: MultipartBody.Part?,
        @Part("BusinessAccountWebsite") BusinessAccountWebsite: RequestBody,
        @Part("BusinessAccountFaceBook") BusinessAccountFaceBook: RequestBody,
        @Part("BusinessAccountInstagram") BusinessAccountInstagram: RequestBody,
        @Part("BusinessAccountTwitter") BusinessAccountTwitter: RequestBody,
        @Part("BusinessAccountYouTube") BusinessAccountYouTube: RequestBody,
        @Part("BusinessAccountLinkedIn") BusinessAccountLinkedIn: RequestBody,
//        @Part("BusinessAccountSnapchat") BusinessAccountSnapchat: RequestBody,
        @Part("BusinessAccountTikTok") BusinessAccountTikTok: RequestBody,
        @Part("RegistrationDocumentType") RegistrationDocumentType: RequestBody,
        @Part("DetailRegistrationNumber") DetailRegistrationNumber: RequestBody,
        @Part("RegistrationNumberExpiryDate") RegistrationNumberExpiryDate: RequestBody,
        @Part("VatNumber") VatNumber: RequestBody,
        @Part("Maroof") Maroof: RequestBody,
        @Part BusinessAccountCertificates: List<MultipartBody.Part>?,
        @Part("CountryId") CountryId: RequestBody,
        @Part("RegionId") RegionId: RequestBody,
        @Part("NeighborhoodId") NeighborhoodId: RequestBody,
        @Part("districtName") District: RequestBody,
        @Part("streetNumber") Street: RequestBody,
        @Part("ZipCode") ZipCode: RequestBody,
        @Part("Trade15Years") Trade15Years: RequestBody,
        @Part("Lat") Lat: RequestBody,
        @Part("Lon") Lon: RequestBody,
        @Part("IsDeleted") IsDeleted: RequestBody,
        @Part("IsActive") IsActive: RequestBody,

        ): Call<GeneralResponse>


    @Multipart
    @POST("EditProfileImage")
    fun editProfileImage(@Part part: MultipartBody.Part?): Call<GeneralResponse>

    @GET("GetUserData")
    fun getUserData(): Call<LoginResp>

    @POST("EditProfileChangePassword")
    fun editProfileChangePassword(@Body data: HashMap<String, Any>): Call<GeneralResponse>

    @POST("EditProfileChangeEmail")
    fun editProfileChangeEmail(@Query("email") email: String): Call<GeneralResponse>

    @POST("ConfirmChangeEmail")
    fun confirmChangeEmail(@Body data: HashMap<String, Any>): Call<GeneralResponse>

    @POST("UpdateUserMobileNumber")
    fun updateUserMobileNumber(@Body data: HashMap<String, Any>): Call<GeneralResponse>

    @POST("UpdateAccountProfile")
    fun updateAccountProfile(@Body data: HashMap<String, Any>): Call<EditProfileResp>


    @Multipart
    @POST("AddBankTransfer")
    fun addBankAccount(
        @Part("accountNumber") accountNumber: RequestBody,
        @Part("bankName") bankName: RequestBody,
        @Part("bankHolderName") bankHolderName: RequestBody,
        @Part("ibanNumber") ibanNumber: RequestBody,
        @Part("swiftCode") swiftCode: RequestBody,
        @Part("expiaryDate") expiaryDate: RequestBody,
        @Part("SaveForLaterUse") SaveForLaterUse: RequestBody,
    ): Call<GeneralResponse>

    @GET("BankTransfersList?pageIndex=1&PageRowsCount=100")
    fun getAllBacksAccount(): Call<AccountBankListResp>

    @POST("AddProductClientOffer")
    fun addProductClientOffer(
        @Query("productId") productId: Int,
        @Query("quantity") quantity: Int,
        @Query("price") price: Float
    ): Call<GeneralResponse>


    @POST("PurchaseProductByOffer")
    fun purchaseProductByOffer(
        @Query("offerId") offerId: Int,
    ): Call<GeneralResponse>

    @POST("GetPurchaseProductsOffers")
    fun getPurchaseProductsOffers(@Query("isSent") isSent: Boolean): Call<NegotiationOfferResp>

    @POST("GetSaleProductsOffers")
    fun getSaleProductsOffers(@Query("isSent") isSent: Boolean): Call<NegotiationOfferResp>

    @POST("CancelProductOfferByClient")
    fun cancelProductOfferByClient(@Query("offerId") offerId: Int): Call<GeneralResponse>


    @POST("CancelProductOfferByProvider")
    fun cancelProductOfferByProvider(@Query("offerId") offerId: Int): Call<GeneralResponse>

    @POST("AcceptRejectOffer")
    fun acceptRejectOffer(
        @Query("lang") language: String = ConstantObjects.currentLanguage,
        @Query("offerId") offerId: Int,
        @Query("productId") productId: Int,
        @Query("acceptOffer") acceptOffer: Boolean,
        @Query("refuseReason") refuseReason: String,
        @Query("OfferExpireHours") OfferExpireHours: Float
    ): Call<GeneralResponse>

    @POST("addFavoriteSeller")
    fun addFavoriteSeller(
        @Query("sellerId") sellerId: String?,
        @Query("sellerBusinessAccountId") sellerBusinessAccountId: String?
    ): Call<GeneralResponse>

    @GET("GetCouponByCode")
    fun getCouponByCode(
        @Query("couponScreen") couponScreen: String,
        @Query("couponCode") couponCode: String,
    ): Call<DiscountCouponResp>

    @POST("RemoveFavoriteSeller")
    fun removeFavoriteSeller(
        @Query("sellerId") sellerId: String?,
        @Query("sellerBusinessAccountId") sellerBusinessAccountId: String?
    ): Call<GeneralResponse>

    @POST("Accounts/CreateBusinessAccount")
    fun addBusinesUser(@Body info: BusinessUserRespone.BusinessUser): Call<GeneralRespone>

    @POST("Accounts/UploadBusinessDocuments")
    fun addBusinessRegisterFile(@Body info: getBusinessRegisterFile.GetDocuments): Call<GeneralRespone>


    @GET("Accounts/getaddresses")
    fun getAddress(@Query("loginId") loginId: String): Call<GetAddressResponse>

    @GET("Accounts/GetUser")
    fun getuser(@Query("id") userid: String): Call<UserObject>

    @GET("all")
    fun getuserfavourites(@Query("loggedIn") userid: String): Call<FavouriteObject>


    @POST("Accounts/ChangePassword")
    fun changepass(@Body changepasspost: User): Call<User>

    @POST("AddFavoriteProduct")
    fun insertAddToUserWatchlist(@Query("productId") productId: Int): Call<AddFavResponse>

    @POST("Watchlist/delete")
    fun deleteAdFromUserWatchlist(
        @Query("userid") userid: String,
        @Query("adid") adsId: String
    ): Call<BasicResponse>

    @POST("?")
    fun deleteFromUserFavoriteList(
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


    @POST("AdvQueAndAns/answer")
    fun postAnsByQid(
        @Query("qId") ansqid: String,
        @Query("answer") ansans: String,
        @Query("Loggedin") ansLoginId: String
    ): Call<ModelPostAns>

    @GET("Accounts/soldunsolditems")
    fun getsolditemsbyId(@Query("userid") userid: String): Call<ModelSoldUnsold>


    @GET("GetAllOnlineUsers")
    fun getTotalOnlineUsers(): Call<ModelGetTotalOnlineUsers>

    @GET("Accounts/usercount")
    fun getTotalMembers(): Call<ModelGetTotalMembers>

    @GET(Constants.GET_BUYNOW_SHIPPINGADDRESS_ENDPOINT)
    fun getshipaddress(@Query("loginId") getaddressbyLoginId: String): Call<ModelShipAddresses>

    @POST(Constants.INSERT_BUYNOW_SHIPPINGADDRESS_ENDPOINT)
    fun addNewShippingAddress(@Body shippingaddress: ShippingAddressesData): Call<BasicResponse>


    //  GeneralResponses
    @GET("CardDetail/getbyuserid")
    fun getUserCreditCards(@Query("usid") userid: String): Call<CreditCardResponse>

    @POST("CardDetail/insertcard")
    fun insertUserCreditCard(@Body insertads: CreditCardModel): Call<BasicResponse>

    @POST("CardDetail/updatecard")
    fun updateUserCreditCard(@Body insertads: CreditCardModel): Call<BasicResponse>

    @DELETE("CardDetail/deletecard")
    fun deleteUserCreditCard(@Query("id") userid: String): Call<BasicResponse>

    @GET("AddToCart/getbyloginuserid")
    fun getUsersCartList(@Query("loggedinUserId") userid: String): Call<AddToCartResponseModel>

    @DELETE("AddToCart/delete")
    fun deleteFromUserCart(@Query("id") userid: String): Call<BasicResponse>

    @GET
    fun jsonTemplates(@Url url: String): Call<JsonObject>

    @POST("AddFollow")
    fun addFollow(@Body addFollow: AddFollowObj): Call<GeneralResponse>


    @DELETE("RemoveFollow")
    fun removeFollow(@Query("catId") catId: Int): Call<GeneralResponse>

    @GET("ListCategoryFollow")
    fun getListCategoryFollow(@Query("lang") language: String = ConstantObjects.currentLanguage): Call<CategoryFollowResp>


    @GET("Serach?pageIndex=1&&PageRowsCount=10")
    fun serach(
        @QueryMap filter: Map<String, String>,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralResponse>

    @POST("SaveSearch")
    fun savedSearch(
        @Query("searchString") searchString: String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<GeneralResponse>

    @GET("GetSubCategoryByMainCategory?currentPage=1")
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
        @Field("deviceId")deviceID:String,
        @Field("deviceType")deviceType:String,
    ): Call<LoginResp>

    @Multipart
    @POST("loginWebsite")
    fun loginUser(

        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
//        @Part("email") email: MultipartBody.Part,
//        @Part("password") password: MultipartBody.Part,
//        @Part("lang") language: MultipartBody.Part,
//        @Part("deviceId") deviceID: MultipartBody.Part,
//        @Part("deviceType") deviceType: MultipartBody.Part
    ): Call<LoginResp>

    @POST("LogoutWebsite")
    fun logout(
        @Query("deviceId")deviceID:String,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<LogoutResp>

    @POST("ChangeLanguage")
    fun changeLanguage(
        @Query("language")language:String,
    ): Call<AddFavResponse>

    @POST("AddBid")
    fun addBid(@Body body: HashMap<String, Any>): Call<AddBidResp>

    @GET("CheckOutAdditionalPakat")
    fun checkOutAdditionalPakat(
        @Query("pakatId") pakatId: Int,
        @Query("categoryId") categoryId: Int,
        @Query("extraProductImageFee") extraProductImageFee: Float,
        @Query("extraProductVidoeFee") extraProductVidoeFee: Float,
        @Query("subTitleFee") subTitleFee: Float,
        @Query("lang") language: String = ConstantObjects.currentLanguage
    ): Call<CartPriceSummeryResp>

    @GET("GetProductShippingOptions")
    fun getProductShippingOptions(@Query("productId") productId: Int): Call<ShippingOptionResp>

    @GET("GetProductBankAccounts")
    fun getProductBankAccounts(@Query("productId") productId: Int): Call<AccountBankListResp>

    @GET("GetProductPaymentOptions")
    fun getProductPaymentOptions(@Query("productId") productId: Int): Call<ShippingOptionResp>

    @GET("GetBids")
    fun getBidsPersons(@Query("productId") productId: Int): Call<BidPersonsResp>

    @POST("AddProductBidOffers")
    fun addProductBidOffers(
        @Body requestBidOffers: RequestBidOffers,
    ): Call<GeneralResponse>

    @GET("MyBids")
    fun getMyBids(): Call<ProductListResp>

//    @GET("${Constants.GET_CATEGORY_TAGS_ENDPOINT}?")
//    fun getCategoryTags(
//        @Query("name") name: String
//    ): Call<CategoryTagsResp>

//    @POST("Accounts/ForgetEmailRequest")
//    fun forgotpassemail(@Body forgottpass: User): Call<GeneralRespone>

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


//    @POST("Accounts/verify")
//    fun verifycode(@Body verifyusercode: PostReqVerifyCode): Call<BasicResponse>

    //    @GET("GetSliderImages")
//    fun getHomeSlidersImages(
//        @Query("lang")
//        language: String = ConstantObjects.currentLanguage
//    ): Call<HomeSliderResp>
//    @GET("GetProductById")
//    fun getAdDetailById(
//        @Query("id") id: String,
//        @Query("lang") language: String = ConstantObjects.currentLanguage
//    ): Call<GeneralRespone>

//    @GET("ListAllSpecificationAndSubSpecificationByCatId")
//    fun getSpecification(
//        @Query("id") id: String,
//        @Query("lang") language: String = ConstantObjects.currentLanguage
//    ): Call<GeneralResponse>

//    @Multipart
//    @POST("AddProduct")
//    fun addProduct3(
//        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
//        @Part part: List<MultipartBody.Part>?,
//    ): Call<AddProductResponse>

    //    @GET("AdvancedFilter")
//    fun searchForProductInCategory(@QueryName(encoded = true) queryString: HashMap<String,String>):Call<ProductListResp>
//    @GET("AdvancedFilter")
//    fun searchForProductInCategory(@Query("mainCatId")mainCatId:Int,@QueryName queryString: HashMap<String,String>):Call<ProductListResp>


//    @POST("UserBankAccount/AddUserBankAccount")
//    fun addbankaccount(@Body info: BankListRespone.BankDetail): Call<GeneralRespone>
//    @GET("UserBankAccount/UserBankAccountsById")
//    fun getBankDetail(@Query("UserID") loginId: String): Call<BankListRespone>
//
//    @GET("Accounts/GetBusinessListByUserId")
//    fun getBusinessUserList(@Query("UserID") loginId: String): Call<BusinessUserRespone>

    //    @GET("Auction/getall")
//    fun getuserfeedback(@Query("loggedin") userid: String): Call<FeedbackObject>

    //    @GET("Accounts/GetUser")
//    fun getAdSeller(
//        @Query("id") id: String,
//        @Query("loggedin") loggedin: String
//    ): Call<SellerResponseBack>
//
//    @GET("Accounts/GetUser")
//    fun getAdSellerByID(
//        @Query("id") id: String
//    ): Call<ModelSellerDetails>

//    @GET("ListAdvertisments")
//    fun SliderAPI(): Call<GeneralResponse>


//    @GET("BussinessProduct/detailsofproduct")
//    fun getProductDetailById(
//        @Query("id") id: String,
//        @Query("loginUserId") loginUserId: String
//    ): Call<ProductResponseBack>


//    @POST("Accounts/updatePersonalInfo")
//    fun updateUserSiginup(@Body updateusersignupp: User): Call<User>
//
//    @POST("Accounts/ResendCode")
//    fun resendcode(@Body resendotpcode: User): Call<BasicResponse>


//    @POST("CarTemplate/Create")
//    fun createAllAd(@Body data: HashMap<String, String>): Call<CreateAdvResponseBack>
//
//
//    @POST("CarTemplate/update")
//    fun updateCarTemplate(@Body data: HashMap<String, String>): Call<CreateAdvResponseBack>
//
//
//    @POST("Accounts/insertaddress")
//    fun insertAddress(@Body info: GetAddressResponse.AddressModel): Call<GeneralRespone>
//
//    @POST("Accounts/updateAdress")
//    fun updateAddress(@Body info: GetAddressResponse.AddressModel): Call<GeneralRespone>

//    @POST("Accounts/userimageupload")
//    fun userimageupload(
//        @Query("userId") userid: String,
//        @Body imagebase64string: String
//    ): Call<UserImageResponseBack>

//    @POST("?")
//    fun InsertToUserFavouritelist(@Body insertfav: favouriteadd): Call<BasicResponse>

//    @GET("BussinessProduct/getall")
//    fun getAllProducts(): Call<AllProductsResponseBack>
//
//
//    @POST("Favourite/insertseller")
//    fun addSellerFav(@Body addfav: ModelAddSellerFav): Call<ModelAddSellerFav>

//    @POST("Favourite/insertcategory")
//    fun addCatFav(@Body addfav: ModelAddCatFav): Call<ModelAddCatFav>
//
//    @POST("Favourite/insertsearch")
//    fun addSearchFav(@Body addfav: ModelAddSearchFav): Call<ModelAddSearchFav>


//    @GET(Constants.HOME_TOTAL_VISIT_COUNT)
//    fun GetTotalVisitCount(): Call<visit_count_object>
//
//    @GET("Accounts/soldunsolditems")
//    fun getunsolditemsbyId(@Query("userid") userid: String): Call<ModelSoldUnsold>


//    @POST("BusinessUser/Insertbusinessuser")
//    fun busiRegis(@Body busiReg: ModelBusinessRegistration): Call<ModelBusinessRegistration>

//    @GET(Constants.GET_AD_DETAIL_BIDING_PRICE_ENDPOINT)
//    fun getbidgpricebyAdvId(@Query("advId") getbidByadvId: String): Call<ModelBidingResponse>
//
//    @POST(Constants.POST_MAX_BIDING_PRICE)
//    fun postBidPrice(@Body postBidpricee: ModelPostBidPrice): Call<ModelPostBidPrice>

//    @GET("CarTemplate/Search")
//    fun searchcategorylist(@Query("query") category: String): Call<CategoryResponse>

//    @POST("BussinessProduct/createproduct")
//    fun createBusinessProduct(@Body createproduct: ModelCreateProduct): Call<CreateProductResponseBack>

//    @POST("Auction/InsertFeedback")
//    fun giveFeedback(@Body giveFeedback: ModelGiveFeedBack): Call<GiveFeedbackResponseBack>
//
//    @POST("BussinessProduct/editproduct")
//    fun editBusinessProduct(@Body editproduct: ModelEditProduct): Call<EditProductResponseBack>

    @GET("ListFavoriteSeller")
    fun getListFavoriteSeller(@Query("lang") language: String = ConstantObjects.currentLanguage): Call<GeneralResponses<List<FavoriteSeller>>>
//
    @GET("ListSavedSearch")
    fun getListSaveSearch(@Query("lang") language: String = ConstantObjects.currentLanguage): Call<GeneralResponses<List<SavedSearch>>>
    @POST("RemoveSavedSearch")
    fun removeSavedSearch(@Query ("savedSearchId") savedSearchId :Int,@Query("lang") language: String = ConstantObjects.currentLanguage): Call<GeneralResponse>

//    @POST("Checkout/checkoutiteminsert")
//    fun PostUserCheckOut(@Body checkout: CheckoutRequestModel): Call<GeneralRespone>

//    @POST("AddToCart/create")
//    fun AddToUserCart(@Body addtocart: InsertToCartRequestModel): Call<BasicResponse>
//
//    @GET("AddToCart/getall")
//    fun getMyRequest(@Query("loginId") loginId: String): Call<getCartModel>

    //    @GET("GetCategoryById")
//    fun GetCategoryById(
//        @Query("id") id: Int,
//        @Query("lang") language: String = ConstantObjects.currentLanguage
//    ): Call<GeneralResponse>
//
//    @GET("AdvanceFiltter")
//    fun AdvanceFiltter(
//        @QueryMap filter: Map<String, String>,
//        @Query("lang") language: String = ConstantObjects.currentLanguage
//    ): Call<GeneralResponse>

//    @GET("GetSubCategoryByMainCategory")
//    fun GetSubCategoryByMainCategory(
//        @Query("id") id: String,
//        @Query("lang") language: String = ConstantObjects.currentLanguage
//    ): Call<GeneralResponse>

//    @GET("ListSimilarProducts?currentPage=1&lang=ar")
//    fun getsimilar(): Call<BasicResponse>
}