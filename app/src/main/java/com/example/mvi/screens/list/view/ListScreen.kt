package com.example.mvi.screens.list.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mvi.R
import com.example.mvi.destinations.ItemScreenDestination
import com.example.mvi.destinations.ListScreenDestination
import com.example.mvi.screens.list.contract.ListContract.Intent
import com.example.mvi.screens.list.contract.ListContract.State
import com.example.mvi.screens.list.contract.ListViewModel
import com.example.mvi.ui.elements.bars.MenuTopBar
import com.example.mvi.ui.elements.drawer.Drawer
import com.example.mvi.ui.elements.drawer.DrawerItem
import com.example.mvi.ui.elements.loading.LoadingScreen
import com.example.mvi.ui.theme.MVIExampleTheme
import com.example.mvi.utils.NavigatorExtend.logoutWithDialog
import com.example.mvi.utils.NavigatorExtend.showErrorDialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.util.*

private sealed class Event {
    object OpenDrawer : Event()
    data class ItemClick(val index: Int, val item: String) : Event()
}

@Destination
@Composable
fun ListScreen(
    viewModel: ListViewModel = viewModel(),
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val isLogged = viewModel.isLogged.collectAsState(initial = true).value
    val logoutMessage = stringResource(id = R.string.not_auth_message)
    val state = viewModel.state.collectAsState().value

    Drawer(
        state = drawerState,
        navigator = navigator,
        currentPage = DrawerItem.List,
        content = {
            ListScreen(
                state = state,
                onIntent = viewModel::handleIntent,
                onEvent = { event ->
                    when (event) {
                        is Event.OpenDrawer -> if (!drawerState.isOpen) scope.launch {
                            drawerState.open()
                        }
                        is Event.ItemClick -> navigator.navigate(
                            direction = ItemScreenDestination.invoke(item = event.item)
                        )
                    }
                }
            )
        }
    )
    LaunchedEffect(state) {
        if (state is State.Failure) navigator.showErrorDialog(
            message = state.message.asString(context = context)
        )
    }
    LaunchedEffect(isLogged) {
        if (!isLogged) {
            navigator.logoutWithDialog(
                message = logoutMessage,
                currentRoute = ListScreenDestination.route
            )
        }
    }
}

@Composable
private fun ListScreen(
    state: State,
    onIntent: (Intent) -> Unit,
    onEvent: (Event) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MenuTopBar(
                title = stringResource(R.string.title_list_screen),
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
                is State.Empty -> EmptyContent()
                is State.Failure -> EmptyContent()
                is State.Loaded -> state.Content(
                    modifier = Modifier.fillMaxSize(),
                    onEvent = onEvent
                )
                is State.Loading -> LoadingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
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
private fun EmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.message_empty_items),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun State.Loaded.Content(
    modifier: Modifier = Modifier,
    onEvent: (Event) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = this@Content.data,
            key = { index, _ ->
                index
            }
        ) { index, item ->
            val click = remember { { onEvent.invoke(Event.ItemClick(index = index, item = item)) } }
            Text(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
                    .clickable(onClick = click),
                text = stringResource(id = R.string.item) + " $item",
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListScreenPreview() {
    ListScreen(
        state = State.Loaded(
            data = mutableListOf<String>().apply {
                repeat(40) {
                    add(element = UUID.randomUUID().toString())
                }
            }
        ),
        onIntent = {},
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ListScreenPreviewLight() {
    MVIExampleTheme(darkTheme = false) {
        ListScreenPreview()
    }
}

@Preview(showBackground = true)
@Composable
private fun ListScreenPreviewDark() {
    MVIExampleTheme(darkTheme = true) {
        ListScreenPreview()
    }
}