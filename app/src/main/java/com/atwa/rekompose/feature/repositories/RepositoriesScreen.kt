package com.atwa.rekompose.feature.repositories

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.atwa.rekompose.R
import com.atwa.rekompose.feature.filter.RepositoryFilterChips
import com.atwa.rekompose.feature.filter.RepositoryLanguageFilter
import com.atwa.rekompose.store.AppState
import com.atwa.rekompose.ui.components.ShimmerItem
import com.atwa.rekompose.ui.theme.Grey200
import com.atwa.rekompose.ui.theme.Teal500
import org.reduxkotlin.compose.rememberDispatcher
import org.reduxkotlin.compose.selectState


@Composable
fun RepositoriesScreen() {

    val state by selectState<AppState, RepositoriesState> { repositories }
    val filters by selectState<AppState, List<RepositoryLanguageFilter>> { repositories.selectedFilters }
    val dispatch = rememberDispatcher()
    LaunchedEffect(Unit) {
        dispatch(RepositoriesAction.FetchRepositories)
    }
    Column {
        if (filters.isNotEmpty()) {
            RepositoryFilterChips(filters) { id ->
                dispatch(
                    RepositoriesAction.UpdateFilterSelection(
                        id,
                        false
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        when {
            state.isLoading -> ShimmerEffectList()
            state.error?.isNotBlank() == true -> ErrorScreen { dispatch(RepositoriesAction.FetchRepositories) }
            state.filteredRepositories.isNotEmpty() -> RepositoriesList(state.filteredRepositories)
        }
    }
}

@Composable
@Preview
fun ShimmerEffectList() {
    LazyColumn(contentPadding = PaddingValues(16.dp, 8.dp, 0.dp, 8.dp)) {
        items(20) {
            ShimmerItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Divider(color = Grey200)
        }
    }
}

@Composable
fun RepositoriesList(repositories: List<Repository>) {
    LazyColumn(contentPadding = PaddingValues(16.dp, 8.dp, 0.dp, 8.dp)) {
        items(items = repositories, key = { repo -> repo.id }) { repository ->
            RepositoryItem(
                repository = repository,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )
            Divider(color = Grey200)
        }
    }
}



@Composable
fun RepositoryItem(repository: Repository, modifier: Modifier) {
    Row(modifier = modifier) {
        Image(
            painter = rememberAsyncImagePainter(repository.ownerImageUrl),
            contentDescription = repository.ownerName,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = repository.ownerName,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(fontSize = 12.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = repository.name,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 16.sp)

            )
            repository.description?.let { description ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            repository.language?.let { language ->
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(repository.languageColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = language,
                        modifier = Modifier.fillMaxWidth(.3f),
                        style = TextStyle(fontSize = 14.sp)
                    )
                    repository.stars?.let { stars ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.star_icon),
                            contentDescription = "star"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stars.toString(),
                            modifier = Modifier.fillMaxWidth(.4f),
                            style = TextStyle(fontSize = 14.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(retry: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error_animation))
    Row {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(horizontal = 32.dp)
        ) {
            LottieAnimation(composition, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Something went wrong..",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(fontSize = 18.sp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "An alien is probably blocking your signal.",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(fontSize = 14.sp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(48.dp))
            OutlinedButton(
                onClick = { retry() },
                border = BorderStroke(1.dp, Teal500),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                elevation = ButtonDefaults.buttonElevation(2.dp),
                shape = RoundedCornerShape(20),
                colors = buttonColors(
                    contentColor = Teal500,
                    containerColor = Color.White,
                )
            ) {
                Text(
                    text = "RETRY",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}


