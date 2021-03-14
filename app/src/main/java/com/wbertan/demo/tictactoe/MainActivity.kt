package com.wbertan.demo.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.wbertan.demo.tictactoe.game.SimpleGameView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_gradient))

        setContent {
            MaterialTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    SimpleGameView.SimpleGame(Modifier.padding(8.dp))
}

@Preview(showBackground = true, backgroundColor = 0xFF03A9F4)
@Composable
fun PreviewApp() {
    MaterialTheme {
        App()
    }
}