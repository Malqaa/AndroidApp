package com.malqaa.androidappp.newPhase.domain.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Sealed class representing the state of a network response.
 */
sealed class NetworkResponse<out T> {

    /**
     * Represents a successful response with the data.
     */
    data class Success<T>(val data: T) : NetworkResponse<T>()

    /**
     * Represents an error response with an optional error message and status code.
     */
    data class Error(val message: String, val code: Int? = null) : NetworkResponse<Nothing>()

    /**
     * Represents a loading state, used to show progress indicators.
     */
    object Loading : NetworkResponse<Nothing>()

    /**
     * Represents a paginated response with data and pagination information.
     */
    data class Paginated<T>(
        val data: List<T>,
        val currentPage: Int,
        val totalPages: Int,
        val totalItems: Int
    ) : NetworkResponse<T>()

    /**
     * Represents a state where additional actions are needed, such as retrying the request.
     */
    data class Retry(val message: String) : NetworkResponse<Nothing>()
}

class NetworkResponseDeserializer : JsonDeserializer<NetworkResponse<*>> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): NetworkResponse<*> {
        val jsonObject = json.asJsonObject

        // Handle different types of responses
        return when {
            jsonObject.has("data") -> {
                val data = context.deserialize<Any>(
                    jsonObject.get("data"),
                    getTypeFromResponseType(typeOfT)
                )
                NetworkResponse.Success(data)
            }

            jsonObject.has("error") -> {
                val message = jsonObject.get("error").asString
                NetworkResponse.Error(message)
            }

            jsonObject.has("status") && jsonObject.get("status").asString == "loading" -> {
                NetworkResponse.Loading
            }

            jsonObject.has("pagination") -> {
                val data = context.deserialize<List<Any>>(
                    jsonObject.get("data"),
                    getListTypeFromResponseType(typeOfT)
                )
                val pagination = jsonObject.get("pagination").asJsonObject
                val currentPage = pagination.get("currentPage").asInt
                val totalPages = pagination.get("totalPages").asInt
                val totalItems = pagination.get("totalItems").asInt
                NetworkResponse.Paginated(data, currentPage, totalPages, totalItems)
            }

            else -> {
                NetworkResponse.Error("Unknown error")
            }
        }
    }

    private fun getTypeFromResponseType(type: Type): Type {
        // Implement method to determine the correct type from the response type
        return type
    }

    private fun getListTypeFromResponseType(type: Type): Type {
        // Implement method to determine the correct list type from the response type
        return type
    }
}
