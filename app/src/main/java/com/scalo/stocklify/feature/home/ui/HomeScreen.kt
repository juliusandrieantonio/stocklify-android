package com.scalo.stocklify.feature.home.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.scalo.stocklify.R
import com.scalo.stocklify.core.config.DevMode
import com.scalo.stocklify.core.ui.borders.topBorder
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import com.scalo.stocklify.core.util.IconPosition
import com.scalo.stocklify.feature.dashboard.ui.DashboardScreen
import com.scalo.stocklify.feature.inventory.ui.InventoryScreen
import com.scalo.stocklify.feature.profile.ui.ProfileScreen
import com.scalo.stocklify.feature.records.ui.RecordsScreen
import com.scalo.stocklify.feature.salesmode.ui.SalesModeViewModel
import com.scalo.stocklify.feature.salesmode.ui.screens.SalesModeScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    salesViewModel: SalesModeViewModel,
    onAddManually: () -> Unit,
    onScanProduct: () -> Unit,
    onCurrentSales: () -> Unit,
    onSignedOut: () -> Unit,
    onOpenDatabaseViewer: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val storeName by viewModel.storeName.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val currentItem = BottomNavItem.items.find { it.route == currentRoute } ?: BottomNavItem.SalesMode

    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                if (DevMode.isEnabled) {
                    Text(
                        text = "Utilities",
                        style = StocklifyTextStyles.subtitleXs,
                        color = StocklifyTheme.colors.typography400,
                        modifier = Modifier.padding(StocklifySpacing.l)
                    )

                    NavigationDrawerItem(
                        label = { Text("Database Viewer") },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.outlined_list),
                                contentDescription = "Database Viewer"
                            )
                        },
                        selected = false,
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent
                        ),
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            onOpenDatabaseViewer()
                        },
                        modifier = Modifier.padding(horizontal = StocklifySpacing.m)
                    )
                }
            }
        }
    ) {
        HomeScreenContent(
            salesViewModel = salesViewModel,
            onAddManually = onAddManually,
            onScanProduct = onScanProduct,
            onCurrentSales = onCurrentSales,
            onMenuClick = { coroutineScope.launch { drawerState.open() } },
            storeName = storeName,
            isSyncing = isSyncing,
            isAuthenticated = isAuthenticated,
            navController = navController,
            currentRoute = currentRoute,
            currentItem = currentItem,
            context = context,
            notificationPermissionLauncher = notificationPermissionLauncher,
            viewModel = viewModel,
            onSignedOut = onSignedOut
        )
    }
}

@Composable
private fun HomeScreenContent(
    salesViewModel: SalesModeViewModel,
    onAddManually: () -> Unit,
    onScanProduct: () -> Unit,
    onCurrentSales: () -> Unit,
    onMenuClick: () -> Unit,
    storeName: String?,
    isSyncing: Boolean,
    isAuthenticated: Boolean,
    navController: NavHostController,
    currentRoute: String?,
    currentItem: BottomNavItem,
    context: android.content.Context,
    notificationPermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>,
    viewModel: HomeViewModel,
    onSignedOut: () -> Unit
) {
    Scaffold(
        topBar = {
            StocklifyTopBar(
                title = currentItem.label,
                titleColor = StocklifyTheme.colors.primary700,
                titleStyle = StocklifyTextStyles.heading5,
                subtitle = {
                    DrawableText(
                        text = buildAnnotatedString { append("$storeName Store") },
                        painter = painterResource(R.drawable.outlined_down_chevron),
                        iconPosition = IconPosition.End,
                        textStyle = StocklifyTextStyles.subtitleXs,
                        textColor = StocklifyTheme.colors.typography400,
                        iconSize = StocklifySize.extraSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            painter = painterResource(R.drawable.outlined_menu),
                            contentDescription = "Menu",
                            tint = StocklifyTheme.colors.primary700
                        )
                    }
                },
                actions = {
                    if (isAuthenticated) {
                        IconButton(
                            enabled = !isSyncing,
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                                viewModel.sync()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outlined_sync),
                                contentDescription = "Sync",
                                tint = StocklifyTheme.colors.primary700
                            )
                        }
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.outlined_robot),
                            contentDescription = "Robot",
                            tint = StocklifyTheme.colors.primary700
                        )
                    }
                },
                showBottomBorder = false
            )
        },
        bottomBar = {
            StocklifyBottomBar(navController, currentRoute)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.SalesMode.route,
            modifier = Modifier.padding(padding).padding(StocklifySpacing.screenPadding)
        ) {

            composable(BottomNavItem.Dashboard.route) {
                DashboardScreen()
            }

            composable(BottomNavItem.Records.route) {
                RecordsScreen()
            }

            composable(BottomNavItem.SalesMode.route) {
                SalesModeScreen(
                    viewModel = salesViewModel,
                    onAddManually = onAddManually,
                    onScanProduct = onScanProduct,
                    onCurrentSales = onCurrentSales,
                )
            }

            composable(BottomNavItem.Inventory.route) {
                InventoryScreen()
            }

            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    onSignedOut = onSignedOut
                )
            }
        }
    }
}

@Composable
fun StocklifyBottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar(
        containerColor = StocklifyTheme.colors.base,
        modifier = Modifier.topBorder(
                width = StocklifyStroke.s,
                color = StocklifyTheme.colors.grayscale300
            )
    ){
        BottomNavItem.items.forEach { item ->

            val selected = currentRoute == item.route

            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .topBorder(
                        width = StocklifyStroke.l,
                        color = if (selected) {
                            StocklifyTheme.colors.primary700
                        } else {
                            Color.Unspecified
                        }
                    ),

                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (selected) item.selectedIcon else item.icon
                        ),
                        contentDescription = item.label,
                        tint = if (selected) {
                            StocklifyTheme.colors.primary700
                        } else {
                            StocklifyTheme.colors.typography100
                        }
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = StocklifyTextStyles.subtitleXxs,
                        color = if (selected) {
                            StocklifyTheme.colors.primary700
                        } else {
                            StocklifyTheme.colors.typography100
                        }
                    )
                }
            )
        }
    }
}