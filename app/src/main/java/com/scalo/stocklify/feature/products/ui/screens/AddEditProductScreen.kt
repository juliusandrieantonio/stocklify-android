package com.scalo.stocklify.feature.products.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.model.models.Category
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
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
import com.scalo.stocklify.feature.products.ui.AddEditProductViewModel
import com.scalo.stocklify.feature.products.ui.SaveState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddEditProductScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    onReviewEditDone: ((index: Int, name: String, price: Double?, categoryId: Long?, stock: Long?, imageUri: Uri?) -> Unit)? = null,
    viewModel: AddEditProductViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val form by viewModel.formState.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val saveState by viewModel.saveState.collectAsState()
    val isSaving = saveState is SaveState.Loading

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
            is SaveState.Success -> onSave()
            is SaveState.ReviewEditDone -> onReviewEditDone?.invoke(
                state.index, state.name, state.price, state.categoryId, state.stock, state.imageUri
            )
            is SaveState.Error -> scope.launch {
                snackbarHostState.showSnackbar(state.message)
            }
            else -> Unit
        }
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Add Product Image") },
            text = { Text("Choose how to add an image for this product.") },
            confirmButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    val hasCameraPermission = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                    if (hasCameraPermission) {
                        cameraLauncher.launch(viewModel.createCameraUri())
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) { Text("Camera") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    galleryLauncher.launch("image/*")
                }) { Text("Gallery") }
            }
        )
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
            StocklifyTopBar(
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
                .fillMaxSize()
                .padding(padding)
                .padding(StocklifySpacing.screenPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(StocklifySize.small))

            FormsSection(
                name = form.name,
                price = form.price,
                stock = form.stock,
                selectedCategory = form.selectedCategory,
                categories = categories,
                imageUri = form.imageUri,
                nameError = form.nameError,
                priceError = form.priceError,
                stockError = form.stockError,
                onNameChange = viewModel::onNameChange,
                onPriceChange = viewModel::onPriceChange,
                onStockChange = viewModel::onStockChange,
                onCategorySelected = viewModel::onCategorySelected,
                onImageClick = { showImageSourceDialog = true },
            )

            Spacer(modifier = Modifier.height(StocklifySize.medium))

            StocklifyPrimaryButton(
                text = if (isSaving) "Saving…" else "Save",
                onClick = viewModel::save,
                fullWidth = true,
                enabled = !isSaving,
            )
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGap)
    ) {
        Text(
            text = "Add Product",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = "Add your product details.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FormsSection(
    name: String,
    price: String,
    stock: String,
    selectedCategory: Category?,
    categories: List<Category>,
    imageUri: Uri?,
    nameError: String?,
    priceError: String?,
    stockError: String?,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onImageClick: () -> Unit,
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
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        Text(
            text = "Product Image",
            style = StocklifyTextStyles.subtitleXs,
            color = StocklifyStaticColors.black
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(StocklifyRadius.s))
                .border(
                    width = StocklifyStroke.s,
                    color = StocklifyTheme.colors.transparentPrimary600,
                    shape = RoundedCornerShape(StocklifyRadius.s)
                )
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = "Product image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        StocklifySpacing.columnGap,
                        alignment = Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.store_7),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(StocklifyRadius.m))
                            .size(64.dp)
                            .background(StocklifyTheme.colors.transparentPrimary600)
                    )

                    Text(
                        text = "Add Product Image",
                        style = StocklifyTextStyles.subtitleXs,
                        color = StocklifyTheme.colors.typography500
                    )

                    Text(
                        text = "JPG, PNG up to 3MB",
                        style = StocklifyTextStyles.subtitleXxs,
                        color = StocklifyTheme.colors.typography100
                    )
                }
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
            value = price,
            onValueChange = onPriceChange,
            label = "Price",
            placeholder = "0.00",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = priceError != null,
            supportingText = priceError,
        )

        StocklifyTextField(
            value = stock,
            onValueChange = onStockChange,
            label = "Stock Quantity",
            placeholder = "Enter stock quantity",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = stockError != null,
            supportingText = stockError,
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
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddEditProductScreenPreview() {
    AddEditProductScreen(
        onBack = {},
        onSave = {}
    )
}
