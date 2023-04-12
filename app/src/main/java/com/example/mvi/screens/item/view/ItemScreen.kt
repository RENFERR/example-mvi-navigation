package com.example.mvi.screens.item.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mvi.destinations.DetailDialogDestination
import com.example.mvi.ui.elements.bars.ItemTopBar
import com.example.mvi.ui.theme.MVIExampleTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ItemScreen(
    item: String,
    navigator: DestinationsNavigator
) {
    ItemScreen(
        item = item,
        onBackPressed = { navigator.popBackStack() },
        onItemClick = {
            navigator.navigate(
                DetailDialogDestination.invoke(
                    title = item,
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce lobortis sit amet elit ac hendrerit. Vivamus dui odio, maximus quis nibh sit amet, sagittis ultrices nisi. Integer in tortor tempor, interdum ligula vel, commodo augue. Phasellus sollicitudin pharetra turpis, eget gravida augue aliquam quis. Duis ornare augue ut erat tempor blandit in ac nisl. Nam sodales sem augue. Nunc sed leo in libero fringilla ultrices. Sed porta odio nisi, in commodo lorem elementum vitae. Vivamus scelerisque pulvinar nunc, in faucibus metus laoreet at. Nunc convallis metus quis purus maximus, quis molestie nunc tempor. Phasellus convallis, mi tincidunt consequat tincidunt, turpis sapien vulputate ex, sed iaculis quam ligula vel odio. Vivamus bibendum pretium tempor. Aenean ultricies nisi at quam ornare imperdiet. Nullam id odio neque. Donec bibendum justo ac iaculis porta."
                )
            )
        }
    )
}

@Composable
private fun ItemScreen(
    item: String,
    onBackPressed: () -> Unit,
    onItemClick: () -> Unit
) {
    Scaffold(
        topBar = {
            ItemTopBar(
                title = "Item Info",
                onBackPressed = onBackPressed
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Item",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    modifier = Modifier.clickable(onClick = onItemClick),
                    text = item,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListItemScreenPreview() {
    ItemScreen(
        item = "Some Item",
        onBackPressed = {},
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ListItemPreviewLight() {
    MVIExampleTheme(darkTheme = false) {
        ListItemScreenPreview()
    }
}

@Preview(showBackground = true)
@Composable
private fun ListItemPreviewDark() {
    MVIExampleTheme(darkTheme = true) {
        ListItemScreenPreview()
    }
}