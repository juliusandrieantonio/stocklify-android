package com.scalo.stocklify.core.data.sync.api

import android.util.Log
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
import com.scalo.stocklify.core.data.sync.exceptions.RpcException
import com.scalo.stocklify.core.util.constants.Tables
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteSyncApiImpl @Inject constructor(
    private val postgrest: Postgrest
) : RemoteSyncApi {


    override suspend fun users(
        userId: String,
        sinceVersion: Long,
        limit: Int
    ): List<UserDto> =
        postgrest.from(Tables.USERS)
            .select {
                filter {
                    eq("id", userId)
                    gt("version", sinceVersion)
                }
                order("version", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList()


    override suspend fun organizationMembers(
        userId: String,
        sinceVersion: Long,
        limit: Int
    ): List<OrganizationMemberDto> =
        postgrest.from(Tables.ORGANIZATION_MEMBERS)
            .select {
                filter {
                    eq("user_id", userId)
                    gt("version", sinceVersion)
                    eq("status", "ACTIVE")
                }
                order("version", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList()


    override suspend fun organizations(
        userId: String,
        sinceVersion: Long,
        limit: Int
    ): List<OrganizationDto> =
        postgrest.from(Tables.ORGANIZATIONS)
            .select {
                filter {
                    gt("version", sinceVersion)
                }
                order("version", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList()


    override suspend fun stores(
        orgId: String,
        sinceVersion: Long,
        limit: Int
    ): List<StoreDto> =
        postgrest.from(Tables.STORES)
            .select {
                filter {
                    eq("organization_id", orgId)
                    gt("version", sinceVersion)
                }
                order("version", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList()


    override suspend fun userStoreMappings(
        storeId: String,
        sinceVersion: Long,
        limit: Int
    ): List<UserStoreMappingDto> =
        postgrest.from(Tables.USER_STORE_MAPPING)
            .select {
                filter {
                    eq("store_id", storeId)
                    gt("version", sinceVersion)
                }
                order("version", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList()


    override suspend fun suppliers(
        storeId: String,
        sinceVersion: Long,
        limit: Int
    ): List<SupplierDto> =
        postgrest.from(Tables.SUPPLIERS)
            .select {
                filter {
                    eq("store_id", storeId)
                    gt("version", sinceVersion)
                }
                order("version", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList()


    override suspend fun products(
        orgId: String,
        sinceVersion: Long,
        limit: Int
    ): List<ProductDto> =
        postgrest.from(Tables.PRODUCTS)
            .select {
                filter {
                    eq("organization_id", orgId)
                    gt("version", sinceVersion)
                }
                order("version", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList()


    override suspend fun storeProductMappings(
        storeId: String,
        sinceVersion: Long,
        limit: Int
    ): List<StoreProductMappingDto> =
        postgrest.from(Tables.STORE_PRODUCT_MAPPING)
            .select {
                filter {
                    eq("store_id", storeId)
                    gt("version", sinceVersion)
                }
                order("version", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList()


    override suspend fun stockMovements(
        storeProductIds: List<String>,
        sinceVersion: Long,
        limit: Int
    ): List<StockMovementDto> =
        postgrest.from(Tables.STOCK_MOVEMENTS)
            .select {
                filter {
                    isIn("store_product_id", storeProductIds)
                    gt("version", sinceVersion)
                }
                order("version", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList()



    override suspend fun pushChanges(
        items: List<SyncOutboxEntity>
    ): PushChangesResult {

        if (items.isEmpty()) {
            return PushChangesResult(
                accepted = emptyList(),
                rejected = emptyList()
            )
        }

        val changes = buildJsonArray {
            items.forEach { item ->
                add(
                    buildJsonObject {
                        put(
                            "id",
                            item.id
                        )
                        put(
                            "table_name",
                            item.tableName
                        )
                        put(
                            "operation",
                            item.operation
                        )
                        put(
                            "payload",
                            Json.parseToJsonElement(
                                item.payloadJson
                            )
                        )
                    }
                )
            }
        }

        Log.d("SyncService", "changes: $changes")

        return runRpc {
            postgrest.rpc(
                "push_changes",
                buildJsonObject {
                    put(
                        "p_changes",
                        changes
                    )
                }
            ).decodeAs()
        }
    }


    override suspend fun createOrganization(name: String): String =
        runRpc {
            postgrest.rpc(
                "create_organization",
                buildJsonObject {
                    put("p_name", name)
                }
            ).decodeAs()
        }


    override suspend fun joinInvite(inviteCode: String): String =
        runRpc {
            postgrest.rpc(
                "join_invite",
                buildJsonObject {
                    put("p_invite_code", inviteCode)
                }
            ).decodeAs()
        }


    /** Re-throws PostgREST RPC failures as the matching [RpcException] subtype, keyed off our custom P0xxx error codes. */
    private suspend inline fun <T> runRpc(crossinline block: suspend () -> T): T =
        try {
            block()
        } catch (e: PostgrestRestException) {
            throw RpcException.fromCode(e.code, e.error)
        }


//    override suspend fun upsertProduct(
//        dto: ProductDto
//    ) {
//        postgrest
//            .from(Tables.PRODUCTS)
//            .upsert(dto)
//    }
//
//
//    override suspend fun upsertStore(
//        dto: StoreDto
//    ) {
//        postgrest
//            .from(Tables.STORES)
//            .upsert(dto)
//    }
//
//
//    override suspend fun upsertSupplier(
//        dto: SupplierDto
//    ) {
//        postgrest
//            .from(Tables.SUPPLIERS)
//            .upsert(dto)
//    }
//
//
//    override suspend fun upsertStoreProductMapping(
//        dto: StoreProductMappingDto
//    ) {
//        postgrest
//            .from(Tables.STORE_PRODUCT_MAPPING)
//            .upsert(dto)
//    }
//
//
//    override suspend fun upsertStockMovement(
//        dto: StockMovementDto
//    ) {
//        postgrest
//            .from(Tables.STOCK_MOVEMENTS)
//            .upsert(dto)
//    }
//
//
//    override suspend fun upsertOrganizationMember(
//        dto: OrganizationMemberDto
//    ) {
//        postgrest
//            .from(Tables.ORGANIZATION_MEMBERS)
//            .upsert(dto)
//    }
//
//
//    override suspend fun upsertUserStoreMapping(
//        dto: UserStoreMappingDto
//    ) {
//        postgrest
//            .from(Tables.USER_STORE_MAPPING)
//            .upsert(dto)
//    }
}