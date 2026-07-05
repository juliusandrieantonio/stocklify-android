package com.scalo.stocklify.di

import com.scalo.stocklify.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

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
