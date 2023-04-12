package com.example.mvi.ui.elements.bars

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvi.ui.theme.MVIExampleTheme
import com.example.mvi.ui.theme.themedTopAppBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTopBar(
    title: String,
    onMenuPressed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuPressed) {
                Icon(
                    Icons.Rounded.Menu,
                    contentDescription = "Open drawer menu"
                )
            }
        },
        colors = themedTopAppBarColors()
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuTopBarPreview() {
    val context = LocalContext.current
    MenuTopBar(
        title = "List Item",
        onMenuPressed = {
            Toast.makeText(
                context,
                "On menu pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuTopBarPreviewLight() {
    MVIExampleTheme(darkTheme = false) {
        MenuTopBarPreview()
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuTopBarPreviewDark() {
    MVIExampleTheme(darkTheme = true) {
        MenuTopBarPreview()
    }
}