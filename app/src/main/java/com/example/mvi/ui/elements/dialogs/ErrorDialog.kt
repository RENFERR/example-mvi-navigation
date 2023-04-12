package com.example.mvi.ui.elements.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mvi.ui.elements.dialogs.styles.ErrorDialogStyle
import com.example.mvi.ui.theme.MVIExampleTheme
import com.example.mvi.utils.NavigatorExtend.logout
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(style = ErrorDialogStyle::class)
@Composable
fun ErrorDialog(
    navigator: DestinationsNavigator,
    message: String
) {
    ErrorDialog(
        message = message,
        onClick = { navigator.popBackStack() }
    )
}

@Destination(style = ErrorDialogStyle::class)
@Composable
fun LogoutDialog(
    navigator: DestinationsNavigator,
    currentRoute: String,
    message: String
) {
    ErrorDialog(
        message = message,
        onClick = { navigator.logout(currentRoute = currentRoute) }
    )
}

@Composable
private fun ErrorDialog(
    modifier: Modifier = Modifier,
    message: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onError,
            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = android.R.string.dialog_alert_title),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                modifier = Modifier
                    .sizeIn(maxHeight = 128.dp)
                    .verticalScroll(state = rememberScrollState()),
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(4.dp))
            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = onClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorDialogPreview() {
    ErrorDialog(
        modifier = Modifier.padding(16.dp),
        message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam non varius tellus. Nunc nec nisl arcu",
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ErrorDialogPreviewLight() {
    MVIExampleTheme(darkTheme = false) {
        ErrorDialogPreview()
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorDialogPreviewDark() {
    MVIExampleTheme(darkTheme = true) {
        ErrorDialogPreview()
    }
}