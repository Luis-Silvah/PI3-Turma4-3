package com.team43.superidpi3.screen.qrcode

import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class QrCodeAnalyzer(
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let {
                        onQrCodeScanned(it)
                    }
                }
            }
            .addOnFailureListener { /* log or handle error */ }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}

object QrCodeActions {
    fun rebindCameraProvider(
        provider: ProcessCameraProvider,
        lifecycleOwner: LifecycleOwner,
        lensFacing: Int,
        preview: Preview,
        analysis: ImageAnalysis
    ): CameraControl {
        val selector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        provider.unbindAll()
        val camera = provider.bindToLifecycle(lifecycleOwner, selector, preview, analysis)
        return camera.cameraControl
    }

}
