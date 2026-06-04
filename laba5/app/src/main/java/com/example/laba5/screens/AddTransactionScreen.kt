package com.example.laba5.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.laba5.model.Category
import com.example.laba5.model.Type
import com.example.laba5.viewmodel.ListViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AddTransactionScreen(
    navController: NavHostController,
    viewModel: ListViewModel
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    val activity = LocalContext.current as? android.app.Activity
    val windowSize = if (activity != null) calculateWindowSizeClass(activity) else null
    val isTablet = windowSize?.widthSizeClass == WindowWidthSizeClass.Expanded

    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var isCategoryExpanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(Type.EXPENSE) }
    var isFavorite by remember { mutableStateOf(false) }
    var priority by remember { mutableStateOf(1f) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }

    // --- ЛОГІКА КАМЕРИ ---
    var imagePath by remember { mutableStateOf<String?>(null) }
    var tempFileUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        // Якщо фото успішно зроблено, зберігаємо шлях (Uri вже прив'язаний до файлу)
        if (!success) {
            imagePath = null // Скидаємо, якщо скасували
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // Створюємо унікальний файл для нового фото
            val file = File(context.filesDir, "photo_${System.currentTimeMillis()}.jpg")
            imagePath = file.absolutePath
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            tempFileUri = uri
            cameraLauncher.launch(uri)
        }
    }

    val focusRequesterAmount = remember { FocusRequester() }

    val isValid by remember {
        derivedStateOf {
            nameError == null && amountError == null && categoryError == null &&
                    name.isNotBlank() && amount.isNotBlank() && selectedCategory != null
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Додати транзакцію") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = if (isTablet) 600.dp else Dp.Infinity)
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .verticalScroll(scrollState)
                    .imePadding()
            ) {

                Text("Основна інформація", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (nameError != null) nameError = if (it.length < 3) "Мінімум 3 символи" else null
                    },
                    label = { Text("Назва") },
                    isError = nameError != null,
                    supportingText = { nameError?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusRequesterAmount.requestFocus() }),
                    modifier = Modifier.fillMaxWidth().onFocusChanged { state ->
                        if (!state.isFocused && name.isNotEmpty()) {
                            nameError = if (name.length < 3) "Мінімум 3 символи" else null
                        }
                    }
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        if (amountError != null) {
                            val value = it.toDoubleOrNull()
                            amountError = if (value == null || value <= 0) "Сума має бути > 0" else null
                        }
                    },
                    label = { Text("Сума") },
                    isError = amountError != null,
                    supportingText = { amountError?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }),
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequesterAmount).onFocusChanged { state ->
                        if (!state.isFocused && amount.isNotEmpty()) {
                            val value = amount.toDoubleOrNull()
                            amountError = if (value == null || value <= 0) "Сума має бути > 0" else null
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text("Додаткові параметри", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = isCategoryExpanded,
                    onExpandedChange = { isCategoryExpanded = !isCategoryExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "Оберіть категорію",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Категорія") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        isError = categoryError != null,
                        supportingText = { categoryError?.let { Text(it) } },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isCategoryExpanded,
                        onDismissRequest = { isCategoryExpanded = false }
                    ) {
                        Category.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category
                                    categoryError = null
                                    isCategoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedType == Type.EXPENSE,
                        onClick = { selectedType = Type.EXPENSE },
                        label = { Text("Витрата") }
                    )
                    FilterChip(
                        selected = selectedType == Type.INCOME,
                        onClick = { selectedType = Type.INCOME },
                        label = { Text("Дохід") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- КНОПКА КАМЕРИ ТА ПРЕВ'Ю ФОТО ---
                Button(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            val file = File(context.filesDir, "photo_${System.currentTimeMillis()}.jpg")
                            imagePath = file.absolutePath
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                            tempFileUri = uri
                            cameraLauncher.launch(uri)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(if (imagePath != null) "📸 Перезняти фото" else "📸 Додати фото чеку")
                }

                if (imagePath != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(File(imagePath!!)),
                        contentDescription = "Зроблене фото",
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                }
                // ------------------------------------

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (selectedCategory == null) {
                            categoryError = "Обов'язково оберіть категорію"
                            return@Button
                        }

                        // ПЕРЕДАЄМО imagePath У VIEWMODEL
                        viewModel.addTransaction(
                            amount.toDouble(),
                            selectedType,
                            selectedCategory!!,
                            imagePath
                        )
                        navController.popBackStack()
                    },
                    enabled = isValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Зберегти")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}