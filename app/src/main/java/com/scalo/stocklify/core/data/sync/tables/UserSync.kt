package com.scalo.stocklify.core.data.sync.tables

import com.scalo.stocklify.core.data.database.dao.UserDao
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.data.sync.api.RemoteSyncApi
import com.scalo.stocklify.core.data.sync.dto.UserDto
import com.scalo.stocklify.core.data.sync.enums.SyncScope
import com.scalo.stocklify.core.data.sync.enums.SyncTask
import com.scalo.stocklify.core.data.sync.orchestrator.TableSyncSpec
import com.scalo.stocklify.core.data.sync.orchestrator.tableSyncSpec
import com.scalo.stocklify.core.util.constants.Tables
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSync @Inject constructor(
    private val remote: RemoteSyncApi,
    private val userDao: UserDao,
    private val currentUserId: () -> String
) {
    /** Unscoped — only ever pulls the caller's own row (RLS: id = auth.uid()). */
    val spec: TableSyncSpec<UserDto> = tableSyncSpec(
        task = SyncTask.USER,
        tableName = Tables.USERS,
        scope = SyncScope.NONE,
        fetchPage = { _, since, limit -> remote.users(currentUserId(), since, limit) },
        versionOf = { it.version },
        applyPage = { items -> userDao.upsertAll(items.map { it.toEntity() }) }
    )
}
