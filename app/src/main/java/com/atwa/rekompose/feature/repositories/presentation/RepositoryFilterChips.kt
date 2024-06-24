package com.atwa.rekompose.feature.repositories.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.atwa.rekompose.feature.repositories.domain.LanguageFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryFilterChips(
    filters: List<LanguageFilter>,
    onRemoveFilter: (Int) -> Unit,
) {

    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(filters, key = { it.id }) { filter ->
            InputChip(
                modifier = Modifier.padding(horizontal = 6.dp), // gap between items
                selected = filter.isSelected,
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove Filter",
                        Modifier.size(InputChipDefaults.AvatarSize)
                    )
                }, onClick = {
                    onRemoveFilter(filter.id)
                },
                label = {
                    Text(text = filter.language)
                }
            )
        }
    }
}