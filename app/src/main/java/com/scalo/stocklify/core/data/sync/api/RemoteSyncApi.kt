package com.scalo.stocklify.core.data.sync.api

import com.scalo.stocklify.core.data.database.entity.SyncOutboxEntity
import com.scalo.stocklify.core.data.sync.dto.OrganizationDto
import com.scalo.stocklify.core.data.sync.dto.OrganizationMemberDto
import com.scalo.stocklify.core.data.sync.dto.ProductDto
import com.scalo.stocklify.core.data.sync.dto.PushChangesResult
import com.scalo.stocklify.core.data.sync.dto.StockMovementDto
import com.scalo.stocklify.core.data.sync.dto.StoreDto
import com.scalo.stocklify.core.data.sync.dto.StoreProductMappingDto
import com.scalo.stocklify.core.data.sync.dto.SupplierDto
import com.scalo.stocklify.core.data.sync.dto.UserDto
import com.scalo.stocklify.core.data.sync.dto.UserStoreMappingDto

/**
 * Wraps the actual Supabase calls. Swap bodies for your real supabase-kt
 * client — the shape (filter column, ordering, pagination) is what matters.
 */
interface RemoteSyncApi {
    suspend fun users(userId: String, sinceVersion: Long, limit: Int): List<UserDto>
    suspend fun organizationMembers(userId: String, sinceVersion: Long, limit: Int): List<OrganizationMemberDto>
    /** organizations SELECT RLS is created_by = auth.uid(), so this is filtered by the caller's own id, not an org id. */
    suspend fun organizations(userId: String, sinceVersion: Long, limit: Int): List<OrganizationDto>
    suspend fun stores(orgId: String, sinceVersion: Long, limit: Int): List<StoreDto>
    suspend fun userStoreMappings(storeId: String, sinceVersion: Long, limit: Int): List<UserStoreMappingDto>
    suspend fun suppliers(storeId: String, sinceVersion: Long, limit: Int): List<SupplierDto>
    suspend fun products(orgId: String, sinceVersion: Long, limit: Int): List<ProductDto>
    suspend fun storeProductMappings(storeId: String, sinceVersion: Long, limit: Int): List<StoreProductMappingDto>
    suspend fun stockMovements(storeProductIds: List<String>, sinceVersion: Long, limit: Int): List<StockMovementDto>

    suspend fun pushChanges(items: List<SyncOutboxEntity>): PushChangesResult

    /** Calls the `create_organization` RPC (SECURITY DEFINER) — creates the org row and an OWNER membership atomically. */
    suspend fun createOrganization(name: String): String

    /** Calls the `join_invite` RPC (SECURITY DEFINER) — validates the code and creates the membership row. Returns the joined organization's id. */
    suspend fun joinInvite(inviteCode: String): String
}