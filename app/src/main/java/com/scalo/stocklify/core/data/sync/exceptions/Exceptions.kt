package com.scalo.stocklify.core.data.sync.exceptions

class RlsDeniedException(message: String) : Exception(message)

/**
 * Maps the custom SQLSTATE codes our RPC functions raise (P0400..P0500) via
 * `RAISE EXCEPTION ... USING ERRCODE = 'P0xxx'`. Branch on the subtype rather than
 * on HTTP status or on the raw error message, which can change independently of meaning.
 */
sealed class RpcException(message: String) : Exception(message) {
    class BadRequest(message: String) : RpcException(message)
    class Unauthorized(message: String) : RpcException(message)
    class Forbidden(message: String) : RpcException(message)
    class NotFound(message: String) : RpcException(message)
    class Conflict(message: String) : RpcException(message)
    class UnprocessableEntity(message: String) : RpcException(message)
    class ServerError(message: String) : RpcException(message)
    class Unknown(message: String) : RpcException(message)

    companion object {
        fun fromCode(code: String?, message: String): RpcException = when (code) {
            "P0400" -> BadRequest(message)
            "P0401" -> Unauthorized(message)
            "P0403" -> Forbidden(message)
            "P0404" -> NotFound(message)
            "P0409" -> Conflict(message)
            "P0422" -> UnprocessableEntity(message)
            "P0500" -> ServerError(message)
            else -> Unknown(message)
        }
    }
}