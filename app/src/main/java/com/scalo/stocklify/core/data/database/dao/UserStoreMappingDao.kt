package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.UserStoreMappingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStoreMappingDao : BaseDao<UserStoreMappingEntity> {

    @Query("""
        SELECT *
        FROM user_store_mapping
        WHERE user_id = :userId
    """)
    fun getByUserId(userId: String): Flow<List<UserStoreMappingEntity>>

    @Query("SELECT COUNT(*) FROM user_store_mapping WHERE user_id = :userId")
    suspend fun getCountByUserId(userId: String): Int
}
