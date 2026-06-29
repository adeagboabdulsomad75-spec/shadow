package com.example.shadow

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shadow.ui.theme.*

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = viewModel()) {
    val displayValue by viewModel.display
    val isScientificMode by viewModel.isScientificMode
    val scrollState = rememberScrollState()

    LaunchedEffect(displayValue) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Display Area
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp),
            color = DisplayBackground,
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = displayValue,
                    fontSize = if (displayValue.length > 10) 40.sp else 64.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    color = White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                )
            }
        }

        // Scientific Mode Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Scientific Mode",
                color = ScientificTextColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Switch(
                checked = isScientificMode,
                onCheckedChange = { viewModel.toggleScientificMode() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = OperatorButtonColor,
                    checkedTrackColor = OperatorButtonColor.copy(alpha = 0.5f),
                    uncheckedThumbColor = White,
                    uncheckedTrackColor = NumberButtonColor
                )
            )
        }

        // Keypad
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Scientific Panel
            AnimatedVisibility(
                visible = isScientificMode,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val sciButtons = listOf(
                        listOf("sin", "cos", "tan", "log", "ln"),
                        listOf("√", "x²", "xʸ", "π", "e"),
                        listOf("%", "!", "1/x", "(", ")")
                    )
                    sciButtons.forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { text ->
                                CalculatorButton(
                                    text = text,
                                    modifier = Modifier.weight(1f),
                                    containerColor = NumberButtonColor,
                                    contentColor = ScientificTextColor,
                                    fontSize = 16.sp,
                                    onClick = { 
                                        if (text == "xʸ") viewModel.onOperationClick("^")
                                        else if (text == "(" || text == ")") { /* Not implemented yet */ }
                                        else viewModel.onScientificClick(text) 
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Standard Panel
            val standardButtons = listOf(
                listOf("C", "(", ")", "÷"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf(".", "0", "⌫", "=")
            )

            standardButtons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { text ->
                        val isOperator = text in listOf("÷", "×", "-", "+", "(", ")")
                        val isAction = text in listOf("C", "=", "⌫")
                        
                        val containerColor = when {
                            isAction -> ActionButtonColor
                            isOperator -> OperatorButtonColor
                            else -> NumberButtonColor
                        }
                        
                        val contentColor = if (isOperator || isAction) BackgroundDark else White

                        CalculatorButton(
                            text = text,
                            modifier = Modifier.weight(1f),
                            containerColor = containerColor,
                            contentColor = contentColor,
                            fontSize = 22.sp,
                            onClick = {
                                when (text) {
                                    "C" -> viewModel.onClearClick()
                                    "=" -> viewModel.onEqualClick()
                                    "⌫" -> viewModel.onBackspaceClick()
                                    "÷" -> viewModel.onOperationClick("/")
                                    "×" -> viewModel.onOperationClick("*")
                                    "+", "-" -> viewModel.onOperationClick(text)
                                    "(", ")" -> { /* Implementation for brackets */ }
                                    else -> viewModel.onDigitClick(text)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    fontSize: androidx.compose.ui.unit.TextUnit = 22.sp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(72.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
