package com.example.mvi.screens.user.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mvi.R
import com.example.mvi.screens.user.contract.UserContract.Intent
import com.example.mvi.screens.user.contract.UserContract.State
import com.example.mvi.screens.user.contract.UserViewModel
import com.example.mvi.screens.user.models.User
import com.example.mvi.ui.elements.bars.MenuTopBar
import com.example.mvi.ui.elements.loading.LoadingScreen
import com.example.mvi.ui.theme.MVIExampleTheme
import com.example.mvi.utils.NavigatorExtend.showErrorDialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

private sealed class Event {
    object OpenDrawer : Event()
}

@Destination
@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    drawerState: DrawerState
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = viewModel.state.collectAsState().value

    UserScreen(
        state = state,
        onIntent = viewModel::handleIntent,
        onEvent = { event ->
            when (event) {
                is Event.OpenDrawer -> if (!drawerState.isOpen)
                    scope.launch { drawerState.open() }
            }
        }
    )
    LaunchedEffect(state) {
        if (state is State.Failure) navigator.showErrorDialog(
            message = state.message.asString(context = context)
        )
    }
}

@Composable
private fun UserScreen(
    state: State,
    onIntent: (Intent) -> Unit,
    onEvent: (Event) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MenuTopBar(
                title = stringResource(R.string.title_user_screen),
                onMenuPressed = { onEvent.invoke(Event.OpenDrawer) }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (state) {
                is State.Loading -> LoadingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
                is State.Loaded -> state.Content()
                else -> Unit
            }
            Button(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = { if (state !is State.Loading) onIntent.invoke(Intent.UpdateScreen) }
            ) {
                Text(
                    text = stringResource(R.string.button_update_screen),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun State.Loaded.Content() {
    this.data?.let { user ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.user_info),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = user.firstName + " " + user.secondName,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_data),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserScreenPreview() {
    UserScreen(
        state = State.Loaded(data = User(
            firstName = "Kirill",
            secondName = "Maenkov",
            email = "someemail@gmail.com"
        )),
        onIntent = {},
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun UserScreenPreviewLight() {
    MVIExampleTheme(darkTheme = false) {
        UserScreenPreview()
    }
}

@Preview(showBackground = true)
@Composable
private fun UserScreenPreviewDark() {
    MVIExampleTheme(darkTheme = true) {
        UserScreenPreview()
    }
}