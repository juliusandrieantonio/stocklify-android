package com.scalo.stocklify.core.data.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: T)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(entities: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<T>)

    @Update
    suspend fun update(entity: T)

    @Update
    suspend fun updateAll(entities: List<T>)

    @Delete
    suspend fun delete(entity: T)
}