package com.example.laba2.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.laba2.data.categories
import com.example.laba2.ui.CategoryGridItem

@Composable
fun CategoryGridScreen() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp)
    ) {
        items(categories.toList()) { category ->
            CategoryGridItem(category)
        }
    }
}