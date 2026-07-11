package com.scalo.stocklify.core.data.sync.pull

import com.scalo.stocklify.core.data.sync.helper.MembershipLocalDataSource
import com.scalo.stocklify.core.data.sync.helper.SyncCursorStore
import com.scalo.stocklify.core.data.sync.orchestrator.SyncPhase
import com.scalo.stocklify.core.data.sync.SyncProgress
import com.scalo.stocklify.core.data.sync.orchestrator.TableSyncer
import com.scalo.stocklify.core.data.sync.enums.SyncTask
import com.scalo.stocklify.core.data.sync.orchestrator.phase
import com.scalo.stocklify.core.data.sync.orchestrator.step
import com.scalo.stocklify.core.data.sync.tables.InventorySync
import com.scalo.stocklify.core.data.sync.tables.OrganizationSync
import com.scalo.stocklify.core.data.sync.tables.ProductSync
import com.scalo.stocklify.core.data.sync.tables.StockMovementSync
import com.scalo.stocklify.core.data.sync.tables.StoreSync
import com.scalo.stocklify.core.data.sync.tables.SupplierSync
import com.scalo.stocklify.core.data.sync.tables.UserSync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Pure orchestration — the only thing this class knows is *order*, which is a
 * relationship between tables and can't live inside any single table's own
 * sync module. Every table-specific detail (fetch shape, DTO, apply logic)
 * lives in its own file under sync/tables/.
 */
@Singleton
class PullServiceImpl @Inject constructor(
    cursorStore: SyncCursorStore,
    private val membership: MembershipLocalDataSource,
    userSync: UserSync,
    organizationSync: OrganizationSync,
    storeSync: StoreSync,
    supplierSync: SupplierSync,
    productSync: ProductSync,
    inventorySync: InventorySync,
    stockMovementSync: StockMovementSync,
) : PullService {

    private val syncer = TableSyncer(cursorStore)

    private val phases: List<SyncPhase> = listOf(
        phase(SyncTask.USER, step(userSync.spec) { listOf(null) }),
        phase(
            SyncTask.ORGANIZATION,
            step(organizationSync.organizationsSpec) { listOf(null) },
            step(organizationSync.membersSpec) { listOf(null) }
        ),
        phase(
            SyncTask.STORE,
            step(storeSync.storesSpec) { membership.myOrganizationIds() },
            step(storeSync.userStoreMappingSpec) { storeSync.localStoreIds(membership.myOrganizationIds()) }
        ),
        phase(SyncTask.SUPPLIERS, step(supplierSync.spec) { membership.myStoreIds() }),
        phase(SyncTask.PRODUCTS, step(productSync.spec) { membership.myOrganizationIds() }),
        phase(SyncTask.INVENTORY, step(inventorySync.spec) { membership.myStoreIds() }),
        phase(SyncTask.STOCK_MOVEMENTS, step(stockMovementSync.spec) { membership.myStoreIds() }),
    )

    override fun initialPull(): Flow<SyncProgress> = flow {
        phases.forEachIndexed { index, phase ->
            emit(SyncProgress.Running(phase.task, index, phases.size))
            phase.steps.forEach { it.run(syncer) }
        }
        emit(SyncProgress.Finished)
    }

    override suspend fun pull() {
        phases.forEach { phase -> phase.steps.forEach { it.run(syncer) } }
    }
}