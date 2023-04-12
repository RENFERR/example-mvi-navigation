package com.example.mvi.ui.elements.loading

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val maxSize = 64.dp
private val minSize = 8.dp

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.sizeIn(
                maxHeight = maxSize,
                maxWidth = maxSize,
                minHeight = minSize,
                minWidth = minSize
            )
        )
    }
}