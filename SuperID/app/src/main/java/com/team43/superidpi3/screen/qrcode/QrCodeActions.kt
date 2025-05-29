package com.team43.superidpi3.screen.qrcode

import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
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

class QrCodeActions {
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

    fun updateLoginDocument(loginToken: String) {
        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser

        if (currentUser == null) {
            Log.d("FIRESTORE", "Usuário não autenticado no aplicativo.")
            return
        }

        val userUid = currentUser.uid
        val loginDocRef = db.collection("login").document(loginToken)

        val updates = hashMapOf<String, Any>(
            "user" to userUid,
            "loggedInAt" to FieldValue.serverTimestamp()
        )

        loginDocRef.update(updates)
            .addOnSuccessListener {
                Log.d("FIRESTORE", "Documento de login atualizado com sucesso para token: $loginToken com UID: $userUid")
                Log.d("SITE-PARCEIRO", "Login realizado com sucesso via QR Code!")
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Erro ao atualizar documento de login: ${e.message}", e)
                Log.d("SITE-PARCEIRO", "Erro ao finalizar login via QR Code!")
            }
    }

}
