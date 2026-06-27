package com.scalo.stocklify.feature.dashboard.ui

sealed class DashboardEvent {
    data class ShowError(val message: String) : DashboardEvent()
}
