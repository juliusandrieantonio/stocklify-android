package com.scalo.stocklify.core.data.sync.tables

import com.scalo.stocklify.core.data.database.dao.OrganizationDao
import com.scalo.stocklify.core.data.database.dao.OrganizationMemberDao
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.data.sync.api.RemoteSyncApi
import com.scalo.stocklify.core.data.sync.dto.OrganizationDto
import com.scalo.stocklify.core.data.sync.dto.OrganizationMemberDto
import com.scalo.stocklify.core.data.sync.enums.SyncScope
import com.scalo.stocklify.core.data.sync.enums.SyncTask
import com.scalo.stocklify.core.data.sync.orchestrator.TableSyncSpec
import com.scalo.stocklify.core.data.sync.orchestrator.tableSyncSpec
import com.scalo.stocklify.core.util.constants.Tables
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrganizationSync @Inject constructor(
    private val remote: RemoteSyncApi,
    private val organizationDao: OrganizationDao,
    private val memberDao: OrganizationMemberDao,
    private val currentUserId: () -> String
) {
    /**
     * organizations SELECT RLS is created_by = auth.uid(), so this needs only the caller's
     * id — no dependency on membersSpec having already run. Runs first so organization_members
     * rows always land after their (own-created) parent org row.
     */
    val organizationsSpec: TableSyncSpec<OrganizationDto> = tableSyncSpec(
        task = SyncTask.ORGANIZATION,
        tableName = Tables.ORGANIZATIONS,
        scope = SyncScope.NONE,
        fetchPage = { _, since, limit -> remote.organizations(currentUserId(), since, limit) },
        versionOf = { it.version },
        applyPage = { items -> organizationDao.upsertAll(items.map { it.toEntity() }) }
    )

    val membersSpec: TableSyncSpec<OrganizationMemberDto> = tableSyncSpec(
        task = SyncTask.ORGANIZATION,
        tableName = Tables.ORGANIZATION_MEMBERS,
        scope = SyncScope.NONE,
        fetchPage = { _, since, limit -> remote.organizationMembers(currentUserId(), since, limit) },
        versionOf = { it.version },
        applyPage = { items -> memberDao.upsertAll(items.map { it.toEntity() }) }
    )
}