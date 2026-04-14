package com.example.laba5.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laba5.model.DetailState
import com.example.laba5.viewmodel.DetailViewModel
import com.example.laba5.viewmodel.DetailViewModelFactory
@Composable
fun DetailScreen(id: Int) {

    val viewModel: DetailViewModel = viewModel(
        factory = DetailViewModelFactory(id)
    )

    val state by viewModel.state.collectAsState()

    when (state) {
        is DetailState.Loading -> CircularProgressIndicator()

        is DetailState.Success -> {
            val item = (state as DetailState.Success).transaction

            Column {
                Spacer(modifier = Modifier.height(55.dp))
                Text(
                    "Сума: ${item.amount}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text("Тип: ${item.type}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text("Категорія: ${item.category}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        is DetailState.Error -> {
            Text("❌ Елемент не знайдено")
        }
    }
}