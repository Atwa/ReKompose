package com.atwa.rekompose.feature.repositories.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.atwa.rekompose.feature.repositories.domain.RepositoriesAction
import com.atwa.rekompose.app.AppState
import com.atwa.rekompose.designsystem.theme.BlueNavy
import com.atwa.rekompose.designsystem.theme.Grey200
import com.atwa.rekompose.feature.repositories.domain.LanguageFilter
import org.reduxkotlin.compose.rememberDispatcher
import org.reduxkotlin.compose.selectState

@Composable
fun RepositoryFilterDialog(
    onDismissRequest: () -> Unit,
) {
    val filters by selectState<AppState, List<LanguageFilter>> { repositories.filters }
    val loading by selectState<AppState, Boolean> { repositories.dialogLoading }
    val dispatch = rememberDispatcher()
    LaunchedEffect(true){
        dispatch(RepositoriesAction.FetchFlowNumbers())
    }
    LaunchedEffect(Unit) {
        if (filters.isEmpty())
            dispatch(RepositoriesAction.FetchLanguageFilters)
    }
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .height(600.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            when {
                loading -> Box(
                        contentAlignment = Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                else -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp, 8.dp, 0.dp, 8.dp)) {
                        items(items = filters, key = { filter -> filter.id }) { filter ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = filter.language,
                                    modifier = Modifier.weight(weight = 1f)
                                )
                                Checkbox(
                                    modifier = Modifier.weight(weight = .4f),
                                    checked = filter.isSelected,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = BlueNavy,
                                        checkmarkColor = BlueNavy
                                    ),
                                    onCheckedChange = {
                                        dispatch(
                                            RepositoriesAction.UpdateFilterSelection(
                                                filter.id,
                                                filter.isSelected.not()
                                            )
                                        )
                                    }
                                )
                            }
                            Divider(color = Grey200)
                        }
                    }
                }
            }
        }
    }
}