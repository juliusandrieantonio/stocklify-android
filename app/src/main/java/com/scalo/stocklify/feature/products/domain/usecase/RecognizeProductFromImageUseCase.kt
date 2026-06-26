package com.scalo.stocklify.feature.products.domain.usecase

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.equationl.paddleocr4android.OCR
import com.equationl.paddleocr4android.OcrConfig
import com.equationl.paddleocr4android.CpuPowerMode
import com.equationl.paddleocr4android.bean.OcrResult
import com.equationl.paddleocr4android.callback.OcrInitCallback
import com.equationl.paddleocr4android.callback.OcrRunCallback
import com.scalo.stocklify.core.data.database.dao.CategoryDao
import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.data.database.entity.CategoryEntity
import com.scalo.stocklify.core.data.database.mapper.toDomain
import com.scalo.stocklify.feature.products.domain.extractor.ProductNamePipeline
import com.scalo.stocklify.feature.products.model.ScannedProductResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RecognizeProductFromImageUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao
) {

    private val ocr by lazy { OCR(context) }
    private var initialized = false

    private val config = OcrConfig().apply {
        modelPath = "models/ch_PP-OCRv4"
        clsModelFilename = "cls.nb"
        detModelFilename = "det.nb"
        recModelFilename = "rec.nb"
        isRunDet = true
        isRunCls = true
        isRunRec = true
        cpuPowerMode = CpuPowerMode.LITE_POWER_FULL
        detLongSize = 2040
        scoreThreshold = 0.3f
        isUseOpencl = true
    }

    suspend operator fun invoke(
        uri: Uri,
        onInitializing: () -> Unit = {},
        onRunning: () -> Unit = {},
        onExtracting: () -> Unit = {},
    ): List<ScannedProductResult> {
        onInitializing()
        ensureInitialized()

        onRunning()
        @Suppress("DEPRECATION")
        val rawBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val ocrText = suspendCancellableCoroutine { cont ->
            ocr.run(rawBitmap, object : OcrRunCallback {
                override fun onSuccess(result: OcrResult) { cont.resume(result.simpleText) }
                override fun onFail(e: Throwable) { cont.resumeWithException(e) }
            })
        }

        onExtracting()
        val existingProducts = productDao.getAll().first().map { it.toDomain() }
        val categories = categoryDao.getAll().first()

        Log.d("ScanProductViewModel", ocrText)
        return ocrText
            .lines()
            .filter { it.isNotBlank() }
            .mapNotNull { block ->
                val result = ProductNamePipeline.process(block, existingProducts)
                if (result.name == null) return@mapNotNull null

                ScannedProductResult(
                    rawText = block,
                    productName = result.suggestedProduct?.name ?: result.name,
                    price = result.price,
                    matchedCategory = matchCategory(result.name, categories)?.toDomain(),
                    suggestedProduct = result.suggestedProduct,
                    similarityScore = result.similarityScore
                )
            }
    }

    private suspend fun ensureInitialized() {
        if (initialized) return
        suspendCancellableCoroutine { cont ->
            ocr.initModel(config, object : OcrInitCallback {
                override fun onSuccess() {
                    initialized = true
                    cont.resume(Unit)
                }
                override fun onFail(e: Throwable) { cont.resumeWithException(e) }
            })
        }
    }

    private fun matchCategory(text: String, categories: List<CategoryEntity>): CategoryEntity? {
        val lowerText = text.lowercase()
        return categories
            .filter { lowerText.contains(it.name.lowercase()) }
            .maxByOrNull { it.name.length }
    }
}
