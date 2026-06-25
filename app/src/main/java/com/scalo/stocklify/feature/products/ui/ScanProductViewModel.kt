package com.scalo.stocklify.feature.products.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.feature.products.domain.usecase.RecognizeProductFromImageUseCase
import com.scalo.stocklify.feature.products.model.ScannedProductResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ScanningState {
    object Idle : ScanningState()
    data class Loading(val progress: Float, val message: String) : ScanningState()
    data class Success(val results: List<ScannedProductResult>) : ScanningState()
    data class Error(val message: String) : ScanningState()
}

@HiltViewModel
class ScanProductViewModel @Inject constructor(
    private val recognizeProductFromImage: RecognizeProductFromImageUseCase
) : ViewModel() {

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    private val _scanningState = MutableStateFlow<ScanningState>(ScanningState.Idle)
    val scanningState: StateFlow<ScanningState> = _scanningState.asStateFlow()

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
        analyzeImage(uri)
    }

    private fun analyzeImage(uri: Uri) {
        viewModelScope.launch {
            try {
                _scanningState.value = ScanningState.Loading(0.1f, "Starting scanner...")

                val results = recognizeProductFromImage(
                    uri = uri,
                    onInitializing = {
                        _scanningState.value = ScanningState.Loading(0.3f, "Initializing scanner...")
                    },
                    onRunning = {
                        _scanningState.value = ScanningState.Loading(0.6f, "Analyzing image...")
                    },
                    onExtracting = {
                        _scanningState.value = ScanningState.Loading(0.85f, "Extracting products...")
                    }
                )

                _scanningState.value = ScanningState.Loading(1f, "Done!")
                Log.d(TAG, "=== OCR Scan Results (${results.size} product(s) detected) ===")
                results.forEachIndexed { index, result ->
                    Log.d(TAG, "--- Product ${index + 1} ---")
                    Log.d(TAG, "  Raw text   : ${result.rawText}")
                    Log.d(TAG, "  Name       : ${result.productName}")
                    Log.d(TAG, "  Price      : ${result.price}")
                    Log.d(TAG, "  Category   : ${result.matchedCategory?.name ?: "none"}")
                }
                _scanningState.value = ScanningState.Success(results)
            } catch (e: Exception) {
                Log.e(TAG, "OCR analysis failed", e)
                _scanningState.value = ScanningState.Error(e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        private const val TAG = "ScanProductViewModel"
    }
}
