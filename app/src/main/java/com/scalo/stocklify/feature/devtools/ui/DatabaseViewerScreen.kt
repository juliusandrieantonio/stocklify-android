package com.scalo.stocklify.feature.devtools.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.borders.bottomBorder
import com.scalo.stocklify.core.ui.borders.rightBorder
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.chips.StocklifyFilterChip
import com.scalo.stocklify.core.ui.components.texts.StocklifyDropdownField
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifyRadius
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun DatabaseViewerScreen(
    onBack: () -> Unit,
    viewModel: DatabaseViewerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            StocklifyTopBar(
                title = "Database Viewer",
                titleColor = StocklifyTheme.colors.primary700,
                titleStyle = StocklifyTextStyles.heading5,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.outlined_left_arrow),
                            contentDescription = "Back",
                            tint = StocklifyTheme.colors.primary700
                        )
                    }
                },
                showBottomBorder = true,
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(StocklifySpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(StocklifySpacing.m)
        ) {

            StocklifyDropdownField(
                options = state.tables,
                value = "Tables",
                onValueSelected = { viewModel.onAction(DatabaseViewerAction.SelectTable(it)) },
            )


            StocklifyTextField(
                value = state.query,
                onValueChange = { viewModel.onAction(DatabaseViewerAction.QueryChanged(it)) },
                label = "SQL query",
                placeholder = "SELECT * FROM table_name LIMIT 200",
                singleLine = false,
            )

            StocklifyPrimaryButton(
                text = "Run query",
                onClick = { viewModel.onAction(DatabaseViewerAction.RunQuery) },
                isLoading = state.isLoading,
                fullWidth = true,
            )

            state.error?.let {
                Text(
                    text = it,
                    style = StocklifyTextStyles.bodyS,
                    color = StocklifyTheme.colors.error
                )
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = StocklifyTheme.colors.primary700)
                }
            } else if (state.columns.isNotEmpty()) {
                val rowScroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rowScroll)
                        .border(
                            width = StocklifyStroke.s,
                            color = StocklifyTheme.colors.grayscale300,
                            shape = RoundedCornerShape(StocklifyRadius.s)
                        )
                ) {
                    Row(
                        modifier = Modifier.bottomBorder(
                            width = StocklifyStroke.m,
                            color = StocklifyTheme.colors.grayscale300
                        )
                    ) {
                        state.columns.forEachIndexed { index, column ->
                            Text(
                                text = column,
                                style = StocklifyTextStyles.subtitleXs,
                                color = StocklifyTheme.colors.typography500,
                                modifier = Modifier
                                    .width(140.dp)
                                    .then(
                                        if (index != state.columns.lastIndex) {
                                            Modifier.rightBorder(
                                                width = StocklifyStroke.s,
                                                color = StocklifyTheme.colors.grayscale300
                                            )
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .padding(StocklifySpacing.s)
                            )
                        }
                    }

                    LazyColumn {
                        items(state.rows) { row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min)
                                    .bottomBorder(
                                        width = StocklifyStroke.s,
                                        color = StocklifyTheme.colors.grayscale200
                                    )
                            ) {
                                row.forEachIndexed { index, cell ->
                                    Box(
                                        modifier = Modifier
                                            .width(140.dp)
                                            .fillMaxHeight()
                                            .then(
                                                if (index != row.lastIndex) {
                                                    Modifier.rightBorder(
                                                        width = StocklifyStroke.s,
                                                        color = StocklifyTheme.colors.grayscale200
                                                    )
                                                } else Modifier
                                            )
                                            .padding(StocklifySpacing.s)
                                    ) {
                                        Text(
                                            text = cell ?: "null",
                                            style = StocklifyTextStyles.bodyS,
                                            color = StocklifyTheme.colors.typography400
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
