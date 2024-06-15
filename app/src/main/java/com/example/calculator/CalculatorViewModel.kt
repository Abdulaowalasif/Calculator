package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Context

class CalculatorViewModel : ViewModel() {

    private val _equationText = MutableLiveData("")
    val equationText: LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText: LiveData<String> = _resultText


    fun onButtonClick(btn: String) {
        _equationText.value?.let {
            if (btn == "AC") {
                _equationText.value = ""
                _resultText.value = "0"
                return
            }


            if (btn == "C") {
                val currentEquation = _equationText.value ?: ""

                if (currentEquation.isNotEmpty()) {
                    val newEquation = currentEquation.substring(0, currentEquation.length - 1)

                    _equationText.value = newEquation

                    if (endsWithOperator(newEquation)) {
                        val trimmedEquation = newEquation.dropLast(1)

                        _resultText.value = trimmedEquation

                    } else if (newEquation.isEmpty()) {
                        _resultText.value = "0"
                    } else {
                        val newResult = calculate(newEquation)
                        _resultText.value = newResult
                    }
                } else {
                    _resultText.value = "0"
                }
                return
            }

            if (btn == "=") {
                _equationText.value = _resultText.value
                return
            }
            _equationText.value = it + btn

            try {
                _resultText.value = calculate(_equationText.value.toString())
            } catch (_: Exception) {
            }

        }
    }


    private fun calculate(equation: String): String {
        val context: Context = Context.enter()
        context.optimizationLevel = -1
        val scriptable = context.initStandardObjects()
        var finalResult =
            context.evaluateString(scriptable, equation, "javascript", 1, null).toString()

        if (finalResult.endsWith(".0")) {
            finalResult = finalResult.replace(".0", "")
        }
        return finalResult
    }


    private fun endsWithOperator(equation: String): Boolean {
        val operators = listOf('+', '-', '*', '/')
        return operators.any { equation.endsWith(it) }
    }

}