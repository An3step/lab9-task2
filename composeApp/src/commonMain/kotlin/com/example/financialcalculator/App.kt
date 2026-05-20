package com.example.financialcalculator

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun App() {
    var language by remember { mutableStateOf(Language.RU) }
    val strings = getStrings(language)
    
    var initialAmount by remember { mutableStateOf("1000") }
    var annualRate by remember { mutableStateOf("5") }
    var years by remember { mutableStateOf("5") }
    var frequency by remember { mutableStateOf(CapitalizationFrequency.MONTHLY) }
    
    var result by remember { mutableStateOf<CalculationResult?>(null) }
    var history by remember { mutableStateOf(listOf<CalculationResult>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    LanguageSwitcher(language) { language = it }
                }

                Text(strings.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                FinancialInputFields(
                    strings = strings,
                    initialAmount = initialAmount,
                    onAmountChange = { initialAmount = it },
                    annualRate = annualRate,
                    onRateChange = { annualRate = it },
                    years = years,
                    onYearsChange = { years = it },
                    frequency = frequency,
                    onFrequencyChange = { frequency = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                val buttonElevation = if (currentPlatform == PlatformType.IOS) 0.dp else 4.dp
                Button(
                    onClick = {
                        val validationError = CalculatorLogic.validateInput(initialAmount, annualRate, years)
                        if (validationError == null) {
                            try {
                                val res = CalculatorLogic.calculate(
                                    initialAmount.toDouble(),
                                    annualRate.toDouble(),
                                    years.toInt(),
                                    frequency
                                )
                                result = res
                                history = history + res
                                errorMessage = null
                            } catch (e: Exception) {
                                errorMessage = "Calculation error"
                            }
                        } else {
                            errorMessage = validationError
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation)
                ) {
                    Text(strings.calculate)
                }

                if (errorMessage != null) {
                    Text(errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                }

                AnimatedVisibility(
                    visible = result != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    val res = result
                    if (res != null) {
                        Column {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text("${strings.totalAmount}: ${res.totalAmount.format(2)}")
                            Text("${strings.totalProfit}: ${res.totalProfit.format(2)}")
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            GrowthChart(res.growthData, modifier = Modifier.height(200.dp).fillMaxWidth())
                        }
                    }
                }
                
                if (history.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("History:", style = MaterialTheme.typography.titleMedium)
                    val lastHistory = history.takeLast(5).reversed()
                    for (h in lastHistory) {
                        Text("Amount: ${h.totalAmount.format(2)} | Profit: ${h.totalProfit.format(2)}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageSwitcher(current: Language, onSelected: (Language) -> Unit) {
    Row {
        for (lang in Language.entries) {
            val color = if (current == lang) MaterialTheme.colorScheme.primary else Color.Gray
            TextButton(onClick = { onSelected(lang) }) {
                Text(lang.name, color = color)
            }
        }
    }
}

@Composable
fun FinancialInputFields(
    strings: AppStrings,
    initialAmount: String,
    onAmountChange: (String) -> Unit,
    annualRate: String,
    onRateChange: (String) -> Unit,
    years: String,
    onYearsChange: (String) -> Unit,
    frequency: CapitalizationFrequency,
    onFrequencyChange: (CapitalizationFrequency) -> Unit
) {
    OutlinedTextField(
        value = initialAmount,
        onValueChange = onAmountChange,
        label = { Text(strings.initialAmount) },
        modifier = Modifier.fillMaxWidth()
    )
    
    Spacer(modifier = Modifier.height(8.dp))
    PlatformSpecificRateInput(strings, annualRate, onRateChange)
    
    Spacer(modifier = Modifier.height(8.dp))
    PlatformSpecificYearsInput(strings, years, onYearsChange)

    Spacer(modifier = Modifier.height(8.dp))
    Text(strings.capitalization)
    CapitalizationPicker(strings, frequency, onFrequencyChange)
}

@Composable
fun PlatformSpecificRateInput(strings: AppStrings, rate: String, onRateChange: (String) -> Unit) {
    when (currentPlatform) {
        PlatformType.ANDROID -> {
            Column {
                Text("${strings.annualRate}: $rate%")
                Slider(
                    value = rate.toFloatOrNull() ?: 0f,
                    onValueChange = { onRateChange(it.toInt().toString()) },
                    valueRange = 0f..30f,
                    steps = 30
                )
            }
        }
        PlatformType.IOS -> {
            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("${strings.annualRate}: $rate%")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    for (i in 1..20) {
                        DropdownMenuItem(
                            text = { Text("$i%") },
                            onClick = { onRateChange(i.toString()); expanded = false }
                        )
                    }
                }
            }
        }
        else -> {
            OutlinedTextField(
                value = rate,
                onValueChange = onRateChange,
                label = { Text(strings.annualRate) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PlatformSpecificYearsInput(strings: AppStrings, years: String, onYearsChange: (String) -> Unit) {
    if (currentPlatform == PlatformType.WINDOWS) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(strings.periodYears, modifier = Modifier.weight(1f))
            IconButton(onClick = { 
                val y = years.toIntOrNull() ?: 0
                if (y > 1) onYearsChange((y - 1).toString())
            }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Dec")
            }
            Text(years, modifier = Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.bodyLarge)
            IconButton(onClick = { 
                val y = years.toIntOrNull() ?: 0
                onYearsChange((y + 1).toString())
            }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Inc")
            }
        }
    } else {
        OutlinedTextField(
            value = years,
            onValueChange = onYearsChange,
            label = { Text(strings.periodYears) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CapitalizationPicker(
    strings: AppStrings,
    current: CapitalizationFrequency,
    onSelected: (CapitalizationFrequency) -> Unit
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        for (freq in CapitalizationFrequency.entries) {
            val label = when(freq) {
                CapitalizationFrequency.MONTHLY -> strings.monthly
                CapitalizationFrequency.QUARTERLY -> strings.quarterly
                CapitalizationFrequency.YEARLY -> strings.yearly
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = current == freq, onClick = { onSelected(freq) })
                Text(label, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun GrowthChart(data: List<Pair<Int, Double>>, modifier: Modifier = Modifier) {
    if (data.isEmpty()) return
    
    val maxVal = data.maxOf { it.second }.toFloat()
    val minVal = data.minOf { it.second }.toFloat()
    val range = maxVal - minVal
    
    val animationProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1500)
    )

    var selectedPointIndex by remember { mutableStateOf(-1) }
    val primaryColor = MaterialTheme.colorScheme.primary
    
    Column {
        if (selectedPointIndex != -1 && (currentPlatform == PlatformType.WEB || currentPlatform == PlatformType.WINDOWS)) {
            val pt = data[selectedPointIndex]
            Text("Year: ${pt.first}, Amount: ${pt.second.format(2)}", style = MaterialTheme.typography.labelSmall)
        }
        
        Canvas(modifier = modifier.pointerInput(data) {
            detectTapGestures { offset ->
                if (currentPlatform == PlatformType.WEB || currentPlatform == PlatformType.WINDOWS) {
                    val width = size.width
                    val spacing = width / (data.size - 1)
                    val index = (offset.x / spacing + 0.5f).toInt().coerceIn(0, data.size - 1)
                    selectedPointIndex = index
                }
            }
        }) {
            val width = size.width
            val height = size.height
            val spacing = width / (data.size - 1)
            
            val points = data.mapIndexed { index, pair ->
                val x = index * spacing
                val y = height - ((pair.second.toFloat() - minVal) / max(1f, range) * height * 0.8f + height * 0.1f)
                Offset(x, y)
            }

            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                val currentPointProgress = (animationProgress * points.size).toInt()
                for (i in 1 until points.size) {
                    if (i <= currentPointProgress) {
                        lineTo(points[i].x, points[i].y)
                    }
                }
            }

            if (currentPlatform == PlatformType.ANDROID) {
                val fillPath = Path().apply {
                    addPath(path)
                    if (points.size > 1) {
                        val lastIdx = ((points.size - 1) * animationProgress).toInt().coerceIn(0, points.size - 1)
                        lineTo(points[lastIdx].x, height)
                        lineTo(points.first().x, height)
                    }
                    close()
                }
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryColor.copy(alpha = 0.3f), Color.Transparent),
                        startY = points.minOf { it.y },
                        endY = height
                    )
                )
            }

            val strokeWidth = if (currentPlatform == PlatformType.IOS) 2f else 4f
            drawPath(path = path, color = primaryColor, style = Stroke(width = strokeWidth))
            
            if (currentPlatform == PlatformType.WINDOWS || currentPlatform == PlatformType.WEB) {
                for (i in points.indices) {
                    val color = if (i == selectedPointIndex) Color.Blue else Color.Red
                    val radius = if (i == selectedPointIndex) 6f else 3f
                    drawCircle(color = color, radius = radius, center = points[i])
                }
            }
        }
    }
}
