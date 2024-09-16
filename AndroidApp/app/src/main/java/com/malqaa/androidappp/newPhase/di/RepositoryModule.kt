package com.malqaa.androidappp.newPhase.di

import com.malqaa.androidappp.newPhase.data.api.OnRufApiService
import com.malqaa.androidappp.newPhase.data.repository.UserRepositoryImpl
import com.malqaa.androidappp.newPhase.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRepository(onRufApiService: OnRufApiService): UserRepository {
        return UserRepositoryImpl(onRufApiService)
    }
}