package com.example.mvi.screens.login.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mvi.NavGraphs
import com.example.mvi.R
import com.example.mvi.destinations.DirectionDestination
import com.example.mvi.destinations.LoginScreenDestination
import com.example.mvi.screens.login.contract.LoginContract
import com.example.mvi.screens.login.contract.LoginViewModel
import com.example.mvi.screens.login.viewmodel.IsLoggedViewModel
import com.example.mvi.startAppDestination
import com.example.mvi.ui.elements.loading.LoadingScreen
import com.example.mvi.ui.theme.MVIExampleTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@Destination
@Composable
fun LoginScreen(
    isLoggedViewModel: IsLoggedViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    BackHandler(enabled = true) { /* We want to disable back clicks */ }
    val isLoggedIn by isLoggedViewModel.isLoggedFlow.collectAsState()
    val hasNavigatedUp = remember { mutableStateOf(false) }

    if (isLoggedIn && !hasNavigatedUp.value) {
        hasNavigatedUp.value = true

        if (!navigator.navigateUp()) {
            navigator.navigate(NavGraphs.root.startAppDestination as DirectionDestination) {
                popUpTo(LoginScreenDestination) {
                    inclusive = true
                }
            }
        }
    }
    LoginScreen(
        state = loginViewModel.state.collectAsState().value,
        onIntent = loginViewModel::handleIntent
    )
}

@Composable
private fun LoginScreen(
    state: LoginContract.State,
    onIntent: (LoginContract.Intent) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (state) {
            is LoginContract.State.Loading -> {
                LoadingScreen(modifier = Modifier.fillMaxSize())
            }

            else -> {
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
                        Button(
                            onClick = {
                                onIntent.invoke(LoginContract.Intent.Login)
                            }
                        ) {
                            Text(text = stringResource(R.string.button_authenticate))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(state = LoginContract.State.Idle) { }
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