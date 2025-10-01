package com.jcu.jc428992.booktracker.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: AccountViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Reading Stats") }) }
    ) { paddingValues ->
        if (stats.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(label = "Books Completed", value = stats.completedCount.toString())
                StatCard(label = "Currently Reading", value = stats.inProgressCount.toString())
                StatCard(label = "Books Abandoned", value = stats.abandonedCount.toString())
                StatCard(label = "On Your Wishlist", value = stats.willReadCount.toString())
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = value, style = MaterialTheme.typography.headlineMedium)
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
