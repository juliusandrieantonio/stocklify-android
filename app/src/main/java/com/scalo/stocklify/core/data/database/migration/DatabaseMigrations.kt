package com.scalo.stocklify.core.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val STORE_TYPE_SEED_SQL = listOf(
    "INSERT INTO `store_types` (`code`, `name`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('SARI_SARI', 'Sari-Sari Store', NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `store_types` (`code`, `name`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('RETAIL', 'Retail', NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `store_types` (`code`, `name`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('FOOD_BEVERAGE', 'Food & Beverage', NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `store_types` (`code`, `name`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('HEALTH_BEAUTY', 'Health & Beauty', NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `store_types` (`code`, `name`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('HARDWARE', 'Hardware', NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `store_types` (`code`, `name`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('ELECTRONICS', 'Electronics', NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `store_types` (`code`, `name`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('OTHER', 'Other', NULL, NULL, 0, 'SYSTEM', 1, 1)"
)

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `categories` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `description` TEXT,
                `updated_at` INTEGER,
                `updated_by` TEXT,
                `created_at` INTEGER NOT NULL,
                `created_by` TEXT NOT NULL,
                `status` INTEGER NOT NULL,
                `version` INTEGER NOT NULL,
                FOREIGN KEY(`store_id`) REFERENCES `stores`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `suppliers` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `store_id` INTEGER NOT NULL,
                `name` TEXT NOT NULL,
                `contact_person` TEXT,
                `phone` TEXT,
                `updated_at` INTEGER,
                `updated_by` TEXT,
                `created_at` INTEGER NOT NULL,
                `created_by` TEXT NOT NULL,
                `status` INTEGER NOT NULL,
                `version` INTEGER NOT NULL,
                FOREIGN KEY(`store_id`) REFERENCES `stores`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_suppliers_store_id`
            ON `suppliers` (`store_id`)
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `products` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `code` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `selling_price` REAL NOT NULL,
                `cost_price` REAL NOT NULL,
                `image_url` TEXT,
                `category_id` INTEGER NOT NULL,
                `updated_at` INTEGER,
                `updated_by` TEXT,
                `created_at` INTEGER NOT NULL,
                `created_by` TEXT NOT NULL,
                `status` INTEGER NOT NULL,
                `version` INTEGER NOT NULL,
                FOREIGN KEY(`category_id`) REFERENCES `categories`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE UNIQUE INDEX IF NOT EXISTS `index_products_code`
            ON `products` (`code`)
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_products_category_id`
            ON `products` (`category_id`)
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `store_product_mapping` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `store_id` INTEGER NOT NULL,
                `product_id` INTEGER NOT NULL,
                `quantity` INTEGER NOT NULL,
                `supplier_id` INTEGER,
                `updated_at` INTEGER,
                `updated_by` TEXT,
                `created_at` INTEGER NOT NULL,
                `created_by` TEXT NOT NULL,
                `status` INTEGER NOT NULL,
                `version` INTEGER NOT NULL,
                FOREIGN KEY(`store_id`) REFERENCES `stores`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT,
                FOREIGN KEY(`product_id`) REFERENCES `products`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT,
                FOREIGN KEY(`supplier_id`) REFERENCES `suppliers`(`id`) ON UPDATE CASCADE ON DELETE SET NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_store_product_mapping_store_id`
            ON `store_product_mapping` (`store_id`)
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_store_product_mapping_product_id`
            ON `store_product_mapping` (`product_id`)
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_store_product_mapping_supplier_id`
            ON `store_product_mapping` (`supplier_id`)
        """.trimIndent())

        db.execSQL("""
            CREATE UNIQUE INDEX IF NOT EXISTS `index_store_product_mapping_store_id_product_id`
            ON `store_product_mapping` (`store_id`, `product_id`)
        """.trimIndent())
    }
}

internal val CATEGORY_SEED_SQL = listOf(
    "INSERT INTO `categories` (`name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('Food & Beverages', NULL, NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `categories` (`name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('Snacks & Candy', NULL, NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `categories` (`name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('Personal Care', NULL, NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `categories` (`name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('Health & Medicine', NULL, NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `categories` (`name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('Electronics', NULL, NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `categories` (`name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('Clothing & Accessories', NULL, NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `categories` (`name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('Hardware & Tools', NULL, NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `categories` (`name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('Household Items', NULL, NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `categories` (`name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('Cleaning Supplies', NULL, NULL, NULL, 0, 'SYSTEM', 1, 1)",
    "INSERT INTO `categories` (`name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`) VALUES ('School & Office Supplies', NULL, NULL, NULL, 0, 'SYSTEM', 1, 1)"
)

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Recreate categories without NOT NULL on store_id so global (store-less) categories are allowed
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `categories_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `description` TEXT,
                `updated_at` INTEGER,
                `updated_by` TEXT,
                `created_at` INTEGER NOT NULL,
                `created_by` TEXT NOT NULL,
                `status` INTEGER NOT NULL,
                `version` INTEGER NOT NULL,
                FOREIGN KEY(`store_id`) REFERENCES `stores`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT
            )
        """.trimIndent())

        db.execSQL("INSERT INTO `categories_new` SELECT * FROM `categories`")
        db.execSQL("DROP TABLE `categories`")
        db.execSQL("ALTER TABLE `categories_new` RENAME TO `categories`")

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_categories_store_id`
            ON `categories` (`store_id`)
        """.trimIndent())

        CATEGORY_SEED_SQL.forEach { db.execSQL(it) }
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Recreate categories without store_id; previous migration left the table
        // in a broken state (FK referencing a non-existent store_id column, broken
        // seed SQL with mismatched value count). Preserve any user-created rows.
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `categories_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `description` TEXT,
                `updated_at` INTEGER,
                `updated_by` TEXT,
                `created_at` INTEGER NOT NULL,
                `created_by` TEXT NOT NULL,
                `status` INTEGER NOT NULL,
                `version` INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO `categories_new`
                (`id`, `name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`)
            SELECT `id`, `name`, `description`, `updated_at`, `updated_by`, `created_at`, `created_by`, `status`, `version`
            FROM `categories`
            WHERE `name` IS NOT NULL
        """.trimIndent())

        db.execSQL("DROP TABLE `categories`")
        db.execSQL("ALTER TABLE `categories_new` RENAME TO `categories`")

        CATEGORY_SEED_SQL.forEach { db.execSQL(it) }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `store_types` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `code` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `updated_at` INTEGER,
                `updated_by` TEXT,
                `created_at` INTEGER NOT NULL,
                `created_by` TEXT NOT NULL,
                `status` INTEGER NOT NULL,
                `version` INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE UNIQUE INDEX IF NOT EXISTS `index_store_types_code`
            ON `store_types` (`code`)
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_stores_store_type_id`
            ON `stores` (`store_type_id`)
        """.trimIndent())

        STORE_TYPE_SEED_SQL.forEach { db.execSQL(it) }
    }
}
