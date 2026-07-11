package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.OrganizationEntity

@Dao
interface OrganizationDao : BaseDao<OrganizationEntity> {

    @Query("""
        SELECT *
        FROM organizations
        WHERE id = :id
    """)
    suspend fun getById(id: String): OrganizationEntity?

    @Query("UPDATE organizations SET sync_status = 'SYNCED' WHERE id = :id")
    suspend fun markSynced(id: String)
}
