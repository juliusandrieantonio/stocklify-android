package com.scalo.stocklify.core.data.sync.tables

import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.data.sync.api.RemoteSyncApi
import com.scalo.stocklify.core.data.sync.enums.SyncScope
import com.scalo.stocklify.core.data.sync.enums.SyncTask
import com.scalo.stocklify.core.data.sync.orchestrator.TableSyncSpec
import com.scalo.stocklify.core.data.sync.orchestrator.tableSyncSpec
import com.scalo.stocklify.core.data.sync.dto.ProductDto
import com.scalo.stocklify.core.util.constants.Tables
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductSync @Inject constructor(
    private val remote: RemoteSyncApi,
    private val productDao: ProductDao
) {
    val spec: TableSyncSpec<ProductDto> = tableSyncSpec(
        task = SyncTask.PRODUCTS,
        tableName = Tables.PRODUCTS,
        scope = SyncScope.ORGANIZATION,
        fetchPage = { orgId, since, limit -> remote.products(orgId!!, since, limit) },
        versionOf = { it.version },
        applyPage = { items -> productDao.upsertAll(items.map { it.toEntity() }) }
    )
}