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
    private var currentOperand = "0"

    fun onDigitClick(digit: String) {
        if (isNewInput) {
            currentOperand = if (digit == ".") "0." else digit
            isNewInput = false
        } else {
            if (digit == "." && currentOperand.contains(".")) return
            if (currentOperand == "0" && digit != ".") {
                currentOperand = digit
            } else {
                currentOperand += digit
            }
        }
        updateDisplay()
    }

    private fun updateDisplay() {
        val firstPart = if (firstValue != null) formatValue(firstValue!!) else ""
        
        // If we have an operator but haven't started typing second number, show first + op
        if (firstValue != null && pendingOperation != null && isNewInput) {
            _display.value = "$firstPart $pendingOperation"
        } else if (firstValue != null && pendingOperation != null) {
            _display.value = "$firstPart $pendingOperation $currentOperand"
        } else {
            _display.value = currentOperand
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
        val currentValue = currentOperand.replace(",", "").toDoubleOrNull() ?: 0.0
        
        if (firstValue == null) {
            firstValue = currentValue
        } else if (!isNewInput && pendingOperation != null) {
            val result = calculateResult(firstValue!!, currentValue, pendingOperation!!)
            firstValue = result
            currentOperand = formatValue(result)
        }
        
        pendingOperation = operation
        isNewInput = true
        updateDisplay()
    }

    fun onEqualClick() {
        val currentValue = currentOperand.replace(",", "").toDoubleOrNull() ?: return
        val operation = pendingOperation ?: return
        val result = calculateResult(firstValue ?: 0.0, currentValue, operation)
        
        val resultString = formatValue(result)
        _display.value = resultString
        currentOperand = resultString
        firstValue = null
        pendingOperation = null
        isNewInput = true
    }

    fun onClearClick() {
        _display.value = "0"
        currentOperand = "0"
        firstValue = null
        pendingOperation = null
        isNewInput = true
    }

    fun onScientificClick(operation: String) {
        val currentValue = currentOperand.replace(",", "").toDoubleOrNull() ?: return
        val result = when (operation) {
            "sin" -> sin(Math.toRadians(currentValue))
            "cos" -> cos(Math.toRadians(currentValue))
            "tan" -> tan(Math.toRadians(currentValue))
            "sqrt" -> if (currentValue >= 0) sqrt(currentValue) else Double.NaN
            "log" -> if (currentValue > 0) log10(currentValue) else Double.NaN
            "ln" -> if (currentValue > 0) ln(currentValue) else Double.NaN
            "x²" -> currentValue.pow(2)
            "x³" -> currentValue.pow(3)
            "eˣ" -> exp(currentValue)
            "n!" -> factorial(currentValue)
            "π" -> PI
            "e" -> E
            else -> currentValue
        }
        
        currentOperand = formatValue(result)
        isNewInput = true
        updateDisplay()
    }

    private fun factorial(n: Double): Double {
        if (n < 0 || n > 170) return Double.NaN
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
