data class getCartModel(
    val `data`: List<Data>,
    val message: String,
    val status_code: Int
) {
    data class Data(
        val _id: String,
        val advertisements: Advertisements,
        val createddate: String,
        val isActive: Boolean,
        val loggenIn: String,
        val sellerId: Any,
        val user: User
    ) {
        data class Advertisements(
            val categoryName: Any,
            val cityName: Any,
            val id: String,
            val image: String,
            val isbankpaid: Any,
            val iscashpaid: Any,
            val listingtitle: String,
            val name: String,
            val pickupOption: Any,
            val price: String,
            val producttitle: Any,
            val sellerId: String,
            val sellername: Any,
            val shippingOption: Any,
            val subCategory1: Any,
            val subCategory2: Any,
            val subCategory3: Any,
            val subCategory4: Any,
            val subCategory5: Any,
            val subCategory6: Any,
            val userBankAccountIDs: Any
        )

        data class User(
            val address: String,
            val city: String,
            val country: String,
            val email: String,
            val id: String,
            val phone: String,
            val state: Any,
            val username: String
        )
    }
}