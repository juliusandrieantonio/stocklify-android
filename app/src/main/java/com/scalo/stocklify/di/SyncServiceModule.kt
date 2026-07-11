package com.scalo.stocklify.di

import com.scalo.stocklify.feature.auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncServiceModule {

    @Provides
    @Singleton
    fun provideCurrentUserId(authRepository: AuthRepository): () -> String =
        { authRepository.getCurrentUser()?.id ?: error("Not authenticated") }
}