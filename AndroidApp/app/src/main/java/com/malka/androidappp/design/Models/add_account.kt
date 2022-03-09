package com.malka.androidappp.design.Models

data class get_add_bank_detail(
    val `data`: List<add_bank_Detail>,
    val message: String,
    val status_code: Int
) {
    data class add_bank_Detail(
        val userBankAccount_IBN_Number: String,
        val userBankAccount_No: String,
        val userBankAccount_Title: String,
        val userBank_Name: String,
        val userID: String,
    )
}