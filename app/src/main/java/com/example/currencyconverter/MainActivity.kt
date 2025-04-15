package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.*

class MainActivity : ComponentActivity() {

    private val usdToIdrRate = 16784.1
    private val idrToUsdRate = 1 / usdToIdrRate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrencyConverter()
                }
            }
        }
    }

    @Composable
    fun CurrencyConverter() {
        var input by remember { mutableStateOf("") }
        var selectedConversion by remember { mutableStateOf("USD to IDR") }
        var result by remember { mutableStateOf("") }

        val options = listOf("USD to IDR", "IDR to USD")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Currency Converter",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Enter amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropdownMenuBox(
                options = options,
                selectedOption = selectedConversion,
                onOptionSelected = { selectedConversion = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val amount = input.toDoubleOrNull()
                    result = if (amount != null) {
                        when (selectedConversion) {
                            "USD to IDR" -> {
                                val formatted = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(amount * usdToIdrRate)
                                "$formatted"
                            }
                            "IDR to USD" -> {
                                val formatted = NumberFormat.getCurrencyInstance(Locale.US).format(amount * idrToUsdRate)
                                "$formatted"
                            }
                            else -> "Invalid conversion"
                        }
                    } else {
                        "Invalid input"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Convert")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = result,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium),
                color = Color.DarkGray
            )
        }
    }

    @Composable
    fun DropdownMenuBox(
        options: List<String>,
        selectedOption: String,
        onOptionSelected: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedOption)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}