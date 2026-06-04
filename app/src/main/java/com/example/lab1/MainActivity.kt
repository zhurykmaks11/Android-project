package com.example.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                FinanceScreen()
            }
        }
    }
}

@Composable
fun FinanceScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xE67CD5CB))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Фінансовий планувальник",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Box(
            modifier = Modifier
                .width(300.dp)
                .height(150.dp)
                .shadow(8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Баланс: 12000 ₴", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFC8E6C9)),
                contentAlignment = Alignment.Center
            ) {
                Text("Доходи\n15000 ₴")
            }

            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFCDD2)),
                contentAlignment = Alignment.Center
            ) {
                Text("Витрати\n3000 ₴")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.wallet),
            contentDescription = "Finance image",
            modifier = Modifier.size(120.dp)
        )
    }
}