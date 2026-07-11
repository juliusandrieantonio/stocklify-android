package com.scalo.stocklify.feature.premium.domain.repository

interface OrganizationRepository {

    /** Creates the org (and an OWNER membership for the caller) via the create_organization RPC. Returns the new org id. */
    suspend fun createOrganization(name: String): String

    /** Joins an org via an invite code, via the join_invite RPC. Returns the joined org id. */
    suspend fun joinOrganization(inviteCode: String): String
}
