package com.wbertan.demo.tictactoe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.launch

class Asas03 {

    @Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
    @Composable
    fun PreviewApp() {
        MaterialTheme {
            App()
        }
    }

    @Composable
    fun App() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "LayoutsCodelab")
                    },
                    actions = {
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Filled.Favorite, contentDescription = null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            SimpleList(
                Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
            )
        }
    }

    @Composable
    fun SimpleList(modifier: Modifier = Modifier) {
        val listSize = 100
        // We save the scrolling position with this state
        val scrollState = rememberLazyListState()
        // We save the coroutine scope where our animated scroll will be executed
        val coroutineScope = rememberCoroutineScope()

        Column(modifier) {
            Row {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        coroutineScope.launch {
                            // 0 is the first item index
                            scrollState.animateScrollToItem(0)
                        }
                    }
                ) {
                    Text("Scroll to the top")
                }

                Divider(Modifier.size(8.dp), color = Color.Transparent)

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        coroutineScope.launch {
                            // listSize - 1 is the last index of the list
                            scrollState.animateScrollToItem(listSize - 1)
                        }
                    }
                ) {
                    Text("Scroll to the end")
                }
            }

            LazyColumn(modifier = Modifier.fillMaxHeight(), state = scrollState) {
                items(100) {
                    ImageListItem(it)
                }
            }
        }
    }

    @Composable
    fun ImageListItem(index: Int) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CoilImage(
                data = "https://dummyimage.com/32&text=$index",
                contentDescription = "Android Logo",
                modifier = Modifier.size(50.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text("Item #$index", style = MaterialTheme.typography.subtitle1)
        }
    }
}