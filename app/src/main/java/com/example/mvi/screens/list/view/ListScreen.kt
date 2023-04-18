package com.example.mvi.screens.list.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mvi.R
import com.example.mvi.destinations.ItemScreenDestination
import com.example.mvi.screens.list.contract.ListContract.Intent
import com.example.mvi.screens.list.contract.ListContract.State
import com.example.mvi.screens.list.contract.ListViewModel
import com.example.mvi.ui.elements.bars.MenuTopBar
import com.example.mvi.ui.elements.loading.LoadingScreen
import com.example.mvi.ui.theme.MVIExampleTheme
import com.example.mvi.utils.NavigatorExtend.showErrorDialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.util.UUID

private sealed class Event {
    object OpenDrawer : Event()
    data class ItemClick(val index: Int, val item: String) : Event()
}

@RootNavGraph(start = true)
@Destination
@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    drawerState: DrawerState
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = viewModel.state.collectAsState().value

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
    LaunchedEffect(state) {
        if (state is State.Failure) navigator.showErrorDialog(
            message = state.message.asString(context = context)
        )
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