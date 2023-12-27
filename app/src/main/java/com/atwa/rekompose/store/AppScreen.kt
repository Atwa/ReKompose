package com.atwa.rekompose.store

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atwa.rekompose.R
import com.atwa.rekompose.designsystem.theme.rekomposeSampleTheme
import com.atwa.rekompose.feature.filter.RepositoryFilterDialog
import com.atwa.rekompose.feature.repositories.RepositoriesScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen() {
    rekomposeSampleTheme {
        val openFilterDialog = remember { mutableStateOf(false) }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Github Repositories",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 70.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    actions = {
                        Box(
                            modifier = Modifier.size(width = 50.dp, height = 25.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { openFilterDialog.value = true }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_filter),
                                    "",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                    )
                )
            },
            content = {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 70.dp), color = MaterialTheme.colorScheme.surface
                ) {
                    RepositoriesScreen()
                    when {
                        openFilterDialog.value -> RepositoryFilterDialog(
                            onDismissRequest = { openFilterDialog.value = false },
                        )
                    }
                }
            }
        )

    }
}
