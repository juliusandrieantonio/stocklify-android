package com.scalo.stocklify.core.model.enums

enum class MembershipRole(val value: String) {
    OWNER("OWNER"),
    MANAGER("MANAGER"),
    EMPLOYEE("EMPLOYEE");

    companion object {
        fun from(value: String): MembershipRole {
            return entries.first { it.value == value }
        }
    }
}
