package com.malqaa.androidappp.newPhase.data.mapper

import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.BusinessAccountsDetails
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.DomainBusinessAccount
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.DomainLoginResponse
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.DomainLoginUser
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.LoginResponse
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.LoginUser

// Extension functions to map API response to domain model

fun LoginResponse.toDomainModel(): DomainLoginResponse {
    return DomainLoginResponse(
        code = this.code,
        message = this.message,
        statusCode = this.statusCode,
        status = this.status,
        loginUser = this.loginUser?.toDomainModel()
    )
}

fun LoginUser.toDomainModel(): DomainLoginUser {
    return DomainLoginUser(
        activeCode = this.activeCode,
        businessAccounts = this.businessAccounts?.map { it.toDomainModel() },
        closeNotify = this.closeNotify,
        code = this.code,
        countryId = this.countryId,
        createdAt = this.createdAt,
        dateOfBirth = this.dateOfBirth,
        districtName = this.districtName,
        email = this.email,
        firstName = this.firstName,
        gender = this.gender,
        id = this.id,
        img = this.img,
        invitationCode = this.invitationCode,
        lang = this.lang,
        lastName = this.lastName,
        membershipNumber = this.membershipNumber,
        neighborhoodId = this.neighborhoodId,
        password = this.password,
        phone = this.phone,
        rate = this.rate,
        regionId = this.regionId,
        streetNumber = this.streetNumber,
        token = this.token,
        typeUser = this.typeUser,
        userName = this.userName,
        zipCode = this.zipCode,
        showUserInformation = this.showUserInformation
    )
}

fun BusinessAccountsDetails.toDomainModel(): DomainBusinessAccount {
    return DomainBusinessAccount(
        businessAccountId = this.businessAccountId,
        businessAccountName = this.businessAccountName,
        providerId = this.providerId
    )
}
