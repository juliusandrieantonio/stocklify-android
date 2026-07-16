package com.scalo.stocklify.di

import com.scalo.stocklify.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.ktor.client.plugins.HttpTimeout
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @OptIn(SupabaseInternal::class)
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
        install(Realtime)
        install(Functions)

        // Default engine socket timeout (~10s) is too tight for image uploads
        // over slow/mobile connections; the HttpTimeout plugin config applies
        // regardless of which Ktor engine ends up handling the request.
        httpConfig {
            install(HttpTimeout) {
                socketTimeoutMillis = 60_000
                connectTimeoutMillis = 30_000
                requestTimeoutMillis = 60_000
            }
        }
    }

    @Provides
    @Singleton
    fun provideAuth(supabaseClient: SupabaseClient): Auth = supabaseClient.auth

    @Provides
    @Singleton
    fun providePostgrest(supabaseClient: SupabaseClient): Postgrest = supabaseClient.postgrest

    @Provides
    @Singleton
    fun provideStorage(supabaseClient: SupabaseClient): Storage = supabaseClient.storage

    @Provides
    @Singleton
    fun provideRealtime(supabaseClient: SupabaseClient): Realtime = supabaseClient.realtime

    @Provides
    @Singleton
    fun provideFunctions(supabaseClient: SupabaseClient): Functions = supabaseClient.functions
}
