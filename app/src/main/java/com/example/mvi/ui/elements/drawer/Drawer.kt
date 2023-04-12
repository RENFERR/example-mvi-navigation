package com.example.mvi.ui.elements.drawer

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mvi.destinations.ListScreenDestination
import com.example.mvi.destinations.LoginScreenDestination
import com.example.mvi.destinations.UserScreenDestination
import com.example.mvi.ui.elements.drawer.DrawerItem.Companion.toRoute
import com.example.mvi.ui.theme.MVIExampleTheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction
import kotlinx.coroutines.launch

@Composable
fun Drawer(
    state: DrawerState,
    currentPage: DrawerItem,
    navigator: DestinationsNavigator,
    content: @Composable () -> Unit
) {
    val drawerItems = listOf(
        DrawerItem.List,
        DrawerItem.User,
        DrawerItem.Logout
    )
    Drawer(
        drawerItems = drawerItems,
        state = state,
        content = content,
        currentPage = currentPage,
        onItemClick = { item ->
            when (item) {
                DrawerItem.List -> navigator.navigateWithPopUp(
                    currentRoute = currentPage.toRoute(),
                    direction = ListScreenDestination
                )
                DrawerItem.Logout -> navigator.navigateWithPopUp(
                    currentRoute = currentPage.toRoute(),
                    direction = LoginScreenDestination
                )
                DrawerItem.User -> navigator.navigateWithPopUp(
                    currentRoute = currentPage.toRoute(),
                    direction = UserScreenDestination
                )
            }
        }
    )
}

private fun DestinationsNavigator.navigateWithPopUp(currentRoute: String, direction: Direction) {
    navigate(direction = direction) {
        popUpTo(route = currentRoute) {
            inclusive = true
        }
    }
}

@Composable
private fun Drawer(
    state: DrawerState,
    drawerItems: List<DrawerItem>,
    currentPage: DrawerItem,
    onItemClick: (DrawerItem) -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = state,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape,
                drawerContainerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(end = 72.dp),
                content = {
                    LazyColumn {
                        items(
                            items = drawerItems,
                            key = { it.title }
                        ) { item ->
                            DrawerItem(
                                item = item,
                                selected = currentPage == item,
                                onClick = {
                                    if (currentPage != item) {
                                        if (state.isOpen) scope.launch { state.close() }
                                        onItemClick.invoke(item)
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
        drawerItems = drawerItems,
        state = state,
        content = {},
        onItemClick = {},
        currentPage = DrawerItem.List
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