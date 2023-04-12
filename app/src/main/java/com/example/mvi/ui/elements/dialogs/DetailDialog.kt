package com.example.mvi.ui.elements.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mvi.ui.elements.dialogs.styles.DetailDialogStyle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(style = DetailDialogStyle::class)
@Composable
fun DetailDialog(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    title: String,
    description: String
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                modifier = Modifier
                    .sizeIn(maxHeight = 128.dp)
                    .verticalScroll(state = rememberScrollState()),
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(4.dp))
            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { navigator.popBackStack() }
            ) {
                Text(text = "ok")
            }
        }
    }
}