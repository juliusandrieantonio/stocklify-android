package com.scalo.stocklify.core.ui.components.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocklifyTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            title?.let {
                Text(text = it)
            }
        },
        navigationIcon = {
            navigationIcon?.invoke()
        },
        actions = actions,
        modifier = modifier
    )
}