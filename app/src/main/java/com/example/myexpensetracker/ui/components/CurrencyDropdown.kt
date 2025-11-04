package com.example.myexpensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myexpensetracker.ui.theme.PrimaryBlue
import com.example.myexpensetracker.ui.theme.TextGray

data class CurrencyOption(
    val code: String,
    val name: String,
    val symbol: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropdown(
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currencies = listOf(
        CurrencyOption("USD", "United States Dollar", "$"),
        CurrencyOption("EUR", "Euro", "€"),
        CurrencyOption("GBP", "British Pound", "£"),
        CurrencyOption("INR", "Indian Rupee", "₹")
    )

    var expanded by remember { mutableStateOf(false) }
    val selectedOption = currencies.find { it.code == selectedCurrency } ?: currencies[0]

    Column(modifier = modifier) {
        // Label
        Text(
            text = "Currency",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Dropdown Button
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = "${selectedOption.code} - ${selectedOption.name}",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                leadingIcon = {
                    Text(
                        text = selectedOption.symbol,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextGray
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown",
                        tint = TextGray
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = currency.symbol,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${currency.code} - ${currency.name}",
                                    fontSize = 14.sp
                                )
                            }
                        },
                        onClick = {
                            onCurrencySelected(currency.code)
                            expanded = false
                        },
                        modifier = Modifier.background(
                            if (currency.code == selectedCurrency)
                                PrimaryBlue.copy(alpha = 0.1f)
                            else Color.White
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyDropdownPreview() {
    var selected by remember { mutableStateOf("USD") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CurrencyDropdown(
            selectedCurrency = selected,
            onCurrencySelected = { selected = it }
        )
    }
}

