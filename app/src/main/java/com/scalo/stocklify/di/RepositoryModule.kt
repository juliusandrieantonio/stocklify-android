package com.scalo.stocklify.di

import com.scalo.stocklify.feature.auth.data.repository.AuthRepositoryImpl
import com.scalo.stocklify.feature.auth.domain.repository.AuthRepository
import com.scalo.stocklify.feature.onboarding.data.repository.OnboardingRepositoryImpl
import com.scalo.stocklify.feature.onboarding.domain.repository.OnboardingRepository
import com.scalo.stocklify.feature.stores.data.repository.StoreRepositoryImpl
import com.scalo.stocklify.feature.stores.data.repository.StoreTypeRepositoryImpl
import com.scalo.stocklify.feature.stores.domain.repository.StoreRepository
import com.scalo.stocklify.feature.stores.domain.repository.StoreTypeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        impl: OnboardingRepositoryImpl
    ): OnboardingRepository

    @Binds
    abstract fun bindStoreRepository(
        impl: StoreRepositoryImpl
    ): StoreRepository

    @Binds
    abstract fun bindStoreTypeRepository(
        impl: StoreTypeRepositoryImpl
    ): StoreTypeRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}