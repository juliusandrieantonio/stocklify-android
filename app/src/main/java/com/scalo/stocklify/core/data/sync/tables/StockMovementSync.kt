package com.scalo.stocklify.core.data.sync.tables

import com.scalo.stocklify.core.data.database.dao.StockMovementDao
import com.scalo.stocklify.core.data.database.entity.SyncOutboxEntity
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.data.sync.helper.MembershipLocalDataSource
import com.scalo.stocklify.core.data.sync.api.RemoteSyncApi
import com.scalo.stocklify.core.data.sync.enums.SyncScope
import com.scalo.stocklify.core.data.sync.enums.SyncTask
import com.scalo.stocklify.core.data.sync.dto.StockMovementDto
import com.scalo.stocklify.core.util.constants.Tables
import com.scalo.stocklify.core.data.sync.orchestrator.TableSyncSpec
import com.scalo.stocklify.core.data.sync.orchestrator.tableSyncSpec
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockMovementSync @Inject constructor(
    private val remote: RemoteSyncApi,
    private val stockMovementDao: StockMovementDao,
    private val membership: MembershipLocalDataSource
) {
    /**
     * Scope is STORE for cursor-tracking, but the remote filter is actually
     * `store_product_id in (...)` since this table has no store_id column —
     * resolved from Room right before fetching. Only valid once InventorySync.spec
     * has already run in the same pull pass.
     */
    val spec: TableSyncSpec<StockMovementDto> = tableSyncSpec(
        task = SyncTask.STOCK_MOVEMENTS,
        tableName = Tables.STOCK_MOVEMENTS,
        scope = SyncScope.STORE,
        fetchPage = { storeId, since, limit ->
            val storeProductIds = membership.storeProductIds(storeId!!)
            if (storeProductIds.isEmpty()) emptyList() else remote.stockMovements(storeProductIds, since, limit)
        },
        versionOf = { it.version },
        applyPage = { items -> stockMovementDao.upsertAll(items.map { it.toEntity() }) }
    )
}