package com.jaamcoding.imagereader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jaamcoding.imagereader.ui.theme.ImageReaderTheme
import com.jaamcoding.imagereader.viewmodel.ScannerViewModel
import com.jaamcoding.imagereader.views.TabsView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel = ScannerViewModel()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageReaderTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { padding ->
                    TabsView(viewModel, modifier = Modifier.padding(padding))
                }
            }
        }
    }
}
