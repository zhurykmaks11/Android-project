package com.example.laba5

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddTransactionUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testAddTransactionFlow() {
        composeTestRule.setContent {
            var amountText by remember { mutableStateOf("") }
            var isSaved by remember { mutableStateOf(false) }

            if (isSaved) {
                Text("Транзакцію успішно збережено!")
            } else {
                Column {
                    Text("Нова транзакція")
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("Сума") }
                    )
                    Button(onClick = { isSaved = true }) {
                        Text("Зберегти")
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("Нова транзакція").assertIsDisplayed()

        composeTestRule.onNodeWithText("Сума").performTextInput("150")

        composeTestRule.onNodeWithText("Зберегти").performClick()

        composeTestRule.onNodeWithText("Транзакцію успішно збережено!").assertIsDisplayed()
    }
}