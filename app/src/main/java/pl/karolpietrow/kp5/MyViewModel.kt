package pl.karolpietrow.kp5

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyViewModel : ViewModel() {
    private val _screenCount = MutableStateFlow(1)
    val screenCount = _screenCount.asStateFlow()

    fun setScreenCount(count: Int) {
        _screenCount.value = count
    }

    private val _currentScreen = MutableStateFlow(1)
    val currentScreen = _currentScreen.asStateFlow()

    fun setCurrentScreen(count: Int) {
        _currentScreen.value = count
    }

    private val _place1 = MutableStateFlow("")
    val place1 = _place1.asStateFlow()

    private val _place2 = MutableStateFlow("")
    val place2 = _place2.asStateFlow()

    private val _place3 = MutableStateFlow("")
    val place3 = _place3.asStateFlow()

    fun updatePlace1(newValue: String) {
        _place1.value = newValue
    }
    fun updatePlace2(newValue: String) {
        _place2.value = newValue
    }
    fun updatePlace3(newValue: String) {
        _place3.value = newValue
    }
}