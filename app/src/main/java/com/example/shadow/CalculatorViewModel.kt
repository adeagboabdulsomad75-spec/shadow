package com.example.shadow

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.*

class CalculatorViewModel : ViewModel() {
    private val _display = mutableStateOf("0")
    val display: State<String> = _display

    private var firstValue: Double? = null
    private var pendingOperation: String? = null
    private var isNewInput = true

    fun onDigitClick(digit: String) {
        if (isNewInput) {
            _display.value = digit
            isNewInput = false
        } else {
            if (_display.value == "0") {
                _display.value = digit
            } else {
                _display.value += digit
            }
        }
    }

    private val symbols = DecimalFormatSymbols(Locale.US)
    private val standardFormat = DecimalFormat("#,###.##########", symbols)
    private val scientificFormat = DecimalFormat("0.######E0", symbols)

    private fun formatValue(value: Double): String {
        if (value.isNaN()) return "Error"
        if (value.isInfinite()) return "Infinity"
        
        val absValue = abs(value)
        return if (absValue != 0.0 && (absValue < 1e-7 || absValue >= 1e12)) {
            scientificFormat.format(value)
        } else {
            standardFormat.format(value)
        }
    }

    fun onOperationClick(operation: String) {
        val currentValue = _display.value.replace(",", "").toDoubleOrNull() ?: return
        if (firstValue == null) {
            firstValue = currentValue
        } else if (pendingOperation != null) {
            val result = calculateResult(firstValue!!, currentValue, pendingOperation!!)
            _display.value = formatValue(result)
            firstValue = result
        }
        pendingOperation = operation
        isNewInput = true
    }

    fun onEqualClick() {
        val currentValue = _display.value.replace(",", "").toDoubleOrNull() ?: return
        val operation = pendingOperation ?: return
        val result = calculateResult(firstValue ?: 0.0, currentValue, operation)
        _display.value = formatValue(result)
        firstValue = null
        pendingOperation = null
        isNewInput = true
    }

    fun onClearClick() {
        _display.value = "0"
        firstValue = null
        pendingOperation = null
        isNewInput = true
    }

    fun onScientificClick(operation: String) {
        val currentValue = _display.value.replace(",", "").toDoubleOrNull() ?: return
        val result = when (operation) {
            "sin" -> sin(Math.toRadians(currentValue))
            "cos" -> cos(Math.toRadians(currentValue))
            "tan" -> tan(Math.toRadians(currentValue))
            "sqrt" -> sqrt(currentValue)
            "log" -> log10(currentValue)
            "ln" -> ln(currentValue)
            "x²" -> currentValue.pow(2)
            "x³" -> currentValue.pow(3)
            "eˣ" -> exp(currentValue)
            "n!" -> factorial(currentValue)
            "π" -> PI
            "e" -> E
            else -> currentValue
        }
        _display.value = formatValue(result)
        isNewInput = true
    }

    private fun factorial(n: Double): Double {
        if (n < 0 || n > 170) return Double.NaN // Limit for Double precision
        if (n == 0.0) return 1.0
        var result = 1.0
        for (i in 1..n.toInt()) {
            result *= i
        }
        return result
    }

    private fun calculateResult(first: Double, second: Double, operation: String): Double {
        return when (operation) {
            "+" -> first + second
            "-" -> first - second
            "*" -> first * second
            "/" -> if (second != 0.0) first / second else Double.NaN
            "^" -> first.pow(second)
            else -> second
        }
    }
}
