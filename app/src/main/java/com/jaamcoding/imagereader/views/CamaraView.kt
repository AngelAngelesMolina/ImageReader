package com.jaamcoding.imagereader.views

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.jaamcoding.imagereader.R
import com.jaamcoding.imagereader.viewmodel.ScannerViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
fun CamaraView(
    vm: ScannerViewModel
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "${context.packageName}.provider",
        file
    )

    var image: Uri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val defaultImage = R.drawable.ic_photo

    //Verificar permiso de la camara
    val permissionCheckResult =
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        image = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it != null) {
            vm.showToast(context, "Permiso concedido")
            camaraLauncher.launch(uri)
        } else {
            vm.showToast(context, "Permiso denegado")
        }
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            modifier = Modifier
                .clickable {
                    if (permissionCheckResult == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        camaraLauncher.launch(uri)
                    } else { //launch permission request
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                }
                .padding(16.dp, 8.dp),
            painter = rememberAsyncImagePainter(if (image.path?.isNotEmpty() == true) image else defaultImage),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(25.dp))

        val scrollState = rememberScrollState()
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .clickable {
                    clipboard.setText(AnnotatedString(vm.recognizedText))
                    vm.showToast(context, "Texto copiado al portapapeles")
                },
            text = vm.recognizedText,
            textAlign = TextAlign.Center
        ).apply { //execute when data change
            vm.onRecognizeText(image, context)
        }
    }


}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timestamp + "_"
    return File.createTempFile(imageFileName, ".jpg", externalCacheDir)
}