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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wbertan.demo.tictactoe.game.SimpleGameView
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

interface Repository {
    val name: String
}

class RepositoryImpl @Inject constructor() : Repository {
    override val name = "repository"
}

@HiltViewModel
class DummyViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {
    val a = repository.name
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    abstract fun bind(impl: RepositoryImpl): Repository
}

@AndroidEntryPoint
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

    @Composable
    fun App(dummyViewModel: DummyViewModel = viewModel()) {
        println("asas ${dummyViewModel.a}")
        SimpleGameView.SimpleGame(Modifier.padding(8.dp))
    }

    @Preview(showBackground = true, backgroundColor = 0xFF03A9F4)
    @Composable
    fun PreviewApp() {
        MaterialTheme {
            App()
        }
    }
}