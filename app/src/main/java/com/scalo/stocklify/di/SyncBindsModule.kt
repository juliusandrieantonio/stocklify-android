package com.scalo.stocklify.di

import com.scalo.stocklify.core.data.sync.helper.MembershipLocalDataSource
import com.scalo.stocklify.core.data.sync.helper.MembershipLocalDataSourceImpl
import com.scalo.stocklify.core.data.sync.helper.SyncCursorStore
import com.scalo.stocklify.core.data.sync.pull.PullService
import com.scalo.stocklify.core.data.sync.pull.PullServiceImpl
import com.scalo.stocklify.core.data.sync.push.PushService
import com.scalo.stocklify.core.data.sync.push.PushServiceImpl
import com.scalo.stocklify.core.data.sync.api.RemoteSyncApi
import com.scalo.stocklify.core.data.sync.api.RemoteSyncApiImpl
import com.scalo.stocklify.core.data.sync.SyncManager
import com.scalo.stocklify.core.data.sync.SyncManagerImpl
import com.scalo.stocklify.core.data.sync.helper.SyncCursorStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncBindsModule {

    @Binds
    @Singleton
    abstract fun bindRemoteSyncApi(impl: RemoteSyncApiImpl): RemoteSyncApi

    @Binds
    @Singleton
    abstract fun bindSyncCursorStore(impl: SyncCursorStoreImpl): SyncCursorStore

    @Binds
    @Singleton
    abstract fun bindMembershipLocalDataSource(impl: MembershipLocalDataSourceImpl): MembershipLocalDataSource

    @Binds
    @Singleton
    abstract fun bindPushService(impl: PushServiceImpl): PushService

    @Binds
    @Singleton
    abstract fun bindPullService(impl: PullServiceImpl): PullService

    @Binds
    @Singleton
    abstract fun bindSyncManager(impl: SyncManagerImpl): SyncManager
}
