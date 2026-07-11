package com.scalo.stocklify.core.data.sync.orchestrator

import com.scalo.stocklify.core.data.sync.enums.SyncScope
import com.scalo.stocklify.core.data.sync.enums.SyncTask

/**
 * Describes how to pull one table: fetch a page from Supabase filtered by
 * scope + cursor version, and how to apply that page locally. T is the
 * network DTO shape (a @Serializable data class), not the Room entity —
 * mapping DTO -> Entity happens inside applyPage.
 */
interface TableSyncSpec<T> {
    val task: SyncTask
    val tableName: String
    val scope: SyncScope

    /** null scopeId only valid when scope == NONE */
    suspend fun fetchPage(scopeId: String?, sinceVersion: Long, limit: Int): List<T>
    fun versionOf(item: T): Long

    /** Upserts into Room. Must be safe to call twice with the same data (idempotent). */
    suspend fun applyPage(items: List<T>)
}

/**
 * Builds a TableSyncSpec from plain lambdas instead of a hand-written anonymous
 * object — cuts each table's definition down to the one thing that's actually
 * different per table, instead of repeating the same 4-member object shape.
 */
fun <T> tableSyncSpec(
    task: SyncTask,
    tableName: String,
    scope: SyncScope,
    fetchPage: suspend (scopeId: String?, sinceVersion: Long, limit: Int) -> List<T>,
    versionOf: (T) -> Long,
    applyPage: suspend (List<T>) -> Unit
): TableSyncSpec<T> = object : TableSyncSpec<T> {
    override val task = task
    override val tableName = tableName
    override val scope = scope
    override suspend fun fetchPage(scopeId: String?, sinceVersion: Long, limit: Int) = fetchPage(scopeId, sinceVersion, limit)
    override fun versionOf(item: T) = versionOf(item)
    override suspend fun applyPage(items: List<T>) = applyPage(items)
}