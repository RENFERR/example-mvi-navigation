package com.example.mvi.screens.drawer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mvi.screens.drawer.contract.DrawerContract
import com.example.mvi.screens.drawer.contract.DrawerViewModel
import com.example.mvi.screens.user.models.User
import com.example.mvi.ui.elements.loading.LoadingScreen
import com.example.mvi.ui.theme.MVIExampleTheme
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import kotlinx.coroutines.launch

@Composable
fun Drawer(
    state: DrawerState,
    viewModel: DrawerViewModel,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val currentDestination by navController.currentDestinationAsState()
    val drawerItems = listOf(
        DrawerItem.List,
        DrawerItem.User,
        DrawerItem.Logout
    )
    val currentDrawerItem = drawerItems.find {
        it.direction == currentDestination
    }
    Drawer(
        drawerState = state,
        values = drawerItems,
        selected = currentDrawerItem,
        onSelectChange = { item ->
            if (item is DrawerItem.Logout) {
                viewModel.handleIntent(
                    event = DrawerContract.Intent.Logout
                )
            } else {
                val currentRoute = currentDestination?.route
                navController.navigate(item.direction) {
                    currentRoute?.let {
                        popUpTo(route = it) {
                            inclusive = true
                        }
                    }
                }
            }
        },
        content = content,
        state = viewModel.state.collectAsState().value
    )
}

@Composable
private fun Drawer(
    state: DrawerContract.State,
    drawerState: DrawerState,
    values: List<DrawerItem>,
    selected: DrawerItem?,
    onSelectChange: (DrawerItem) -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape,
                drawerContainerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(end = 72.dp),
                content = {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        item {
                            state.Header(
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        items(
                            items = values,
                            key = { it.title }
                        ) { item ->
                            DrawerItem(
                                item = item,
                                selected = selected == item,
                                onClick = {
                                    if (selected != item) {
                                        if (drawerState.isOpen) scope.launch { drawerState.close() }
                                        onSelectChange.invoke(item)
                                    }
                                }
                            )
                        }
                    }
                }
            )
        },
        content = content
    )
}

@Composable
private fun DrawerContract.State.Header(
    modifier: Modifier = Modifier
) {
    HeaderContainer(
        modifier = modifier,
        content = {
            when (this@Header) {
                is DrawerContract.State.Loading -> {
                    LoadingScreen(modifier = Modifier.size(64.dp))
                }
                is DrawerContract.State.Loaded -> {
                    Text(
                        text = "${data.secondName} ${data.firstName}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.W600
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = data.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.W500
                    )
                }
                is DrawerContract.State.Failure -> Unit
            }
        }
    )
}

@Composable
private fun HeaderContainer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun DrawerPreview() {
    val state = rememberDrawerState(initialValue = DrawerValue.Open)
    val drawerItems = listOf(
        DrawerItem.List,
        DrawerItem.User,
        DrawerItem.Logout
    )
    Drawer(
        state = DrawerContract.State.Loaded(
            data = User(
                firstName = "Kirill",
                secondName = "Maenkov",
                email = "someemail@gmail.com"
            )
        ),
        drawerState = state,
        values = drawerItems,
        onSelectChange = {},
        selected = DrawerItem.List,
        content = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun DrawerPreviewLight() {
    MVIExampleTheme(darkTheme = false) {
        DrawerPreview()
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawerPreviewDark() {
    MVIExampleTheme(darkTheme = true) {
        DrawerPreview()
    }
}