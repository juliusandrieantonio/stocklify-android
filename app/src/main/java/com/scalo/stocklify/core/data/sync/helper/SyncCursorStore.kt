package com.scalo.stocklify.core.data.sync.helper

import com.scalo.stocklify.core.data.sync.enums.SyncScope

/**
 * Local cache of last-synced version per (table, scope). The server's
 * device_sync_state row is the durable source of truth (survives reinstall);
 * this is just so we don't hit the network before every pull to ask "where was I".
 */
interface SyncCursorStore {
    suspend fun get(tableName: String, scope: SyncScope, scopeId: String?): Long
    suspend fun set(tableName: String, scope: SyncScope, scopeId: String?, version: Long)
}