package com.jaamcoding.imagereader.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

class ScannerViewModel : ViewModel() {

    var recognizedText by mutableStateOf("")
        private set

    fun cleanText() {
        recognizedText = ""
    }

    fun onRecognizeText(text: Any?, context: Context) {
        var image: InputImage? = null
        try {
            image = InputImage.fromFilePath(context, text as android.net.Uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        image?.let {
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS).process(it)
                .addOnSuccessListener { visionText ->
                    recognizedText = visionText.text
                }
                .addOnFailureListener {
                    showToast(context, "Error al reconocer el texto")
                }
        }

    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}