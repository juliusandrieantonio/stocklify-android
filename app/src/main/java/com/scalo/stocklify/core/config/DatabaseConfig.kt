package com.scalo.stocklify.core.config

import com.scalo.stocklify.BuildConfig

object DatabaseConfig {

    private const val BASE_DB_NAME = "stocklify"
    private const val PASSWORD_KEY = "db-pplus2-password"

    fun databaseName(): String {
        return if (BuildConfig.DEBUG) {
            "${BASE_DB_NAME}_dev.db"
        } else {
            "$BASE_DB_NAME.db"
        }
    }

    fun databasePassword(): String {
        return PASSWORD_KEY
    }
}