package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.UserEntity

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("""
        SELECT *
        FROM users
        WHERE id = :id
    """)
    suspend fun getById(id: String): UserEntity?
}
