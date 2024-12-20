package com.kls.fetchtakehometest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kls.fetchtakehometest.ui.theme.FetchTakeHomeTestTheme
import dagger.hilt.android.AndroidEntryPoint
import com.kls.fetchtakehometest.data.Result
import androidx.lifecycle.viewmodel.compose.*
import com.kls.fetchtakehometest.data.Data

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FetchTakeHomeTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box {
                        // A simple screen to display the list of items.
                        // If there were multiple pages, I'd add navigation.
                        MainScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val groupedItems by viewModel.groupedItems.collectAsState()
    when (groupedItems) {
        is Result.Loading -> {
            LoadingView()
        }
        is Result.Success -> {
            val groupedData = (groupedItems as Result.Success).data
            ItemsList(groupedData = groupedData)
        }
        is Result.Error -> {
            val exception = (groupedItems as Result.Error).exception
            Text("Error: ${exception.message}")
        }
    }
}

@Composable
fun ItemsList(groupedData: Map<Int, List<Data>>) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        groupedData.forEach { (listId, items) ->
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = "List ID: $listId",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 8.dp))
            }
            items(items) { data ->
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = data.name ?: "Unknown Name",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color.Red)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FetchTakeHomeTestTheme {
        // Simulate the state of `groupedItems` for the preview
        val groupedItems = Result.Success(
            data = mapOf(
                1 to listOf(
                    Data(id = 101, listId = 1, name = "Item A"),
                    Data(id = 102, listId = 1, name = "Item B")
                ),
                2 to listOf(
                    Data(id = 203, listId = 2, name = "Item C")
                )
            )
        )

        // Call the MainScreen and pass the simulated state
        MainScreenPreviewContent(groupedItems)
    }
}

@Composable
fun MainScreenPreviewContent(groupedItems: Result<Map<Int, List<Data>>>) {
    when (groupedItems) {
        is Result.Loading -> {
            LoadingView()
        }
        is Result.Success -> {
            val groupedData = groupedItems.data
            ItemsList(groupedData = groupedData)
        }
        is Result.Error -> {
            Text("Error: ${groupedItems.exception.message}")
        }
    }
}