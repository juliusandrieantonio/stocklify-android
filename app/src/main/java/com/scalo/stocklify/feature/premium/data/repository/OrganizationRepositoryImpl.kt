package com.scalo.stocklify.feature.premium.data.repository

import com.scalo.stocklify.core.data.sync.api.RemoteSyncApi
import com.scalo.stocklify.feature.premium.domain.repository.OrganizationRepository
import javax.inject.Inject

/**
 * organizations/organization_members have no client-side INSERT policy — creation
 * and joining only happen through the create_organization/join_invite RPCs
 * (SECURITY DEFINER), so there is no outbox write here. Callers are responsible
 * for triggering a sync afterward to pull the resulting rows into Room.
 */
class OrganizationRepositoryImpl @Inject constructor(
    private val remoteSyncApi: RemoteSyncApi,
) : OrganizationRepository {

    override suspend fun createOrganization(name: String): String =
        remoteSyncApi.createOrganization(name)

    override suspend fun joinOrganization(inviteCode: String): String =
        remoteSyncApi.joinInvite(inviteCode)
}
