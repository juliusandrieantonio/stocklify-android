package com.scalo.stocklify.feature.dashboard.ui

sealed interface DashboardAction {
    data object Refresh : DashboardAction
}
