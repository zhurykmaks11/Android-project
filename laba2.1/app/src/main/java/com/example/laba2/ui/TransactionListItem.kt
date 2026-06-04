package com.example.laba2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.shadow
import com.example.laba2.model.Expense
import com.example.laba2.model.Transaction

@Composable
fun TransactionListItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(text = "Витрата: ${transaction.amount} грн")
            Text(text = "Категорія: ${(transaction as Expense).category}")
        }
    }
}