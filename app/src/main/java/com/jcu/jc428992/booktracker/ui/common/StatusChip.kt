package com.jcu.jc428992.booktracker.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jcu.jc428992.booktracker.data.local.ReadingStatus

@Composable
fun StatusChip(status: ReadingStatus) {
    val (text, color) = when (status) {
        ReadingStatus.READ -> "Read" to MaterialTheme.colorScheme.primaryContainer
        ReadingStatus.READING -> "Reading" to MaterialTheme.colorScheme.secondaryContainer
        ReadingStatus.ABANDONED -> "Abandoned" to MaterialTheme.colorScheme.errorContainer
        else -> return // Don't show a chip for WILL_READ
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = contentColorFor(backgroundColor = color)
        )
    }
}
