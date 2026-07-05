package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.OrganizationMemberEntity

@Dao
interface OrganizationMemberDao : BaseDao<OrganizationMemberEntity> {

    @Query("""
        SELECT *
        FROM organization_members
        WHERE user_id = :userId
        LIMIT 1
    """)
    suspend fun getByUserId(userId: String): OrganizationMemberEntity?
}
