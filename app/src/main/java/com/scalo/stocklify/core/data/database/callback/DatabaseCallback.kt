package com.scalo.stocklify.core.data.database.callback

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

internal val STORE_TYPE_SEED_SQL = listOf(
    "INSERT INTO `store_types` (`code`, `name`, `status`, `version`) VALUES ('SARI_SARI', 'Sari-Sari Store', 'ACTIVE', 1)",
    "INSERT INTO `store_types` (`code`, `name`, `status`, `version`) VALUES ('RETAIL', 'Retail', 'ACTIVE', 1)",
    "INSERT INTO `store_types` (`code`, `name`, `status`, `version`) VALUES ('FOOD_BEVERAGE', 'Food & Beverage', 'ACTIVE', 1)",
    "INSERT INTO `store_types` (`code`, `name`, `status`, `version`) VALUES ('HEALTH_BEAUTY', 'Health & Beauty', 'ACTIVE', 1)",
    "INSERT INTO `store_types` (`code`, `name`, `status`, `version`) VALUES ('HARDWARE', 'Hardware', 'ACTIVE', 1)",
    "INSERT INTO `store_types` (`code`, `name`, `status`, `version`) VALUES ('ELECTRONICS', 'Electronics', 'ACTIVE', 1)",
    "INSERT INTO `store_types` (`code`, `name`, `status`, `version`) VALUES ('OTHER', 'Other', 'ACTIVE', 1)"
)

internal val CATEGORY_SEED_SQL = listOf(
    "INSERT INTO `categories` (`code`, `name`, `description`, `status`, `version`) VALUES ('FOOD_BEVERAGES', 'Food & Beverages', NULL, 'ACTIVE', 1)",
    "INSERT INTO `categories` (`code`, `name`, `description`, `status`, `version`) VALUES ('SNACKS_CANDY', 'Snacks & Candy', NULL, 'ACTIVE', 1)",
    "INSERT INTO `categories` (`code`, `name`, `description`, `status`, `version`) VALUES ('PERSONAL_CARE', 'Personal Care', NULL, 'ACTIVE', 1)",
    "INSERT INTO `categories` (`code`, `name`, `description`, `status`, `version`) VALUES ('HEALTH_MEDICINE', 'Health & Medicine', NULL, 'ACTIVE', 1)",
    "INSERT INTO `categories` (`code`, `name`, `description`, `status`, `version`) VALUES ('ELECTRONICS', 'Electronics', NULL, 'ACTIVE', 1)",
    "INSERT INTO `categories` (`code`, `name`, `description`, `status`, `version`) VALUES ('CLOTHING_ACCESSORIES', 'Clothing & Accessories', NULL, 'ACTIVE', 1)",
    "INSERT INTO `categories` (`code`, `name`, `description`, `status`, `version`) VALUES ('HARDWARE_TOOLS', 'Hardware & Tools', NULL, 'ACTIVE', 1)",
    "INSERT INTO `categories` (`code`, `name`, `description`, `status`, `version`) VALUES ('HOUSEHOLD_ITEMS', 'Household Items', NULL, 'ACTIVE', 1)",
    "INSERT INTO `categories` (`code`, `name`, `description`, `status`, `version`) VALUES ('CLEANING_SUPPLIES', 'Cleaning Supplies', NULL, 'ACTIVE', 1)",
    "INSERT INTO `categories` (`code`, `name`, `description`, `status`, `version`) VALUES ('SCHOOL_OFFICE_SUPPLIES', 'School & Office Supplies', NULL, 'ACTIVE', 1)"
)

/**
 * store_product_mapping.quantity is derived and must only ever be mutated by inserting a
 * stock_movements row; this trigger mirrors the Postgres trigger that keeps it in sync.
 */
private const val STOCK_MOVEMENT_TRIGGER_SQL = """
    CREATE TRIGGER IF NOT EXISTS trg_stock_movement_apply
    AFTER INSERT ON stock_movements
    BEGIN
        UPDATE store_product_mapping
        SET quantity = quantity + NEW.quantity
        WHERE id = NEW.store_product_id;
    END
"""

class DatabaseCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        STORE_TYPE_SEED_SQL.forEach { db.execSQL(it) }
        CATEGORY_SEED_SQL.forEach { db.execSQL(it) }
        db.execSQL(STOCK_MOVEMENT_TRIGGER_SQL)
    }
}
