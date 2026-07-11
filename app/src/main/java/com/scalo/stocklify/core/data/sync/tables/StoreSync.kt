package com.scalo.stocklify.core.data.sync.tables

import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.dao.UserStoreMappingDao
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.data.sync.api.RemoteSyncApi
import com.scalo.stocklify.core.data.sync.enums.SyncScope
import com.scalo.stocklify.core.data.sync.enums.SyncTask
import com.scalo.stocklify.core.data.sync.dto.StoreDto
import com.scalo.stocklify.core.data.sync.dto.UserStoreMappingDto
import com.scalo.stocklify.core.data.sync.orchestrator.TableSyncSpec
import com.scalo.stocklify.core.data.sync.orchestrator.tableSyncSpec
import com.scalo.stocklify.core.util.constants.Tables
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreSync @Inject constructor(
    private val remote: RemoteSyncApi,
    private val storeDao: StoreDao,
    private val userStoreMappingDao: UserStoreMappingDao
) {
    val storesSpec: TableSyncSpec<StoreDto> = tableSyncSpec(
        task = SyncTask.STORE,
        tableName = Tables.STORES,
        scope = SyncScope.ORGANIZATION,
        fetchPage = { orgId, since, limit -> remote.stores(orgId!!, since, limit) },
        versionOf = { it.version },
        applyPage = { items -> storeDao.upsertAll(items.map { it.toEntity() }) }
    )

    /**
     * Resolves scope from stores already written to Room by storesSpec, rather than from
     * user_store_mapping — that table doesn't exist yet on a fresh pull, since this very step
     * is what populates it (using myStoreIds() here would be circular and always empty).
     */
    suspend fun localStoreIds(organizationIds: List<String>): List<String> =
        if (organizationIds.isEmpty()) emptyList() else storeDao.getIdsByOrganizationIds(organizationIds)

    /** Must run after storesSpec within the same phase — depends on stores already being in Room. */
    val userStoreMappingSpec: TableSyncSpec<UserStoreMappingDto> = tableSyncSpec(
        task = SyncTask.STORE,
        tableName = Tables.USER_STORE_MAPPING,
        scope = SyncScope.STORE,
        fetchPage = { storeId, since, limit -> remote.userStoreMappings(storeId!!, since, limit) },
        versionOf = { it.version },
        applyPage = { items -> userStoreMappingDao.upsertAll(items.map { it.toEntity() }) }
    )
}