package com.example.mvi.screens.login.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mvi.R
import com.example.mvi.destinations.ListScreenDestination
import com.example.mvi.destinations.LoginScreenDestination
import com.example.mvi.ui.theme.MVIExampleTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator
) {
    LoginScreen(
        onLogin = {
            navigator.navigate(ListScreenDestination) {
                popUpTo(route = LoginScreenDestination.route) {
                    inclusive = true
                }
            }
        }
    )
}

@Composable
private fun LoginScreen(
    onLogin: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = stringResource(R.string.title_login_screen),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = onLogin) {
                    Text(text = stringResource(R.string.button_authenticate))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen { }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreviewLight() {
    MVIExampleTheme(darkTheme = false) {
        LoginScreenPreview()
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreviewDark() {
    MVIExampleTheme(darkTheme = true) {
        LoginScreenPreview()
    }
}