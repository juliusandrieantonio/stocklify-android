package com.scalo.stocklify.core.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/** Adds the local image cache columns used to lazily download and cache a
 *  product's Storage image on-device (see ProductSync, GetProductImageFileUseCase). */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE products ADD COLUMN local_image_path TEXT")
        db.execSQL("ALTER TABLE products ADD COLUMN cached_image_key TEXT")
    }
}

val APP_MIGRATIONS = arrayOf(MIGRATION_1_2)
