package com.scalo.stocklify.core.data.sync.helper

import com.scalo.stocklify.core.data.database.dao.SyncCursorDao
import com.scalo.stocklify.core.data.database.entity.SyncCursorEntity
import com.scalo.stocklify.core.data.sync.enums.SyncScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncCursorStoreImpl @Inject constructor(
    private val dao: SyncCursorDao
) : SyncCursorStore {

    override suspend fun get(tableName: String, scope: SyncScope, scopeId: String?): Long {
        val (storeId, organizationId) = axisFor(scope, scopeId)
        return dao.getLastSyncedVersion(tableName, storeId, organizationId) ?: 0L
    }

    override suspend fun set(tableName: String, scope: SyncScope, scopeId: String?, version: Long) {
        val (storeId, organizationId) = axisFor(scope, scopeId)
        dao.upsert(SyncCursorEntity(tableName, storeId, organizationId, version))
    }

    private fun axisFor(scope: SyncScope, scopeId: String?): Pair<String, String> =
        when (scope) {
            SyncScope.STORE -> (scopeId ?: "") to ""
            SyncScope.ORGANIZATION -> "" to (scopeId ?: "")
            SyncScope.NONE -> "" to ""
        }
}
