package com.scalo.stocklify.core.data.sync.orchestrator

import com.scalo.stocklify.core.data.sync.SyncTableException
import kotlinx.coroutines.CancellationException

/**
 * One unit of orchestration: a spec plus how to find which scope ids to run it
 * for. `resolveScopeIds` is resolved lazily, at run time — not precomputed —
 * because "my store ids" only exists in Room after an earlier step (stores)
 * has already synced. This is what lets initialPull() and pull() share one
 * ordered list instead of each hand-writing the same sequence.
 */
class ScopedStep<T>(
    private val spec: TableSyncSpec<T>,
    private val resolveScopeIds: suspend () -> List<String?>
) {
    suspend fun run(syncer: TableSyncer) {
        resolveScopeIds().forEach { id ->
            try {
                syncer.syncOne(spec, id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                throw SyncTableException(spec.task, spec.tableName, e)
            }
        }
    }
}

fun <T> step(spec: TableSyncSpec<T>, resolveScopeIds: suspend () -> List<String?>) =
    ScopedStep(spec, resolveScopeIds)