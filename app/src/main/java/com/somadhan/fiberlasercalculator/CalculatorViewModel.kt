package com.somadhan.fiberlasercalculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.log10

class CalculatorViewModel : ViewModel() {
    private val _splitType = MutableStateFlow("ratio")
    val splitType: StateFlow<String> = _splitType

    private val _inputPower = MutableStateFlow("")
    val inputPower: StateFlow<String> = _inputPower

    private val _splitRatio = MutableStateFlow("")
    val splitRatio: StateFlow<String> = _splitRatio

    private val _splitPercentage = MutableStateFlow("")
    val splitPercentage: StateFlow<String> = _splitPercentage

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result

    fun setSplitType(type: String) {
        _splitType.value = type
    }

    fun setInputPower(power: String) {
        _inputPower.value = power
    }

    fun setSplitRatio(ratio: String) {
        _splitRatio.value = ratio
    }

    fun setSplitPercentage(percentage: String) {
        _splitPercentage.value = percentage
    }

    fun calculatePower() {
        val power = _inputPower.value.toDoubleOrNull() ?: return setError()
        if (_splitType.value == "ratio") {
            val ratio = _splitRatio.value.toDoubleOrNull() ?: return setError()
            val power1 = power + 10 * log10(1 / ratio)
            val power2 = power + 10 * log10((ratio - 1) / ratio)
            _result.value = "Output Power (First Split): %.2f dBm\nOutput Power (Second Split): %.2f dBm".format(power1, power2)
        } else {
            val percentage = _splitPercentage.value.toDoubleOrNull() ?: return setError()
            val split1 = percentage / 100
            val split2 = 1 - split1
            val power1 = power + 10 * log10(split1)
            val power2 = power + 10 * log10(split2)
            _result.value = "Output Power (First Split): %.2f dBm\nOutput Power (Second Split): %.2f dBm".format(power1, power2)
        }
    }

    private fun setError() {
        _result.value = "Please enter valid values."
    }
}