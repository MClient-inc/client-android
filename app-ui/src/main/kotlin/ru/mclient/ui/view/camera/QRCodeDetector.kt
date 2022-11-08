package ru.mclient.ui.view.camera

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@Composable
fun rememberImageRecognizer(
    onRecognize: (String) -> Unit = {},
): ImageRecognizer {
    val recognizer = remember { MLKitImageRecognizer(onRecognize) }
    DisposableEffect(key1 = recognizer, effect = {
        recognizer.setEnabled(false)
        onDispose {
            recognizer.disable()
        }
    })

    return recognizer
}

sealed interface ImageRecognizer {

    val isEnabled: Boolean

    fun setEnabled(value: Boolean)

}

internal class MLKitImageRecognizer(
    private val onRecognize: (String) -> Unit,
) : ImageRecognizer {

    private val mlKit = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    val analysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .apply { setAnalyzer(Dispatchers.IO.asExecutor(), ::analyze) }


    @SuppressLint("UnsafeOptInUsageError")
    private fun analyze(proxy: ImageProxy) {
        if (isEnabled)
            proxy.image?.let { it ->
                val image =
                    InputImage.fromMediaImage(it, proxy.imageInfo.rotationDegrees)
                mlKit.process(image).addOnSuccessListener {
                    produceBarcodes(it)
                }.addOnCompleteListener {
                    proxy.close()
                }
            }
    }

    private fun produceBarcodes(barcodes: List<Barcode>) {
        if (!isEnabled)
            return
        val barcode = barcodes.firstOrNull() ?: return
        val barcodeValue = barcode.rawValue ?: return
        onRecognize.invoke(barcodeValue)
    }

    private val _isEnabled = mutableStateOf(false)
    override val isEnabled by _isEnabled


    override fun setEnabled(value: Boolean) {
        _isEnabled.value = value
    }

    internal fun disable() {
        setEnabled(false)
        mlKit.close()
    }

}