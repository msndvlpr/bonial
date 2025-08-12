package com.bonial.codechallenge.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType
import com.bonial.codechallenge.ui.ViewState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bonial.codechallenge.ui.home.widget.BrochureGrid
import com.bonial.codechallenge.ui.model.BrochureUiModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrochureScreen(
    viewModel: BrochureViewModel = hiltViewModel(),
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val selectedFilters by viewModel.filter.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current

    // Dynamically decide column count based on orientation
    val columnCount = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Brochures") },
                actions = {

                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Filter")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        ContentType.entries.toTypedArray().take(3).forEach { type ->
                            val isChecked = type in selectedFilters.contentTypeFilter
                            DropdownMenuItem(
                                enabled = type != ContentType.SUPER_BANNER_CAROUSEL,
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { checked ->
                                                val newList = if (checked) {
                                                    selectedFilters.contentTypeFilter + type
                                                } else {
                                                    selectedFilters.contentTypeFilter - type
                                                }
                                                viewModel.updateContentTypeFilter(newList)
                                            },
                                            enabled = type != ContentType.SUPER_BANNER_CAROUSEL,
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(type.name)
                                    }
                                },
                                onClick = {
                                    val newChecked = !isChecked
                                    val newList = if (newChecked) {
                                        selectedFilters.contentTypeFilter + type
                                    } else {
                                        selectedFilters.contentTypeFilter - type
                                    }
                                    viewModel.updateContentTypeFilter(newList)
                                }
                            )
                        }

                    HorizontalDivider()

                    // Theme toggle menu item
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (isDarkTheme) "Dark" else "Light")
                                Switch(
                                    checked = isDarkTheme,
                                    onCheckedChange = { onThemeToggle() }
                                )
                            }
                        },
                        onClick = { onThemeToggle() }
                    )
                }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Explore") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    label = { Text("Favourites") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Check, contentDescription = null) },
                    label = { Text("List") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountBox, contentDescription = null) },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
                    label = { Text("More") },
                    selected = false,
                    onClick = {}
                )
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
            when (viewState) {

                is ViewState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("tag_loading"))

                is ViewState.Failure -> ErrorScreen { viewModel.retryLoadData() }

                is ViewState.Success -> {
                    val brochures = (viewState as ViewState.Success<List<BrochureUiModel>>).data
                    if (brochures.isNotEmpty()) {
                        BrochureGrid(brochures, columns = columnCount)
                    } else {
                        EmptyStateScreen()
                    }
                }

                null -> {}
            }
        }
    }
}