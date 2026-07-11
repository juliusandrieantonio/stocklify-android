package com.scalo.stocklify.core.data.sync.orchestrator

import com.scalo.stocklify.core.data.sync.helper.SyncCursorStore
import com.scalo.stocklify.core.util.constants.Constants


/**
 * Pages through one table until exhausted, applying each page and advancing
 * the cursor only after that page's local write succeeds — so a crash mid-sync
 * just re-pulls the same page next time rather than losing the cursor position.
 */
class TableSyncer(
    private val cursorStore: SyncCursorStore
) {
    suspend fun <T> syncOne(spec: TableSyncSpec<T>, scopeId: String?, pageSize: Int = Constants.PAGE_SIZE) {
        var cursor = cursorStore.get(spec.tableName, spec.scope, scopeId)

        while (true) {
            val page = spec.fetchPage(scopeId, cursor, pageSize)
            if (page.isEmpty()) break

            spec.applyPage(page)
            cursor = page.maxOf { spec.versionOf(it) }
            cursorStore.set(spec.tableName, spec.scope, scopeId, cursor)

            if (page.size < pageSize) break
        }
    }

    /** Convenience for STORE/ORGANIZATION-scoped specs that need to run once per known id. */
    suspend fun <T> syncForEach(spec: TableSyncSpec<T>, scopeIds: List<String>, pageSize: Int = Constants.PAGE_SIZE) {
        scopeIds.forEach { id -> syncOne(spec, id, pageSize) }
    }
}