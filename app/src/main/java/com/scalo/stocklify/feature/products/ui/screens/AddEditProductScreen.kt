package com.scalo.stocklify.feature.products.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.model.models.Category
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.components.texts.StocklifyDropdownField
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifyRadius
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStaticColors
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import com.scalo.stocklify.feature.products.model.ManualProductDraft
import com.scalo.stocklify.feature.products.ui.AddEditProductViewModel
import com.scalo.stocklify.feature.products.ui.EditTarget
import com.scalo.stocklify.feature.products.ui.SaveState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddEditProductScreen(
    onBack: () -> Unit,
    onProductStaged: (index: Int?, draft: ManualProductDraft) -> Unit,
    onReviewEditDone: ((index: Int?, name: String, price: Double?, categoryCode: String?, stock: Int?, imageUri: Uri?) -> Unit)? = null,
    editTarget: EditTarget? = null,
    showTopBar: Boolean = true,
    viewModel: AddEditProductViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val form by viewModel.formState.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val saveState by viewModel.saveState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        when (editTarget) {
            is EditTarget.ManualDraft -> viewModel.loadDraft(editTarget.index, editTarget.draft)
            is EditTarget.ScanReview -> viewModel.loadScanReviewItem(editTarget.index, editTarget.result)
            null -> Unit
        }
    }

    var showImageSourceDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onImageSelected(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) viewModel.onCameraImageCaptured()
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = viewModel.createCameraUri()
            cameraLauncher.launch(uri)
        }
    }

    LaunchedEffect(saveState) {
        when (val state = saveState) {
            is SaveState.Staged -> onProductStaged(state.index, state.draft)
            is SaveState.ReviewEditDone -> onReviewEditDone?.invoke(
                state.index, state.name, state.price, state.categoryCode, state.stock, state.imageUri
            )
            is SaveState.Error -> scope.launch {
                snackbarHostState.showSnackbar(state.message)
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = StocklifyTheme.colors.error,
                    contentColor = StocklifyStaticColors.white,
                )
            }
        },
        topBar = {
            if (showTopBar) {
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(StocklifySpacing.screenPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(StocklifySize.small))

            FormsSection(
                name = form.name,
                sellingPrice = form.sellingPrice,
                costPrice = form.costPrice,
                stock = form.stock,
                selectedCategory = form.selectedCategory,
                categories = categories,
                imageUri = form.imageUri,
                nameError = form.nameError,
                costPriceError = form.costPriceError,
                sellingPriceError = form.sellingPriceError,
                stockError = form.stockError,
                onNameChange = viewModel::onNameChange,
                onSellingPriceChange = viewModel::onSellingPriceChange,
                onCostPriceChange = viewModel::onCostPriceChange,
                onStockChange = viewModel::onStockChange,
                onCategorySelected = viewModel::onCategorySelected,
                onImageClick = { showImageSourceDialog = true },
                onGalleryLauncher = {
                    galleryLauncher.launch("image/*")
                },
                onCameraLauncher = {
                    val hasCameraPermission = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                    if (hasCameraPermission) {
                        cameraLauncher.launch(viewModel.createCameraUri())
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            )

            Spacer(modifier = Modifier.height(StocklifySize.medium))

            StocklifyPrimaryButton(
                text = "Save",
                onClick = viewModel::save,
                fullWidth = true,
            )

            Spacer(modifier = Modifier.height(StocklifySize.extraSmall))

            FooterNote()
        }
    }
}

@Composable
private fun FooterNote() {
    DrawableText(
        painter = painterResource(R.drawable.outlined_info),
        text = buildAnnotatedString { append("You can add, edit, or remove products anytime from inventory. ") },
        textStyle = StocklifyTextStyles.subtitleXxs,
        textColor = StocklifyTheme.colors.typography100,
        iconTint = StocklifyTheme.colors.typography100,
        spacing = StocklifySpacing.s
    )
}

@Composable
private fun FormsSection(
    name: String,
    sellingPrice: String,
    costPrice: String,
    stock: String,
    selectedCategory: Category?,
    categories: List<Category>,
    imageUri: Uri?,
    nameError: String?,
    costPriceError: String?,
    sellingPriceError: String?,
    stockError: String?,
    onNameChange: (String) -> Unit,
    onSellingPriceChange: (String) -> Unit,
    onCostPriceChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onImageClick: () -> Unit,
    onGalleryLauncher: () -> Unit,
    onCameraLauncher: () -> Unit
) {
    val context = LocalContext.current

    val imageBitmap by produceState<ImageBitmap?>(null, imageUri) {
        value = withContext(Dispatchers.IO) {
            imageUri?.let { uri ->
                context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it)?.asImageBitmap()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
    ) {
        Text(
            text = "Product Image",
            style = StocklifyTextStyles.subtitleXs,
            color = StocklifyStaticColors.black
        )

        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = "Product image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(StocklifySize.xxlarge)
                    .align(Alignment.CenterHorizontally)
                    .clickable(
                        onClick = { onImageClick() }
                    )
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap, Alignment.CenterHorizontally)
            ) {
                DrawableText(
                    text = buildAnnotatedString { append("Take a Photo") },
                    textStyle = StocklifyTextStyles.subtitleXs,
                    textColor = StocklifyTheme.colors.primary700,
                    painter = painterResource(R.drawable.outlined_camera),
                    iconTint = StocklifyTheme.colors.primary700,
                    modifier = Modifier
                        .border(
                            width = StocklifyStroke.s,
                            color = StocklifyTheme.colors.transparentPrimary600,
                            shape = RoundedCornerShape(StocklifyRadius.s)
                        )
                        .padding(StocklifySpacing.l)
                        .weight(1f)
                        .clickable(
                            onClick = { onCameraLauncher() }
                        )
                )
                DrawableText(
                    text = buildAnnotatedString { append("Upload a Photo") },
                    textStyle = StocklifyTextStyles.subtitleXs,
                    textColor = StocklifyTheme.colors.primary700,
                    painter = painterResource(R.drawable.outlined_photo),
                    iconTint = StocklifyTheme.colors.primary700,
                    modifier = Modifier
                        .border(
                            width = StocklifyStroke.s,
                            color = StocklifyTheme.colors.transparentPrimary600,
                            shape = RoundedCornerShape(StocklifyRadius.s)
                        )
                        .padding(StocklifySpacing.l)
                        .weight(1f)
                        .clickable(
                            onClick = { onGalleryLauncher() }
                        )
                )
            }
        }

        StocklifyTextField(
            value = name,
            onValueChange = onNameChange,
            label = "Product Name",
            placeholder = "Enter product name",
            isError = nameError != null,
            supportingText = nameError,
        )

        StocklifyDropdownField(
            value = selectedCategory?.name ?: "",
            options = categories.map { it.name },
            onValueSelected = { selected ->
                categories.find { it.name == selected }?.let(onCategorySelected)
            },
            label = "Category",
            placeholder = "Select a category",
            optional = true,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap, Alignment.CenterHorizontally)
        ) {
            StocklifyTextField(
                leadingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.outlined_peso),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(StocklifyTheme.colors.grayscale400),
                            modifier = Modifier
                                .padding(horizontal = 14.dp)
                                .size(StocklifySize.extraSmall)
                        )
                        VerticalDivider(
                            modifier = Modifier
                                .height(48.dp)
                                .padding(end = 12.dp),
                            thickness = StocklifyStroke.s,
                            color = StocklifyTheme.colors.grayscale200,
                        )
                    }
                },
                value = sellingPrice,
                onValueChange = onSellingPriceChange,
                label = "Selling Price",
                placeholder = "0.00",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = sellingPriceError != null,
                supportingText = sellingPriceError,
                modifier = Modifier.weight(1f)
            )

            StocklifyTextField(
                leadingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.outlined_peso),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(StocklifyTheme.colors.grayscale400),
                            modifier = Modifier
                                .padding(horizontal = 14.dp)
                                .size(StocklifySize.extraSmall)
                        )
                        VerticalDivider(
                            modifier = Modifier
                                .height(48.dp)
                                .padding(end = 12.dp),
                            thickness = StocklifyStroke.s,
                            color = StocklifyTheme.colors.grayscale200,
                        )
                    }
                },
                value = costPrice,
                onValueChange = onCostPriceChange,
                label = "Cost Price",
                placeholder = "0.00",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = costPriceError != null,
                supportingText = costPriceError,
                modifier = Modifier.weight(1f)
            )
        }

        StocklifyTextField(
            value = stock,
            onValueChange = onStockChange,
            label = "Stock Quantity",
            placeholder = "Enter stock quantity",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = stockError != null,
            supportingText = stockError,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddEditProductScreenPreview() {
    AddEditProductScreen(
        onBack = {},
        onProductStaged = { _, _ -> }
    )
}
