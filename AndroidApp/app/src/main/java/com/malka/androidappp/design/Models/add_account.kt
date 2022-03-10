package com.malka.androidappp.design.Models

data class BankListRespone(
    val `data`: List<BankDetail>,
    val message: String,
    val status_code: Int
) {
    data class BankDetail(
        val userBankAccount_IBN_Number: String,
        val userBankAccount_No: String,
        val userBankAccount_Title: String,
        val userBank_Name: String,
        val userID: String,
        var isSelect: Boolean=false,
    )
}