package com.scalo.stocklify.core.data.sync.tables

import com.scalo.stocklify.core.data.database.dao.StoreProductMappingDao
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.data.sync.api.RemoteSyncApi
import com.scalo.stocklify.core.data.sync.enums.SyncScope
import com.scalo.stocklify.core.data.sync.enums.SyncTask
import com.scalo.stocklify.core.data.sync.orchestrator.TableSyncSpec
import com.scalo.stocklify.core.data.sync.dto.StoreProductMappingDto
import com.scalo.stocklify.core.data.sync.orchestrator.tableSyncSpec
import com.scalo.stocklify.core.util.constants.Tables
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventorySync @Inject constructor(
    private val remote: RemoteSyncApi,
    private val storeProductMappingDao: StoreProductMappingDao
) {
    /** Must run before StockMovementSync.spec — that spec resolves store_product_ids from Room. */
    val spec: TableSyncSpec<StoreProductMappingDto> = tableSyncSpec(
        task = SyncTask.INVENTORY,
        tableName = Tables.STORE_PRODUCT_MAPPING,
        scope = SyncScope.STORE,
        fetchPage = { storeId, since, limit -> remote.storeProductMappings(storeId!!, since, limit) },
        versionOf = { it.version },
        applyPage = { items -> storeProductMappingDao.upsertAll(items.map { it.toEntity() }) }
    )
}