package com.malqaa.androidappp.newPhase.di

import com.malqaa.androidappp.newPhase.domain.repository.UserRepository
import com.malqaa.androidappp.newPhase.domain.usecase.ChangeLanguageUseCase
import com.malqaa.androidappp.newPhase.domain.usecase.ChangePasswordUseCase
import com.malqaa.androidappp.newPhase.domain.usecase.ForgetPasswordUseCase
import com.malqaa.androidappp.newPhase.domain.usecase.LoginWebsiteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideChangeLanguageUseCase(userRepository: UserRepository): ChangeLanguageUseCase {
        return ChangeLanguageUseCase(userRepository)
    }

    @Provides
    fun provideLoginWebsiteUseCase(userRepository: UserRepository): LoginWebsiteUseCase {
        return LoginWebsiteUseCase(userRepository)
    }

    @Provides
    fun provideForgetPasswordUseCase(userRepository: UserRepository): ForgetPasswordUseCase {
        return ForgetPasswordUseCase(userRepository)
    }

    @Provides
    fun provideChangePasswordUseCase(userRepository: UserRepository): ChangePasswordUseCase {
        return ChangePasswordUseCase(userRepository)
    }

}