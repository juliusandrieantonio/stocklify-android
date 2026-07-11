package com.scalo.stocklify.core.data.sync.tables

import com.scalo.stocklify.core.data.database.dao.SupplierDao
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.data.sync.api.RemoteSyncApi
import com.scalo.stocklify.core.data.sync.enums.SyncScope
import com.scalo.stocklify.core.data.sync.enums.SyncTask
import com.scalo.stocklify.core.data.sync.dto.SupplierDto
import com.scalo.stocklify.core.util.constants.Tables
import com.scalo.stocklify.core.data.sync.orchestrator.TableSyncSpec
import com.scalo.stocklify.core.data.sync.orchestrator.tableSyncSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupplierSync @Inject constructor(
    private val remote: RemoteSyncApi,
    private val supplierDao: SupplierDao
) {
    val spec: TableSyncSpec<SupplierDto> = tableSyncSpec(
        task = SyncTask.SUPPLIERS,
        tableName = Tables.SUPPLIERS,
        scope = SyncScope.STORE,
        fetchPage = { storeId, since, limit -> remote.suppliers(storeId!!, since, limit) },
        versionOf = { it.version },
        applyPage = { items -> supplierDao.upsertAll(items.map { it.toEntity() }) }
    )
}