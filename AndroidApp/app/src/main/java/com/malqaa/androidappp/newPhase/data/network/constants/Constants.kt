package com.malqaa.androidappp.newPhase.data.network.constants

object Constants {

//    http://onrufwebsite6-001-site1.htempurl.com/
    const val HTTP_PROTOCOL = "https"

    //   const val SERVER_LOCATION = "89.40.10.168:85"
//    const val SERVER_LOCATION = "advertleap-001-site1.itempurl.com"
    const val SERVER_LOCATION = "onrufwebsite6-001-site1.htempurl.com"
    const val Api = "api"
    const val API_VERSION = "v1"
    const val IMAGE_FOLDER = "ImageFile"

    const val IMAGE_URL2 =
        HTTP_PROTOCOL + "://" + SERVER_LOCATION + "/"


    const val API_BASE_URL =
//        HTTP_PROTOCOL + "://" + SERVER_LOCATION + "/" + API_VERSION + "/";

        HTTP_PROTOCOL + "://" + SERVER_LOCATION + "/" + Api + "/" + API_VERSION + "/";

    const val IMAGE_URL =
        HTTP_PROTOCOL + "://" + SERVER_LOCATION + "/" + IMAGE_FOLDER + "/";


    const val GET_WATCHLIST_LISTING = "Watchlist/"

    const val FAVOURITE_ENDPOINT = "Favourite/"

    const val USERFAVOURITE_ENDPOINT = FAVOURITE_ENDPOINT

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


    const val HOME_TOTAL_VISIT_COUNT = GET_WATCHLIST_LISTING + "totalVisitCount/"


    const val GET_AD_DETAIL_BIDING_PRICE_ENDPOINT = "Bid/maxbid"
    const val POST_MAX_BIDING_PRICE = "Bid/placebid"


    const val GET_BUYNOW_SHIPPINGADDRESS_ENDPOINT = "Accounts/getaddresses"
    const val INSERT_BUYNOW_SHIPPINGADDRESS_ENDPOINT = "Accounts/insertaddress"


    const val GET_CATEGORY_TAGS_ENDPOINT = "Category/Tags"

    const val otpDataKey = "OtpDataKey"
    const val male = 0
    const val female = 1


}