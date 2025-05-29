package com.team43.superidpi3.screen.qrcode

import android.content.pm.PackageManager
import androidx.camera.core.CameraControl
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.camera.core.CameraSelector
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView

@Composable
fun CameraAppScreen() {
    val lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    val zoomLevel by remember { mutableFloatStateOf(0.0f) }
    var qrCodeResult by remember { mutableStateOf<String?>(null) }

    Box {
        CameraPreview(
            lensFacing = lensFacing,
            zoomLevel = zoomLevel,
            onQrCodeScanned = { result ->
                qrCodeResult = result
            }
        )

        qrCodeResult?.let {
            Text(
                text = "QR Code: $it",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lensFacing: Int,
    zoomLevel: Float,
    onQrCodeScanned: (String) -> Unit
) {
    val previewUseCase = remember { Preview.Builder().build() }
    val analysisUseCase = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    fun rebindCameraProvider() {
        cameraProvider?.let { provider ->
            cameraControl = QrCodeActions.rebindCameraProvider(
                provider,
                lifecycleOwner,
                lensFacing,
                previewUseCase,
                analysisUseCase
            )
        }
    }

    LaunchedEffect(Unit) {
        cameraProvider = ProcessCameraProvider.getInstance(context).get()
        analysisUseCase.setAnalyzer(
            ContextCompat.getMainExecutor(context),
            QrCodeAnalyzer(onQrCodeScanned)
        )
        rebindCameraProvider()
    }

    LaunchedEffect(lensFacing) {
        rebindCameraProvider()
    }

    LaunchedEffect(zoomLevel) {
        cameraControl?.setLinearZoom(zoomLevel)
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            PreviewView(ctx).also {
                previewUseCase.surfaceProvider = it.surfaceProvider
            }
        }
    )
}

@Composable
fun WithPermission(
    modifier: Modifier = Modifier,
    permission: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    var permissionGranted by remember {
        mutableStateOf(checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
    }

    if (!permissionGranted) {
        PermissionRequiredScreen(modifier = modifier, permission = permission) { permissionGranted = true }
    } else {
        Surface(modifier = modifier) {
            content()
        }
    }
}

@Composable
fun PermissionRequiredScreen(modifier: Modifier = Modifier, permission: String, onPermissionGranted: () -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) onPermissionGranted()
    }

    Box(modifier = modifier.fillMaxSize()) {
        Button(
            modifier = modifier.align(Alignment.Center),
            onClick = { launcher.launch(permission) }
        ) {
            Text("Grant camera permission")
        }
    }
}
