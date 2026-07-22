package com.actaks.nexledger.feature.categories.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.model.Category
import com.actaks.nexledger.core.ui.components.NexEmptyState
import com.actaks.nexledger.core.ui.components.NexErrorState
import com.actaks.nexledger.core.ui.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CategoryRoot(
    onNavigateToAddCategory: () -> Unit,
    onNavigateToEditCategory: (categoryId: Long) -> Unit,
    viewModel: CategoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CategoryEvent.NavigateToAddCategory -> onNavigateToAddCategory()
            is CategoryEvent.NavigateToEditCategory -> onNavigateToEditCategory(event.categoryId)
        }
    }

    CategoryScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryScreen(
    state: CategoryState,
    onAction: (CategoryAction) -> Unit
) {

    Scaffold(topBar = { TopAppBar(title = { Text("Categories") }) }, floatingActionButton = {
        FloatingActionButton(onClick = { onAction(CategoryAction.OnAddCategoryClick) }) {
            Icon(Icons.Rounded.Add, "Add category")
        }
    }) { innerPadding ->
        when {
            state.error != null -> NexErrorState(
                message = state.error,
                onRetry = { onAction(CategoryAction.OnRefresh) },
                modifier = Modifier.padding(innerPadding)
            )

            state.loading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            else -> Column(Modifier.padding(innerPadding)) {
                SecondaryTabRow(selectedTabIndex = state.selectedTab.ordinal) {
                    Tab(
                        selected = state.selectedTab == CategoryTab.EXPENSE,
                        onClick = { onAction(CategoryAction.OnTabSelect(CategoryTab.EXPENSE)) },
                        text = { Text("Expenses") })
                    Tab(
                        selected = state.selectedTab == CategoryTab.INCOME,
                        onClick = { onAction(CategoryAction.OnTabSelect(CategoryTab.INCOME)) },
                        text = { Text("Income") })
                }
                val categories = when (state.selectedTab) {
                    CategoryTab.EXPENSE -> state.expenseCategories
                    CategoryTab.INCOME -> state.incomeCategories
                }
                if (categories.isEmpty()) {
                    NexEmptyState(
                        message = "No ${state.selectedTab.name.lowercase()} categories",
                        subtitle = "Tap + to create one"
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            categories, key = { it.id }) { category ->
                            CategoryCard(
                                category,
                                onClick = { onAction(CategoryAction.OnCategoryClick(category.id)) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(category: Category, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Circle, null, modifier = Modifier.size(24.dp), tint = try {
                    Color(category.color.toColorInt())
                } catch (_: Exception) {
                    MaterialTheme.colorScheme.primary
                }
            )
            Spacer(Modifier.width(12.dp))
            Text(
                category.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}