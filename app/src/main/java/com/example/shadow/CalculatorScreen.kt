package com.example.shadow

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = viewModel()) {
    val displayValue = viewModel.display.value
    val scrollState = rememberScrollState()

    // Auto-scroll to the end when display changes
    LaunchedEffect(displayValue) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Display area
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.large
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = displayValue,
                    fontSize = if (displayValue.length > 10) 36.sp else 56.sp,
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                )
            }
        }

        // Scientific Panel
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ScientificButton("sin", Modifier.weight(1f)) { viewModel.onScientificClick("sin") }
                ScientificButton("cos", Modifier.weight(1f)) { viewModel.onScientificClick("cos") }
                ScientificButton("tan", Modifier.weight(1f)) { viewModel.onScientificClick("tan") }
                ScientificButton("x²", Modifier.weight(1f)) { viewModel.onScientificClick("x²") }
                ScientificButton("x³", Modifier.weight(1f)) { viewModel.onScientificClick("x³") }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ScientificButton("log", Modifier.weight(1f)) { viewModel.onScientificClick("log") }
                ScientificButton("ln", Modifier.weight(1f)) { viewModel.onScientificClick("ln") }
                ScientificButton("sqrt", Modifier.weight(1f)) { viewModel.onScientificClick("sqrt") }
                ScientificButton("^", Modifier.weight(1f)) { viewModel.onOperationClick("^") }
                ScientificButton("eˣ", Modifier.weight(1f)) { viewModel.onScientificClick("eˣ") }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ScientificButton("π", Modifier.weight(1f)) { viewModel.onScientificClick("π") }
                ScientificButton("e", Modifier.weight(1f)) { viewModel.onScientificClick("e") }
                ScientificButton("n!", Modifier.weight(1f)) { viewModel.onScientificClick("n!") }
                ScientificButton("exp", Modifier.weight(1f)) { viewModel.onScientificClick("eˣ") }
                ScientificButton("C", Modifier.weight(1f)) { viewModel.onClearClick() }
            }
        }

        Spacer(modifier = Modifier.weight(0.05f))

        // Rows of buttons
        val buttons = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("0", ".", "=", "+")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { char ->
                    CalculatorButton(
                        text = char,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            when (char) {
                                "C" -> viewModel.onClearClick()
                                "=" -> viewModel.onEqualClick()
                                "+", "-", "*", "/" -> viewModel.onOperationClick(char)
                                else -> viewModel.onDigitClick(char)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ScientificButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier
            .height(44.dp)
            .padding(1.dp),
        contentPadding = PaddingValues(0.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text = text, fontSize = 12.sp)
    }
}

@Composable
fun CalculatorButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1.2f)
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = text, fontSize = 20.sp)
    }
}
