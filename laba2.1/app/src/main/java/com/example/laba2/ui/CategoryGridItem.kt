package com.example.laba2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import com.example.laba2.model.Category

@Composable
fun CategoryGridItem(category: Category) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(120.dp)
            .background(Color.Cyan),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(category.name)
            Text("Категорія")
        }
    }
}