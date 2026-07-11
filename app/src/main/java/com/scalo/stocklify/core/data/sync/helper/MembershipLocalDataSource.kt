package com.scalo.stocklify.core.data.sync.helper

/**
 * After organizations/stores have been pulled locally, other steps need to know
 * which org ids and store ids to iterate over. This reads that back out of Room —
 * it does NOT hit the network, since RLS already filtered those tables to what
 * this user can see when they were pulled.
 */
interface MembershipLocalDataSource {
    suspend fun myOrganizationIds(): List<String>
    suspend fun myStoreIds(): List<String>
    suspend fun storeProductIds(storeId: String): List<String>
}