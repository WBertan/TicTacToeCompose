package com.wbertan.demo.tictactoe

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class Asas01 {

    @Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
    @Composable
    fun PreviewApp() {
        App()
    }

    @Composable
    fun NameList(names: List<String>, modifier: Modifier = Modifier) {
        LazyColumn(modifier = modifier) {
            items(items = names) { name ->
                Greeting(name = name)
                Divider(color = Color.Black)
            }
        }
    }

    @Composable
    fun Greeting(name: String) {
        val isSelected = remember { mutableStateOf(false) }
        val backgroundColor by animateColorAsState(if (isSelected.value) Color.Red else Color.Transparent)

        Text(
            text = "Hello $name!",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(24.dp)
                .background(color = backgroundColor)
                .clickable(onClick = { isSelected.value = !isSelected.value })
        )
    }

    @Composable
    fun Counter(count: Int, updateCount: (Int) -> Unit) {
        Button(
            onClick = { updateCount(count + 1) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("I've been clicked $count times")
        }
    }

    @Composable
    fun NewsStory() {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.header),
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "A day wandering through the sandhills " +
                        "in Shark Fin Cove, and a few of the " +
                        "sights I saw",
                style = MaterialTheme.typography.h6,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text("Davenport, California", style = MaterialTheme.typography.body2)
            Text("December 2018", style = MaterialTheme.typography.body2)
        }
    }

    @Preview
    @Composable
    fun PreviewGreeting() {
        NewsStory()
    }
}