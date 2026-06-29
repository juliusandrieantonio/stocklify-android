package com.scalo.stocklify.core.ui.components.topbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.scalo.stocklify.core.ui.borders.bottomBorder
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocklifyTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleColor: Color = StocklifyTheme.colors.typography500,
    titleStyle: TextStyle = StocklifyTextStyles.subtitleM,
    subtitle: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    containerColor: Color = StocklifyTheme.colors.base,
    showBottomBorder: Boolean = false,
    centerTitle: Boolean = false,
) {
    val titleContent: @Composable () -> Unit = {
        Column {
            title?.let {
                Text(
                    text = it,
                    style = titleStyle,
                    color = titleColor,
                )
            }
            subtitle?.invoke()
        }
    }

    val resolvedModifier = modifier.then(
        if (showBottomBorder) {
            Modifier.bottomBorder(
                width = StocklifyStroke.s,
                color = StocklifyTheme.colors.grayscale300
            )
        } else {
            Modifier
        }
    )

    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = containerColor,
        navigationIconContentColor = StocklifyTheme.colors.typography500,
        actionIconContentColor = StocklifyTheme.colors.typography500,
    )

    if (centerTitle) {
        CenterAlignedTopAppBar(
            title = titleContent,
            navigationIcon = { navigationIcon?.invoke() },
            actions = actions,
            modifier = resolvedModifier,
            colors = colors,
        )
    } else {
        TopAppBar(
            title = titleContent,
            navigationIcon = { navigationIcon?.invoke() },
            actions = actions,
            modifier = resolvedModifier,
            colors = colors,
        )
    }
}
