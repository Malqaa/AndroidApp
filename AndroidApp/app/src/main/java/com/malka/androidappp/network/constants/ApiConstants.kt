package com.malka.androidappp.network.constants

object ApiConstants {

    const val HTTP_PROTOCOL = "http"

    const val SERVER_LOCATION2 = "89.40.10.168:82" // TODO REMOVE IT
    const val SERVER_LOCATION = "89.40.10.168:85"
    private const val API_VERSION = "v1"
    private const val IMAGE_FOLDER = "ImageFile"


    const val API_BASE_URL =
        HTTP_PROTOCOL + "://" + SERVER_LOCATION + "/" + API_VERSION + "/";

    const val IMAGE_URL =
        HTTP_PROTOCOL + "://" + SERVER_LOCATION + "/" + IMAGE_FOLDER + "/";

    const val HOME_URL = API_BASE_URL + "CarTemplate/"
    const val HOME_RECENT_URL = HOME_URL + "RecentListing/"
    const val HOME_FEATURED_PROPERTY_URL = HOME_URL + "feactureAdvsInProperty/"
    const val HOME_FEATURED_MOTORS_URL = HOME_URL + "feactureAdvsInMotors/"
    const val HOME_CLOSING_SOON_URL = HOME_URL + "closingSoon/"
    const val HOME_GENERAL_ADS_URL = HOME_URL + "RecentListing/"
    const val HOME_ALL_ADS_URL = HOME_URL + "All/"

    const val CREATE_USER_ENDPOINT = "Accounts/signUp"
    const val LOGIN_ENDPOINT = "Accounts/login"
    const val ADDRESS_ENDPOINT = "Accounts/insertaddress"
    const val ADDBANK_ENDPOINT = "UserBankAccount/AddUserBankAccount"
    const val GET_ADDRESS_ENDPOINT = "Accounts/getaddresses"
    const val GET_BANK__ACCOUNT_DETAIL = "UserBankAccount/UserBankAccountsById"
    const val ACCOUNT_BASE_URL = API_BASE_URL + "Accounts/"
    const val VERIFY_API_ENDPOINT = "Accounts/verify"
    const val BUSSINESS_USER_ENDPOINT = "Accounts/CreateBusinessAccount"
    const val BUSSINESS_REGISTER_FILE_ENDPOINT = "Accounts/UploadBusinessDocuments"
    const val GET_BUSINESS_USER_LIST = "Accounts/GetBusinessListByUserId"
    const val RESEND_OTPCODE_API_ENDPOINT = "Accounts/ResendCode"
    const val UPDATEUSER_SIGNUP_ENDPOINT = "Accounts/updatePersonalInfo"
    const val GET_CATEGORY_LISTING_ENDPOINT = "SearchFilter/generalsearchfilters"
    const val GET_USER = ACCOUNT_BASE_URL
    const val GET_WATCHLIST_LISTING = API_BASE_URL + "Watchlist/"
    const val GET_WATCHLIST_LISTING_ENDPOINT = "Watchlist/getall"
    const val GETUSER_ENDPOINT = "Accounts/GetUser"
    const val FEEDBACK_PARAMETER = "Auction/getall"
    const val GIVE_FEEDBACK_ENDPOINT = "Auction/InsertFeedback"
    const val FAVOURITE_ENDPOINT = "Favourite/"
    const val FAVOURITE_PARAMETER = "all"
    const val USERFAVOURITE_ENDPOINT = API_BASE_URL + FAVOURITE_ENDPOINT

    const val ADVBYID_ENDPOINT = "CarTemplate/Details"

    const val FORGOTPASS_EMAIL_ENDPOINT = "Accounts/ForgetEmailRequest"

    const val CHANGEPASS_ENDPOINT = "Accounts/ChangePassword"

    const val INSERT_AD_WATCHLIST_ENDPOINT = "Watchlist/insert"

    const val DELETE_AD_WATCHLIST_ENDPOINT = "Watchlist/delete"

    const val INSERT_FAVOURITE_SELLER_ENDPOINT = "Favourite/insertseller"
    const val INSERT_FAVOURITE_CATEGORY_ENDPOINT = "Favourite/insertcategory"
    const val INSERT_FAVOURITE_SEARCH_ENDPOINT = "Favourite/insertsearch"

    const val REMOVE_FAVOURITE_SELLER_ENDPOINT = "Favourite/RemoveFavoriteSeller"
    const val REMOVE_FAVOURITE_CATEGORY_ENDPOINT = "Favourite/RemoveFavoriteCategory"
    const val REMOVE_FAVOURITE_SEARCH_ENDPOINT = "Favourite/RemoveFavoriteSearch"

    const val INSERT_FAVOURTIE_SELLER_URL =
        USERFAVOURITE_ENDPOINT + INSERT_FAVOURITE_SELLER_ENDPOINT
    const val INSERT_FAVOURTIE_CATEGORY_URL =
        USERFAVOURITE_ENDPOINT + INSERT_FAVOURITE_CATEGORY_ENDPOINT
    const val INSERT_FAVOURTIE_SEARCH_URL =
        USERFAVOURITE_ENDPOINT + INSERT_FAVOURITE_SEARCH_ENDPOINT

    const val REMOVE_FAVOURTIE_SELLER_URL =
        USERFAVOURITE_ENDPOINT + REMOVE_FAVOURITE_SELLER_ENDPOINT
    const val REMOVE_FAVOURTIE_CATEGORY_URL =
        USERFAVOURITE_ENDPOINT + REMOVE_FAVOURITE_CATEGORY_ENDPOINT
    const val REMOVE_FAVOURTIE_SEARCH_URL =
        USERFAVOURITE_ENDPOINT + REMOVE_FAVOURITE_SEARCH_ENDPOINT

    const val ASK_QUES_AD_ENDPOINT = "AdvQueAndAns/create"

    const val GET_QUES_ANS_COMMENT_ENDPOINT = "CarTemplate/GetQuestionByAd"

    const val HOME_TOTAL_VISIT_COUNT = GET_WATCHLIST_LISTING + "totalVisitCount/"

    const val POST_ANS_ONADD_QUESID_ENDPOINT = "AdvQueAndAns/answer"


    const val GET_AD_DETAIL_BIDING_PRICE_ENDPOINT = "Bid/maxbid"
    const val POST_MAX_BIDING_PRICE = "Bid/placebid"

    const val GET_UNSOLDITEMS_ENDPOINT = "Accounts/soldunsolditems"

    const val GET_SOLDITEMS_ENDPOINT = "Accounts/soldunsolditems"

    const val POST_BUSINESS_USER_REGISTRATION_ENDPOINT = "BusinessUser/Insertbusinessuser"

    const val GET_WONLOST_ENDPOINT = "Bid/UserWonNLostList"

    const val HOME_TOTAL_ONLINEVISITS_ENDPOINT = "GetAllOnlineUsers"

    const val GET_SELLER_ENDPOINT = "Accounts/GetUser"

    const val POST_USER_IMAGE_ENDPOINT = "Accounts/userimageupload"

    const val HOME_TOTAL_NUMBERS_OF_MEMBERS_ENDPOINT = "Accounts/usercount"

    const val GET_BUYNOW_SHIPPINGADDRESS_ENDPOINT = "Accounts/getaddresses"
    const val INSERT_BUYNOW_SHIPPINGADDRESS_ENDPOINT = "Accounts/insertaddress"



    const val SEARCH_CATEGORY_LISTING_ENDPOINT = "CarTemplate/Search"

    const val GET_CATEGORY_TAGS_ENDPOINT = "Category/Tags"
    const val GET_ALL_CATEGORIES = "Category/GetAllCategoryByCulture"
    const val GET_ALL_CATEGORIES_BY_ID = "Category/getAllCategoryByTemplateName"


    const val CREATE_BUSINESS_PRODUCT_ENDPOINT = "BussinessProduct/createproduct"
    const val PRODUCTBYID_ENDPOINT = "BussinessProduct/detailsofproduct"
    const val ALL_PRODUCTS_ENDPOINT = "BussinessProduct/getall"
    const val EDIT_PRODUCTS_ENDPOINT = "BussinessProduct/editproduct"


    const val GET_USER_CREDIT_CARD_ENDPOINT = "CardDetail/getbyuserid"

    const val INSERT_CREDIT_CARD_ENDPOINT = "CardDetail/insertcard"

    const val DELETE_CREDIT_CARD_ENDPOINT = "CardDetail/deletecard"

    const val ADD_TO_CART_DELETE_ENDPOINT = "AddToCart/delete"
    const val ADD_TO_CART_INSERT_ENDPOINT = "AddToCart/create"
    const val getOrderHistory = "AddToCart/getall"
    const val ADD_TO_CART_USER_LIST_ENDPOINT = "AddToCart/getbyloginuserid"

    const val CHECKOUT_INSERT_ENDPOINT = "Checkout/checkoutiteminsert"
}