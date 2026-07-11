package com.scalo.stocklify.core.data.sync.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PushChangesResult(
    val accepted: List<AcceptedChange>,
    val rejected: List<RejectedChange>,
    val summary: PushSummary? = null
)

@Serializable
data class AcceptedChange(
    val id: String,
    @SerialName("table_name") val tableName: String,
    val status: String
)

@Serializable
data class RejectedChange(
    val id: String,
    @SerialName("table_name") val tableName: String,
    val status: String,
    val code: String,
    val message: String
)

@Serializable
data class PushSummary(
    val total: Int,
    val accepted: Int,
    val rejected: Int
)
