package com.malqaa.androidappp.newPhase.domain.utils

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
