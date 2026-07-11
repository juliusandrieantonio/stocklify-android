package com.scalo.stocklify.feature.products.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStaticColors
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import com.scalo.stocklify.feature.products.model.ManualProductDraft
import com.scalo.stocklify.feature.products.ui.AddEditProductViewModel
import com.scalo.stocklify.feature.products.ui.EditTarget
import com.scalo.stocklify.feature.products.ui.ManualAddProductViewModel
import com.scalo.stocklify.feature.products.ui.ManualSaveState
import com.scalo.stocklify.feature.products.ui.component.ProductCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewManualProductScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: ManualAddProductViewModel = hiltViewModel(),
) {
    val drafts by viewModel.drafts.collectAsState()
    val saveState by viewModel.saveState.collectAsState()
    val isSaving = saveState is ManualSaveState.Loading

    var showAddSheet by remember { mutableStateOf(false) }
    var addSheetKey by remember { mutableIntStateOf(0) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(saveState) {
        if (saveState is ManualSaveState.Success) onSaved()
    }

    if (showAddSheet) {
        val editTarget = editingIndex?.let { index ->
            drafts.getOrNull(index)?.let { EditTarget.ManualDraft(index, it) }
        }
        ModalBottomSheet(
            onDismissRequest = {
                showAddSheet = false
                editingIndex = null
            },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = StocklifyStaticColors.white,
        ) {
            Box(
                modifier = Modifier.heightIn(max = StocklifySize.bottomSheetHeight)
            ) {
                AddEditProductScreen(
                    onBack = {
                        showAddSheet = false
                        editingIndex = null
                    },
                    onProductStaged = { index, draft ->
                        viewModel.upsertDraft(index, draft)
                        showAddSheet = false
                        editingIndex = null
                    },
                    editTarget = editTarget,
                    showTopBar = false,
                    viewModel = hiltViewModel(key = "manual_add_sheet_$addSheetKey")
                )
            }
        }
    }

    Scaffold(
        topBar = {
            StocklifyTopBar(
                title = "Add Product Manually",
                centerTitle = true,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.outlined_left_arrow),
                            contentDescription = "Back",
                            tint = StocklifyTheme.colors.primary700
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(StocklifySpacing.screenPadding)
        ) {
            Text(
                text = "Added Products (${drafts.size})",
                style = StocklifyTextStyles.subtitleXs,
                color = StocklifyTheme.colors.typography500
            )

            ProductList(
                drafts = drafts,
                onEdit = { index ->
                    editingIndex = index
                    addSheetKey++
                    showAddSheet = true
                },
                modifier = Modifier.weight(1f)
            )

            BottomActions(
                isSaving = isSaving,
                canSave = drafts.isNotEmpty(),
                onAddMore = {
                    editingIndex = null
                    addSheetKey++
                    showAddSheet = true
                },
                onSave = { viewModel.saveProducts() }
            )
        }
    }
}

@Composable
private fun ProductList(
    drafts: List<ManualProductDraft>,
    onEdit: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            vertical = StocklifySpacing.columnGap
        ),
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        itemsIndexed(drafts) { index, draft ->
            val imagePainter by produceState<BitmapPainter?>(null, draft.imageUri) {
                value = draft.imageUri?.let { uri ->
                    withContext(Dispatchers.IO) {
                        context.contentResolver.openInputStream(uri)?.use {
                            BitmapFactory.decodeStream(it)?.asImageBitmap()?.let(::BitmapPainter)
                        }
                    }
                }
            }
            ProductCard(
                modifier = Modifier.fillMaxWidth(),
                image = imagePainter,
                title = draft.name,
                sellingPrice = draft.sellingPrice,
                costPrice = draft.costPrice,
                stock = draft.stock,
                category = draft.category?.name,
                onEditClick = { onEdit(index) },
            )
        }
    }
}

@Composable
private fun BottomActions(
    isSaving: Boolean,
    canSave: Boolean,
    onAddMore: () -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        StocklifyOutlineButton(
            text = "Add More Products",
            onClick = onAddMore,
            fullWidth = true
        )
        StocklifyPrimaryButton(
            text = if (isSaving) "Saving…" else "Save Products",
            onClick = onSave,
            enabled = !isSaving && canSave,
            fullWidth = true
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReviewManualProductScreenPreview() {
    ReviewManualProductScreen(
        onBack = {},
        onSaved = {},
    )
}
