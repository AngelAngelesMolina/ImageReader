package com.jaamcoding.imagereader.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jaamcoding.imagereader.ui.theme.ImageReaderTheme
import com.jaamcoding.imagereader.viewmodel.ScannerViewModel

@Composable
fun TabsView(vm: ScannerViewModel, modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Galeria", "Camara", "Coleccion")


    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = selectedTab,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab])
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }, // Asegúrate que `selectedTab` sea mutable
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = title,
                        color = Color.Black
                    )
                }
            }

        }
        when (selectedTab) {
            0 -> GalleryView(vm).apply { vm.cleanText() }
            1 -> CamaraView(vm).apply { vm.cleanText() }
            2 -> CollectionGalleryView()
        }
    }

}

@Preview
@Composable
private fun TabsViewPreview() {
    ImageReaderTheme {
        TabsView(ScannerViewModel())
    }
}