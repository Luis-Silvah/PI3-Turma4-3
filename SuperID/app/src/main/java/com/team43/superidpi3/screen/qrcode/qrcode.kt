package com.team43.superidpi3.screen.qrcode



import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.lifecycle.LifecycleOwner
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.team43.superidpi3.ui.theme.SuperIDPI3Theme
import java.io.File
import androidx.camera.core.ImageAnalysis
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import androidx.camera.core.ImageProxy
import androidx.compose.ui.unit.dp
import androidx.camera.core.ExperimentalGetImage
import androidx.lifecycle.compose.LocalLifecycleOwner


// Marque a função como opt-in para usar a API experimental
class QrCodeAnalyzer(
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    // Marque a função analyze com @OptIn
    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    @OptIn(ExperimentalGetImage::class)  // Marca a função como opt-in para o uso experimental da CameraX
    override fun analyze(imageProxy: ImageProxy) {
        // Acesso à imagem
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()  // Fecha o ImageProxy se não houver imagem
            return
        }

        // Cria o InputImage a partir da imagem da câmera
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        // Processa a imagem para tentar identificar códigos de barras ou QR codes
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                // Se um código de barras for detectado
                for (barcode in barcodes) {
                    barcode.rawValue?.let {
                        // Envia o valor do QR code encontrado
                        onQrCodeScanned(it)
                    }
                }
            }
            .addOnFailureListener {
                // Caso haja falha no processamento
            }
            .addOnCompleteListener {
                // Fecha o ImageProxy para liberar recursos
                imageProxy.close()
            }
    }
}




@Composable
fun PermissionRequiredScreen(modifier: Modifier = Modifier, permission: String, onPermissionGranted: () -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            onPermissionGranted()
        }
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



@Composable
fun WithPermission(
    modifier: Modifier = Modifier,
    permission: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    var permissionGranted by remember {
        mutableStateOf (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
    }

    if (!permissionGranted) {
        PermissionRequiredScreen(modifier = modifier, permission = permission) { permissionGranted = true }
    }
    else {
        Surface(modifier = modifier) {
            content()
        }
    }
}

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            SuperIDPI3Theme {
//                val navController = rememberNavController()
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    WithPermission(
//                        modifier = Modifier.padding(innerPadding),
//                        permission = Manifest.permission.CAMERA
//                    ) {
//                        CameraAppScreen(navController)
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun CameraAppScreen(navController: NavHostController) {
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var zoomLevel by remember { mutableFloatStateOf(0.0f) }
    val localContext = LocalContext.current
    var qrCodeResult by remember { mutableStateOf<String?>(null) }

    Box {
        CameraPreview(
            lensFacing = lensFacing,
            zoomLevel = zoomLevel,
            onQrCodeScanned = { result ->
                qrCodeResult = result
            }
        )


        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            Row {
                Button(onClick = { lensFacing = CameraSelector.LENS_FACING_BACK }) {
                    Text("Back camera")
                }
            }
        }

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
    val previewUseCase = remember { androidx.camera.core.Preview.Builder().build() }
    val analysisUseCase = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val localContext = LocalContext.current
//
//    fun rebindCameraProvider() {
//        cameraProvider?.let { provider ->
//            val selector = CameraSelector.Builder()
//                .requireLensFacing(lensFacing)
//                .build()
//            provider.unbindAll()
//            val camera = provider.bindToLifecycle(
//                localContext as LifecycleOwner,
//                selector,
//                previewUseCase,
//                analysisUseCase
//            )
//            cameraControl = camera.cameraControl
//        }
//    }

    fun rebindCameraProvider() {
        cameraProvider?.let { provider ->
            val selector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
            provider.unbindAll()
            val camera = provider.bindToLifecycle(
                lifecycleOwner,
                selector,
                previewUseCase,
                analysisUseCase
            )
            cameraControl = camera.cameraControl
        }
    }

    LaunchedEffect(Unit) {
        cameraProvider = ProcessCameraProvider.awaitInstance(localContext)
        analysisUseCase.setAnalyzer(
            ContextCompat.getMainExecutor(localContext),
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
        factory = { context ->
            PreviewView(context).also {
                previewUseCase.surfaceProvider = it.surfaceProvider
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    SuperIDPI3Theme {
        val navController = rememberNavController()
        CameraAppScreen(navController)
    }
}

fun Uri.shareAsImage(context: Context) {
    val contentUri = FileProvider.getUriForFile(context, "com.tdcolvin.cameraxworkshop.fileprovider", toFile())
    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, contentUri)
        type = "image/jpeg"
    }
    context.startActivity(Intent.createChooser(shareIntent, null))
}