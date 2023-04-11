package view

import androidx.compose.ui.input.key.*

infix fun KeyEvent.isCompletePressOf(key: Key): Boolean = this.key == key && this.type == KeyEventType.KeyUp