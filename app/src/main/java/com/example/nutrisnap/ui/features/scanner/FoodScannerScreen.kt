package com.example.nutrisnap.ui.features.scanner

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nutrisnap.BuildConfig
import com.example.nutrisnap.data.repository.FoodScannerRepository
import com.example.nutrisnap.data.remote.PredictionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID


private fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap): String {
    val file = File(context.filesDir, "meal_${UUID.randomUUID()}.jpg")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return Uri.fromFile(file).toString()
}

private suspend fun copyUriToInternalStorage(context: Context, uri: Uri): String =
    withContext(Dispatchers.IO) {
        try {
            val file = File(context.filesDir, "meal_${UUID.randomUUID()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(file).toString()
        } catch (e: Exception) {
            uri.toString()
        }
    }


@Composable
fun FoodScannerScreen(
    modifier: Modifier = Modifier,
    onResultStateChanged: (Boolean) -> Unit = {},
    onConfirm: (PredictionResponse, String) -> Unit = { _, _ -> },
    onCloseScanner: () -> Unit = {}
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val repository = remember {
        FoodScannerRepository(
            context = context,
            usdaApiKey = BuildConfig.USDA_API_KEY
        )
    }

    // Selected gallery image
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Captured camera image
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    // API states
    var isLoading by remember { mutableStateOf(false) }
    var predictionResult by remember { mutableStateOf<PredictionResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDetailsDialog by remember { mutableStateOf(false) }

    // Notify MainScreen whether we are on the Results page (so it can show/hide BottomNavBar)
    LaunchedEffect(predictionResult) {
        onResultStateChanged(predictionResult != null)
    }

    // Gallery picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            capturedImage = null
            predictionResult = null
            errorMessage = null
        }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedImage = bitmap
            selectedImageUri = null
            predictionResult = null
            errorMessage = null
        }
    }

    val hasImage = selectedImageUri != null || capturedImage != null

    val analyzeFood: () -> Unit = {
        scope.launch {
            isLoading = true
            predictionResult = null
            errorMessage = null

            try {
                predictionResult = repository.analyzeFood(
                    context = context,
                    imageUri = selectedImageUri,
                    bitmap = capturedImage
                )
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Something went wrong"
            } finally {
                isLoading = false
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (predictionResult == null) {
            ScanPageContent(
                selectedImageUri = selectedImageUri,
                capturedImage = capturedImage,
                hasImage = hasImage,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onAnalyzeClick = analyzeFood,
                onCameraClick = { cameraLauncher.launch(null) },
                onGalleryClick = {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onCloseClick = {
                    if (hasImage) {
                        selectedImageUri = null
                        capturedImage = null
                        errorMessage = null
                    } else {
                        onCloseScanner()
                    }
                }
            )
        } else {
            ScannedResultsContent(
                result = predictionResult!!,
                selectedImageUri = selectedImageUri,
                capturedImage = capturedImage,
                onCloseClick = { predictionResult = null },
                onInfoClick = { showDetailsDialog = true },
                onUpdateDetailsClick = {
                    predictionResult = null
                    errorMessage = null
                },
                onConfirmClick = {
                    val confirmedResult = predictionResult ?: return@ScannedResultsContent
                    val uriSnapshot = selectedImageUri
                    val bitmapSnapshot = capturedImage

                    scope.launch {
                        val finalImageUri = when {
                            uriSnapshot != null -> copyUriToInternalStorage(context, uriSnapshot)
                            bitmapSnapshot != null -> saveBitmapToInternalStorage(context, bitmapSnapshot)
                            else -> ""
                        }

                        predictionResult = null
                        selectedImageUri = null
                        capturedImage = null
                        onConfirm(confirmedResult, finalImageUri)
                    }
                },
                modifier = modifier
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(color = OrangeAccent)
                        Text(
                            text = "Identifying your food...",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        if (showDetailsDialog && predictionResult != null) {
            PredictionDetailsDialog(
                result = predictionResult!!,
                onDismiss = { showDetailsDialog = false }
            )
        }
    }
}