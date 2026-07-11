package com.scalo.stocklify.core.data.sync.push

interface PushService {
    suspend fun push()
}