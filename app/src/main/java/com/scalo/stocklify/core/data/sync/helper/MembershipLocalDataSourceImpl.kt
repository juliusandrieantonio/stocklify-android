package com.scalo.stocklify.core.data.sync.helper

import com.scalo.stocklify.core.data.database.dao.OrganizationMemberDao
import com.scalo.stocklify.core.data.database.dao.StoreProductMappingDao
import com.scalo.stocklify.core.data.database.dao.UserStoreMappingDao
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MembershipLocalDataSourceImpl @Inject constructor(
    private val organizationMemberDao: OrganizationMemberDao,
    private val userStoreMappingDao: UserStoreMappingDao,
    private val storeProductMappingDao: StoreProductMappingDao,
    private val currentUserId: () -> String
) : MembershipLocalDataSource {

    override suspend fun myOrganizationIds(): List<String> =
        organizationMemberDao.getAllByUserId(currentUserId()).map { it.organizationId }.distinct()

    override suspend fun myStoreIds(): List<String> =
        userStoreMappingDao.getByUserId(currentUserId()).first().map { it.storeId }.distinct()

    override suspend fun storeProductIds(storeId: String): List<String> =
        storeProductMappingDao.getByStoreId(storeId).first().map { it.id }
}
