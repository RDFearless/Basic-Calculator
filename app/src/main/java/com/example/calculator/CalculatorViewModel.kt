package com.example.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(actions: CalculatorActions){
        when(actions){
            is CalculatorActions.Number -> enterNumber(actions.number)
            is CalculatorActions.Clear -> state = CalculatorState()
            is CalculatorActions.Delete -> performDeletion()
            is CalculatorActions.Decimal -> addDecimal()
            is CalculatorActions.Calculate -> performCalculation()
            is CalculatorActions.Operations -> performOperation(actions.operations)
        }
    }

    private fun performOperation(operations: CalculatorOperations) {
        if(state.number1.isNotBlank()){
            state = state.copy(
                operations = operations
            )
        }
    }

    private fun performCalculation() {
        val num1 = state.number1.toDoubleOrNull()
        val num2 = state.number2.toDoubleOrNull()
        if(num1 != null && num2 != null){
            val result = when(state.operations){
                is CalculatorOperations.Add -> num1 + num2
                is CalculatorOperations.Subtract -> num1 - num2
                is CalculatorOperations.Multiply -> num1 * num2
                is CalculatorOperations.Divide -> num1 / num2
                null -> return
            }
            state = state.copy(
                number1 = result.toString().take(15),
                number2 = "",
                operations = null
            )
        }
    }

    private fun addDecimal() {
        if(state.operations == null && !state.number1.contains(".")
            && state.number1.isNotBlank()
            ){
            state = state.copy(
                number1 = state.number1 + "."
            )
            return
        }
        if(!state.number2.contains(".") && state.number2.isNotBlank()){
            state = state.copy(
                number2 = state.number2 + "."
            )
        }
    }

    private fun performDeletion() {
        when{
            state.number2.isNotBlank() -> state = state.copy(
                number2 = state.number2.dropLast(1)
            )
            state.operations != null -> state = state.copy(
                operations = null
            )
            state.number1.isNotBlank() -> state = state.copy(
                number1 = state.number1.dropLast(1)
            )
        }
    }

    private fun enterNumber(number: Int) {
        if(state.operations == null){
            if(state.number1.length >= MAX_LENGTH){
                return
            }
            state = state.copy(
                number1 = state.number1 + number
            )
            return
        }
        if(state.number2.length >= MAX_LENGTH){
            return
        }
        state = state.copy(
            number2 = state.number2 + number
        )
    }

    companion object {
        private const val MAX_LENGTH = 7
    }
}