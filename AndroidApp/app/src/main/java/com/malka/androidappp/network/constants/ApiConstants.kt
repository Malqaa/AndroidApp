package com.malka.androidappp.network.constants

object ApiConstants {

    const val HTTP_PROTOCOL = "http";
    const val SERVER_LOCATION = "89.40.10.168:81";
    const val API_FOLDER = "api";
    const val API_VERSION = "v1"
    const val IMAGE_FOLDER = "ImageFile";

//    const val HTTP_PROTOCOL = "http"
//    const val SERVER_LOCATION = "192.168.2.27"
//    const val API_FOLDER = "Com.Techxcape.Mazad.Web.Services";
//    const val API_VERSION = "v1"
//    const val IMAGE_FOLDER = "ImageFile";

    const val api = "http:///Com.Techxcape.Mazad.Web.Services/swagger/index.html"

    const val API_BASE_URL =
        HTTP_PROTOCOL + "://" + SERVER_LOCATION + "/" + API_FOLDER + "/" + API_VERSION + "/";

    const val IMAGE_URL =
        HTTP_PROTOCOL + "://" + SERVER_LOCATION + "/" + API_FOLDER + "/" + IMAGE_FOLDER + "/";

    const val HOME_URL = API_BASE_URL + "CarTemplate/"
    const val HOME_RECENT_URL = HOME_URL + "RecentListing/"
    const val HOME_FEATURED_PROPERTY_URL = HOME_URL + "feactureAdvsInProperty/"
    const val HOME_FEATURED_MOTORS_URL = HOME_URL + "feactureAdvsInMotors/"
    const val HOME_CLOSING_SOON_URL = HOME_URL + "closingSoon/"
    const val HOME_GENERAL_ADS_URL = HOME_URL + "RecentListing/"
    const val HOME_ALL_ADS_URL = HOME_URL + "All/"

    const val CREATE_USER_ENDPOINT = "register"
    const val CREATE_USER_BASEURL = API_BASE_URL + "Accounts/"
    const val ALL_USER_ENDPOINT = "All"
    const val CREATE_ADVERTISEMENT_ENDPOINT = "Create"
    const val LOGIN_ENDPOINT = "login"
    const val ACCOUNT_BASE_URL = API_BASE_URL + "Accounts/"
    const val VERIFY_API_ENDPOINT = "verify"
    const val VERIFY_API_BASEURL = API_BASE_URL + "Accounts/"
    const val RESEND_OTPCODE_API_ENDPOINT = "ResendCode"
    const val RESEND_OTPCODE_API_BASEURL = API_BASE_URL + "Accounts/"
    const val UPDATEUSER_SIGNUP_ENDPOINT = "UpdateUser"
    const val UPDATEUSER_SIGNUP_BASEURL = API_BASE_URL + "Accounts/"
    const val CREATE_GENERAL_ADVERTISEMENT_ENDPOINTT = "Create"
    const val CREATE_GENERAL_ADVERTISEMENT_BASEURL = HOME_URL
    const val GET_CATEGORY_LISTING = API_BASE_URL + "SearchFilter/"
    const val GET_CATEGORY_LISTING_ENDPOINT = "generalsearchfilters"
    const val GET_USER = ACCOUNT_BASE_URL
    const val GET_WATCHLIST_LISTING = API_BASE_URL + "Watchlist/"
    const val GET_WATCHLIST_LISTING_ENDPOINT = "getall"
    const val GETUSER_BASEURL = ACCOUNT_BASE_URL
    const val GETUSER_ENDPOINT = "GetUser"
    const val FEEDBACK_ENDPOINT = "Auction/"
    const val FEEDBACK_PARAMETER = "getall"
    const val USERFEEDBACK_ENDPOINT = API_BASE_URL + FEEDBACK_ENDPOINT
    const val GIVE_FEEDBACK_ENDPOINT = "InsertFeedback"
    const val FAVOURITE_ENDPOINT = "Favourite/"
    const val FAVOURITE_PARAMETER = "all"
    const val USERFAVOURITE_ENDPOINT = API_BASE_URL + FAVOURITE_ENDPOINT

    const val ADVBYID_URL = HOME_URL
    const val ADVBYID_ENDPOINT = "Details"

    const val FORGOTPASS_EMAIL_BASEURL = ACCOUNT_BASE_URL
    const val FORGOTPASS_EMAIL_ENDPOINT = "ForgetEmailRequest"

    const val CHANGEPASS_BASEURL = ACCOUNT_BASE_URL
    const val CHANGEPASS_ENDPOINT = "ChangePassword"

    const val INSERT_AD_WATCHLIST = GET_WATCHLIST_LISTING
    const val INSERT_AD_WATCHLIST_ENDPOINT = "insert"

    const val DELETE_AD_WATCHLIST = GET_WATCHLIST_LISTING
    const val DELETE_AD_WATCHLIST_ENDPOINT = "delete"

    const val INSERT_FAVOURITE_SELLER_ENDPOINT = "insertseller/"
    const val INSERT_FAVOURITE_CATEGORY_ENDPOINT = "insertcategory/"
    const val INSERT_FAVOURITE_SEARCH_ENDPOINT = "insertsearch/"

    const val REMOVE_FAVOURITE_SELLER_ENDPOINT = "RemoveFavoriteSeller/"
    const val REMOVE_FAVOURITE_CATEGORY_ENDPOINT = "RemoveFavoriteCategory/"
    const val REMOVE_FAVOURITE_SEARCH_ENDPOINT = "RemoveFavoriteSearch/"

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

    const val ASK_QUES_AD_BASEURL = API_BASE_URL + "AdvQueAndAns/"
    const val ASK_QUES_AD_ENDPOINT = "create"

    const val GET_QUES_ANS_COMMENT_BASEURL = HOME_URL
    const val GET_QUES_ANS_COMMENT_ENDPOINT = "GetQuestionByAd"

    const val HOME_TOTAL_VISIT_COUNT = GET_WATCHLIST_LISTING + "totalVisitCount/"

    const val POST_ANS_ONADD_QUESID_BASEURL = API_BASE_URL + "AdvQueAndAns/"
    const val POST_ANS_ONADD_QUESID_ENDPOINT = "answer"

    const val POST_COMMENT_ONADD_BASEURL = API_BASE_URL + "AdvQueAndAns/"
    const val POST_COMMENT_ONADD_ENDPOINT = "comment"

    const val GETPOST_AD_DETAIL_BIDING_PRICE_BASEURL = API_BASE_URL + "Bid/"
    const val GET_AD_DETAIL_BIDING_PRICE_ENDPOINT = "maxbid"
    const val POST_MAX_BIDING_PRICE = "placebid"

    const val GET_UNSOLDITEMS_BASEURL = ACCOUNT_BASE_URL
    const val GET_UNSOLDITEMS_ENDPOINT = "soldunsolditems"

    const val GET_SOLDITEMS_BASEURL = ACCOUNT_BASE_URL
    const val GET_SOLDITEMS_ENDPOINT = "soldunsolditems"

    const val POST_BUSINESS_USER_REGISTRATION_BASEURL = API_BASE_URL + "BusinessUser/"
    const val POST_BUSINESS_USER_REGISTRATION_ENDPOINT = "Insertbusinessuser"

    const val GET_WONLOST_BASEURL = API_BASE_URL + "Bid/"
    const val GET_WONLOST_ENDPOINT = "UserWonNLostList"

    const val HOME_TOTAL_ONLINEVISITS_BASEURL = API_BASE_URL + "Accounts/"
    const val HOME_TOTAL_ONLINEVISITS_ENDPOINT = "GetAllOnlineUsers"

    const val GET_SELLER_BASE_URL = API_BASE_URL + "Accounts/"
    const val GET_SELLER_ENDPOINT = "GetUser"

    const val POST_USER_IMAGE = API_BASE_URL + "Accounts/"
    const val POST_USER_IMAGE_ENDPOINT = "userimageupload"

    const val HOME_TOTAL_NUMBERS_OF_MEMBERS_BASEURL = API_BASE_URL + "Accounts/"
    const val HOME_TOTAL_NUMBERS_OF_MEMBERS_ENDPOINT = "usercount"

    const val GET_BUYNOW_SHIPPINGADDRESS_BASEURL = API_BASE_URL + "Accounts/"
    const val GET_BUYNOW_SHIPPINGADDRESS_ENDPOINT = "getaddresses"
    const val INSERT_BUYNOW_SHIPPINGADDRESS_ENDPOINT = "insertaddress"

    const val HASSAN_SERVER = "http://192.168.2.51/"
    const val CREATE_ADV_BYWAQAR_TEST_SERVER_BASEURL =
        HASSAN_SERVER + "Com.Techxcape.Mazad.Web.Services/v1/CarTemplate/"

    const val SEARCH_CATEGORY_LISTING = HOME_URL
    const val SEARCH_CATEGORY_LISTING_ENDPOINT = "Search"

    const val GET_CATEGORY_TAGS_BASE_URL = API_BASE_URL + "Category/"
    const val GET_CATEGORY_TAGS_ENDPOINT = "Tags"
    const val GET_ALL_CATEGORIES = "getAllCategory"
    const val GET_ALL_CATEGORIES_BY_ID = "getAllCategoryByTemplateId"


    const val CREATE_BUSINESS_PRODUCT_BASEURL = API_BASE_URL + "BussinessProduct/"
    const val CREATE_BUSINESS_PRODUCT_ENDPOINT = "createproduct"
    const val PRODUCTBYID_ENDPOINT = "detailsofproduct"
    const val ALL_PRODUCTS_ENDPOINT = "getall"
    const val EDIT_PRODUCTS_ENDPOINT = "editproduct"

    const val GET_CREDIT_CARD = API_BASE_URL + "CardDetail/"

    const val GET_USER_CREDIT_CARD = GET_CREDIT_CARD
    const val GET_USER_CREDIT_CARD_ENDPOINT = "getbyuserid"

    const val INSERT_CREDIT_CARD = GET_CREDIT_CARD
    const val INSERT_CREDIT_CARD_ENDPOINT = "insertcard"

    const val DELETE_CREDIT_CARD = GET_CREDIT_CARD
    const val DELETE_CREDIT_CARD_ENDPOINT = "deletecard"

    const val ADD_TO_CART_BASE_URL = API_BASE_URL + "AddToCart/"
    const val ADD_TO_CART_DELETE_ENDPOINT = "delete"
    const val ADD_TO_CART_INSERT_ENDPOINT = "create"
    const val ADD_TO_CART_USER_LIST_ENDPOINT = "getbyloginuserid"

    const val CHECKOUT_BASE_URL = API_BASE_URL + "Checkout/"
    const val CHECKOUT_INSERT_ENDPOINT = "checkoutiteminsert/"
}